package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    int checkUserName(String username);
    //mybatis在传递多个参数的时候需要用到@Param("username")注解
    User selectLogin(@Param("username") String username,@Param("password") String password);
    int checkEmail(String email);
    //根据姓名获得用户
    String selectQuestion(String username);
    //校验密保问题
    int checkAnswer(@Param("username") String username,@Param("question")String question,@Param("answer")String answer);
    //重置密码
    int fogetRestPassword(@Param("username") String username,@Param("passwordNew") String passwordNew);
    //查看是否存在这个用户并且密码也对
    int checkPassword(@Param("passwordOld") String passwordNew,@Param("user_id") Integer user_id);
}