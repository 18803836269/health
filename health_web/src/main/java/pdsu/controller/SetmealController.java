package pdsu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pdsu.constant.MessageConstant;
import pdsu.constant.RedisConstant;
import pdsu.entity.PageResult;
import pdsu.entity.QueryPageBean;
import pdsu.entity.Result;
import pdsu.pojo.Setmeal;
import pdsu.service.SetmealService;
import pdsu.utils.QiniuUtils;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RequestMapping("/setmeal")
@RestController
public class SetmealController {

    @Reference
    SetmealService setmealService;
    @Autowired
    JedisPool jedisPool;

    @RequestMapping("upload")
    public Result upload(MultipartFile imgFile){
        try {
            //修改文件名
            String originalFilename = imgFile.getOriginalFilename();
            int lastIndexOf = originalFilename.lastIndexOf(".");
            String suffix = originalFilename.substring(lastIndexOf);
            String fileName = UUID.randomUUID().toString() + suffix;
            //调用七牛云SDK上传图片
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //把上传好的图片保存到setmealPicResources
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            //返回fileName给页面
            return new Result(true, MessageConstant.UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("/add")
    public Result add(Integer[] checkgroupIds, @RequestBody Setmeal setmeal){
        try {
            setmealService.add(checkgroupIds,setmeal);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.findPage(queryPageBean.getQueryString(),queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
    }
}
