package com.mk.manageuserpro.service;

import com.mk.manageuserpro.model.JapanLevel;
import com.mk.manageuserpro.repository.JapanLevelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JapanLevelServiceImpl implements JapanLevelService {

    private final JapanLevelRepository japanLevelRepository;

    public JapanLevelServiceImpl(JapanLevelRepository japanLevelRepository) {
        this.japanLevelRepository = japanLevelRepository;
    }


    @Override
    public List<JapanLevel> findAll() {
        return japanLevelRepository.findAll();
    }
}
