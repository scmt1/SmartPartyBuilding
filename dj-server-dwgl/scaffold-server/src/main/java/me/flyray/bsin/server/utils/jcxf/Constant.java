package me.flyray.bsin.server.utils.jcxf;

public class Constant {

  //支部数组  631:党支部 632:联合党支部 931:临时党支部 932:临时联合党支部
  public static final int[] BRANCH_ARRAY = {631, 632, 931, 932};

  //--------------------------------------------------------
  public static final Integer MEETING_1 = 1;//未开始

  public static final Integer MEETING_2 = 2;//进行中

  public static final Integer MEETING_3 = 3;//已经结束

  public static final Integer MEETING_4 = 4;//取消

  //-----------------------------------------------------------
  public static final Integer MEETING_USER_STATUS_1 = 1;//缺席
  public static final Integer MEETING_USER_STATUS_2 = 2;//病假
  public static final Integer MEETING_USER_STATUS_3 = 3;//公假
  public static final Integer MEETING_USER_STATUS_4 = 4;//事假
  public static final Integer MEETING_USER_STATUS_5 = 5;//迟到
  public static final Integer MEETING_USER_STATUS_6 = 6;//正常参会人员

}
