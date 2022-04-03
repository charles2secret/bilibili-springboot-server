package com.api;

import com.api.support.UserSupport;
import com.bilibili.domain.FollowingGroup;
import com.bilibili.domain.JsonResponse;
import com.bilibili.domain.UserFollowing;
import com.bilibili.domain.annotation.ApiLimitedRole;
import com.bilibili.domain.annotation.DataLimited;
import com.bilibili.domain.constant.AuthRoleConstant;
import com.bilibili.service.UserFollowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserFollowingApi {

    @Autowired
    private UserFollowingService userFollowingService;

    @Autowired
    private UserSupport userSupport;

    // if the user is Lv0, then he or she can not follow others
    // the code will go to the aspect when reach this point cut
    @ApiLimitedRole(limitedRoleCodeList = {AuthRoleConstant.ROLE_CODE_LV0})
    @PostMapping("/user-followings")
    public JsonResponse<String> addUserFollowings (@RequestBody UserFollowing userFollowing) {
        Long userId = userSupport.getCurrentUserId();
        userFollowing.setUserId(userId);
        userFollowingService.addUserFollowings((userFollowing));
        return JsonResponse.success();
    }

    @GetMapping("/user-followings")
    public JsonResponse<List<FollowingGroup>> getUserFollowings(){
        Long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> result = userFollowingService.getUserFollowings(userId);
        return new JsonResponse<>(result);
    }

    @GetMapping("/user-fans")
    public JsonResponse<List<UserFollowing>> getUserFans(){
        Long userId = userSupport.getCurrentUserId();
        List<UserFollowing> result = userFollowingService.getUserFans(userId);
        return new JsonResponse<>(result);

    }

    @DataLimited
    @PostMapping("/user-following-groups")
    public JsonResponse<Long> addUserFollowingGroups(@RequestBody FollowingGroup followingGroup) {
        Long userId = userSupport.getCurrentUserId();
        followingGroup.setUserId(userId);
        Long groupId = userFollowingService.addUserFollowingGroups(followingGroup);
        return new JsonResponse<>(groupId);
    }

    @GetMapping("/user-following-groups")
    public JsonResponse<List<FollowingGroup>> getUserFollowingGroups(){
        Long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> result = userFollowingService.getUserFollowingGroups(userId);
        return new JsonResponse<>(result);
    }
}
