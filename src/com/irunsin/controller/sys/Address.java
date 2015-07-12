package com.irunsin.controller.sys;

public class Address {
	// 请求地址
	public static final String BASE_URL = "http://59.41.9.73:8085/";

	public static final String USER_LOGIN = "userLogin";
	public static final String USER_REGISTER = "registerUser";
	public static final String VERIFICATION_CODE = "verificationCode";
	public static final String ADD_CARD = "addCard";
	public static final String UPDATE_MOBIBLE = "updateMoible";
	public static final String GET_HOSPITALINFO = "getHospitalInfo";
	public static final String GET_DEPTINFO = "getDeptInfo";
	public static final String GET_DOCTORINFO = "getDoctorInfo";
	public static final String GET_REGINFO = "getRegInfo";
	public static final String GET_TIMEREGINFO = "getTimeRegInfo";
	public static final String ADD_ORDER = "addOrder";
	public static final String SEARCH_ORDER = "searchOrder";
	public static final String PURCHASE = "purchase";
	public static final String GET_DOCTOR_BY_NAME = "getDoctorInfoByName";
	public static final String CANCEL_ORDER = "cancelOrder";
	public static final String PLUGIN_LISTS = "pluginLists";
	public static final String CHANGE_MOBILE = "updateMobile";
	public static final String CHANGE_CARD = "addCard";
	public static final String PASS_TOKEN = "passToken";
	public static final String CHECK_UPDATE = "versionCheckNormal";
	public static final String GET_MSG = "getMessage";
	public static final String LngLatSearch = "lngLatSearch";
	public static final String HistoryRegDoctorInfo = "historyRegDoctorInfo";
	public static final String UPDATEPASSWORD = "updatePassword";
	public static final String RESWTPASSWORD = "resetPassword";
	public static final String GETORDERTOPAYMENT = "getOrderToPayment";
	public static final String GETDETAILTOPAYMENT = "getDetailToPayment";
	public static final String PURCHASEFORBIGORDER = "purchaseForBigOrder";
	public static final String TODAY_GETHOSPITAL = "getHospitalInfoToday";
	public static final String TODAY_GETDEPT = "getDeptInfoToday";
	public static final String TODAY_GETDOCTOR = "getDoctorInfoToday";
	public static final String TODAY_GETREGINFO = "getRegInfoToday";
	public static final String TODAY_GETTIMEREGINFO = "getTimeRegInfoToday";
	public static final String TODAY_ADDORDER = "addOrderToday";
	// 是否打印指定交易的报文
	public static boolean isLog(String requestKey) {
		return true;
	}

	// 根绝交易号得到交易的请求地址
	public static String getUrl(String requestKey) {
		return BASE_URL + "/service/mobileClient?service=" + requestKey;
	}

}
