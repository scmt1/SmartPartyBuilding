/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.multishop.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.AttachFile;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.BASE64DecodedMultipartFile;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.config.ShopConfig;
import com.yami.shop.multishop.uitls.UploadFileUtils;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.AttachFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Api(tags = "上传文件记录接口")
public class FileController {

    @Autowired
    private AttachFileService attachFileService;
    @Autowired
    private ShopConfig shopConfig;

    @PostMapping("/upload/element")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    public ServerResponseEntity<Long> uploadElementFile(@RequestParam("file") MultipartFile file) throws IOException{
        if(file.isEmpty()){
            return ServerResponseEntity.success();
        }
        AttachFile attachFile = new AttachFile();
        //视频上传
        attachFile.setType(2);
        attachFile.setShopId(SecurityUtils.getShopUser().getShopId());
        attachFile.setUploadTime(new Date());
        Long fileId = attachFileService.mpUploadFile(file.getBytes(),file.getOriginalFilename(),attachFile);
        return ServerResponseEntity.success(fileId);
    }

    @PostMapping("/upload/uploadVideo")
    @ApiOperation(value = "视频上传", notes = "视频上传")
    public ServerResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) throws IOException{
        if(file.isEmpty()){
            return ServerResponseEntity.success();
        }
        AttachFile attachFile = new AttachFile();
        //视频上传
        attachFile.setType(2);
        attachFile.setShopId(Constant.PLATFORM_SHOP_ID);
        attachFile.setUploadTime(new Date());
        String fileName = attachFileService.mpUploadVideo(file.getBytes(),file.getOriginalFilename(),attachFile);
        System.out.println(shopConfig.getDomain().getResourcesDomainName() + "/" + fileName);
        return ServerResponseEntity.success(shopConfig.getDomain().getResourcesDomainName() + "/" + fileName);
    }

    @PostMapping("/upload/img")
    @ApiOperation(value = "上传图片", notes = "上传图片")
    public ServerResponseEntity<Long> uploadImg(@RequestParam("file") MultipartFile file) throws IOException{
        if(file.isEmpty()){
            return ServerResponseEntity.success();
        }
        Long shopId = SecurityUtils.getShopUser().getShopId();
        AttachFile attachFile = new AttachFile();
        String extName = FileUtil.extName(file.getOriginalFilename());
        attachFile.setFileType(extName);
        attachFile.setFileName(FileUtil.mainName(file.getOriginalFilename()));
        attachFile.setType(1);
        attachFile.setShopId(shopId);
        attachFile.setUploadTime(new Date());
        Long fileId = attachFileService.uploadImg(file.getBytes(),attachFile,extName);
        return ServerResponseEntity.success(fileId);
    }

    @PostMapping("/upload/imFile")
    @ApiOperation(value = "上传聊天时的文件", notes = "上传聊天时的文件")
    public ServerResponseEntity<String> uploadImFile(@RequestParam("file") MultipartFile file) throws IOException{
        if(file.isEmpty()){
            return ServerResponseEntity.success();
        }
        String fileName = attachFileService.uploadImFile(file.getBytes(),file.getOriginalFilename());
        return ServerResponseEntity.success(fileName);
    }

    @GetMapping("/attachFilePage")
    @ApiOperation(value = "根据参数分页获取文件记录列表", notes = "根据参数分页获取文件记录列表")
    public ServerResponseEntity<IPage<AttachFile>> attachFilePage(PageParam<AttachFile> page, AttachFile attachFile) {
        attachFile.setShopId(SecurityUtils.getShopUser().getShopId());
        IPage<AttachFile> attachFilePage = attachFileService.getPage(page,attachFile);
        return ServerResponseEntity.success(attachFilePage);
    }

    @DeleteMapping("/deleteFile/{fileId}")
    @ApiOperation(value = "根据文件记录id删除文件", notes = "根据文件记录id删除文件")
    public ServerResponseEntity<Void> deleteFile(@PathVariable("fileId") Long fileId){
        AttachFile attachFile = attachFileService.getById(fileId);
        if (Objects.isNull(attachFile)) {
            return ServerResponseEntity.success();
        }
        if (!Objects.equals(attachFile.getShopId(), SecurityUtils.getShopUser().getShopId())) {
            throw new YamiShopBindException("当前文件不属于你的店铺");
        }
        attachFileService.deleteFile(attachFile.getFilePath());
        return ServerResponseEntity.success();
    }

    @PutMapping("/updateFile")
    @ApiOperation(value = "更改文件记录信息", notes = "更改文件记录信息")
    public ServerResponseEntity<Boolean> updateFile(@RequestBody AttachFile attachFile) {
        if (Objects.isNull(attachFile.getFileName())){
            // 图片名称不能为空
            throw new YamiShopBindException("yami.img.name.exist");
        }
        attachFile.setShopId(SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success(attachFileService.updateFile(attachFile));
    }

    @DeleteMapping("/deleteByIds")
    @ApiOperation(value = "根据文件id列表批量删除文件记录", notes = "根据文件id列表批量删除文件记录")
    public ServerResponseEntity<Void> deleteByIds(@RequestBody List<Long> ids) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        attachFileService.deleteByIdsAndShopId(ids, shopId);
        return ServerResponseEntity.success();
    }

    @PutMapping("/batchMove")
    @ApiOperation(value = "根据文件id列表与分组id批量移动文件", notes = "根据文件id列表与分组id批量移动文件")
    public ServerResponseEntity<Void> batchMove(@RequestBody AttachFile attachFile) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        attachFileService.batchMoveByShopIdAndIdsAndGroupId(shopId, attachFile);
        return ServerResponseEntity.success();
    }

    @GetMapping("/get_file_by_id")
    @ApiOperation(value = "根据文件id获取文件信息")
    public ServerResponseEntity<AttachFile> getFileById(@RequestParam("fileId") Long fileId){
        AttachFile attachFile = attachFileService.getById(fileId);
        Long shopId = SecurityUtils.getShopUser().getShopId();
        if (!Objects.equals(shopId,attachFile.getShopId())){
            throw new YamiShopBindException("yami.file.isNot.shop");
        }
        return ServerResponseEntity.success(attachFile);
    }

    @PostMapping("/save_pdf_file")
    @ApiOperation(value = "保存商家发票")
    @PreAuthorize("@pms.hasPermission('order:orderInvoice:upload')")
    public ServerResponseEntity<AttachFile> savePdfFile(@RequestParam("file") MultipartFile file) throws IOException{
        if(file.isEmpty()){
            return ServerResponseEntity.success();
        }
        Long shopId = SecurityUtils.getShopUser().getShopId();
        AttachFile attachFile = new AttachFile();
        attachFile.setFileType(FileUtil.extName(file.getOriginalFilename()));
        attachFile.setFileName(FileUtil.mainName(file.getOriginalFilename()));
        attachFile.setType(3);
        attachFile.setShopId(shopId);
        attachFile.setUploadTime(new Date());
        Long fileId = attachFileService.mpUploadFile(file.getBytes(),file.getOriginalFilename(),attachFile);
        return ServerResponseEntity.success(attachFileService.getById(fileId));
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

    @PostMapping("/upload/tinymceEditor")
    @ApiOperation(value = "上传文件（返回文件名）", notes = "上传文件（返回文件名）")
    public ServerResponseEntity<String> uploadTinymceEditorImages(@RequestParam("file") MultipartFile editorFile) throws IOException{
        String fileName =  attachFileService.uploadFile(editorFile.getBytes(),editorFile.getOriginalFilename());
        return ServerResponseEntity.success(shopConfig.getDomain().getResourcesDomainName() + "/" + fileName);
    }
}
