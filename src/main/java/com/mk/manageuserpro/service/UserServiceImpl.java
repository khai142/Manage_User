package com.mk.manageuserpro.service;

import com.mk.manageuserpro.model.Role;
import com.mk.manageuserpro.model.UserDetailJapan;
import com.mk.manageuserpro.utils.Common;
import com.mk.manageuserpro.utils.Constant;
import com.mk.manageuserpro.model.Group;
import com.mk.manageuserpro.model.User;
import com.mk.manageuserpro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {
    @PersistenceContext
    private final EntityManager entityManager;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(
            EntityManager entityManager,
            UserRepository userRepository,
            BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(user.getRoles());
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Page<User> getTotalUsers(String name, String groupId, int page) {
        if(Common.isEmpty(name) && Common.isEmpty(groupId)) {
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
        entityManager.persist(user);

        UserDetailJapan userDetailJapan = user.getUserDetailJapan();
        userDetailJapan.setUser(user);
        entityManager.persist(userDetailJapan);
        return user;
    }

}
