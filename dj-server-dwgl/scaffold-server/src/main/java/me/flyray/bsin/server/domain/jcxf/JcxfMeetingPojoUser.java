package me.flyray.bsin.server.domain.jcxf;

import java.io.Serializable;

public class JcxfMeetingPojoUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String partyName;
    private String avatar;
    private String deptName;
    private String leaveStatus;
    private Integer status;
    private Integer signInStatus;
    private String isFlow;
    private String phone;
    private String position;
    private String positionName;

    public Long getId(){
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(String leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIsFlow() {
        return isFlow;
    }

    public void setIsFlow(String isFlow) {
        this.isFlow = isFlow;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public Integer getSignInStatus() {
        return signInStatus;
    }

    public void setSignInStatus(Integer signInStatus) {
        this.signInStatus = signInStatus;
    }
}
