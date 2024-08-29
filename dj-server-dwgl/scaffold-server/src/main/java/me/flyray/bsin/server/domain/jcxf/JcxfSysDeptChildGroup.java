package me.flyray.bsin.server.domain.jcxf;

import lombok.Data;

import java.util.List;

@Data
public class JcxfSysDeptChildGroup {

    private Integer id;

    private String name;

    private List<Integer> childIds;

}
