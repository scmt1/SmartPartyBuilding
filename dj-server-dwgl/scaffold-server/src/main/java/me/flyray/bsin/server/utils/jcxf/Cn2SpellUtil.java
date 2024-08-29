package me.flyray.bsin.server.utils.jcxf;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kill on 2017/2/27.
 */
public class Cn2SpellUtil {
    /**
     * 汉字转换位汉语拼音首字母，英文字符不变
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToFirstSpell(String chines){
        if(StringUtils.isBlank(chines))
            return "";
        chines = chines.replaceAll("\\s*", "");
        chines = chines.replaceAll(" ", "");
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    String[] str = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
                    if(str != null && str.length > 0){
                        pinyinName += str[0].charAt(0);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }

    /**
     * 汉字转换位汉语拼音，英文字符不变
     * @param chines 汉字
     * @return 拼音
     */
    public static String converterToSpell(String chines){
        if(StringUtils.isBlank(chines))
            return "";
        chines = chines.replaceAll("\\s*", "");
        chines = chines.replaceAll(" ", "");
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    String[] str = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
                    if(str != null && str.length > 0){
                        pinyinName += str[0];
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }else{
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }

    public static String makeStringByStringSet(Set<String> stringSet){
        StringBuilder str = new StringBuilder();
        int i=0;
        for(String s : stringSet){
            if(i == stringSet.size() - 1){
                str.append(s);
            }else{
                str.append(s + ",");
            }
            i++;
        }
        return str.toString().toLowerCase();
    }

    /**
     * 获取拼音集合
     * @author wyh
     * @param src
     * @return Set<String>
     */
    public static Set<String> getPinyin(String src){
        if(src!=null && !src.trim().equalsIgnoreCase("")){
            char[] srcChar ;
            srcChar=src.toCharArray();
            //汉语拼音格式输出类
            HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();

//输出设置，大小写，音标方式等
            hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

            String[][] temp = new String[src.length()][];
            for(int i=0;i<srcChar.length;i++){
                char c = srcChar[i];
                //是中文或者a-z或者A-Z转换拼音(我的需求，是保留中文或者a-z或者A-Z)
                if(String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")){
                    try{
                        temp[i] = PinyinHelper.toHanyuPinyinStringArray(srcChar[i], hanYuPinOutputFormat);
                    }catch(BadHanyuPinyinOutputFormatCombination e) {
                        e.printStackTrace();
                    }
                }else if(((int)c>=65 && (int)c<=90) || ((int)c>=97 && (int)c<=122)){
                    temp[i] = new String[]{String.valueOf(srcChar[i])};
                }else{
                    temp[i] = new String[]{""};
                }
            }
            String[] pingyinArray = Exchange(temp);
            Set<String> pinyinSet = new HashSet<String>();
            for(int i=0;i<pingyinArray.length;i++){
                pinyinSet.add(pingyinArray[i]);
            }
            return pinyinSet;
        }
        return null;
    }
    /**
     * 递归
     * @author wyh
     * @param strJaggedArray
     * @return
     */
    public static String[] Exchange(String[][] strJaggedArray){
        String[][] temp = DoExchange(strJaggedArray);
        return temp[0];
    }

    /**
     * 递归
     * @author wyh
     * @param strJaggedArray
     * @return
     */
    private static String[][] DoExchange(String[][] strJaggedArray){
        int len = strJaggedArray.length;
        if(len >= 2){
            int len1 = strJaggedArray[0].length;
            int len2 = strJaggedArray[1].length;
            int newlen = len1*len2;
            String[] temp = new String[newlen];
            int Index = 0;
            for(int i=0;i<len1;i++){
                for(int j=0;j<len2;j++){
                    temp[Index] = strJaggedArray[0][i] + strJaggedArray[1][j];
                    Index ++;
                }
            }
            String[][] newArray = new String[len-1][];
            for(int i=2;i<len;i++){
                newArray[i-1] = strJaggedArray[i];
            }
            newArray[0] = temp;
            return DoExchange(newArray);
        }else{
            return strJaggedArray;
        }
    }
    /**
     * @Description:
     * 通过用户名字获取拼音的方法，
     * 判断原有名字是否在现有拼音集合中， 如果有不通过名字生成新的拼音
     *
     *
     * @ClassName:
     * @author czw
     * @since JDK1.7
     * @date 2017/2/27
     * @version V1.0
     */
    public static String getPinYinByName(String userName, String pinYin) {
        //读取拼音字段 判断 拼音字段是否为空
        if(userName!=null&&!userName.equals("")){
            if (pinYin!=null&&!pinYin.equals("")){
                //判断 用户名字生成拼音集合 是否包含原有拼音
                String regPinYin=".*"+pinYin+".*";
                String pinYinSets=makeStringByStringSet(getPinyin(userName));
                if (pinYinSets.matches(regPinYin)){
                    return pinYin;
                }else {
                    return converterToSpell(userName);
                }
            }else {
                return converterToSpell(userName);
            }
        }else{
            return null;
        }

    }
}
