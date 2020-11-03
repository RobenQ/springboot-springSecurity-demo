package zq.springsecuritydemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity  //启用Spring Security的Web支持，并提供与SpringMVC的集成
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)//允许使用@Secured注解
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService myUserDetailsService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedPage("/nopermission");
        http.authorizeRequests()
                .antMatchers("/", "/home").permitAll()  //  /和/home不需要认证就能访问
                .antMatchers("/permission").hasAuthority("admin")//基于权限的控制
//                .antMatchers("/permission").hasRole("zq")//基于角色的控制，注意在从数据库获取认证信息的方法中添加ROLE_前缀
                .anyRequest().authenticated()
                .and()
                .rememberMe().tokenRepository(persistentTokenRepository())//设置记住我功能
                .tokenValiditySeconds(60)//设置有时长
                .userDetailsService(myUserDetailsService)
                .and()
                .formLogin()
                .loginPage("/login")    //设置登录页面
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")//注销访问的url
                .logoutSuccessUrl("/home")//注销成功访问的url
                .permitAll();
    }



    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    //配置密码加密，必须配置
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //记住我的功能配置，先在数据库建表，SQL语句在JdbcTokenRepositoryImpl这个类中有。
    //之后还需要去配置类中configure方法中进行配置,最后去登录页面加上复选框
    @Autowired
    private DataSource dataSource;//注入数据源
    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
//        jdbcTokenRepository.setCreateTableOnStartup(true);//设置自动生成数据库表
        jdbcTokenRepository.setDataSource(dataSource);//设置数据源
        return jdbcTokenRepository;
    }

}
