package com.example.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Api(value = "文件管理",tags = "文件管理")
@RequestMapping("/v1/file_manage")
public interface FileManageApi {

    @GetMapping
    @ApiOperation("查询文件列表")
    List<String> findList(String path);

    @GetMapping("/down")
    @ApiOperation("下载文件")
    void getFile(@RequestParam("path") String path);

    @PostMapping
    @ApiOperation("上传文件")
    void uploadFile();

    @DeleteMapping
    @ApiOperation("删除文件")
    void delete();


}
