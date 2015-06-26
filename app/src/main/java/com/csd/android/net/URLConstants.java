package com.csd.android.net;

public interface URLConstants {

	public static final String SERVER_URL = "http://devdtapi.cocar.com/index.php?r=";
	public static final String URL_LOGIN = SERVER_URL + "user/login";
	public static final String URL_GET_INSTALL_TASK_LIST = SERVER_URL + "task/list";
	public static final String URL_GET_CHE_LIANG_SHANGJIA_XINXI_VIEW_INFO = SERVER_URL + "car/getCarInfo";
	public static final String URL_GET_CAR_BRAND_LIST = SERVER_URL + "car/getBrandList";
	public static final String URL_GET_CAR_CONSUME = SERVER_URL + "car/getPetrolConsume";
	public static final String URL_SAVE_CHELIANG_SHANGJIA_VIEW_INFO = SERVER_URL + "car/carOnHire";
	public static final String URL_VERIFY_IDENTITY_CARD_INFO = SERVER_URL + "license/checkChacterNumber";
	public static final String URL_POST_PHOTO = SERVER_URL + "license/licenceUpload";
	public static final String URL_REQUEST_IDENTITY_CARD_INFO_PASS_SAVE = SERVER_URL + "license/identifyLicenseAudit";
	public static final String URL_REQUEST_IDENTITY_CARD_INFO_REJECT = SERVER_URL + "license/identifyLicenseFailedAudit";
	public static final String URL_REQUEST_DRIVING_LICENSE_INFO_PASS_SAVE = SERVER_URL + "license/driveLicenseAudit";
	public static final String URL_REQUEST_DRIVING_LICENSE_INFO_REJECT = SERVER_URL + "license/driveLicenseFailedAudit";
	public static final String URL_REQUEST_TOGETHER_CARDS_CHECK_REJECT = SERVER_URL + "license/ownerCheckFailedAudit";
	public static final String URL_REQUEST_TOGETHER_CARDS_CHECK_PASS = SERVER_URL + "license/ownerCheckAudit";
	public static final String URL_REQUEST_INSURANCE_CHECK_PASS = SERVER_URL + "license/policyLicenseAudit";
	public static final String URL_REQUEST_INSURANCE_CHECK_REJECT = SERVER_URL + "license/policyLicenseFailedAudit";
	public static final String URL_REQUEST_CAR_PHOTO_PASS = SERVER_URL + "license/carImageAudit";
	public static final String URL_GET_TASK_CATEGORY_DES = SERVER_URL + "task/index";
	public static final String URL_GET_TASK_SUBMIT = SERVER_URL + "task/complete";
	public static final String URL_GET_TASK_SET_READ = SERVER_URL + "task/setRead";
	public static final String URL_GET_BOX_CHECK = SERVER_URL + "carBox/checkGpsId";
	public static final String URL_GET_BOX_TEST = SERVER_URL + "carBox/testing";
	public static final String URL_GET_BOX_BIND = SERVER_URL + "carBox/bind";
	public static final String URL_GET_CAR_PRICE = SERVER_URL + "car/getCarPrice";
	public static final String URL_POST_LOCATION = SERVER_URL + "carBox/addAdminTrack";

}
