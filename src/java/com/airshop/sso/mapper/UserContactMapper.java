package com.airshop.sso.mapper;

import com.airshop.sso.entity.UserContact;
import com.airshop.support.base.SqlMapper;

public interface UserContactMapper extends SqlMapper {
	
    int deleteByPrimaryKey(Long contactId);

    int insert(UserContact record);

    int insertSelective(UserContact record);

    UserContact selectByPrimaryKey(Long contactId);

    int updateByPrimaryKeySelective(UserContact record);

    int updateByPrimaryKey(UserContact record);
}