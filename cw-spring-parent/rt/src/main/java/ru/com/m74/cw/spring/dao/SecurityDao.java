package ru.com.m74.cw.spring.dao;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author mixam
 * @since 10.05.17 15:42
 */
public interface SecurityDao extends UserDetailsService {
    User principal();
}
