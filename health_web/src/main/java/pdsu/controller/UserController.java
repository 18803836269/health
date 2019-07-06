package pdsu.controller;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import pdsu.constant.MessageConstant;
import pdsu.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pdsu.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    /**
     * 获取当前登录用户的用户名
     * @return
     * @throws Exception
     */
    @RequestMapping("/getUsername")
    public Result getUsername()throws Exception{
//        try{
//            org.springframework.security.core.userdetails.User user =
//                    (org.springframework.security.core.userdetails.User)
//                            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
//        }catch (Exception e){
//            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
//        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        String password = user.getPassword();
        List<String> menuList = userService.findMenuListByUsername(username);
        if (CollectionUtil.isNotEmpty(menuList)){
            for (String firstMenu : menuList) {

            }
        }
        System.out.println(password);
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
    }
}