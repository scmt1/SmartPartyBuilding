/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.server.common.bean;

/**
 * @author Citrus
 * @date 2021/8/6 10:25
 */
public class SensitiveWord {
    /**
     * 敏感词
     */
    private String sensitiveWord;

    public String getSensitiveWord() {
        return sensitiveWord;
    }

    public void setSensitiveWord(String sensitiveWord) {
        this.sensitiveWord = sensitiveWord;
    }

    @Override
    public String toString() {
        return "SensitiveWord{" +
                "sensitiveWord='" + sensitiveWord + '\'' +
                '}';
    }
}
