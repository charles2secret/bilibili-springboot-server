package com.bilibili.dao;

import com.bilibili.domain.auth.AuthRoleElementOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

@Mapper
public interface AuthRoleElementOperationDao {

    // use @Param when passing Set or List or Map to the SQL
    List<AuthRoleElementOperation> getRoleElementOperationsByRoleIds(@Param("roleIdSet") Set<Long> roleIdSet);

}
