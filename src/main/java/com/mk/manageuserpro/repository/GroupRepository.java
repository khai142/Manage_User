package com.mk.manageuserpro.repository;

import com.mk.manageuserpro.model.Group;
import com.mk.manageuserpro.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    public List<Group> findAll();

}
