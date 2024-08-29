package me.flyray.bsin.server.domain.jcxf;

import lombok.Data;
import me.flyray.bsin.server.domain.TzPayFeeDetail;

import java.util.List;

@Data
public class RepayFeeEntity {

    private List<TzPayFeeDetail> repayList;
}
