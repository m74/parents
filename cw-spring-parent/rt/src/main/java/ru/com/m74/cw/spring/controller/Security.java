package ru.com.m74.cw.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.com.m74.cw.spring.dao.SecurityDao;

/**
 * @author mixam
 * @since 10.05.17 14:52
 */
@RestController
@RequestMapping("/security")
public class Security {
    @Autowired
    private SecurityDao securityDao;

    @RequestMapping("/principal")
    public Object principal() {
        return securityDao.principal();
    }

}
