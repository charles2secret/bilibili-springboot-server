package com.bilibili.dao.repository;

import com.bilibili.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepository extends ElasticsearchRepository<User, Long> {

    // find by email like
    User findByEmailLike(String keyword);
}
