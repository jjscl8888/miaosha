package com.jjs.miaosha.dao;

import com.jjs.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MiaoShaUserDao {

    @Select("select * from miaosha_user where id = #{id}")
    MiaoshaUser getById(@Param("id")long id);

    @Update("update miaosha_user password = ${password} where id = #{id} ")
    void update(MiaoshaUser miaoshaUser);
}
