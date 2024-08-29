package me.flyray.bsin.server.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.flyray.bsin.server.utils.TreeNode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeptTree extends TreeNode<DeptTreeVo> {

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门未交党费数量
     */
    private Integer noDoneNum;
}
