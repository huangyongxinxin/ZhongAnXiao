package com.dykj.zhonganxiao.util;

import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static final String BLANK_SPACE = " ";
    public static String EMPTY = "";

    /**
     * 计算金额保留小数点两位，四舍五入
     * @param f
     * @return
     */
    public static String price(double f){
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(f);
    }


    /**
     * 如果str为null或空字符串返回true
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return isNullOrEmpty(str);
    }

    /**
     * Returns true if the String is null or empty.
     *
     * @param str String to check
     * @return true- is null or empty false - is not null or empty.
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().compareTo("") == 0 || str.equals("null");
    }


    // 验证是否邮箱
    public static boolean isEmail(String strEmail) {
        String strPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否符合手机号码格式
     * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
     * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
     * 电信号段: 133,149,153,170,173,177,180,181,189
     * @param str
     * @return 待检测的字符串
     */
    // 验证是否手机号
    public static boolean isMobileNumber(String str) {
//        Pattern pattern =
//                Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\\\d{8}$");
//        Matcher matcher = pattern.matcher(str);
//        if (matcher.matches()) {
//            return true;
//        } else {
//            return false;
//        }
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(19[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return str.matches(telRegex);
    }

    /**
     * 是否匹配制定的正则
     * @param str   带匹配字符串
     * @param pattern   正则
     * @return
     */
    public static boolean isMatcherStr(String str, String pattern){
//        Pattern patternStr = Pattern.compile(pattern);
//        Matcher matcher = patternStr.matcher(str);
//        if (matcher.matches()) {
//            return true;
//        } else {
//            return false;
//        }
        return str.matches(pattern);
    }


    /**
     * 去掉价格后面的小数点
     * @param price
     * @return
     */
    public static String splitDoubleZero(String price){
        if(price.endsWith("0")){
            String substring = price.substring(0, price.length() - 1);
            String substring1;
            if(substring.endsWith(".")){
                substring1= price.substring(0, substring.length() - 1);
                return substring1;
            }else {
                substring1 = splitDoubleZero(substring);
            }
            return substring1;
        }else if(price.endsWith(".")){
            String s = price.substring(0, price.length() - 1);
            return s;
        }else {
            return price;
        }
    }
    //处理手机号格式为138****2222的方法
    public static String getPhone(String pNumber) {
        if (!TextUtils.isEmpty(pNumber) && pNumber.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return null;
        }
    }

    /**
     * 传入控件判断输入是否为空的方法
     * edittext也是继承textview
     * @param view
     * @return  true 为空 false 不为空
     */
    public static boolean viewGetTextIsEmpty(TextView view) {
        String mContent = view.getText().toString().trim();
        if (isEmpty(mContent)) {
            return true;
        } else {
            return  false;
        }
    }

    /**
     * 获取包含内容的方法
     * @param view
     * @return
     */
    public static String viewGetText(TextView view) {
       return  view.getText().toString().trim();
    }

    /**
     * 显示隐藏edittext
     * @param et
     */
    public static void changeEtShow(EditText et) {
        if (et.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        if (!StringUtil.viewGetTextIsEmpty(et)) {
            String mText = StringUtil.viewGetText(et);
            et.setText(mText);
            et.setSelection(mText.length());
        }
    }



    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        return (a == b) || (b != null) && (a.length() == b.length()) && a.regionMatches(true, 0, b, 0, b.length());
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 禁止EditText输入空格
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText){
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                return " ".equals(source) ? "" : null;
            }
        };

        //保留原有的exittext的filter
        InputFilter[] oldFilters = editText.getFilters();
        int oldFiltersLength = oldFilters.length;
        InputFilter[] newFilters = new InputFilter[oldFiltersLength + 1];
        if (oldFiltersLength > 0) {
            System.arraycopy(oldFilters, 0, newFilters, 0, oldFiltersLength);
        }
        //添加新的过滤规则
        newFilters[oldFiltersLength] = filter;
        editText.setFilters(newFilters);
    }


    /**
     * 将字符串的指定位置区间用*代替
     * @param str
     * @param front
     * @param behind
     * @return
     */
    public static String dimStrings(String str, int front, int behind){
        String stars = "";
        for (int i = 0; i < (str.length() - front - behind); i++) {
            stars = stars + "*";
        }
        if(behind == 0){
            return str.substring(0,front) + stars;
        }else {
            return str.substring(0,front) + stars + str.substring(str.length() - behind,str.length());
        }
    }


    /**
     * 格式化数字，用逗号分割
     *
     * @param number 1000000.7569 to 1,000,000.76 or
     * @return
     */
    public static String formatNumberWithCommaSplit(double number) {
        String firstStr = "";//第一个字符
        String middleStr = "";//中间字符
        String endStr = "";//小数后两位
        if (number < 0) {
            firstStr = "-";
        } else if (number != 0 && number < 0.1) {
            return number + "";
        }

        String tempNumberStr = number + "00";
        int endIndex = tempNumberStr.lastIndexOf(".");
        endStr = tempNumberStr.substring(endIndex+1, endIndex + 3);

        String numberStr = Math.abs((long) number) + "";//取正

        int firstIndex = numberStr.length() % 3;
        int bitCount = numberStr.length() / 3;

        if (firstIndex > 0) {
            middleStr += numberStr.substring(0, firstIndex) + ",";
        }
        for (int i = 0; i < bitCount; i++) {
            middleStr += numberStr.substring(firstIndex + i * 3, firstIndex + i * 3 + 3) + ",";
        }
        if (middleStr.length() > 1) {
            middleStr = middleStr.substring(0, middleStr.length() - 1);
        }
        return firstStr + middleStr + "." + endStr;
    }


    /**
     *
     * 方法描述 隐藏银行卡号中间的字符串（使用*号），显示前四后四
     *
     * @param cardNo
     * @return
     *
     * @author yaomy
     * @date 2018年4月3日 上午10:37:00
     */
    public static String hideCardNo(String cardNo) {
        if(TextUtils.isEmpty(cardNo)) {
            return cardNo;
        }

        int length = cardNo.length();
        int beforeLength = 4;
        int afterLength = 4;
        //替换字符串，当前使用“*”
        String replaceSymbol = "*";
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<length; i++) {
            if(i < beforeLength || i >= (length - afterLength)) {
                sb.append(cardNo.charAt(i));
            } else {
                sb.append(replaceSymbol);
            }
        }

        return sb.toString();
    }
    /**
     *
     * 方法描述 隐藏手机号中间位置字符，显示前三后三个字符
     *
     * @param phoneNo
     * @return
     *
     * @author yaomy
     * @date 2018年4月3日 上午10:38:51
     */
    public static String hidePhoneNo(String phoneNo) {
        if(TextUtils.isEmpty(phoneNo)) {
            return phoneNo;
        }

        int length = phoneNo.length();
        int beforeLength = 3;
        int afterLength = 3;
        //替换字符串，当前使用“*”
        String replaceSymbol = "*";
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<length; i++) {
            if(i < beforeLength || i >= (length - afterLength)) {
                sb.append(phoneNo.charAt(i));
            } else {
                sb.append(replaceSymbol);
            }
        }

        return sb.toString();
    }

}
