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

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;
import java.util.Objects;

/**
 * 国际化 语言枚举类
 * @author LGH
 */
@Getter
@AllArgsConstructor
public enum LanguageEnum {

    /**
     * 简体中文
     */
    LANGUAGE_ZH_CN("zh", 0),
    /**
     * 英文
     */
    LANGUAGE_EN("en", 1)

    ;

    private String language;

    private Integer lang;


    /**
     * 获取指定语言类型(如果没有对应的语言类型,则返回中文)
     *
     * @param language 语言类型
     * @return
     */
    public static String getLanguageType(String language){
        if (StrUtil.isEmpty(language)) {
            return LANGUAGE_ZH_CN.language;
        }
        for (LanguageEnum languageEnum : LanguageEnum.values()) {
            if (languageEnum.language.equalsIgnoreCase(language)) {
                return languageEnum.language;
            }
        }
        return LANGUAGE_ZH_CN.language;
    }


    public static LanguageEnum valueOf(int lang) {
        for (LanguageEnum languageEnum : values()) {
            if (languageEnum.lang == lang) {
                return languageEnum;
            }
        }
        return LanguageEnum.LANGUAGE_ZH_CN;
    }

    public static LanguageEnum valueOf(Locale locale) {
        for (LanguageEnum languageEnum : values()) {
            if (Objects.equals(languageEnum.language, locale.toString())) {
                return languageEnum;
            }
        }
        return LanguageEnum.LANGUAGE_ZH_CN;
    }

}
