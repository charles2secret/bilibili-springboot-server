package com.bilibili.service;

import com.bilibili.dao.repository.UserInfoRepository;
import com.bilibili.dao.repository.UserRepository;
import com.bilibili.domain.User;
import com.bilibili.domain.UserInfo;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ElasticSearchService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void addUserInfo(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }

    public List<Map<String, Object>> getContents(String keyword, Integer pageNo, Integer pageSize) throws IOException {
        String[] indices = {"users", "user-infos"};
        SearchRequest searchRequest = new SearchRequest(indices);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(pageNo - 1);
        sourceBuilder.size(pageSize);
        // can user multiple keywords to make query
        MultiMatchQueryBuilder matchQueryBuilder = QueryBuilders.multiMatchQuery(keyword, "email", "nick");
        sourceBuilder.query(matchQueryBuilder);
        searchRequest.source(sourceBuilder);
        // if it can not search in 60s, stop
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // highlight
        String[] array = {"email", "nick"};
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for (String key : array) {
            highlightBuilder.fields().add(new HighlightBuilder.Field(key));
        }
        highlightBuilder.requireFieldMatch(false); // if it has multiple keys to highlight, set it to false
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        // start query
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // build and return the result
        List<Map<String, Object>> arrayList = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits()) {
            // highlight keys
            Map<String, HighlightField> highLightBuilderFields = hit.getHighlightFields();
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            for (String key : array) {
                HighlightField field = highLightBuilderFields.get(key);
                if (field != null) {
                    Text[] fragments = field.fragments();
                    String str = Arrays.toString(fragments);
                    str = str.substring(1, str.length() - 1);
                    sourceMap.put(key, str);
                }
            }
            arrayList.add(sourceMap);
        }

        return arrayList;
    }

    public User getUser(String keyword) {
        return userRepository.findByEmailLike(keyword);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}
