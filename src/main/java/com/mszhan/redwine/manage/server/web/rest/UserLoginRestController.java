package com.mszhan.redwine.manage.server.web.rest;

import com.mszhan.redwine.manage.server.core.Responses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 20:34 2017/12/27
 */
@RestController
@RequestMapping(value = "/api/v1")
public class UserLoginRestController {

    @PostMapping(value = "/user/sign_up")
    public Object signUp(){
        //TODO: 处理用户注册
        return Responses.newInstance().succeed();
    }

    public String encodePasswordByMd5(String userName, String password) {
        if (StringUtils.isBlank(password) || StringUtils.isBlank(userName)) {
            return null;
        }
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        return encoder.encodePassword(password, userName);
    }

    public static void main(String[] args) {
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        System.out.println(encoder.encodePassword("admin", "admin"));
    }
}
