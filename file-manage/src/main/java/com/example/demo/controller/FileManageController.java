package com.example.demo.controller;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.HeaderUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.nio.ch.IOUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.spi.http.HttpContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Getter
@RestController
public class FileManageController implements FileManageApi {

    @Autowired
    private Environment environment;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @Override
    public List<String> findList(String path) {
        if (!StringUtils.hasText(path)) {
            path = "/";
        }
        File file = new File(path);
        return Arrays.stream(file.listFiles())
                .map(File::getAbsolutePath)
                .sorted(String::compareTo)
                .collect(Collectors.toList());
    }

    @Override
    public void getFile(String path) {
        Assert.hasText(path, "路径是错误的！");
        File file = new File(path);
        Assert.isTrue(file.exists(), "文件不存在！");

        try {
            // 设置强制下载不打开
            response.setContentType("application/force-download");
            //设置请求内容配置，返回头中返回文件名称
            response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
            //返回头中，告知即将下载文件的大小
            response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
            //将文件写入输入流(给请求者)
            int count = IOUtils.copy(new FileInputStream(file), response.getOutputStream());
            //对于服务器是上传文件的操作，对于请求方是下载操作。
            log.error("文件上传完成，文件大小为：{}", count);
        } catch (IOException e) {
            log.error("文件下载失败，请求数据为：{}", path);
            log.error("文件下载失败，异常信息为：", e);
        }

    }

    @Override
    public void uploadFile() {

    }

    @Override
    public void delete() {

    }
}
