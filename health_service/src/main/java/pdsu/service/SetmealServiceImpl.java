package pdsu.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pdsu.constant.RedisConstant;
import pdsu.dao.SetmealDao;
import pdsu.entity.PageResult;
import pdsu.pojo.CheckGroup;
import pdsu.pojo.CheckItem;
import pdsu.pojo.Setmeal;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService{

    @Autowired
    SetmealDao setmealDao;
    @Autowired
    JedisPool jedisPool;

    @Override
    public void add(Integer[] checkgroupIds, Setmeal setmeal) {
        //保存套餐基本信息
        setmealDao.add(setmeal);
        //获取套餐主键循环保存
        Integer setmealId = setmeal.getId();
        setSetmealAndCheckGroup(checkgroupIds,setmealId);
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }

    @Override
    public PageResult findPage(String queryString, Integer currentPage, Integer pageSize) {
        Page<Object> page = PageHelper.startPage(currentPage, pageSize);
//        Page<Setmeal> page = setmealDao.findPage(queryString);
        List<Setmeal> setmeals = setmealDao.findPage(queryString);
        return new PageResult(page.getTotal(),setmeals);
    }

    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    @Override
    public Setmeal findById(Integer setmealId) {
        //根据套餐id查询套餐详情
        Setmeal setmeal = setmealDao.findById(setmealId);
        if (null != setmeal){
            //根据套餐id查询下面的检查组
            List<CheckGroup> checkGroups = setmealDao.findCheckGroupBySetmealId(setmealId);
            if (CollectionUtil.isNotEmpty(checkGroups)){
                for (CheckGroup checkGroup : checkGroups) {
                    //3、根据检查组id查询检查组下面的检查项
                    Integer checkGroupId = checkGroup.getId();
                    List<CheckItem> checkItems = setmealDao.findCheckItemByCheckGroupId(checkGroupId);
                    checkGroup.setCheckItems(checkItems);
                }
                setmeal.setCheckGroups(checkGroups);
            }
        }
        return setmeal;
    }

    @Override
    public Setmeal findDetailById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    private void setSetmealAndCheckGroup(Integer[] checkgroupIds, Integer setmealId) {
        if(null != checkgroupIds && checkgroupIds.length > 0){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.setSetmealAndCheckGroup(checkgroupId,setmealId);
            }
        }
    }
}
