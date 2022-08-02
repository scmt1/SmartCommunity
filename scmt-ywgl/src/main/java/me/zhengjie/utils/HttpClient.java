package me.zhengjie.utils;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author:WangZiBin
 * @description:http发送工具类
 */
public class HttpClient {

    /**
     * 向目的URL发送post请求
     *
     * @param url    目的url
     * @param params 发送的参数
     * @return AdToutiaoJsonTokenData
     */
    public static String sendPostRequest(String url, Map<String, String> params) {
        RestTemplate client = new RestTemplate();
        //新建Http头，add方法可以添加参数
        HttpHeaders headers = new HttpHeaders();
        //设置请求发送方式
        HttpMethod method = HttpMethod.POST;
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //将请求头部和参数合成一个请求
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用String 类格式化（可设置为对应返回值格式的类）
        ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);

        return response.getBody();
    }

    /**
     * 向目的URL发送post请求
     *
     * @param url    目的url
     * @param params 发送的参数
     * @return AdToutiaoJsonTokenData
     */
    public static String sendPostRequest(String url, Map<String, String> params, HttpHeaders headers) {
        RestTemplate client = new RestTemplate();
        //新建Http头，add方法可以添加参数
        if (headers == null) {
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        //设置请求发送方式
        HttpMethod method = HttpMethod.POST;

        //将请求头部和参数合成一个请求
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用String 类格式化（可设置为对应返回值格式的类）
        ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);

        return response.getBody();
    }

    public static String sendPostRequest(String url, String params, HttpHeaders headers) {
        RestTemplate client = new RestTemplate();
        //新建Http头，add方法可以添加参数
        if (headers == null) {
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        //设置请求发送方式
        HttpMethod method = HttpMethod.POST;

        //将请求头部和参数合成一个请求
        HttpEntity<String> objectHttpEntity = new HttpEntity<String>(params, headers);
        //执行HTTP请求，将返回的结构使用String 类格式化（可设置为对应返回值格式的类）
        ResponseEntity<String> response = client.exchange(url, method, objectHttpEntity, String.class);

        return response.getBody();
    }

    /**
     * 向目的URL发送get请求
     *
     * @param url    目的url
     * @param params 发送的参数
     * @return String
     */
    public static String sendGetRequest(String url, MultiValueMap<String, String> params) {
        RestTemplate client = new RestTemplate();
        //新建Http头，add方法可以添加参数
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.GET;
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用String 类格式化
        ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);

        return response.getBody();
    }


    public static String sendPost(String url, String headKey,String param) {
        try {
            PostMethod postMethod = null;
            postMethod = new PostMethod(url);
            postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            postMethod.addRequestHeader("BDCDJXXGX", headKey);

            //参数设置，需要注意的就是里边不能传NULL，要传空字符串
            NameValuePair[] data = {
                    new NameValuePair("QD", param),
            };

            postMethod.setRequestBody(data);
            org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
            int response = httpClient.executeMethod(postMethod); // 执行POST方法
            String result = postMethod.getResponseBodyAsString();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
