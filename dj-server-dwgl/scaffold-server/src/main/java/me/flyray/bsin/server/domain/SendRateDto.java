package me.flyray.bsin.server.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class SendRateDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer success;

    private Integer fail;

    private String time;
}
