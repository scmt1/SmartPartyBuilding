package com.yami.shop.bean.enums;

public enum RemindStatusEnum {
    /**
     * 开启提醒
     */
    OPEN(1),

    /**
     * 关闭提醒
     */
    CLOSED(0)
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    RemindStatusEnum(Integer num) {
        this.num = num;
    }

    public static RemindStatusEnum instance(Integer value) {
        RemindStatusEnum[] enums = values();
        for (RemindStatusEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
