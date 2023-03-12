package com.mk.manageuserpro.repository;

import com.mk.manageuserpro.model.UserDetailJapan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailJapanRepository extends JpaRepository<UserDetailJapan, Long> {
    public UserDetailJapan findByUserId(Long userId);

}
