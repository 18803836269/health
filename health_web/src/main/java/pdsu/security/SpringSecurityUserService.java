package pdsu.security;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import pdsu.pojo.CheckItem;
import pdsu.pojo.Permission;
import pdsu.pojo.Role;
import pdsu.service.CheckitemService;
import pdsu.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //远程调用用户服务，根据用户名查询用户信息
        pdsu.pojo.User user = userService.findByUsername(username);
        if(user == null){
            //用户名不存在
            return null;
        }
        //角色权限
        List<GrantedAuthority> list = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        if (CollectionUtil.isNotEmpty(roles)){
            for(Role role : roles){
                Set<Permission> permissions = role.getPermissions();
                if (CollectionUtil.isNotEmpty(permissions)){
                    for(Permission permission : permissions){
                        //授权
                        list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }

        UserDetails userDetails = new User(username,user.getPassword(),list);
        return userDetails;
    }
}
