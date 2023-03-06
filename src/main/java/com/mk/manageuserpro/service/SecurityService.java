package com.mk.manageuserpro.service;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
