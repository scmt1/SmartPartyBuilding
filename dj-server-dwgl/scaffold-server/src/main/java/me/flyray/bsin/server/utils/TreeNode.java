package me.flyray.bsin.server.utils;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 树节点，所有需要实现树节点的，都需要继承该类
 *
 * @author 阿沐 babamu@126.com
 */
@Data
public class TreeNode<T> implements Serializable {
    /**
     * 主键
     */
    public String id;
    /**
     * 上级ID
     */
    @NotNull(message = "上级ID不能为空")
    public String parentId;
    /**
     * 子节点列表
     */
    protected List<T> children = new ArrayList<>();
}
