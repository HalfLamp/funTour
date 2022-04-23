package com.cai.funtour.controller;

import cn.hutool.core.io.FileTypeUtil;
import com.cai.funtour.config.WebConfig;
import com.cai.funtour.pojo.Result;
import com.cai.funtour.tools.TraceId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ：caizhiyuan
 * @date ：Created in 2022/4/22 11:27
 * @description：文件上传controller层
 */
@Api("文件上传")
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${upload.types}")
    private String imageTypes;
    // 文件上传路径
    @Value("${upload.path}")
    private String path;
    private final String URL_PRE = "http://czytgc:8080/public/file/";

    @ApiOperation("图片上传")
    @PostMapping("/images")
    public Result uploadImages(@ApiParam("图片数组") MultipartFile images[]) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile image : images) {
            InputStream inputStream = image.getInputStream();
            String type = FileTypeUtil.getType(inputStream);
            String[] split = imageTypes.split(",");
            // 判断文件类型是否支持
            if (Arrays.asList(imageTypes.split(",")).contains(type)){
                String fileName = TraceId.getTraceId() + "." + type;
                File file = new File(path, fileName);
                image.transferTo(file);
                urls.add(URL_PRE + fileName);
            }
        }
        return Result.toData("images", urls);
    }
}
