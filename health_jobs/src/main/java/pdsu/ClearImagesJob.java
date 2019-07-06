package pdsu;

import cn.hutool.core.collection.CollectionUtil;
import pdsu.constant.RedisConstant;
import pdsu.utils.QiniuUtils;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * 清理图片
 */
public class ClearImagesJob {

//    @Autowired
    JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void run(){
        //比较

        //套餐图片所有图片名称
//        public static final String SETMEAL_PIC_RESOURCES = "setmealPicResources";
//        //套餐图片保存在数据库中的图片名称
//        public static final String SETMEAL_PIC_DB_RESOURCES = "setmealPicDbResources";
        Set<String> deleteImgs = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(CollectionUtil.isNotEmpty(deleteImgs)){
            for (String deleteImg : deleteImgs) {
                QiniuUtils.deleteFileFromQiniu(deleteImg);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,deleteImg);
            }
        }
    }
}
