package me.flyray.bsin.server.utils.jcxf;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static String[] parsePatterns = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm"};
    /**
     * 格式化形式：yyyy-MM
     */
    public static final String DATE_PATTERN_YYYY_MM = "yyyy-MM";
    /**
     * 格式化形式：yyyy-MM-dd
     */
    public static final String DATE_PATTERN_YYYYMMDD1 = "yyyy-MM-dd";
    /**
     * 格式化形式：yyyy/MM/dd
     */
    public static final String DATE_PATTERN_YYYYMMDD2 = "yyyy/MM/dd";
    /**
     * 格式化形式：yyyy年MM月dd日
     */
    public static final String DATE_PATTERN_YYYYMMDD3 = "yyyy年MM月dd日";

    /**
     * 格式化形式：yyyy年MM月dd日
     */
    public static final String DATE_PATTERN_YYYYMMDD4 = "MM月dd日 HH时mm分";
    /**
     * 格式化形式：M月d号
     */
    public static final String DATE_PATTERN_MD = "M月d号";
    /**
     * 格式化形式：yyyyMMdd
     */
    public static final String DATE_PATTERN_YYYYMMDD = "yyyyMMdd";
    /**
     * 格式化形式：yyyy-MM-dd HH:MM:SS
     */
    public static final String DATE_PATTERN_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 格式化形式：yyyy-MM-dd HH:MM
     */
    public static final String DATE_PATTERN_YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * czw 修改
     * 增加获取月开始时间和结束时间
     * 增加获取季度开始时间 和季度结束时间
     */
    private final static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    private final static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ;

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }


    /**
     * 得到当前月份字符串 格式（HH）
     */
    public static Integer getHour() {
        return Integer.parseInt(formatDate(new Date(), "HH"));
    }

    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / (60 * 1000);
    }

    public static Date getDateStart(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateEnd(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(formatDate(date, "yyyy-MM-dd") + " 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取当前字符串类型时间
     *
     * @param pattern
     * @return
     */
    public static String nowStringDate(String pattern) {
        return dateToString(new Date(), pattern);
    }

    /**
     * 获取当前日期格式的时间
     *
     * @param pattern
     * @return
     */
    public static Date nowDate(String pattern) {
        String nowStringDate = nowStringDate(pattern);
        return stringToDate(nowStringDate, pattern);
    }

    /**
     * 格式化时间为字符串
     *
     * @param date
     * @param pattern yyyy-MM-dd
     * @param locale  时区
     * @return
     */
    public static String dateToString(Date date, String pattern, Locale locale) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
            return sdf.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格式化时间为字符串
     *
     * @param date
     * @param pattern yyyy-MM-dd HH:mm
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 格式化字符型时间为长整型
     *
     * @param strDate
     * @param pattern yyyy-MM-dd
     * @param locale
     * @return
     * @throws ParseException
     */
    public static long stringToLong(String strDate, String pattern,
                                    Locale locale) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
        Date date = sdf.parse(strDate);
        return date.getTime();
    }

    /**
     * 格式化字符型时间为长整型
     *
     * @param strDate
     * @param pattern yyyy-MM-dd
     * @return
     * @throws ParseException
     */
    public static long stringToLong(String strDate, String pattern)
            throws ParseException {
        Locale locale = Locale.CHINESE;
        return stringToLong(strDate, pattern, locale);
    }

    /**
     * 格式化字符型为Date型
     *
     * @param strDate
     * @param pattern yyyy-MM-dd
     * @return
     */
    public static Date stringToDate(String strDate, String pattern) {
        try {
            long ltime = stringToLong(strDate, pattern);
            return new Date(ltime);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 格式化字符型为Date型
     *
     * @param strDate
     * @param pattern      yyyy-MM-dd
     * @param otherPattern
     * @return
     */
    public static Date stringToDate(String strDate, String pattern,
                                    String otherPattern) {
        try {
            long ltime = stringToLong(strDate, pattern);
            return new Date(ltime);
        } catch (Exception ex) {
            try {
                long ltime = stringToLong(strDate, otherPattern);
                return new Date(ltime);
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param pattern yyyy-MM-dd
     * @return
     */
    public static Date formateDate(Date date, String pattern) {
        String s = dateToString(date, pattern);
        return stringToDate(s, pattern);
    }

    /**
     * @Description: 通过时间格式转换
     * @ClassName: getDateFormatWithNull
     * @date 2018/1/25
     * @version V1.0
     * @since JDK1.7
     */
    public static String getDateFormatWithNull(String pattern, Date date) {
        if (date == null) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 日期计算
     *
     * @param date
     * @param field
     * @param amount
     * @return
     */
    public static final Date addDate(Date date, int field, int amount) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    public static String formatDateByPattern(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    public static String getCron(Date date) {
        String dateFormat = "s m H d M ? yyyy";
        return formatDateByPattern(date, dateFormat);
    }

    /**
     * 获得本月的开始时间
     *
     * @return
     */
    public static Date getCurrentMonthStartTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 月的25号
     *
     * @return
     */
    public static Date getCurrentMonth25Time() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 25);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 本月的结束时间
     *
     * @return
     */
    public static Date getCurrentMonthEndTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前季度的开始时间
     *
     * @return
     */
    public static Date getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 6);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 季度末的25号
     *
     * @return
     */
    public static Date getCurrentQuarter25Time() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 25);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 25);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 25);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 25);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前季度的结束时间
     *
     * @return
     */
    public static Date getCurrentQuarterEndTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getYearMonth() {
        return formatDate(new Date(), "yyyy-MM");
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static Integer getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        Integer year = cal.get(Calendar.YEAR);
        return year;
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static Integer getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        Integer month = cal.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 获取上个月最后一天
     *
     * @return
     */
    public static String getPrevMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        return dateToString(calendar.getTime(), DATE_PATTERN_YYYYMMDD1);
    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_YYYYMMDD1);
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }

    public static String getYearMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month - 1);
        //设置月的第一天
        cal.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_YYYY_MM);
        return sdf.format(cal.getTime());
    }

    /**
     * 当前上下半年的25号时间
     *
     * @return
     */
    public static Date getCurrentHalfYear25Time() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 6)
                c.set(Calendar.MONTH, 5);
            else if (currentMonth >= 7 && currentMonth <= 12)
                c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 25);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前上下半年的开始时间
     *
     * @return
     */
    public static Date getCurrentHalfYearStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 6)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 7 && currentMonth <= 12)
                c.set(Calendar.MONTH, 6);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当上下半年的结束时间
     *
     * @return
     */
    public static Date getCurrentHalfYearEndTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前年的开始时间
     *
     * @return
     */
    public static Date getCurrentYearStartTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前年的末的25号
     *
     * @return
     */
    public static Date getCurrentYear25Time() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 25);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前年的结束时间
     *
     * @return
     */
    public static Date getCurrentYearEndTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 常规自动日期格式识别  返回对应的时间格式  yyyy-MM-dd   yyyy-MM-dd HH:mm:ss
     *
     * @param str 时间字符串
     * @return Date
     * @author dc
     */
    public static String getDateFormat(String str) {
        boolean year = false;
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        if (pattern.matcher(str.substring(0, 4)).matches()) {
            year = true;
        }
        StringBuilder sb = new StringBuilder();
        int index = 0;
        if (!year) {
            if (str.contains("月") || str.contains("-") || str.contains("/")) {
                if (Character.isDigit(str.charAt(0))) {
                    index = 1;
                }
            } else {
                index = 3;
            }
        }
        for (int i = 0; i < str.length(); i++) {
            char chr = str.charAt(i);
            if (Character.isDigit(chr)) {
                if (index == 0) {
                    sb.append("y");
                }
                if (index == 1) {
                    sb.append("M");
                }
                if (index == 2) {
                    sb.append("d");
                }
                if (index == 3) {
                    sb.append("H");
                }
                if (index == 4) {
                    sb.append("m");
                }
                if (index == 5) {
                    sb.append("s");
                }

                if (index == 6) {
                    sb.append("S");
                }

            } else {
                if (i > 0) {
                    char lastChar = str.charAt(i - 1);
                    if (Character.isDigit(lastChar)) {
                        index++;
                    }
                }
                sb.append(chr);
            }
        }
        return sb.toString();
    }

}
