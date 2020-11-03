package zq.springsecuritydemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import zq.springsecuritydemo.bean.User;

@Mapper
public interface UserMapper {

    @Select("select * from users where username=#{name}")
    public User selectUserByName(@Param("name") String name);
}
