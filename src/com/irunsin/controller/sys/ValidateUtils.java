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
	public static final String USERNAME_RULE_DESC = "�û�����Ҫ6~18���ַ�����ʹ����ĸ�����֡��»��ߣ�������ĸ��ͷ";

	public static final String JKK_RULE = "1234567890";
	public static final String JKK_RULE_DESC = "����������Ϊ16λ����";

	public static final String SMK_RULE = "1234567890";
	public static final String SMK_RULE_DESC = "���񿨱���Ϊ9λ���ֻ������֤����";

	public static String checkPassword(String pwd) {
		if (StreamsUtils.isStrBlank(pwd)) {
			return "���벻��Ϊ��";
		}

		if (pwd.length() < 6 || pwd.length() > 50) {
			return "����������6λ";
		}

		return OK;
	}

	public static String checkSMK(String SMKNo) {
		if (StreamsUtils.isStrBlank(SMKNo)) {
			return "���񿨿��Ų���Ϊ��";
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
			return "��ʵ��������Ϊ��";
		}
		if (name.length() > 20) {
			return "��ʵ�������ȷǷ�";
		}

		return OK;
	}

	public static String checkAddress(String name) {
		if (StreamsUtils.isStrBlank(name)) {
			return "��ַ����Ϊ��";
		}
		if (name.length() > 300) {
			return "��ַ���ȷǷ�";
		}

		return OK;
	}

	public static String checkUserName(String userName) {
		if (StreamsUtils.isStrBlank(userName)) {
			return "�û�������Ϊ��";
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
			return "�������Ų���Ϊ��";
		}
		if (JKKNo.length() != 16) {
			return "����������Ϊ16λ����";
		}
		for (int i = 0; i < JKKNo.length(); i++) {
			if (!JKK_RULE.contains(JKKNo.charAt(i) + "")) {
				return JKK_RULE_DESC;
			}
		}

		return OK;
	}

	/*********************************** ���֤��֤��ʼ ****************************************/
	/**
	 * ���֤������֤ 1������Ľṹ ������ݺ�������������룬��ʮ��λ���ֱ������һλУ������ɡ�����˳�������������Ϊ����λ���ֵ�ַ�룬
	 * ��λ���ֳ��������룬��λ����˳�����һλ����У���롣 2����ַ��(ǰ��λ����
	 * ��ʾ�������ס����������(�С��졢��)�������������룬��GB/T2260�Ĺ涨ִ�С� 3�����������루����λ��ʮ��λ��
	 * ��ʾ�������������ꡢ�¡��գ���GB/T7408�Ĺ涨ִ�У��ꡢ�¡��մ���֮�䲻�÷ָ����� 4��˳���루��ʮ��λ��ʮ��λ��
	 * ��ʾ��ͬһ��ַ������ʶ������Χ�ڣ���ͬ�ꡢͬ�¡�ͬ�ճ������˱ඨ��˳��ţ� ˳�����������������ԣ�ż�������Ů�ԡ� 5��У���루��ʮ��λ����
	 * ��1��ʮ��λ���ֱ������Ȩ��͹�ʽ S = Sum(Ai * Wi), i = 0, , 16 ���ȶ�ǰ17λ���ֵ�Ȩ���
	 * Ai:��ʾ��iλ���ϵ����֤��������ֵ Wi:��ʾ��iλ���ϵļ�Ȩ���� Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4
	 * 2 ��2������ģ Y = mod(S, 11) ��3��ͨ��ģ�õ���Ӧ��У���� Y: 0 1 2 3 4 5 6 7 8 9 10 У����: 1 0
	 * X 9 8 7 6 5 4 3 2
	 */

	/**
	 * ���ܣ����֤����Ч��֤
	 * 
	 * @param IDStr
	 *            ���֤��
	 * @return ��Ч������"" ��Ч������String��Ϣ
	 * @throws ParseException
	 */
	public static String checkIDCardNo(String IDStr) {
		try {
			String errorInfo = "";// ��¼������Ϣ
			String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" };
			String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5",
					"8", "4", "2" };
			String Ai = "";
			// ================ ����ĳ��� 15λ��18λ ================
			if (IDStr.length() != 15 && IDStr.length() != 18) {
				errorInfo = "���֤���볤��Ӧ��Ϊ15λ��18λ��";
				return errorInfo;
			}
			// =======================(end)========================

			// ================ ���� �������Ϊ��Ϊ���� ================
			if (IDStr.length() == 18) {
				Ai = IDStr.substring(0, 17);
			}
			else if (IDStr.length() == 15) {
				Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
			}
			if (isNumeric(Ai) == false) {
				errorInfo = "���֤15λ���붼ӦΪ���� ; 18λ��������һλ�⣬��ӦΪ���֡�";
				return errorInfo;
			}
			// =======================(end)========================

			// ================ ���������Ƿ���Ч ================
			String strYear = Ai.substring(6, 10);// ���
			String strMonth = Ai.substring(10, 12);// �·�
			String strDay = Ai.substring(12, 14);// �·�
			if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
				errorInfo = "���֤������Ч��";
				return errorInfo;
			}
			GregorianCalendar gc = new GregorianCalendar();
			SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay)
							.getTime()) < 0) {
				errorInfo = "���֤���ղ�����Ч��Χ��";
				return errorInfo;
			}
			if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
				errorInfo = "���֤�·���Ч";
				return errorInfo;
			}
			if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
				errorInfo = "���֤������Ч";
				return errorInfo;
			}
			// =====================(end)=====================

			// ================ ������ʱ����Ч ================
			@SuppressWarnings("rawtypes") Hashtable h = GetAreaCode();
			if (h.get(Ai.substring(0, 2)) == null) {
				errorInfo = "���֤�����������";
				return errorInfo;
			}
			// ==============================================

			// ================ �ж����һλ��ֵ ================
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
					errorInfo = "���֤��Ч�����ǺϷ������֤����";
					return errorInfo;
				}
			}
			else {
				return OK;
			}
			// =====================(end)=====================
			return OK;

		} catch (Exception e) {
			return "���֤�����ʽ����";
		}
	}

	/**
	 * ���ܣ����õ�������
	 * 
	 * @return Hashtable ����
	 */
	private static Hashtable GetAreaCode() {
		Hashtable hashtable = new Hashtable();
		hashtable.put("11", "����");
		hashtable.put("12", "���");
		hashtable.put("13", "�ӱ�");
		hashtable.put("14", "ɽ��");
		hashtable.put("15", "���ɹ�");
		hashtable.put("21", "����");
		hashtable.put("22", "����");
		hashtable.put("23", "������");
		hashtable.put("31", "�Ϻ�");
		hashtable.put("32", "����");
		hashtable.put("33", "�㽭");
		hashtable.put("34", "����");
		hashtable.put("35", "����");
		hashtable.put("36", "����");
		hashtable.put("37", "ɽ��");
		hashtable.put("41", "����");
		hashtable.put("42", "����");
		hashtable.put("43", "����");
		hashtable.put("44", "�㶫");
		hashtable.put("45", "����");
		hashtable.put("46", "����");
		hashtable.put("50", "����");
		hashtable.put("51", "�Ĵ�");
		hashtable.put("52", "����");
		hashtable.put("53", "����");
		hashtable.put("54", "����");
		hashtable.put("61", "����");
		hashtable.put("62", "����");
		hashtable.put("63", "�ຣ");
		hashtable.put("64", "����");
		hashtable.put("65", "�½�");
		hashtable.put("71", "̨��");
		hashtable.put("81", "���");
		hashtable.put("82", "����");
		hashtable.put("91", "����");
		return hashtable;
	}

	/**
	 * ���ܣ��ж��ַ����Ƿ�Ϊ����
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
	 * ���ܣ��ж��ַ����Ƿ�Ϊ���ڸ�ʽ
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
	/*********************************** ���֤��֤���� ****************************************/

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
			return "�û�������Ϊ��";
		}
		if (StreamsUtils.isStrBlank(password)) {
			return "���벻��Ϊ��";
		}
		return OK;
	}

	public static String checkUserRegister(String username, String pass1, String pass2) {
		if (StreamsUtils.isStrBlank(username)) {
			return "�û�������Ϊ��";
		}
		if (StreamsUtils.isStrBlank(pass1)) {
			return "���벻��Ϊ��";
		}
		if (StreamsUtils.isStrBlank(pass2)) {
			return "�ظ����벻��Ϊ��";
		}
		if (!pass1.equals(pass2)) {
			return "�����������벻һ��";
		}
		return OK;
	}

	public static String checkMsgCode(String msgCode) {
		if (StreamsUtils.isStrBlank(msgCode)) {
			return "������֤�벻��Ϊ��";
		}
		for (int i = 0; i < msgCode.length(); i++) {
			if (!Character.isDigit(msgCode.charAt(i))) {
				return "������֤�����Ϊ6Ϊ����";
			}
		}
		return OK;
	}

	public static String checkMobileNo(String mobileNo) {
		if (StreamsUtils.isStrBlank(mobileNo)) {
			return "�ֻ����벻��Ϊ��";
		}
		if (mobileNo.length() != 11) {
			return "������11λ���ֻ�����";
		}
		for (int i = 0; i < mobileNo.length(); i++) {
			if (!Character.isDigit(mobileNo.charAt(i))) {
				return "������Ϸ����ֻ�����";
			}
		}
		if (mobileNo.charAt(0) != '1') {
			return "������Ϸ����ֻ�����";
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
			return (i / 10) + "��";
		}
		else {
			return (i / 10) + "." + (i % 10) + "��";
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
