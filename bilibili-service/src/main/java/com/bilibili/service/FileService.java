package com.bilibili.service;

import com.bilibili.service.util.FastDFSUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {


    public String uploadFileBySlices(MultipartFile slice, String fileMd5, Integer sliceNo, Integer totalSliceNo) throws Exception {
        String path = FastDFSUtil.uploadFileBySlices(slice, fileMd5, sliceNo, totalSliceNo);
        return path;
    }
}
