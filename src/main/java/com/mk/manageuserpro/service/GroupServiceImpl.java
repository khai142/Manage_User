package com.mk.manageuserpro.service;

import com.mk.manageuserpro.model.Group;
import com.mk.manageuserpro.model.User;
import com.mk.manageuserpro.repository.GroupRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }


    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }
}
