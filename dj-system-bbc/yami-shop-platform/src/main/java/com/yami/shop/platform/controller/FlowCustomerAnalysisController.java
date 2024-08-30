package com.yami.shop.platform.controller;

import com.yami.shop.bean.dto.flow.CustomerRetainedDTO;
import com.yami.shop.bean.dto.flow.MemberReqDTO;
import com.yami.shop.bean.vo.flow.*;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.CustomerAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 顾客分析接口
 * @author
 */
@Api(tags = "顾客分析接口")
@RestController("flowCustomerAnalysisController")
@RequestMapping("/platform/flowCustomerAnalysis")
public class FlowCustomerAnalysisController {

    @Autowired
    private CustomerAnalysisService customerAnalysisService;


    /**
     * 会员分析，会员概况
     */
    @ApiOperation(value = "会员分析，会员概况", notes = "会员分析，会员概况")
    @GetMapping("/getMemberSurvey")
    public ServerResponseEntity<MemberSurveyRespVO> getMemberSurvey(MemberReqDTO param) {
        MemberSurveyRespVO memberSurveyRespVO = customerAnalysisService.getMemberSurvey(param);
        return ServerResponseEntity.success(memberSurveyRespVO);
    }

    /**
     * // bbc平台/b2c商家 接口
     * 会员分析，会员人数趋势/ 会员占比趋势
     */
    @ApiOperation(value = "会员分析，会员人数趋势/ 会员占比趋势", notes = "会员分析，会员人数趋势/ 会员占比趋势")
    @GetMapping("/getMemberTrend")
    public ServerResponseEntity<List<MemberTrendRespVO>> getMemberTrend(MemberReqDTO param) {
        List<MemberTrendRespVO> resList = customerAnalysisService.getMemberTrend(param);
        return ServerResponseEntity.success(resList);
    }

    /**
     * // bbc平台/b2c商家 接口
     * 导出会员分析，会员人数趋势
     */
    @ApiOperation(value = "导出导出会员分析，会员人数趋势/ 会员占比趋势", notes = "导出会员分析，会员人数趋势/ 会员占比趋势")
    @GetMapping("/memberTrendExport")
    @PreAuthorize("@pms.hasPermission('member:analysis:export')")
    public void memberTrendExport(MemberReqDTO param, HttpServletResponse response) {
        customerAnalysisService.memberTrendExport(param, response);
    }

    /**
     * // bbc平台/b2c商家 接口
     * 会员分析，会员贡献价值分析
     */
    @ApiOperation(value = "会员分析，会员贡献价值分析", notes = "会员分析，会员贡献价值分析")
    @GetMapping("/getMemberVontributeValue")
    public ServerResponseEntity<MemberContributeRespVO> getMemberContributeValue(MemberReqDTO param) {
        MemberContributeRespVO contributeRespVO = customerAnalysisService.getMemberContributeValue(param);
        return ServerResponseEntity.success(contributeRespVO);
    }

    /**
     * // bbc平台/b2c商家 接口
     * 会员分析，新老会员成交分析
     */
    @GetMapping("/getMemberDeal")
    @ApiOperation(value = "会员分析，新老会员成交分析", notes = "会员分析，新老会员成交分析")
    public ServerResponseEntity<MemberDealRespVO> getMemberDeal(MemberReqDTO param) {
        MemberDealRespVO respParam = customerAnalysisService.getMemberDeal(param);
        return ServerResponseEntity.success(respParam);
    }

    /**
     * 客户分析，客户留存分析
     */
    @ApiOperation(value = "客户分析-客户留存分析",notes = "客户分析，客户留存分析,不做周留存数据")
    @GetMapping("/getCustomerRetained")
    public ServerResponseEntity<List<CustomerRetainVO>> getCustomerRetained(CustomerRetainedDTO customerRetainedDTO) {
        Integer dateType = customerRetainedDTO.getDateType();
        Integer dateRetainType = customerRetainedDTO.getDateRetainType();
        List<CustomerRetainVO> respList = new ArrayList<>();
        if (Objects.equals(1,dateType) && !Objects.equals(1,dateRetainType)) {
            // 最近一月，月留存。此时不显示数据
            return ServerResponseEntity.success(respList);
        }
        Integer retainType = customerRetainedDTO.getRetainType();
        respList = customerAnalysisService.getTradeRetained(customerRetainedDTO);
//        if (Objects.equals(1,retainType)) {
//            // 访问留存
//            respList = customerAnalysisService.getVisitRetained(customerRetainedDTO);
//        } else {
//            // 成交留存
//        }
        return ServerResponseEntity.success(respList);
    }
}
