package com.jjs.miaosha.dao;

import com.jjs.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author jjs
 * @Version 1.0 2020/4/12
 */
@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    public User getByUid(@Param("id")int id);

    @Insert("insert into user(name) values(#{name})")
    public void insert(@Param("name") String name);
}
