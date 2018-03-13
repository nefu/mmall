package com.mmall.service.Impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.pojo.User;
import com.mmall.dao.UserMapper;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Created by jay on 2018/3/11.
 */
/**
 * 检查用户是否存在
 *检查用户名密码是否匹配
 * */

//这个注释表示这是一个service
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;
    //由于注册时 password要经过MD5加密 所以在登录校验时 要将用户提交的密码在用MD5加密一次再校验
    @Override
    public ServerResponse<User> login(String username, String password) {

        int resultCount = userMapper.checkUserName(username);
        if(resultCount==0){
            return ServerResponse.createByErrorMsg("用户不存在");
        }
        //todo 密码登录M5加密
        User user = userMapper.selectLogin(username,MD5Util.MD5EncodeUtf8(password));
        if(user==null){
            return ServerResponse.createByErrorMsg("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登录成功",user);
    }

    @Override
    public ServerResponse<String> regist(User user) {
       /* if(userMapper.checkUserName(user.getUsername())>0){
            return ServerResponse.createByErrorMsg("用户名已经存在");
        }*/
        ServerResponse serverResponse = checkValid(user.getUsername(),Const.USERNAEM);
        if(serverResponse.isSuccess()){
            return serverResponse;
        }
       /* if(userMapper.checkEmail(user.getEmail())>0){
            return ServerResponse.createByErrorMsg("该邮箱已经注册过了");
        }*/
        serverResponse = checkValid(user.getUsername(),Const.EMAIL);
        if(serverResponse.isSuccess()){
            return serverResponse;
        }


            user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
           user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int result = userMapper.insert(user);
        if(result==0){
            return ServerResponse.createByErrorMsg("注册失败");
        }


            return ServerResponse.createBySuccessMsg("注册成功");


    }

    @Override
    public ServerResponse<String> checkValid(String str,String type){
     if(StringUtils.isNotBlank(type)){
     //开始校验
         if(Const.USERNAEM.equals(type)){
             int resultCount = userMapper.checkUserName(str);
             if(resultCount>0){
                 return ServerResponse.createByErrorMsg("用户已存在");
             }
         }
         if(Const.EMAIL.equals(type)){
             int resultCount = userMapper.checkEmail(str);
             if(resultCount>0){
                 return ServerResponse.createByErrorMsg("Email已存在");
             }
         }

     }
     else{
         return ServerResponse.createByErrorMsg("参数有误");
     }

        return ServerResponse.createBySuccessMsg("校验成功");
    }


    //忘记密码
    @Override
    public ServerResponse<String> selectQuestion(String username){
       // int result = userMapper.checkUserName(username);
        ServerResponse serverResponse = this.checkValid(username,Const.USERNAEM);
        if(serverResponse.isSuccess()){
            return ServerResponse.createByErrorMsg("用户不存在");
        }
        String question = userMapper.selectQuestion(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }


        return ServerResponse.createByErrorMsg("密保问题为空");
    }
    @Override
    public ServerResponse<String> checkAnswer(String username,String question,String anwser){
         int result = userMapper.checkAnswer(username,question,anwser);
        if(result>0){

            String token = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.prefix+username,token);
            return ServerResponse.createBySuccess(token);
        }
        else{
           return ServerResponse.createByErrorMsg("密保回答问题错误");
        }
    }
    @Override
    public ServerResponse<String> fogetRestPassword(String username,String passwordNew,String forgetToken){

        if(!StringUtils.isNotBlank(forgetToken)){
           return ServerResponse.createByErrorMsg("token参数错误");
        }
        ServerResponse serverResponse = this.checkValid(username,Const.USERNAEM);
        if(serverResponse.isSuccess()){
            return ServerResponse.createByErrorMsg("用户不存在");
        }

        //说明缓存里还有这个token 合法
        String token = TokenCache.getKey(TokenCache.prefix+forgetToken);

        if(StringUtils.isBlank(token)){
           return ServerResponse.createByErrorMsg("token参数错误");
        }

        if(StringUtils.equals(token,forgetToken)){

          int rowcount =  userMapper.fogetRestPassword(username,MD5Util.MD5EncodeUtf8(passwordNew));
            if(rowcount>0){
                return ServerResponse.createBySuccessMsg("密码重置成功");
            }
        }
        else{
            return ServerResponse.createByErrorMsg("token错误，请重新获取");
        }
        return ServerResponse.createByErrorMsg("密码修改失败");
    }
    @Override
    public ServerResponse<String> restPassword(User user,String passwordOld,String passwordNew){
        //指定这个用户的旧密码是这个用户的

         int resultcount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultcount==0){
            return ServerResponse.createByErrorMsg("旧密码错误");
        }
        //用新密码替换旧密码
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updatecount = userMapper.updateByPrimaryKeySelective(user);
        if(updatecount>0){
         return ServerResponse.createBySuccessMsg("密码更新成功");
        }
        return ServerResponse.createByErrorMsg("密码更新失败");
    }

}
