package me.flyray.bsin.server.common;

public enum ResponseCode implements ReturnCode {

    /***********************************通用错误码*****************************************/
    OK("000000", "请求成功"),
    FAIL("100000","请求失败"),

    // 用户 100100
    USER_NAME_ISNULL("100101","用户名为空值"),
    USER_DELETE_EXCEPTION("100102","用户删除异常"),
    USER_SAVE_EXCEPTION("100103","用户插入异常"),
    USER_NOT_EXIST("100104","不存在该用户"),
    USER_EXIST("100105","该用户已存在"),
    USER_UPDATE_EXCEPTION("100106","用户更新异常"),
    USERNAME_PASSWORD_ERROR("100107","账号密码错误"),
    PASSWORD_ERROR("100108","密码错误"),
    USER_POST_IS_RELATED("100109","用户岗位存在关联关系"),
    USER_PASSWORD_IS_FALSE("100110","用户不存在或密码错误"),
    USER_NOT_APP_ROLE("100111","您没有该应用的角色"),
    USER_PHONE_EXIST("100112","该电话号码已存在"),
    USER_PHONE_IS_EMPTY("100113","电话号码为空"),
    PARTYMEMBER_ID_IS_EMPTY("100114","党员ID为空"),
    PARTYMEMBER_DELETE_EXCEPTION("100115","党员删除失败"),
    PARTYMEMBER_ADD_EXCEPTION("100116","党员添加失败"),
    PARTYMEMBER_PAGE_EXCEPTION("100117","党员分页查询失败"),
    PARTYMEMBER_ACTIVE_EXCEPTION("100118","查询激活党员数量失败"),
    PARAMS_EMPTY_EXCEPTION("100119","参数为空，请联系管理员"),
    PARTYMEMBER_SHIFT_EXCEPTION("100120","党组织转移异常"),
    PARTYMEMBER_REST_PASSWORD_EXCEPTION("100121","党组织转移异常"),
    PARTYMEMBER_INFO_EXCEPTION("100122","统计异常"),
    PARTYMEMBER_REVOVER_EXCEPTION("100123","党员信息恢复异常"),
    PARTYMEMBER_GETINFO_EXCEPTION("100124","党员信息查询异常"),
    PARTYMEMBER_UPDATA_EXCEPTION("100125","党员信息查询异常"),

    // 机构 100200
    ORG_NOT_EXIST("100201","机构不存在"),
    ORG_UPDATE_EXCEPTION("100202","机构更新异常"),
    ORG_HAVE_CHILDREN_ORG("100203","机构下存在子集部门"),
    ORG_CODE_EXIST("100204","机构编号已存在"),
    ORG_APP_IS_RELATED("100205","机构应用存在关联"),
    ORG_POST_IS_RELATED("100206","机构岗位存在关联"),
    DEPT_IS_NOT_EXIST("100207","改部门不存在"),

    // 岗位 100300
    POSITION_USER_IS_RELATED("100301","岗位和用户存在关联"),
    POSITION_ROLE_IS_RELATED("100302","岗位角色存在关联"),
    POST_NOT_EXIST("100303","岗位不存在"),
    POST_CODE_EXIST("100304","岗位编号已存在"),
    INVALID_FIELDS("100305", "请求参数非法"),

    // 角色 100400
    ROLE_CODE_EXISTS("100401","编码已存在"),
    ROLE_NOT_ADD("100402","您没有权限为该应用添加角色"),
    ROLE_NOT_DELETE("100403","您没有权限删除该应用的角色"),
    ROLE_NOT_UPDATE("100404","您没有权限编辑该应用的角色"),
    ROLE_NOT_AUTHORIZE_MENU("100405","您没有权限为该应用的角色授予菜单权限"),

    // 菜单 100500
    MENU_CODE_EXISTS("100501","编码已存在"),
    MENU_NOT_ADD("100502","您没有权限为该应用添加菜单"),
    MENU_NOT_DELETE("100503","您没有权限删除该应用的菜单"),
    MENU_NOT_UPDATE("100504","您没有权限编辑该应用的菜单"),
    MENU_EXIST_SUBMENU("100505","该菜单存在子菜单"),
    NOT_TOP_MENU("100506","不能添加顶级菜单"),

    // 应用 100600
    APP_CODE_EXISTS("100601","应用编码已存在"),
    APP_EXIST_USER("100602","应用被其他机构使用中"),
    APP_NOT_DELETE("100603","您没有权限删除该应用"),
    APP_NOT_UPDATE ("100604","您没有权限编辑该应用"),
    APP_NOT_EXISTS("1006005","该应用不存在"),

    // 租户 100700
    TENANT_CODE_EXISTS("100701","租户编码已存在"),
    SUB_TENANT_NOT_AUTH("100702","超级租户不能授权"),

    // 机构岗位 100800
    ID_NOT_ISNULL("100801","Id不能为空"),

    // 分页 100900
    PAGE_NUM_ISNULL("100901","分页参数为空"),


    OPENID_NOT_EXISTS("222222","党员微信OPENID不存在");

    private String code;
    private String message;

    private ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public String getReturnCode() {
        return code;
    }

    @Override
    public String getReturnMessage() {
        return message;
    }

}
