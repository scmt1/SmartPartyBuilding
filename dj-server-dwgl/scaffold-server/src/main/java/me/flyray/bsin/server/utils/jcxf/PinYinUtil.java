package me.flyray.bsin.server.utils.jcxf;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音工具类
 */
public class PinYinUtil {
    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     *
     * @param inputString 字符串
     * @return 返回值
     */
    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();
        StringBuilder output = new StringBuilder();

        try {
            for (char anInput : input) {
                if (Character.toString(anInput).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(anInput, format);
                    output.append(temp[0]);
                } else {
                    output.append(Character.toString(anInput));
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    /**
     * 获取汉字串拼音首字母，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音首字母
     */
    public static String getFirstSpell(String chinese) {
        StringBuilder pybf = new StringBuilder();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char anArr : arr) {
            if (anArr > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(anArr, defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(anArr);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim().toUpperCase();
    }

    /**
     * 获取字符串的第一个汉字的首字母
     *
     * @param chinese 汉字
     * @return 返回值
     */
    public static String getFirstCode(String chinese) {
        String code = getFirstSpell(chinese);
        if (!"".equals(code)) {
            code = code.toUpperCase();
            code = code.substring(0, 1);
        }
        return code;
    }

    /**
     * 获取汉字串拼音，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音
     */
    public static String getFullSpell(String chinese) {
        StringBuilder pybf = new StringBuilder();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char anArr : arr) {
            if (anArr > 128) {
                try {
                    pybf.append(PinyinHelper.toHanyuPinyinStringArray(anArr, defaultFormat)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(anArr);
            }
        }
        return pybf.toString();
    }
}
