package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("branch_task_execute")
public class JcxfBranchTaskExecute implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long task_id;

    private Long dept_id;

    private Integer execute_progress;

    private String execute_description;

    private Integer execute_status;

    private Long create_by;

    private Date create_date;

    private Long update_by;

    private Date update_date;

    private Integer del_flag;

    private Integer execute_type;
}
