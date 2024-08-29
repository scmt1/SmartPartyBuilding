/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.server.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.obs.services.ObsClient;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.facade.service.AttachFileService;

import me.flyray.bsin.server.common.bean.*;
import me.flyray.bsin.server.common.config.ShopConfig;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.AttachFile;
import me.flyray.bsin.server.common.enums.QiniuZone;
import me.flyray.bsin.server.mapper.AttachFileMapper;
import me.flyray.bsin.server.utils.BASE64DecodedMultipartFile;
import me.flyray.bsin.server.utils.RespBodyHandler;
import me.flyray.bsin.server.utils.UploadFileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author lgh
 * @date 2018/07/27
 */
@Slf4j
@Service
public class AttachFileServiceImpl  extends ServiceImpl<AttachFileMapper, AttachFile> implements AttachFileService{

    @Autowired
    private AttachFileMapper attachFileMapper;


    @Autowired
    private ShopConfig shopConfig;

    public final static String NORM_MONTH_PATTERN = "yyyy/MM/";

    @Autowired
    @Value("${yami.expose.operation.auth:}")
    private Boolean permission;

    /**
     * 认证信息实例
     * @return
     */
    private Auth auth(Qiniu qiniu) {
        return Auth.create(qiniu.getAccessKey(), qiniu.getSecretKey());
    }

    /**
     * 视频上传
     * @param requestMap
     * @return
     * @throws IOException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public  Map<String, Object> mpUploadVideo( Map<String, Object> requestMap) throws IOException {
       byte[] bytes= (byte[]) requestMap.get("data");
        String originalName = (String) requestMap.get("originalName");
        AttachFile attachFile = (AttachFile) requestMap.get("attachFile");

        String extName = FileUtil.extName(originalName);
        String imgName = FileUtil.mainName(originalName);
        String filePath =randomFilePath(extName);
        attachFile.setFilePath(filePath);
        attachFile.setFileName(imgName);
        //attachFileMapper.save(attachFile);
        upload(bytes,filePath,extName);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(filePath));
    }

    /**
     * 图片上传
     * @param requestMap
     * @return
     * @throws IOException
     */
    @Override
    public Map<String, Object> uploadImages(Map<String, Object> requestMap) throws IOException {
        String imgUrl= (String) requestMap.get("imgUrl");
        //base64 转文件
        MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(imgUrl);
        //文件存储在nginx代理路径下
        String fileName = UploadFileUtils.uploadFile(imgFile);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(fileName));
    }

    @Override
    public Map<String, Object> uploadTinymceEditor(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

        String fileStr = (String) map.get("file");
        String name = (String) map.get("name");
        try {
            //base64 转文件
            MultipartFile file = BASE64DecodedMultipartFile.base64ToMultipart(fileStr);
            //文件存储在nginx代理路径下
            AttachFile attachFile = UploadFileUtils.uploadFile2(file, name);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(attachFile));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"上传错误");
        }

    }

    @Override
    public Map<String, Object> uploadVideo(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            String fileStr = (String) map.get("file");
            String name = (String) map.get("name");
            MultipartFile file = BASE64DecodedMultipartFile.base64ToMultipart(fileStr);

            if(file.isEmpty()) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未获取到文件");
            }

            AttachFile attachFile = UploadFileUtils.uploadFile2(file, name);
            //视频上传
            attachFile.setType(2);
            // attachFile.setShopId(Constant.PLATFORM_SHOP_ID);
            attachFile.setUploadTime(new Date());
            //String fileName = mpUploadVideo(file.getBytes(),file.getOriginalFilename(), attachFile, name);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(attachFile));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"上传错误");
        }

    }

    @Override
    public Map<String, Object> getFileDateForDown(Map<String, Object> requestMap) throws IOException {
        try {
            String url = (String) requestMap.get("url");

            URL url1 = new URL(url);
            URLConnection connection = url1.openConnection();
            InputStream inputStream = connection.getInputStream();

            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            byte[] temp = new byte[1024];
            int size = 0;
            while((size = inputStream.read(temp)) != -1){
                out.write(temp, 0, size);
            }
            inputStream.close();
            byte[] content = out.toByteArray();
            return RespBodyHandler.setRespBodyDto(content);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"下载异常");
        }

    }

    @Override
    public Map<String, Object> downloadLocalFile(Map<String, Object> requestMap) throws IOException {
        try {
            String url = (String) requestMap.get("url");
            //读取本地的文件
            InputStream inputStream = new ClassPathResource(url).getStream();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"下载异常");
        }
    }

    public String mpUploadVideo(byte[] bytes,String originalName,AttachFile attachFile) throws IOException {
        String extName = FileUtil.extName(originalName);
        String imgName = FileUtil.mainName(originalName);
        String filePath =randomFilePath(extName);
        attachFile.setFilePath(filePath);
        attachFile.setFileName(imgName);
        //attachFileMapper.save(attachFile);
        upload(bytes,filePath,extName);
        return filePath;
    }

    public String mpUploadVideo(byte[] bytes,String originalName,AttachFile attachFile, String fileOrgName) throws IOException {
        String extName = fileOrgName.split("\\.")[1];
        String imgName = fileOrgName.split("\\.")[0];
        String filePath = randomFilePath(extName);
        attachFile.setFilePath(filePath);
        attachFile.setFileName(imgName);
        //attachFileMapper.save(attachFile);
        upload(bytes,filePath,extName);
        return filePath;
    }

    private void upload(byte[] bytes, String filePath, String extName) throws IOException {
        System.out.println("进入");
        if (uploadQiniu(bytes, filePath)) {
            return;
        }

        System.out.println("进2");
        if (uploadAliOss(bytes, filePath)){
            return;
        }

        System.out.println("进入3");
        if (uploadQCloud(bytes, filePath, extName)) {
            return;
        }

        System.out.println("进入4");
        if (uploadHuaWeiOss(bytes, filePath)) {
            return;
        }

        uploadMinio(bytes, filePath);
    }

    private void uploadMinio(byte[] bytes, String filePath) throws IOException {
        Minio minio = shopConfig.getMinio();
        if (Objects.nonNull(minio) && BooleanUtil.isTrue(minio.getIsOpen())) {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minio.getEndpoint())
                    .credentials(minio.getAccessKey(), minio.getSecretKey())
                    .build();
            InputStream input = null;
            try {
                input = new ByteArrayInputStream(bytes);
                minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minio.getBucketName())
                            .stream(input, input.available(), -1)
                            .object(filePath)
                            .build()
                );
            } catch (Exception e) {
                log.error("minio上传文件错误：", e);
            } finally {
                if (Objects.nonNull(input)) {
                    input.close();
                }
            }
        }
    }

    private boolean uploadHuaWeiOss(byte[] bytes, String filePath) throws IOException {
        HuaWeiOss huaWeiOss = shopConfig.getHuaWeiObs();
        if (Objects.nonNull(huaWeiOss) && BooleanUtil.isTrue(huaWeiOss.getIsOpen())){
            // 创建ObsClient实例
            ObsClient obsClient = new ObsClient(huaWeiOss.getAccessKeyId(), huaWeiOss.getSecretAccessKey(), huaWeiOss.getEndpoint());
            InputStream input = null;
            try {
                input = new ByteArrayInputStream(bytes);
                obsClient.putObject(huaWeiOss.getBucketName(), filePath, input);
            } catch (Exception e) {
                log.error("华为Oss上传文件错误：", e);
            } finally {
                obsClient.close();
                if (Objects.nonNull(input)) {
                    input.close();
                }
            }
            return true;
        }
        return false;
    }

    private boolean uploadQCloud(byte[] bytes, String filePath, String extName) throws IOException {
        QCloud qCloud = shopConfig.getQCloud();
        if (Objects.nonNull(qCloud) && BooleanUtil.isTrue(qCloud.getIsOpen())){
            //初始化cosClient
            COSClient cosClient = cosClient(qCloud);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(bytes.length);
            objectMetadata.setContentType(extName);
            ByteArrayInputStream input = null;
            try {
                input = new ByteArrayInputStream(bytes);
                com.qcloud.cos.model.PutObjectRequest putObjectRequest =
                        new com.qcloud.cos.model.PutObjectRequest(qCloud.getBucketName(), filePath, input, objectMetadata);
                cosClient.putObject(putObjectRequest);
            } catch (Exception e){
                log.error("腾讯云Oss上传文件错误：", e);
            } finally {
                cosClient.shutdown();
                if (Objects.nonNull(input)) {
                    input.close();
                }
            }
            return true;
        }
        return false;
    }

    private boolean uploadAliOss(byte[] bytes, String filePath) throws IOException {
        AliOss aliOss = shopConfig.getAliOss();
        if (Objects.nonNull(aliOss.getIsOpen()) && BooleanUtil.isTrue(aliOss.getIsOpen())) {
            OSS ossClient = new OSSClientBuilder().build(aliOss.getEndpoint(), aliOss.getAccessKeyId(), aliOss.getAccessKeySecret());
            InputStream input = null;
            try {
                input = new ByteArrayInputStream(bytes);
                ossClient.putObject(new PutObjectRequest(aliOss.getBucketName(), filePath, input));
            } catch (Exception e) {
                log.error("阿里云Oss上传文件错误：", e);
            } finally {
                ossClient.shutdown();
                if (Objects.nonNull(input)) {
                    input.close();
                }
            }
            return true;
        }
        return false;
    }

    private boolean uploadQiniu(byte[] bytes, String filePath) throws QiniuException {
        Qiniu qiniu = shopConfig.getQiniu();
        if (Objects.nonNull(qiniu) && BooleanUtil.isTrue(qiniu.getIsOpen())) {
            String upToken = auth(qiniu).uploadToken(qiniu.getBucket(), filePath);
            uploadManager(qiniu).put(bytes, filePath, upToken);
            return true;
        }
        return false;
    }
    /**
     * 构建一个七牛上传工具实例
     */
    private UploadManager uploadManager(Qiniu qiniu) {
        return new UploadManager(qiniuConfig(qiniu));
    }
    /**
     * 根据配置文件选择机房
     */
    private com.qiniu.storage.Configuration qiniuConfig(Qiniu qiniu) {

        Region region = null;
        if (Objects.equals(qiniu.getZone(), QiniuZone.HUA_BEI)) {
            region = Region.huabei();
        } else if (Objects.equals(qiniu.getZone(), QiniuZone.HUA_DONG)) {
            region = Region.huadong();
        } else if (Objects.equals(qiniu.getZone(), QiniuZone.HUA_NAN)) {
            region = Region.huanan();
        } else if (Objects.equals(qiniu.getZone(), QiniuZone.BEI_MEI)) {
            region = Region.beimei();
        } else if (Objects.equals(qiniu.getZone(), QiniuZone.XIN_JIA_PO)) {
            region = Region.xinjiapo();
        }
        return new com.qiniu.storage.Configuration(region);
    }
    /**
     * 创建腾讯云cos client
     * @return
     */
    private COSClient cosClient(QCloud qCloud){
        BasicCOSCredentials cred = new BasicCOSCredentials(qCloud.getSecretId(), qCloud.getSecretKey());
        com.qcloud.cos.region.Region region = new com.qcloud.cos.region.Region(qCloud.getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        //https协议
        clientConfig.setHttpProtocol(HttpProtocol.https);
        return new COSClient(cred, clientConfig);
    }

    private String randomFilePath(String fileType) {
        return DateUtil.format(new Date(), NORM_MONTH_PATTERN)+ IdUtil.simpleUUID() + "." + fileType;
    }



}
