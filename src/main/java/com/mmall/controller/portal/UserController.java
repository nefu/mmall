package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by jay on 2018/3/11.
 */
@Controller
@RequestMapping("/user/")
public class UserController {
    //将service注入
    @Autowired
    private IUserService iUserService;


    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    //将输出格式设为配置好的json 配置在spring mvc的dispcther-servlet.xml中
    // 的org.springframework.http.converter.json.MappingJacksonHttpMessageConverter
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        //service---mybatis--dao
        ServerResponse serverResponse = iUserService.login(username,password);
        if(serverResponse.isSuccess()){
           session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return serverResponse;
    }

    @RequestMapping(value="logout.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
         session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }


    @RequestMapping(value="regist.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> regist(User user){
        return iUserService.regist(user);

    }
    @RequestMapping(value="checkvalid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){

        return iUserService.checkValid(str,type);
    }


    @RequestMapping(value="getUserInfo.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
       User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user!=null){
            return ServerResponse.createBySuccess(user);
        }
        else{
            return ServerResponse.createByErrorMsg("用户未登录，无法获取当前用户信息");
        }
    }

    @RequestMapping(value="selectQuestion.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> selectQuestion(String username){
            return iUserService.selectQuestion(username);
    }
    @RequestMapping(value="checkAnswer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkAnswer(String username,String question,String anwser){
       return iUserService.checkAnswer(username,question,anwser);
    }
    @RequestMapping(value="fogetRestPassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> restPassword(String username,String passwordNew,String forgetToken){
       return iUserService.fogetRestPassword(username,passwordNew,forgetToken);
    }

    public ServerResponse restPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorMsg("用户未登录");
        }
        return iUserService.restPassword(user,passwordOld,passwordNew);
    }

}
