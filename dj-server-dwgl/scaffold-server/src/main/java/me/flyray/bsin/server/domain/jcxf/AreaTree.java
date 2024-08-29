package me.flyray.bsin.server.domain.jcxf;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AreaTree implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long areaId;

    private String name;

    private Long parentAreaId;

    private List<AreaTree> children;



}
