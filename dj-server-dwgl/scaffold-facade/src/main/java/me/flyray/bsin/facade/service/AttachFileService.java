/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.facade.service;


import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author lgh
 * @date 2018/07/27
 */
@Path("attachFile")
public interface AttachFileService  {

    /**
     * 视频上传
     */
    @POST
    @Path("mpUploadVideo")
    @Produces("application/json")
    Map<String, Object> mpUploadVideo(Map<String, Object> requestMap) throws IOException;
    /**
     * 图片上传
     */
    @POST
    @Path("uploadImages")
    @Produces("application/json")
    Map<String, Object> uploadImages(Map<String, Object> requestMap) throws IOException;

    @POST
    @Path("uploadTinymceEditor")
    @Produces("application/json")
    Map<String, Object> uploadTinymceEditor(Map<String, Object> requestMap) throws IOException;


    @POST
    @Path("uploadVideo")
    @Produces("application/json")
    Map<String, Object> uploadVideo(Map<String, Object> requestMap) throws IOException;

    @POST
    @Path("getFileDateForDown")
    @Produces("application/json")
    Map<String, Object> getFileDateForDown(Map<String, Object> requestMap) throws IOException;

    @POST
    @Path("downloadLocalFile")
    @Produces("application/json")
    Map<String, Object> downloadLocalFile(Map<String, Object> requestMap) throws IOException;
}
