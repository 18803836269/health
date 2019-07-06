package pdsu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pdsu.constant.MessageConstant;
import pdsu.constant.RedisMessageConstant;
import pdsu.entity.Result;
import pdsu.utils.JuheSmsUtil;
import pdsu.utils.ValidateCodeUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        //生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        //调用短信SDK发送验证码
//        JuheSmsUtil.sendCode(telephone, String.valueOf(code));
        System.out.println("验证码:" + code);
        //将验证码存入redis，用户点击提交时取出验证
        Jedis jedis = jedisPool.getResource();
        jedis.setex(RedisMessageConstant.SENDTYPE_ORDER + telephone, 5 * 60, String.valueOf(code));
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        //生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        //调用短信sdk发送验证码
//        JuheSmsUtil.sendCode(telephone,String.valueOf(code));
        System.out.println("验证码:" + code);
        //把验证码存入redis，用户点击提交时取出验证
        Jedis jedis = jedisPool.getResource();
        jedis.setex(RedisMessageConstant.SENDTYPE_LOGIN + telephone, 5 * 60, String.valueOf(code));
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
