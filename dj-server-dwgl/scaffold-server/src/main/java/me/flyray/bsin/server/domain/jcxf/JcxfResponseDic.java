package me.flyray.bsin.server.domain.jcxf;

import lombok.Data;

import java.io.Serializable;

@Data
public class JcxfResponseDic implements Serializable {

    private static final long serialVersionUID = 1L;

    private String label;

    private String itemValue;

}
