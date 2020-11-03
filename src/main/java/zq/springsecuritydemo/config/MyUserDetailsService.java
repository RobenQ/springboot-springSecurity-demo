package zq.springsecuritydemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import zq.springsecuritydemo.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;

// 自定义数据源来获取数据
// 这里只要是存在一个自定义的 UserDetailsService ，那么security将会使用该实例进行配置
@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    // 可以从任何地方获取数据
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        zq.springsecuritydemo.bean.User user = userMapper.selectUserByName(username);
        if (user==null)
            throw new UsernameNotFoundException("用户名不存在！");
        System.out.println("用户名:"+username);
        System.out.println("密码:"+user.getPassword());
        String password = user.getPassword();
        User loginUser = new User(username, password, true, true,
                true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList(user.getPerms()));
        return loginUser;
    }
}
