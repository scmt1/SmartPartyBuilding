/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.platform.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.AttachFile;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.BASE64DecodedMultipartFile;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.config.ShopConfig;
import com.yami.shop.platform.uitls.UploadFileUtils;
import com.yami.shop.service.AttachFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 文件上传 controller
 * @author lgh
 *
 */
@RestController
@RequestMapping("/admin/file")
@Api(tags = "文件上传")
public class FileController {

    @Autowired
    private AttachFileService attachFileService;
    @Autowired
    private ShopConfig shopConfig;

    @PostMapping("/upload/element")
    @ApiOperation(value = "视频上传", notes = "视频上传")
    public ServerResponseEntity<Long> uploadElementFile(@RequestParam("file") MultipartFile file) throws IOException{
        if(file.isEmpty()){
            return ServerResponseEntity.success();
        }
        AttachFile attachFile = new AttachFile();
        //视频上传
        attachFile.setType(2);
        attachFile.setShopId(Constant.PLATFORM_SHOP_ID);
        attachFile.setUploadTime(new Date());
        Long fileId = attachFileService.mpUploadFile(file.getBytes(),file.getOriginalFilename(),attachFile);
        return ServerResponseEntity.success(fileId);
    }

    /**
     * 上传聊天时的文件
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload/imFile")
    @ApiOperation(value = "图片上传（返回图片名）", notes = "图片上传（返回图片名）")
    public ServerResponseEntity<String> uploadImFile(@RequestParam("file") MultipartFile file) throws IOException{
        if(file.isEmpty()){
            return ServerResponseEntity.success();
        }
        AttachFile attachFile = new AttachFile();
        attachFile.setFileType(FileUtil.extName(file.getOriginalFilename()));
        attachFile.setFileName(FileUtil.mainName(file.getOriginalFilename()));
        attachFile.setType(1);
        attachFile.setShopId(Constant.PLATFORM_SHOP_ID);
        attachFile.setUploadTime(new Date());
        String fileName = attachFileService.uploadImFile(file.getBytes(),file.getOriginalFilename());
        return ServerResponseEntity.success(fileName);
    }

    @PostMapping("/upload/img")
    @ApiOperation(value = "图片上传（返回文件id）", notes = "图片上传（返回文件id）")
    public ServerResponseEntity<Long> uploadImg(@RequestParam("file") MultipartFile file) throws IOException{
        if(file.isEmpty()){
            return ServerResponseEntity.success();
        }
        AttachFile attachFile = new AttachFile();
        String extName = FileUtil.extName(file.getOriginalFilename());
        attachFile.setFileType(extName);
        attachFile.setFileName(FileUtil.mainName(file.getOriginalFilename()));
        attachFile.setType(1);
        attachFile.setShopId(Constant.PLATFORM_SHOP_ID);
        attachFile.setUploadTime(new Date());
        Long fileId = attachFileService.uploadImg(file.getBytes(),attachFile,extName);
        return ServerResponseEntity.success(fileId);
    }

    @PostMapping("/upload/tinymceEditor")
    @ApiOperation(value = "上传文件（返回文件名）", notes = "上传文件（返回文件名）")
    public ServerResponseEntity<String> uploadTinymceEditorImages(@RequestParam("file") MultipartFile editorFile) throws IOException{
        String fileName =  attachFileService.uploadFile(editorFile.getBytes(),editorFile.getOriginalFilename());
        return ServerResponseEntity.success(shopConfig.getDomain().getResourcesDomainName() + "/" + fileName);
    }

    @GetMapping("/attachFilePage")
    @ApiOperation(value = "分页获取文件", notes = "分页获取文件")
    public ServerResponseEntity<IPage<AttachFile>> attachFilePage(PageParam<AttachFile> page, AttachFile attachFile) {
        attachFile.setShopId(Constant.PLATFORM_SHOP_ID);
        IPage<AttachFile> attachFilePage = attachFileService.getPage(page,attachFile);

        return ServerResponseEntity.success(attachFilePage);
    }

    @DeleteMapping("/deleteFile/{fileId}")
    @ApiOperation(value = "删除文件", notes = "删除文件")
    @ApiImplicitParam(name = "fileId", value = "文件id")
    public ServerResponseEntity<Void> deleteFile(@PathVariable("fileId") Long fileId){
        AttachFile attachFile = attachFileService.getById(fileId);
        attachFileService.deleteFile(attachFile.getFilePath());
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateFile")
    @ApiOperation(value = "更改图片名称", notes = "更改图片名称")
    public ServerResponseEntity<Boolean> updateFile(@RequestBody  AttachFile attachFile) {
        if (Objects.isNull(attachFile.getFileName())){
            // 图片名称不能为空
            throw new YamiShopBindException("yami.img.name.exist");
        }
        attachFile.setShopId(Constant.PLATFORM_SHOP_ID);
        return ServerResponseEntity.success(attachFileService.updateFile(attachFile));
    }

    @DeleteMapping("/deleteByIds")
    @ApiOperation(value = "根据文件id列表批量删除文件记录", notes = "根据文件id列表批量删除文件记录")
    @ApiImplicitParam(name = "ids", value = "文件id列表")
    public ServerResponseEntity<Void> deleteByIds(@RequestBody List<Long> ids) {
        Long shopId = Constant.PLATFORM_SHOP_ID;
        attachFileService.deleteByIdsAndShopId(ids, shopId);
        return ServerResponseEntity.success();
    }

    @PutMapping("/batchMove")
    @ApiOperation(value = "根据文件id列表与分组id批量移动文件", notes = "根据文件id列表与分组id批量移动文件")
    public ServerResponseEntity<Void> batchMove(@RequestBody  AttachFile attachFile) {
        Long shopId = Constant.PLATFORM_SHOP_ID;
        attachFileService.batchMoveByShopIdAndIdsAndGroupId(shopId, attachFile);
        return ServerResponseEntity.success();
    }

    /**
     * 功能描述：上传图片
     */
    @PostMapping("/uploadImages")
    public ServerResponseEntity<Object> uploadImages(@RequestBody String imgUrl) throws UnsupportedEncodingException {
        //base64 转文件
        MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(imgUrl);
        //文件存储在nginx代理路径下
        String fileName = UploadFileUtils.uploadFile(imgFile);
        return ServerResponseEntity.success(fileName);
    }
}
