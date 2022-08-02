package me.zhengjie.utils;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.common.utils.ResultUtil;
import me.zhengjie.common.utils.SmsUtil;
import me.zhengjie.common.vo.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class ShortMessageUtil {
    @Autowired
    private SmsUtil smsUtil;
    
    @Autowired
    private MyProps myProps;

    /**
     * 发送验证码
     * @param phone
     * @return
     * @throws ClientException
     */
    public  Result<Object> sendVCode(String phone, int code) throws ClientException {
        if (StringUtils.isEmpty(phone)) {
            return new ResultUtil<Object>().setErrorMsg("手机号不能为空！");
        }

        System.out.println("验证码======" + code);
        //调用发送短信的功能
        SendSmsResponse sendSmsResponse = smsUtil.sendCode(phone, code + "", "SMS_187951013");
        if(!myProps.getIsSms()){
        	return new ResultUtil<Object>().setSuccessMsg("验证码发送成功");
        }
        if ("OK".equals(sendSmsResponse.getCode())) {
            return new ResultUtil<Object>().setSuccessMsg("验证码发送成功");
        } else {
            return new ResultUtil<Object>().setErrorMsg("验证码发送失败");
        }
    }

    /**
     * 审核通过通知
     * @param phone 电话
     * @param code 编号
     * @param num 金额
     * @return
     * @throws ClientException
     */
    public  Result<Object> sendPassMessage(String phone, String code, String num) throws ClientException {
        
        if (StringUtils.isEmpty(phone)) {
            return new ResultUtil<Object>().setErrorMsg("手机号不能为空！");
        }

        System.out.println("验证码======" + code);
        String tempParam =" {\"code\":\""+code+"\",\"num\":\""+num+"\"}";
        if(!myProps.getIsSms()){
        	return new ResultUtil<Object>().setSuccessMsg("验证码发送成功");
        }
        //调用发送短信的功能
        SendSmsResponse sendSmsResponse = smsUtil.sendSms(phone, tempParam , "SMS_189524041");
        if ("OK".equals(sendSmsResponse.getCode())) {
            return new ResultUtil<Object>().setSuccessMsg("审核通过通知短信发送成功");
        } else {
            return new ResultUtil<Object>().setErrorMsg("审核通过通知短信发送失败");
        }
    }

    /**
     * 审核不通过通知
     * @param phone 电话
     * @param code 编号
     * @param reason 原因
     * @return
     * @throws ClientException
     */
    public  Result<Object> sendNotPassMessage(String phone, String code, String reason) throws ClientException {
      
        if (StringUtils.isEmpty(phone)) {
            return new ResultUtil<Object>().setErrorMsg("手机号不能为空！");
        }

        System.out.println("验证码======" + code);
        String tempParam =" {\"code\":\""+code+"\",\"reason\":\""+reason+"\"}";
        if(!myProps.getIsSms()){
        	return new ResultUtil<Object>().setSuccessMsg("验证码发送成功");
        }
        //调用发送短信的功能
        SendSmsResponse sendSmsResponse = smsUtil.sendSms(phone, tempParam , "SMS_189613617");
        if ("OK".equals(sendSmsResponse.getCode())) {
            return new ResultUtil<Object>().setSuccessMsg("审核不通过通知短信发送成功");
        } else {
            return new ResultUtil<Object>().setErrorMsg("审核不通过通知短信发送失败");
        }
    }

    /**
     * 受理通知
     * @param phone 电话
     * @param code 编号
     * @return
     * @throws ClientException
     */
    public  Result<Object> sendAcceptMessage(String phone, String code) throws ClientException {
       
        if (StringUtils.isEmpty(phone)) {
            return new ResultUtil<Object>().setErrorMsg("手机号不能为空！");
        }

        System.out.println("验证码======" + code);
        String tempParam =" {\"code\":\""+code+"\"}";
        if(!myProps.getIsSms()){
        	return new ResultUtil<Object>().setSuccessMsg("验证码发送成功");
        }
        //调用发送短信的功能
        SendSmsResponse sendSmsResponse = smsUtil.sendSms(phone, tempParam , "SMS_189618673");
        if ("OK".equals(sendSmsResponse.getCode())) {
            return new ResultUtil<Object>().setSuccessMsg("受理通知短信发送成功");
        } else {
            return new ResultUtil<Object>().setErrorMsg("受理通知短信发送失败");
        }
    }

    /**
     * 补助发放通知
     * @param phone 电话
     * @param code 编号
     * @param money 金额
     * @param bank 银行
     * @return
     * @throws ClientException
     */
    public  Result<Object> sendGrantMessage(String phone, String code, String money, String bank) throws ClientException {
       
        if (StringUtils.isEmpty(phone)) {
            return new ResultUtil<Object>().setErrorMsg("手机号不能为空！");
        }
        if(!myProps.getIsSms()){
        	return new ResultUtil<Object>().setSuccessMsg("验证码发送成功");
        }
        System.out.println("验证码======" + code);
        String tempParam =" {\"code\":\""+code+"\",\"money\":\""+money+"\",\"bank\":\""+bank+"\"}";
        //调用发送短信的功能
        SendSmsResponse sendSmsResponse = smsUtil.sendSms(phone, tempParam , "SMS_189618684");
        if ("OK".equals(sendSmsResponse.getCode())) {
            return new ResultUtil<Object>().setSuccessMsg("补助发放通知短信发送成功");
        } else {
            return new ResultUtil<Object>().setErrorMsg("补助发放通知短信发送失败");
        }
    }
}
