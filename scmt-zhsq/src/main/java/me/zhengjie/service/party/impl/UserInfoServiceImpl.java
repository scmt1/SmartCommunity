package me.zhengjie.service.party.impl;

import java.util.Map;

import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import me.zhengjie.common.utils.ResultCode;
import me.zhengjie.dto.UserInfomation;
import me.zhengjie.common.BusinessException;
import me.zhengjie.service.party.IUserInfoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserInfoServiceImpl implements IUserInfoService {

//    @Autowired
//    private RestTemplate restTemplate;

    @Override
    public UserInfomation getCurrentUserInfo(String accessToken) {
        try {
            log.info("accessToken:{}", accessToken);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", accessToken);
            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

            //            String url = "http://47.108.93.140:8082/grid/userInfo/loadCurrentInfo";
            //            ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

            //            log.info("url:{}", url);
//            ServiceInstance serviceInstance = loadBalancerClient.choose("base");
//            System.out.println(JSON.toJSONString(serviceInstance));
//            ResponseEntity<String> resEntity = restTemplate.exchange(serviceInstance.getUri() + "/oauth/user-info",
//                HttpMethod.GET, requestEntity, String.class);
//            log.info("rsp:{}", resEntity);
//            if (resEntity.getStatusCodeValue() != 200) {
//                throw new BusinessException(ResultCode.FAILURE);
//            }
//            R<?> result = JSON.parseObject(resEntity.getBody(), R.class);
//            if (result.getCode() != 200) {
//                throw new BusinessException(ResultCode.FAILURE);
//            }
//            UserInfomation userInfomation = JSON.parseObject(JSON.toJSONString(result.getData()), UserInfomation.class);
//            log.info("userinformation:{}", JSON.toJSONString(userInfomation));
            return null;
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException(ResultCode.UN_AUTHORIZED);
        }
    }

    @Override
    public UserInfomation getCurrentUserInfo(Map<String, String> headerMap) {
        try {

            HttpHeaders headers = new HttpHeaders();
            for (String key : headerMap.keySet()) {
                String value = headerMap.get(key).toString();
                headers.add(key, value);
            }
            log.info("headers:{}", JSON.toJSONString(headers));
            HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

//            ServiceInstance serviceInstance = loadBalancerClient.choose("base");
//            System.out.println(JSON.toJSONString(serviceInstance));
//            ResponseEntity<String> resEntity = restTemplate.exchange(serviceInstance.getUri() + "/oauth/user-info",
//                HttpMethod.GET, requestEntity, String.class);
//            log.info("rsp:{}", resEntity);
//            if (resEntity.getStatusCodeValue() != 200) {
//                throw new BusinessException(ResultCode.FAILURE);
//            }
//            R<?> result = JSON.parseObject(resEntity.getBody(), R.class);
//            if (result.getCode() != 200) {
//                throw new BusinessException(ResultCode.FAILURE);
//            }
//            UserInfomation userInfomation = JSON.parseObject(JSON.toJSONString(result.getData()), UserInfomation.class);
//            log.info("userinformation:{}", JSON.toJSONString(userInfomation));
//            return userInfomation;
            return null;
        } catch (Exception e) {
            log.error("", e);
            throw new BusinessException(ResultCode.UN_AUTHORIZED);
        }
    }
}
