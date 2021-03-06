package com.bilibili.dao;

import com.alibaba.fastjson.JSONObject;
import com.bilibili.domain.RefreshTokenDetail;
import com.bilibili.domain.User;
import com.bilibili.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface UserDao {

    User getUserByPhone(String phone);

    User getUserByEmail(String email);

    Integer addUser(User user);

    Integer addUserInfo(UserInfo userInfo);

    User getUserById(Long id);

    UserInfo getUserInfoByUserId(Long userId);

    List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList);

    Integer updateUsers(User user);

    Integer updateUserInfos(UserInfo userInfo);

    Integer pageCountUserInfos(Map<String, Object> params);

    List<UserInfo> pageListUserInfos(JSONObject params);

    Integer deleteRefreshToken(@Param("refreshToken") String refreshToken,
                               @Param("userId") Long userId);

    Integer addRefreshToken(@Param("refreshToken") String refreshToken,
                            @Param("userId") Long userId,
                            @Param("createTime") Date date);

    RefreshTokenDetail getRefreshTokenDetail(String refreshToken);


}
