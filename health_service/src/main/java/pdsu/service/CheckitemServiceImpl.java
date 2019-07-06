package pdsu.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pdsu.dao.CheckitemDao;
import pdsu.entity.PageResult;
import pdsu.pojo.CheckItem;

import java.util.List;

@Service(interfaceClass = CheckitemService.class)
@Transactional
public class CheckitemServiceImpl implements CheckitemService {

    @Autowired
    CheckitemDao checkitemDao;
    @Override
    public void add(CheckItem checkItem) {
        checkitemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //使用分页插件查询数据库中的数据
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkitemDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());

//        Page<CheckItem> page = checkitemDao.findPage(queryString);
//        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
//        return pageResult;
    }

    @Override
    public int delete(Integer id) {
        //企业不是真删除，是修改数据的状态，为不可见
        /**
         * 查询一下是否存在引用
         */
        Integer count = checkitemDao.findCountByCheckItemId(id);
        if (count > 0){
            //throw new RuntimeException("存在引用，不能删除")
            return count;
        }
        checkitemDao.delete(id);
        return 0;
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkitemDao.findById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkitemDao.edit(checkItem);
    }

}
