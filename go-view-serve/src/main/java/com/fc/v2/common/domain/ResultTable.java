package com.fc.v2.common.domain;

public class ResultTable {
     /**
     * 状态码
     * */
    private Integer code;

    /**
     * 提示消息
     * */
    private String msg;

    /**
     * 消息总量
     * */
    private Long count;

    /**
     * 数据对象
     * */
    private Object data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 构 建
     * */
    public static ResultTable pageTable(long count,Object data){
        ResultTable resultTable = new ResultTable();
        resultTable.setData(data);
        resultTable.setCode(0);
        resultTable.setCount(count);
        if(data!=null) {
       	 resultTable.setMsg("获取成功");
       }else {
       	 resultTable.setMsg("获取失败");
       }
        return resultTable;
    }

    public static ResultTable dataTable(Object data){
        ResultTable resultTable = new ResultTable();
        resultTable.setData(data);
        resultTable.setCode(0);
        if(data!=null) {
        	 resultTable.setMsg("获取成功");
        }else {
        	 resultTable.setMsg("获取失败");
        }

        return resultTable;
    }

    public static ResultTable listDataTable(Object data){
        ResultTable resultTable = new ResultTable();
        resultTable.setData(data);
        resultTable.setCode(200);
        if(data!=null) {
            resultTable.setMsg("获取成功");
        }else {
            resultTable.setMsg("获取失败");
        }

        return resultTable;
    }
}
