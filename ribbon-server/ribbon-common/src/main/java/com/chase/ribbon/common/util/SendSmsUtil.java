package com.chase.ribbon.common.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;

import java.util.Random;

/**
 * @version 1.0
 * @Description 短信发送
 * @Author chase
 * @Date 2020/12/1 11:09
 */
public class SendSmsUtil {

    @Value("${aliyun.sms.accessKeyId}")
    private static String accessKeyID;
    @Value("${aliyun.sms.accessKeySecret}")
    private static String accessKeySecret;

    /**
     * 发送短信
     * @param phoneNumber 手机号码
     */
    public static void SendSms(String phoneNumber) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyID, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        String captcha = generateCaptcha(6);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("SignName", "四季奶坊");
        request.putQueryParameter("TemplateCode", "SMS_205893417");
        request.putQueryParameter("TemplateParam", "{\"code\": "+ captcha +"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成验证码
     * @param length 验证码长度
     */
    private static String generateCaptcha(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(6));
        }
        return sb.toString();
    }

}
