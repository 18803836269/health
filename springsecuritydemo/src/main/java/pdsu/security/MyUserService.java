package pdsu.security;

import pdsu.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MyUserService implements UserDetailsService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    Map<String, User> map = new HashMap<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        initData();
        //模拟从数据库查询用户
        User user = map.get(username);
        if(null == user){
            return null;
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if("snoopy".equals(username)){
            authorities.add( new SimpleGrantedAuthority("add"));
            authorities.add( new SimpleGrantedAuthority("ROLE_user"));
        } else {
            authorities.add( new SimpleGrantedAuthority("ROLE_admin"));
            authorities.add( new SimpleGrantedAuthority("del"));
        }
        org.springframework.security.core.userdetails.User securityUser =
                new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
        return securityUser;
    }

    private void initData(){
        User user1= new User();
        user1.setUsername("snoopy");
        user1.setPassword(bCryptPasswordEncoder.encode("snoopy"));
        map.put(user1.getUsername(),user1);

        User user2= new User();
        user2.setUsername("admin");
        user2.setPassword(bCryptPasswordEncoder.encode("admin"));
        map.put(user2.getUsername(),user2);
    }


}
