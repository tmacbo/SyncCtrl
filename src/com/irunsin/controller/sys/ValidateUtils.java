package com.irunsin.controller.sys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtils
{
	public static final String OK = "-OK";
	public static final String EMAIL_RULE = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	public static final String GOODS_RULE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

	public static final String USERNAME_RULE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";
	public static final String USERNAME_RULE_DESC = "用户名需要6~18个字符，可使用字母、数字、下划线，需以字母开头";

	public static final String JKK_RULE = "1234567890";
	public static final String JKK_RULE_DESC = "健康卡必须为16位数字";

	public static final String SMK_RULE = "1234567890";
	public static final String SMK_RULE_DESC = "市民卡必须为9位数字或者身份证号码";

	public static String checkPassword(String pwd) {
		if (StreamsUtils.isStrBlank(pwd)) {
			return "密码不能为空";
		}

		if (pwd.length() < 6 || pwd.length() > 50) {
			return "密码必须大于6位";
		}

		return OK;
	}

	public static String checkSMK(String SMKNo) {
		if (StreamsUtils.isStrBlank(SMKNo)) {
			return "市民卡卡号不能为空";
		}
		if (checkIDCardNo(SMKNo).equals(OK)) {
			return OK;
		}
		if (SMKNo.length() != 9) {
			return SMK_RULE_DESC;
		}

		for (int i = 0; i < SMKNo.length(); i++) {
			if (!SMK_RULE.contains(SMKNo.charAt(i) + "")) {
				return SMK_RULE_DESC;
			}
		}

		return OK;
	}

	public static String checkRealName(String name) {
		if (StreamsUtils.isStrBlank(name)) {
			return "真实姓名不能为空";
		}
		if (name.length() > 20) {
			return "真实姓名长度非法";
		}

		return OK;
	}

	public static String checkAddress(String name) {
		if (StreamsUtils.isStrBlank(name)) {
			return "地址不能为空";
		}
		if (name.length() > 300) {
			return "地址长度非法";
		}

		return OK;
	}

	public static String checkUserName(String userName) {
		if (StreamsUtils.isStrBlank(userName)) {
			return "用户名不能为空";
		}
		if (userName.length() < 6 || userName.length() > 18) {
			return USERNAME_RULE_DESC;
		}
		for (int i = 0; i < userName.length(); i++) {
			if (!USERNAME_RULE.contains(userName.charAt(i) + "")) {
				return USERNAME_RULE_DESC;
			}
		}

		char ch = userName.charAt(0);
		if (ch == '_' || Character.isDigit(ch)) {
			return USERNAME_RULE_DESC;
		}

		return OK;
	}

	public static String checkJKKNo(String JKKNo) {
		if (StreamsUtils.isStrBlank(JKKNo)) {
			return "健康卡号不能为空";
		}
		if (JKKNo.length() != 16) {
			return "健康卡必须为16位数字";
		}
		for (int i = 0; i < JKKNo.length(); i++) {
			if (!JKK_RULE.contains(JKKNo.charAt(i) + "")) {
				return JKK_RULE_DESC;
			}
		}

		return OK;
	}

	/*********************************** 身份证验证开始 ****************************************/
	/**
	 * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
	 * 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
	 * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位）
	 * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 4、顺序码（第十五位至十七位）
	 * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数）
	 * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, , 16 ，先对前17位数字的权求和
	 * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
	 * 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0
	 * X 9 8 7 6 5 4 3 2
	 */

	/**
	 * 功能：身份证的有效验证
	 * 
	 * @param IDStr
	 *            身份证号
	 * @return 有效：返回"" 无效：返回String信息
	 * @throws ParseException
	 */
	public static String checkIDCardNo(String IDStr) {
		try {
			String errorInfo = "";// 记录错误信息
			String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" };
			String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5",
					"8", "4", "2" };
			String Ai = "";
			// ================ 号码的长度 15位或18位 ================
			if (IDStr.length() != 15 && IDStr.length() != 18) {
				errorInfo = "身份证号码长度应该为15位或18位。";
				return errorInfo;
			}
			// =======================(end)========================

			// ================ 数字 除最后以为都为数字 ================
			if (IDStr.length() == 18) {
				Ai = IDStr.substring(0, 17);
			}
			else if (IDStr.length() == 15) {
				Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
			}
			if (isNumeric(Ai) == false) {
				errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
				return errorInfo;
			}
			// =======================(end)========================

			// ================ 出生年月是否有效 ================
			String strYear = Ai.substring(6, 10);// 年份
			String strMonth = Ai.substring(10, 12);// 月份
			String strDay = Ai.substring(12, 14);// 月份
			if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
				errorInfo = "身份证生日无效。";
				return errorInfo;
			}
			GregorianCalendar gc = new GregorianCalendar();
			SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay)
							.getTime()) < 0) {
				errorInfo = "身份证生日不在有效范围。";
				return errorInfo;
			}
			if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
				errorInfo = "身份证月份无效";
				return errorInfo;
			}
			if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
				errorInfo = "身份证日期无效";
				return errorInfo;
			}
			// =====================(end)=====================

			// ================ 地区码时候有效 ================
			@SuppressWarnings("rawtypes") Hashtable h = GetAreaCode();
			if (h.get(Ai.substring(0, 2)) == null) {
				errorInfo = "身份证地区编码错误。";
				return errorInfo;
			}
			// ==============================================

			// ================ 判断最后一位的值 ================
			int TotalmulAiWi = 0;
			for (int i = 0; i < 17; i++) {
				TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i)))
						* Integer.parseInt(Wi[i]);
			}
			int modValue = TotalmulAiWi % 11;
			String strVerifyCode = ValCodeArr[modValue];
			Ai = Ai + strVerifyCode;

			if (IDStr.length() == 18) {
				if (Ai.equals(IDStr.toLowerCase()) == false) {
					errorInfo = "身份证无效，不是合法的身份证号码";
					return errorInfo;
				}
			}
			else {
				return OK;
			}
			// =====================(end)=====================
			return OK;

		} catch (Exception e) {
			return "身份证号码格式错误";
		}
	}

	/**
	 * 功能：设置地区编码
	 * 
	 * @return Hashtable 对象
	 */
	private static Hashtable GetAreaCode() {
		Hashtable hashtable = new Hashtable();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");
		return hashtable;
	}

	/**
	 * 功能：判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * 功能：判断字符串是否为日期格式
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @param args
	 * @throws ParseException
	 */
	// public static void main(String[] args) throws ParseException {
	// // String IDCardNum="210102820826411";
	// // String IDCardNum="210102198208264114";
	// String IDCardNum = "500113198606245216";
	// CommonUtil cc = new CommonUtil();
	// System.out.println(cc.IDCardValidate(IDCardNum));
	// // System.out.println(cc.isDate("1996-02-29"));
	// }
	/*********************************** 身份证验证结束 ****************************************/

	public static boolean isGoodsCode(String str) {
		if (StreamsUtils.isStrBlank(str)) {
			return false;
		}
		if (str.length() > 25) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!GOODS_RULE.contains(str.charAt(i) + "")) {
				return false;
			}
		}
		return true;
	}

	public static String checkLogin(String username, String password) {
		if (StreamsUtils.isStrBlank(username)) {
			return "用户名不能为空";
		}
		if (StreamsUtils.isStrBlank(password)) {
			return "密码不能为空";
		}
		return OK;
	}

	public static String checkUserRegister(String username, String pass1, String pass2) {
		if (StreamsUtils.isStrBlank(username)) {
			return "用户名不能为空";
		}
		if (StreamsUtils.isStrBlank(pass1)) {
			return "密码不能为空";
		}
		if (StreamsUtils.isStrBlank(pass2)) {
			return "重复密码不能为空";
		}
		if (!pass1.equals(pass2)) {
			return "两次密码输入不一致";
		}
		return OK;
	}

	public static String checkMsgCode(String msgCode) {
		if (StreamsUtils.isStrBlank(msgCode)) {
			return "短信验证码不能为空";
		}
		for (int i = 0; i < msgCode.length(); i++) {
			if (!Character.isDigit(msgCode.charAt(i))) {
				return "短信验证码必须为6为数字";
			}
		}
		return OK;
	}

	public static String checkMobileNo(String mobileNo) {
		if (StreamsUtils.isStrBlank(mobileNo)) {
			return "手机号码不能为空";
		}
		if (mobileNo.length() != 11) {
			return "请输入11位的手机号码";
		}
		for (int i = 0; i < mobileNo.length(); i++) {
			if (!Character.isDigit(mobileNo.charAt(i))) {
				return "请输入合法的手机号码";
			}
		}
		if (mobileNo.charAt(0) != '1') {
			return "请输入合法的手机号码";
		}

		return OK;
	}

	public static boolean checkMoney(String money) {
		if (money == null || money.equals("")) {
			return false;
		}
		boolean Checked = false;
		int checkedIndex = -1;
		for (int i = 0; i < money.length(); i++) {
			char c = money.charAt(i);
			if (c >= 48 && c <= 57) {
				if (!Checked) {
					continue;
				}
				else {
					if (i > checkedIndex + 2) {
						return false;
					}
				}
			}
			else if (c == 46) {
				if (!Checked) {
					checkedIndex = i;
					Checked = true;
					continue;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		return true;
	}

	public static String getDiscount(String str) {
		int i = Integer.parseInt(str);
		if (i % 10 == 0) {
			return (i / 10) + "折";
		}
		else {
			return (i / 10) + "." + (i % 10) + "折";
		}
	}

	public static String getMoneyAsYuan(String money) {
		try {
			return getMoneyAsYuan((Long.parseLong(money)));
		} catch (Exception e) {
			return "";
		}

	}

	public static String getMoneyAsYuan(long money) {
		if (money == 0) {
			return "0";
		}
		else {
			boolean b = false;
			String strMoney = null;
			if (money < 0) {
				b = true;
				money = Math.abs(money);
			}
			if (money < 10) {
				strMoney = "0.0" + money;
			}
			else if (money >= 10 && money < 100) {
				strMoney = "0." + money;
			}
			else {
				if (money % 100 < 10) {
					strMoney = money / 100 + ".0" + money % 100;
				}
				else {
					strMoney = money / 100 + "." + money % 100;
				}
			}
			return b ? ("-" + strMoney) : strMoney;
		}

	}

	public static long getMoneyAsFeng(String strMoney) {

		strMoney += "00";
		int index = strMoney.indexOf('.');
		if (index != -1) {
			strMoney = strMoney.substring(0, index) + strMoney.substring(index + 1, index + 3);
		}
		return Long.parseLong(strMoney);

	}
}
