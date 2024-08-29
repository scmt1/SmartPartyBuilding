package me.flyray.bsin.server.domain.jcxf;

import lombok.Data;
import me.flyray.bsin.server.domain.TzPayFeeDetail;

import java.util.List;

@Data
public class PayFeeImportEntity {
    private String deptId;

    private String payMonth;

    private List<TzPayFeeDetail> payPersonList;
}
