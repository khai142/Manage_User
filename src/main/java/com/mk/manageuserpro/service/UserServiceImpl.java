package com.mk.manageuserpro.service;

import com.mk.manageuserpro.model.Group;
import com.mk.manageuserpro.model.Role;
import com.mk.manageuserpro.model.User;
import com.mk.manageuserpro.model.UserDetailJapan;
import com.mk.manageuserpro.repository.GroupRepository;
import com.mk.manageuserpro.repository.JapanLevelRepository;
import com.mk.manageuserpro.repository.UserDetailJapanRepository;
import com.mk.manageuserpro.repository.UserRepository;
import com.mk.manageuserpro.utils.Common;
import com.mk.manageuserpro.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Join;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @PersistenceContext
    private final EntityManager entityManager;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final GroupRepository groupRepository;
    private final UserDetailJapanRepository userDetailJapanRepository;
    private final JapanLevelRepository japanLevelRepository;

    @Autowired
    public UserServiceImpl(
            EntityManager entityManager,
            UserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            GroupRepository groupRepository,
            UserDetailJapanRepository userDetailJapanRepository,
            JapanLevelRepository japanLevelRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.groupRepository = groupRepository;
        this.userDetailJapanRepository = userDetailJapanRepository;
        this.japanLevelRepository = japanLevelRepository;
    }

    private static Specification<User> hasNameLike(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.<String>get("name")), "%" + name.toLowerCase() + "%");
    }

    private static Specification<User> hasGroup(String groupId) {
        return (root, query, criteriaBuilder) -> {
            Join<Group, User> userGroup = root.join("group");
            return criteriaBuilder.equal(userGroup.get("groupId"), groupId);
        };
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Page<User> getTotalUsers(String name, String groupId, int page) {
        if (Common.isEmpty(name) && Common.isEmpty(groupId)) {
            return userRepository.findAll(PageRequest.of(page, Constant.PAGE_SIZE));
        } else {
            Specification<User> specification = null;
            if (!Common.isEmpty(name) && Common.isEmpty(groupId)) {
                specification = hasNameLike("%" + name + "%");
            } else if (Common.isEmpty(name) && !Common.isEmpty(groupId)) {
                specification = hasGroup(groupId);
            } else {
                specification = hasNameLike("%" + name + "%").and(hasGroup(groupId));
            }
            return userRepository.findAll(specification, PageRequest.of(page, Constant.PAGE_SIZE));
        }
    }

    @Override
    @Transactional
    public User createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRoles(new HashSet<Role>(user.getRoles()));

        UserDetailJapan userDetailJapan = user.getUserDetailJapan();
        if (Common.isEmpty(userDetailJapan.getJapanLevel().getCodeLevel())) {
            user.setUserDetailJapan(null);
            userRepository.save(user);
        } else {
            entityManager.persist(user);
            userDetailJapan.setJapanLevel(japanLevelRepository.findByCodeLevel(user.getUserDetailJapan().getJapanLevel().getCodeLevel()));
            userDetailJapan.setUser(user);
            entityManager.persist(userDetailJapan);
        }
        return user;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User editUser(User userData, Long userId) {
        User user = userRepository.findById(userId).get();
        user.setUsername(userData.getUsername());
        user.setGroup(groupRepository.findById(userData.getGroup().getGroupId()).get());
        user.setEmail(userData.getEmail());
        if (userData.isUpdatePasswordFlag()) {
            user.setPassword(bCryptPasswordEncoder.encode(userData.getPassword()));
        }
        user.setName(userData.getName());
        user.setBirthday(userData.getBirthday());
        user.setActive(true);
        user.setRoles(new HashSet<Role>(userData.getRoles()));
        UserDetailJapan userDetailJapan = userDetailJapanRepository.findByUserId(userId);
        if (!Common.isEmpty(userData.getUserDetailJapan().getJapanLevel().getCodeLevel())) {
            if (userDetailJapan == null) {
                userDetailJapan = new UserDetailJapan();
                userDetailJapan.setUser(user);
            }
            userDetailJapan.setJapanLevel(japanLevelRepository.findByCodeLevel(userData.getUserDetailJapan().getJapanLevel().getCodeLevel()));
            userDetailJapan.setStartDate(userData.getUserDetailJapan().getStartDate());
            userDetailJapan.setEndDate(userData.getUserDetailJapan().getEndDate());
            userDetailJapan.setScore(userData.getUserDetailJapan().getScore());
            user.setUserDetailJapan(userDetailJapan);
        } else {
            user.setUserDetailJapan(null);
            if (userDetailJapan != null) {
                userDetailJapanRepository.deleteById(userDetailJapan.getUserDetailJapanId());
            }
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

}
