package zq.springsecuritydemo.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Test {

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/home")
    public String home(){
        return "index";
    }

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/login2")
    public String login2(){
        return "hello";
    }

    @RequestMapping("/permission")
    public String permission(){
        return "permission";
    }

    @RequestMapping("/nopermission")
    public String nopermission(){
        return "403";
    }

    @RequestMapping("/annotation")
    public String annotation(){
        System.out.println(role());
        return "annotation";
    }

    @RequestMapping("role")
    //注意以下注解需要在配置类上添加注解启用他们
//    @Secured({"ROLE_admin"})//验证是否有角色访问该方法，主要是验证角色
    @PreAuthorize("hasAuthority('admin')")//使用方法之前验证权限，该注解可以将权限注入带方法的参数中
//    @PostAuthorize("hasAuthority('admin')")//使用方法之后再验证权限
//    @PreFilter("")
//    @PostFilter("")
    public String role(){
        return "role";
    }
}
