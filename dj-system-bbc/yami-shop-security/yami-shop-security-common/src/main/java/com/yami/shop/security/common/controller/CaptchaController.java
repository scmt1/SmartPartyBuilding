/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.IdUtil;
import com.anji.captcha.model.common.RepCodeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.IpInfoUtil;
import com.yami.shop.security.common.redis.RedisTemplateHelper;
import com.yami.shop.security.common.util.CreateVerifyCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author FrozenWatermelon
 * @date 2020/7/30
 */
@RestController
@RequestMapping("/captcha")
@Api(tags = "验证码")
public class CaptchaController {

    private final CaptchaService captchaService;

  /*  @Autowired
    private RedisTemplateHelper redisTemplate;*/

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IpInfoUtil ipInfoUtil;
    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @PostMapping({ "/get" })
    public ServerResponseEntity<ResponseModel> get(@RequestBody CaptchaVO captchaVO) {
        return ServerResponseEntity.success(captchaService.get(captchaVO));
    }

    @PostMapping({ "/check" })
    public ServerResponseEntity<ResponseModel> check(@RequestBody CaptchaVO captchaVO) {
        ResponseModel responseModel;
        try {
            responseModel = captchaService.check(captchaVO);
        }catch (Exception e) {
            return ServerResponseEntity.success(ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_COORDINATE_ERROR));
        }
        return ServerResponseEntity.success(responseModel);
    }
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    @ApiOperation(value = "初始化验证码")
    public ServerResponseEntity<Object> initCaptcha() {
/*
        String captchaId = IdUtil.simpleUUID();
        String code = new CreateVerifyCode().randomStr(4);
        // 缓存验证码
        redisTemplate.set(captchaId, code, 2L, TimeUnit.MINUTES);
        return ServerResponseEntity.success(captchaId);*/

        CircleCaptcha gifCaptcha = CaptchaUtil.createCircleCaptcha(160, 60,4,1);
        String imageBase64 = gifCaptcha.getImageBase64();

        String uuid = IdUtil.fastSimpleUUID();
        redisTemplate.opsForValue().set(uuid,gifCaptcha.getCode(),5,TimeUnit.MINUTES);
        Map<String,String> map  =new HashMap<String,String>();
        map.put("imageBase64",imageBase64);
        map.put("uuid",uuid);
        return ServerResponseEntity.success(map);

    }

    @RequestMapping(value = "/draw/{captchaId}", method = RequestMethod.GET)
    @ApiOperation(value = "根据验证码ID获取图片")
    public ServerResponseEntity<Object> drawCaptcha(@PathVariable("captchaId") String captchaId,
                            HttpServletResponse response) throws Exception {
      //
      //  // 得到验证码 生成指定验证码
      //  String code = redisTemplate.get(captchaId);
      //  CreateVerifyCode vCode = new CreateVerifyCode(116, 36, 4, 10, code);
      //
      //  //CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(160, 60);
      //  GifCaptcha gifCaptcha = CaptchaUtil.createGifCaptcha(160, 60);
      //  String imageBase64 = gifCaptcha.getImageBase64();
      //
      //  String uuid = IdUtil.fastSimpleUUID();
      ///*  response.setContentType("image/png");
      //  vCode.write(response.getOutputStream());*/
      //  redisTemplate2.opsForValue().set(uuid,gifCaptcha.getCode(),5,TimeUnit.MINUTES);
      //  return ServerResponseEntity.success(imageBase64);
        return null;
    }
}
