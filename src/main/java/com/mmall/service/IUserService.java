package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import javax.servlet.http.HttpSession;

/**
 * Created by jay on 2018/3/11.
 */
public interface IUserService {

    ServerResponse<User> login(String name, String password);
    ServerResponse<String> regist(User user);
    ServerResponse<String> checkValid(String str,String type);
    ServerResponse<String> selectQuestion(String username);
    ServerResponse<String> checkAnswer(String username,String question,String answer);
    ServerResponse<String> fogetRestPassword(String username,String passwordNew,String forgetToken);
    ServerResponse<String> restPassword(User user,String passwordOld,String passwordNew);
}
