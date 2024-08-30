package com.yami.shop.bean.enums;

public enum ProdWriteOffNumEnum {

    WRITE_OFF_MULTIPLE(-1, "多次核销"),
    WRITE_OFF_NOT(0, "无需核销"),
    WRITE_OFF_SINGLE(1, "单次核销");

    private Integer num;
    private String desc;


    ProdWriteOffNumEnum(Integer num, String desc) {
        this.num = num;
        this.desc = desc;
    }

    public Integer value() {
        return num;
    }

    public String desc() {
        return desc;
    }

    public static ProdWriteOffNumEnum instance(Integer value) {
        ProdWriteOffNumEnum[] enums = values();
        for (ProdWriteOffNumEnum writeOffNumEnum : enums) {
            if (writeOffNumEnum.value().equals(value)) {
                return writeOffNumEnum;
            }
        }
        return null;
    }
}
