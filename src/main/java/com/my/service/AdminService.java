package com.my.service;

import com.my.pojo.Admin;

public interface AdminService {
    //完成登录判断,将数据库中查到的admin返回给控制器，携带密码用户名等信息。
    Admin login(String name, String pwd);
 
}
