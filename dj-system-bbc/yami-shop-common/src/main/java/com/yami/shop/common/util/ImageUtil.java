/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.util;

import cn.hutool.core.util.StrUtil;
import com.yami.shop.common.exception.YamiShopBindException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * 图片处理工具类
 * @author yami
 */
public class ImageUtil {

    /**
     * 将图片转为二进制数组
     * @param imgUrl
     * @return
     */
    public static byte[] imgToBinary(String imgUrl) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new URL(imgUrl));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String suffix = imgUrlFileType(imgUrl);
            //ImageIO无法写入jpeg文件 报Invalid argument to native writeImage，需重画
            if(StrUtil.equals(suffix, "jpg") || StrUtil.equals(suffix, "jpeg")){
                BufferedImage tag;
                tag = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_BGR);
                Graphics g = tag.getGraphics();
                g.drawImage(bufferedImage, 0, 0, null);
                g.dispose();
                bufferedImage = tag;
            }
            ImageIO.write(bufferedImage, suffix, baos);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (IOException e) {
            // 图片丢失，请重新上传图片
            throw new YamiShopBindException("yami.img.lose");
        }
    }

    /**
     * @param imgUrl
     * @return 文件得后缀，文件类型 jpg ,  png , ...
     */
    public static String imgUrlFileType(String imgUrl) {
        if (StrUtil.isBlank(imgUrl)) {
            return imgUrl;
        }
        imgUrl.trim();
        String[] split = imgUrl.split("\\.");
        String s = split[split.length - 1];
        return s;
    }

    /**
     * @param imgUrl
     * @return 获取文件名称
     */
    public static String imgUrlFileName(String imgUrl) {
        if (StrUtil.isBlank(imgUrl)) {
            return imgUrl;
        }
        imgUrl.trim();
        String[] split = imgUrl.split("/");
        String s = split[split.length - 1];
        return s;
    }
    /**
     * @param imgUrl
     * @return 获取文件名称 45d3631e97d8438d80f9db1369595b8c
     */
    public static String imgUrlFileNameNoSuffix(String imgUrl) {
        if (StrUtil.isBlank(imgUrl)) {
            return imgUrl;
        }
        imgUrl.trim();
        String[] split = imgUrl.split("/");
        String s = split[split.length - 1];
        String[] split1 = s.split("\\.");
        return split1[0];
    }
}
