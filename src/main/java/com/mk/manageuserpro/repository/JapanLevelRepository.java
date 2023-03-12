package com.mk.manageuserpro.repository;

import com.mk.manageuserpro.model.JapanLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JapanLevelRepository extends JpaRepository<JapanLevel, String> {

    public JapanLevel findByCodeLevel(String codeLevel);
}
