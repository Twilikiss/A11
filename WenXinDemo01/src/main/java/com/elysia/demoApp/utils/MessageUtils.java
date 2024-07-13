package com.elysia.demoApp.utils;

import com.elysia.demoApp.model.entity.user.MessageStatus;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 验证码的工具类
 * @author cxb
 * @ClassName MessageUtils
 * @date 14/5/2023 下午8:42
 */
@Component
@Slf4j
public class MessageUtils {
    @Resource
    private RedisUtils redisUtils;

    private static final Integer MAX_EXPIRE_TIME = 300;

    private static final Integer MIN_WAITING_TIME = 60;

    @Value("${user.message.apiKey}")
    private String apiKey;

    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    public boolean sendMessage(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        String s = "";
        for (int i = 0; i < 6; i++) {
            s += (int) (Math.random() * 10);
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(" http://apis.haoservice.com/sms/sendv2?mobile=" + phone + "&content=" + "【微云信息团队】您的验证码为：" + s + ",有效时长为5分钟，若非本人操作，请忽略." + "&key=" + apiKey).get().build();
        try {
            // 先看看在redis中是否有对应的缓存，有则删掉
            if (redisUtils.hasKey(phone)){
                redisUtils.del(phone);
            }
            boolean isOk = redisUtils.set(phone, s, 300);
            if (!isOk) {
                return false;
            }
            Response response = client.newCall(request).execute();
            log.info(response.message());
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 校验验证码
     * @param phone 手机号码
     * @param code 待校验的验证码
     * @return
     */
    public boolean checkMessage(String phone, String code) {
        if (!redisUtils.hasKey(phone)) {
            return false;
        }
        String key = String.valueOf(redisUtils.get(phone));
        if (key.equals(code)) {
            redisUtils.del(phone);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查是否满足【距离上一次验证码发送60秒】的要求
     * @param phone 手机号码
     * @return 返回-1表示>60秒， 0<x<60表示x秒后可以再次发送
     */
    public MessageStatus checkIntervalTime(String phone) {
        long expire = redisUtils.getExpire(phone);
        MessageStatus messageStatus = new MessageStatus();
        messageStatus.setPhone(phone);
        if (MAX_EXPIRE_TIME - expire > MIN_WAITING_TIME) {
            messageStatus.setStatus(true);
            messageStatus.setWaitingTime(-1L);
            return messageStatus;
        } else {
            messageStatus.setStatus(false);
            Long time = MIN_WAITING_TIME - (MAX_EXPIRE_TIME - expire);
            messageStatus.setWaitingTime(time);
            return messageStatus;
        }
    }
}
