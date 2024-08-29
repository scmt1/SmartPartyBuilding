/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.server.i18n;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

/**
 * 多语言国际化消息工具类
 * @author yami
 */
@Slf4j
public class I18nMessage {
    private static MessageSourceAccessor accessor;

//    private static final String BASE_FOLDE = "i18n";

    private static final String BASE_NAME = "i18n/messages";

    static{
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasenames(BASE_NAME);
        reloadableResourceBundleMessageSource.setCacheSeconds(5);
        reloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8");
        accessor = new MessageSourceAccessor(reloadableResourceBundleMessageSource);
    }


//    /**
//     * 获取一条语言配置信息
//     *
//     * @param message 配置信息属性名,eg: api.response.code.user.signUp
//     * @return
//     */
//    public static String getMessage(String message) {
//        Locale locale = LocaleContextHolder.getLocale();
//        return I18nMessage.accessor.getMessage(message,locale);
//    }

    /**
     * 获取一条语言配置信息
     *
     * @param message 配置信息属性名,eg: api.response.code.user.signUp
     * @return
     */
    public static String getMessage(String message) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return accessor.getMessage(message,locale);
        }catch (Exception e){
            return message;
        }

    }
    /**
     * 获取一条语言配置信息（后台管理）
     *
     * @return
     * @throws IOException
     */
    public static Integer getLang() {
        Locale locale = LocaleContextHolder.getLocale();
        return LanguageEnum.valueOf(locale).getLang();
    }

    /**
     * 获取一条语言配置信息(小程序、pc)
     *
     * @return
     * @throws IOException
     */
    public static Integer getDbLang() {
        Integer lang = getLang();
        if (Objects.equals(lang, 0)) {
            return LanguageEnum.LANGUAGE_ZH_CN.getLang();
        }
        return lang;
    }

//    /**
//     * 获取文件夹下所有的国际化文件名
//     *
//     * @param folderName 文件名
//     * @return
//     * @throws IOException
//     */
//    private static String[] getAllBaseNames(final String folderName) throws IOException {
//        URL url = Thread.currentThread().getContextClassLoader()
//                .getResource(folderName);
//        if (null == url) {
//            throw new RuntimeException("无法获取资源文件路径");
//        }
//
//        List<String> baseNames = new ArrayList<>();
//        if (url.getProtocol().equalsIgnoreCase("file")) {
//            // 文件夹形式,用File获取资源路径
//            File file = new File(url.getFile());
//            if (file.exists() && file.isDirectory()) {
//                baseNames = Files.walk(file.toPath())
//                        .filter(path -> path.toFile().isFile())
//                        .map(Path::toString)
//                        .map(path -> path.substring(path.indexOf(folderName)))
//                        .map(I18nMessage::getI18FileName)
//                        .distinct()
//                        .collect(Collectors.toList());
//            } else {
//                log.error("指定的baseFile不存在或者不是文件夹");
//            }
//        }
//        return baseNames.toArray(new String[0]);
//    }
//
//    /**
//     * 把普通文件名转换成国际化文件名
//     *
//     * @param filename
//     * @return
//     */
//    private static String getI18FileName(String filename) {
//        filename = filename.replace(".properties", "");
////        for (int i = 0; i < 2; i++) {
////            int index = filename.lastIndexOf("_");
////            if (index != -1) {
////                filename = filename.substring(0, index);
////            }
////        }
//        return filename.replace("\\", "/");
//    }

}
