/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.platform.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yami.shop.bean.model.User;
import com.yami.shop.bean.param.*;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.i18n.LanguageEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.Json;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.common.util.PoiExcelUtil;
import com.yami.shop.common.util.PrincipalUtil;
//import com.yami.shop.coupon.common.service.CouponUserService;
import com.yami.shop.platform.task.AnalysisSalseTask;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.common.service.AppConnectService;
import com.yami.shop.service.UserService;
import com.yami.shop.user.common.model.UserLevel;
import com.yami.shop.user.common.service.UserLevelService;
import com.yami.shop.user.common.service.UserTagUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * @author lgh on 2018/10/16.
 */
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
@Api(tags = "用户")
public class UserController {

    @Value("${yami.expose.operation.auth:}")
    private Boolean permission;

    @Autowired
    private UserService userService;

    @Autowired
    private AppConnectService appConnectService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private UserTagUserService userTagUserService;

//    @Autowired
//    private CouponUserService couponUserService;

    @Autowired
    private UserLevelService userLevelService;

    @Autowired
    private MapperFacade mapperFacade;

    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('plateform:user:page')")
    @ApiOperation(value = "分页获取用户基本信息", notes = "分页获取用户基本信息")
    public ServerResponseEntity<IPage<User>> page(User user, PageParam<User> page) {
        IPage<User> userPage = userService.getUserPage(page, user);
        return ServerResponseEntity.success(userPage);
    }

//    @GetMapping("/info/{userId}")
//    @PreAuthorize("@pms.hasPermission('plateform:user:info')")
//    @ApiOperation(value = "获取信息", notes = "获取信息")
//    public ServerResponseEntity<UserManagerParam> info(@PathVariable("userId") String userId) {
//        UserManagerParam param = userService.getuserInfo(userId);
//        List<UserTagParam> userTagParams = userTagUserService.getUserTagsUserByUserId(userId);
//        param.setUserTagParam(userTagParams);
//        CouponUserParam couponUser = couponUserService.getCouponCountByUserId(userId);
//        param.setCouponUserParam(couponUser);
//        return ServerResponseEntity.success(param);
//    }

    @PutMapping
    @PreAuthorize("@pms.hasPermission('plateform:user:update')")
    @ApiOperation(value = "修改", notes = "修改")
    public ServerResponseEntity<Void> update(@RequestBody User user) {
        user.setModifyTime(new Date());
        user.setNickName(user.getNickName());
        userService.updateById(user);
        tokenStore.deleteAllToken(SysTypeEnum.ORDINARY.value().toString(), user.getUserId());
        return ServerResponseEntity.success();
    }

    @GetMapping("/userPage")
    @ApiOperation(value = "分页获取用户及扩展信息", notes = "分页获取用户及扩展信息")
    public ServerResponseEntity<IPage<UserManagerParam>> userPage(PageParam<User> page, UserManagerReqParam user) {
        IPage<UserManagerParam> userPage = userService.getUserInfoPage(page, user);
        return ServerResponseEntity.success(userPage);
    }

    @GetMapping("/importUserModel")
    @ApiOperation(value = "用户导出模板", notes = "用户导出模板")
    public void importRetailProdModel(HttpServletResponse response) {
        // 用户导出模板
        userService.downloadUserModel(response);
    }

    @ApiOperation(value = "导入文件", notes = "导入文件")
    @PostMapping("/importExcel")
    @ResponseBody
    @PreAuthorize("@pms.hasPermission('platform:user:import')")
    public ServerResponseEntity importExcel(@RequestParam("excelFile") MultipartFile excelFile) throws Exception {
        if (Objects.isNull(excelFile)) {
            // 网络繁忙，请稍后重试
            throw new YamiShopBindException("yami.network.busy");
        }
        // 需要判断成长值所在等级
        List<UserLevel> list = userLevelService.list();
        List<UserLevelParam> userLevels = mapperFacade.mapAsList(list, UserLevelParam.class);
        UserExcelParam param = userService.parseUserImportFile(excelFile, userLevels);
        if (param.getImmediately()) {
            return ServerResponseEntity.success(param.getMsg());
        }
        Integer registerNum = 0;
        //重复数据的set
        Set<String> repeatPhoneSet = new HashSet<>();
        Set<String> repeatMailSet = new HashSet<>();
        if (param.getSuccess()) {
            // 获取到参数
            List<UserRegisterExcelParam> userList = Json.parseArray(param.getParam(), UserRegisterExcelParam[].class);
            // 批量注册用户
            registerNum = appConnectService.batchRegisterUser(param, userList, repeatPhoneSet, repeatMailSet);
        }
        Integer lang = I18nMessage.getDbLang();
        param.setMsg(getMsg(param, registerNum, repeatPhoneSet, repeatMailSet, lang));
        return ServerResponseEntity.success(param.getMsg());
    }

    @GetMapping("/exportUser")
    @ApiOperation(value = "导出用户信息", notes = "导出用户信息")
    public void exportUser(UserManagerReqParam user, HttpServletResponse response) {
        // 开始时间
        long start = System.currentTimeMillis();
        PageParam<User> userPageParam = new PageParam<>();
        userPageParam.setCurrent(1);
        userPageParam.setSize(10L);
        IPage<UserManagerParam> userPage = userService.getUserInfoPage(userPageParam, user);
        // 总共有多少条数据
        Long total = userPage.getTotal();
        // 用户有很多，考虑2000条以上数据的导出 一个最多104w 行数据
        Long rowMaxCount = 500000L;
        // 每一次查询条数
        Long eachCount = 1000L;
        // 这里不用PageParam<User> ，为了方便自由调整查询条数
        Page<User> pages = new Page<>();
        pages.setCurrent(1);

        List<UserManagerParam> list = new ArrayList<>();
        // 查询记录数
        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getBigWriter();
        //
        String fileName = I18nMessage.getMessage("yami.excel.user.info") + ".xlsx";
        if (total <= rowMaxCount) {
            if (total <= eachCount) {
                pages.setSize(total);
                userPage = userService.getUserInfoPage(pages, user);
                exportExcel(userPage.getRecords(), 1, 1, response, writer);
                // 导出
                PoiExcelUtil.writeExcel(fileName, writer, response);
            } else {
                // 开启多线程查询，每eachCount(100)行数据为一个线程开始
                int pageSize = getPageSize(total, eachCount);
//                ExecutorService execservice = new ThreadPoolExecutor(4,10,200L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(10));
                ExecutorService execservice = Executors.newFixedThreadPool(15);
                try {
                    List<Callable<List<UserManagerParam>>> tasks = new ArrayList<Callable<List<UserManagerParam>>>();
                    for (int i = 1; i <= pageSize; i++) {
                        Page<User> pagesIndex = new Page<>();
                        pagesIndex.setCurrent(i);
                        pagesIndex.setSize(eachCount);
                        Callable<List<UserManagerParam>> task = new AnalysisSalseTask(userService, user, pagesIndex);
                        tasks.add(task);
                    }
                    List<Future<List<UserManagerParam>>> futures = execservice.invokeAll(tasks);
                    if (futures != null && futures.size() > 0) {
                        for (Future<List<UserManagerParam>> future : futures) {
                            list.addAll(future.get());
                        }
                    }
                    execservice.shutdown();
                    long end = System.currentTimeMillis();
                    System.out.println("线程查询数据用时:" + (end - start) + "ms");
                } catch (Exception e) {
                    System.out.println("多线程查询异常");
                }
                userPage.setRecords(list);
                exportExcel(list, 1, 1, response, writer);
                // 导出
                PoiExcelUtil.writeExcel(fileName, writer, response);
                list.clear();
            }
        } else {
            // 分多少个 sheet
            exportBigData(user, response, total, writer, fileName);
        }


    }

    private void exportBigData(UserManagerReqParam user, HttpServletResponse response, Long total, ExcelWriter writer, String fileName) {
        Long rowMaxCount = 500000L;
        List<UserManagerParam> list = new ArrayList<>();
        Long eachCount;
        int pageSize = getPageSize(total, rowMaxCount);
        eachCount = 1000L;
        List<String> filepaths = new ArrayList<>();
        for (int i = 1; i <= pageSize; i++) {
            list.clear();
            int size = getPageSize(rowMaxCount, eachCount);
            try {
                ExecutorService execservice = Executors.newFixedThreadPool(15);
                List<Callable<List<UserManagerParam>>> tasks = new ArrayList<Callable<List<UserManagerParam>>>();
                for (int j = 1; j <= size; j++) {
                    Page<User> pagesIndex = new Page<>();
                    pagesIndex.setSize(eachCount);
                    pagesIndex.setCurrent((i - 1) * size + j);
                    Callable<List<UserManagerParam>> task = new AnalysisSalseTask(userService, user, pagesIndex);
                    tasks.add(task);
                }
                List<Future<List<UserManagerParam>>> futures = execservice.invokeAll(tasks);
                if (futures.size() > 0) {
                    for (Future<List<UserManagerParam>> future : futures) {
                        list.addAll(future.get());
                    }
                }
                tasks.clear();
                execservice.shutdown();
            } catch (Exception e) {
                System.out.println("多线程查询异常");
            }
            // 序号 (i-1) * rowMaxCount + 1
            int rowStart = new BigDecimal(i - 1).multiply(new BigDecimal(rowMaxCount)).add(new BigDecimal(1)).intValue();
            //方法1： 导出到一个临时文件，
            // 然后合并小于50W行的Excel到100W行为一个Excel文件,此处逃过，直接是100W行为一个文件
            // 然后将100W的每一个Excel文件进行压缩

            // 方法2：分多个sheet 导出
            // 此时达到 rowMaxCount 行数据 list 导出到excel
            exportExcel(list, rowStart, i - 1, response, writer);
            list.clear();
        }
        // 导出
        PoiExcelUtil.writeExcel(fileName, writer, response);
    }

    private int getPageSize(Long total, Long eachCount) {
        int pageSize = new BigDecimal(total).divide(new BigDecimal(eachCount), 1).intValue();
        int mod = new BigDecimal(total).divideAndRemainder(new BigDecimal(eachCount))[1].intValue();
        if (mod > 0) {
            pageSize = pageSize + 1;
        }
        return pageSize;
    }

    private String generateKey(Map<String, String> values) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(values.toString().getBytes(StandardCharsets.UTF_8));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (NoSuchAlgorithmException nsae) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).", nsae);
        }
    }

    @NotNull
    private String getMsg(UserExcelParam param, Integer registerNum, Set<String> repeatPhoneSet, Set<String> repeatMailSet, Integer lang) {
        String msg;
        if (Objects.equals(lang, LanguageEnum.LANGUAGE_ZH_CN.getLang())) {
            String str = CollectionUtils.isNotEmpty(param.getErrorRowInfos()) ? "错误数据信息" + Json.toJsonString(param.getErrorRowInfos()) : "";
            String repeatPhone = CollUtil.isNotEmpty(repeatPhoneSet) ? "重复的手机号" + repeatPhoneSet.toString() + "," : "";
            String repeatMail = CollUtil.isNotEmpty(repeatMailSet) ? "重复的邮箱" + repeatMailSet.toString() + "," : "";
            msg = "检查到" + param.getTotal() + "条用户，正确格式数据有" + param.getSuccessNum() + "条！错误" + param.getErrorNum() + "条用户信息！" +
                    (param.getSuccessNum() - registerNum) + "条数据已存在！" + repeatPhone + repeatMail + "注册成功" + registerNum + "条数据！" + str;
        } else {
            String str = CollectionUtils.isNotEmpty(param.getErrorRowInfos()) ? "Error data information" + Json.toJsonString(param.getErrorRowInfos()) : "";
            String repeatPhone = CollUtil.isNotEmpty(repeatPhoneSet) ? "Duplicate cell phone numbers" + repeatPhoneSet.toString() + "," : "";
            String repeatMail = CollUtil.isNotEmpty(repeatMailSet) ? "Duplicate mailboxes" + repeatMailSet.toString() + "," : "";
            msg = "Checked" + param.getTotal() + "user with" + param.getSuccessNum() + "piece of correctly formatted data! Error" + param.getErrorNum() + "user information!" +
                    (param.getSuccessNum() - registerNum) + "data already exists! " + repeatPhone + repeatMail + "Registration success" + registerNum + "data！" + str;
        }
        return msg;
    }

    /**
     * 商品导出or模板
     *
     * @param list     导出的数据
     * @param rowStart 开始的行数
     * @param pages    一共有多少页
     */
    private void exportExcel(List<UserManagerParam> list, int rowStart, int pages, HttpServletResponse response, ExcelWriter writer) {
        List<String> headerList = getHeadList();
        Sheet sheet = writer.getSheet();
        writer.merge(headerList.size() - 1, I18nMessage.getMessage("yami.excel.user.info"));
        writer.writeRow(headerList);
        for (int i = 0; i < headerList.size(); i++) {
            if (i == 19 || i == 20) {
                sheet.setColumnWidth(i, 30 * 256);
            } else {
                sheet.setColumnWidth(i, 20 * 256);
            }
        }
        // 如果要导出的数据为空，导出一个模板
        if (CollectionUtils.isEmpty(list)) {
            PoiExcelUtil.writeExcel(response, writer);
            return;
        }
        int row = rowStart;
        for (UserManagerParam param : list) {
            int firstRow = row + 1;
            int lastRow = row + 1;
            int col = -1;
            // 序号
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, rowStart++);
            // 用户昵称
            String nickName = Objects.isNull(param.getNickName()) ? "" : BooleanUtil.isFalse(permission) && PrincipalUtil.isMobile(param.getNickName()) ? PhoneUtil.hideBetween(param.getNickName()).toString() : param.getNickName();
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, nickName);
//            // 用户名称
//            String realName = Objects.isNull(param.getRealName())?"":param.getRealName();
//            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col,realName);
            // 联系电话
            String userMobile = Objects.isNull(param.getUserMobile()) ? "" : BooleanUtil.isFalse(permission) ? PhoneUtil.hideBetween(param.getUserMobile()).toString() : param.getUserMobile();
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, userMobile);
            // 会员等级
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, param.getLevelName());
            // 会员类型
            String levelType;
            if (Objects.equals(I18nMessage.getDbLang(), LanguageEnum.LANGUAGE_ZH_CN.getLang())) {
                levelType = param.getLevelType() == 0 ? "普通会员" : "付费会员";
            } else {
                levelType = param.getLevelType() == 0 ? "Ordinary member" :"Paid membership";
            }
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, levelType);
            // 用户积分
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, param.getScore());
            // 状态
            String status;
            if (Objects.equals(I18nMessage.getDbLang(), LanguageEnum.LANGUAGE_ZH_CN.getLang())) {
                status = param.getStatus() == 0 ? "禁用" : "正常";
            } else {
                status = param.getStatus() == 0 ? "Disable" : "Normal";
            }
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, status);
            // 消费金额
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, param.getConsAmount());
            // 实付金额
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, param.getActualAmount());
            // 消费次数
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, param.getConsTimes());
            // 平均折扣
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, param.getAverDiscount());

            // 充值金额
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, param.getRechargeAmount());
            // 充值次数
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, param.getRechargeTimes());

            // 退款金额
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, param.getAfterSaleAmount());
            // 退款次数
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, param.getAfterSaleTimes());
            // 累计积分
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, param.getSumScore());
            // 当前余额
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, param.getCurrentBalance());
            // 累计余额
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, param.getSumBalance());
            // 注册时间
            String regTime = "";
            if (Objects.nonNull(param.getUserRegtime())) {
                regTime = DateUtil.format(param.getUserRegtime(), "yyyy-MM-dd HH:mm:ss");
            }
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, regTime);
            // 最近消费时间
            String recTime = Objects.nonNull(param.getUserRegtime()) ? DateUtil.format(param.getReConsTime(), "yyyy-MM-dd HH:mm:ss") : "";
            PoiExcelUtil.mergeIfNeed(writer, firstRow, lastRow, ++col, col, recTime);
            row++;
        }
    }

    private List<String> getHeadList() {
        List<String> headerList;
        // ["序号","用户昵称","用户名称","联系电话","会员等级","会员类型","用户积分","状态","消费金额","实付金额",
        // "消费次数","平均折扣","充值金额","充值次数","退款金额","退款次数","累计积分","当前余额","累计余额","注册时间","最近消费时间"]
        String[] header = {
                I18nMessage.getMessage("yami.excel.user.number"),
                I18nMessage.getMessage("yami.excel.user.userNickname"),
//                I18nMessage.getMessage("yami.excel.user.userName"),
                I18nMessage.getMessage("yami.excel.user.contactNumber"),
                I18nMessage.getMessage("yami.excel.user.memberLevel"),
                I18nMessage.getMessage("yami.excel.user.memberType"),
                I18nMessage.getMessage("yami.excel.user.accScore"),
                I18nMessage.getMessage("yami.excel.user.status"),
                I18nMessage.getMessage("yami.excel.user.amountConsum"),
                I18nMessage.getMessage("yami.excel.user.amountActPaid"),
                I18nMessage.getMessage("yami.excel.user.numberConsum"),
                I18nMessage.getMessage("yami.excel.user.aveDiscount"),
                I18nMessage.getMessage("yami.excel.user.rechargeAmount"),
                I18nMessage.getMessage("yami.excel.user.topUpTimes"),
                I18nMessage.getMessage("yami.excel.user.refundAmount"),
                I18nMessage.getMessage("yami.excel.user.numberOfRefund"),
                I18nMessage.getMessage("yami.excel.user.accPoints"),
                I18nMessage.getMessage("yami.excel.user.currBalance"),
                I18nMessage.getMessage("yami.excel.user.accBalance"),
                I18nMessage.getMessage("yami.excel.user.regTime"),
                I18nMessage.getMessage("yami.excel.user.receConsumDate")
        };
        headerList = Arrays.asList(header);
        return headerList;
    }


}
