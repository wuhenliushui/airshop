package com.airshop.sso.mapper;

import com.airshop.sso.entity.User;
import com.airshop.support.base.SqlMapper;

public interface UserMapper extends SqlMapper {
    int deleteByPrimaryKey(String userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}