package com.jjs.miaosha.service;

import com.jjs.miaosha.dao.UserDao;
import com.jjs.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jjs
 * @Version 1.0 2020/4/12
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getUserByid(int id) {
        return userDao.getByUid(id);
    }


}
