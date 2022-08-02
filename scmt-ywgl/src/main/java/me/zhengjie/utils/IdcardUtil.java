package me.zhengjie.utils;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * <p>
 * Description:身份证识别<br />
 * </p>
 *
 * @author dengjie
 * @version 0.1 2020年4月8日
 * @title IdcardUtil.java
 * @package me.zhengjie.utils
 */

public class IdcardUtil {

    // 这里为固定的,需要自行申请配置
    public static final String APP_ID = "19325739";
    public static final String API_KEY = "GSln11jV0ShpASAoxVUegqow";
    public static final String SECRET_KEY = "dGgrT6wftevUvnn7sPHEjOPIqOxaOAEu";

    public static JSONObject getIdcard(String filePath) {
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 调用接口
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("detect_risk", "false");

        // 正面为font 反面为back
        String idCardSide = "front";

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(3000);
        client.setSocketTimeoutInMillis(30000);
        JSONObject res = client.idcard(filePath, idCardSide, options);
        return res;
    }

    /**
     * <p>
     * Description:获取营业执照<br />
     * </p>
     *
     * @param filePath
     * @return JSONObject
     * @author dengjie
     * @version 0.1 2020年4月28日
     */
    public static JSONObject getBusinessLicense(String filePath) {
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 调用接口
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(3000);
        client.setSocketTimeoutInMillis(30000);

        JSONObject res = client.businessLicense(filePath, options);
        return res;
    }

    /***
     * 识别表格文字
     * @param filePath
     * @return
     */
    public static JSONObject tableRecognitionAsync(String filePath) {

        filePath = "G:\\0001.jpg";

        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 调用接口
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("result_type", "json");

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(3000);
        client.setSocketTimeoutInMillis(30000);

        JSONObject res = client.tableRecognitionAsync(filePath, options);
        JSONArray result = res.getJSONArray("result");
        JSONObject object = (JSONObject) result.get(0);
        String request_id = object.getString("request_id");

        res = client.tableResultGet(request_id, options);
        System.out.println("表格识别结果：" + res.toString(2));
        return res;
    }

}
