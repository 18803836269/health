package pdsu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import pdsu.constant.MessageConstant;
import pdsu.constant.RedisMessageConstant;
import pdsu.entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pdsu.pojo.Member;
import pdsu.service.MemberService;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Reference
    MemberService memberService;
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/check")
    public Result check(@RequestBody Map map, HttpServletResponse response){
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //通过手机号码获取redis里面的验证码
        String code = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN + telephone);
        //校验验证码
        if (null == code){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        if (!code.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        //检查一下用户是否存在
        Member member = memberService.findByTelephone(telephone);

        if (null == member){
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            //保存用户到数据库
            memberService.add(member);
        }
        //把用户信息保存到cookie & session & redis
        Cookie cookie = new Cookie("shouji", telephone);
        //设置cookie在同一服务器内共享
        cookie.setPath("/");
        cookie.setMaxAge(30*60);
        response.addCookie(cookie);
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }

}
