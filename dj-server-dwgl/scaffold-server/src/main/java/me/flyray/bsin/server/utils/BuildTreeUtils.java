package me.flyray.bsin.server.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BuildTreeUtils {


    /**
     * 构建树节点
     */
    public static <T extends TreeNode> List<T> build(List<T> treeNodes) {
        List<T> result = new ArrayList<>();

        // list转map
        Map<String, T> nodeMap = new LinkedHashMap<>(treeNodes.size());
        for(T treeNode : treeNodes){
            nodeMap.put(treeNode.getId(), treeNode);
        }

        for(T node : nodeMap.values()) {
            T parent = nodeMap.get(node.getParentId());
            if(parent != null && !(node.getId().equals(parent.getId()))){
                parent.getChildren().add(node);
                continue;
            }
            result.add(node);
        }
        return result;
    }

}
