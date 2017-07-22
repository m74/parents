package ru.com.m74.cw.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Collections;

/**
 * @author mixam
 * @since 10.05.17 15:43
 */
@Repository
public class SecurityDaoImpl implements SecurityDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return jdbcTemplate.queryForObject("select password, active, name, lastAccess from RemoteUser WHERE id=?", (rs, rowNum) -> {
            UserDetails user = new User(username, rs.getString("password"), Collections.emptyList());
            return user;
        }, username);
    }

    @Override
    public User principal() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    }
}
