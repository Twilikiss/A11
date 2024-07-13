package com.elysia.demoApp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.elysia.demoApp.model.entity.user.MessageStatus;
import com.elysia.demoApp.model.entity.user.UserInfo;
import com.elysia.demoApp.model.result.ImageClassifyResult;
import com.elysia.demoApp.model.result.ImageProcessResult;
import com.elysia.demoApp.model.result.OcrResult;
import com.elysia.demoApp.model.result.Text2ImageResult;
import com.elysia.demoApp.model.vo.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author cxb
 * @ClassName AccountService
 * @date 21/5/2023 下午7:12
 */
public interface AccountService extends IService<UserInfo> {
    /**
     * 验证码登录（注册）
     * @param loginVo
     * @return
     */
    Map<String, Object> userLoginByWeiXin(LoginVo loginVo);

    /**
     * 通过手机号码发送验证码
     * @param phone
     * @return
     */
    boolean sendMessage(String phone);

    /**
     * 检查短信间隔时间
     * @param phone
     * @return
     */
    MessageStatus checkIntervalTime(String phone);

    boolean completeInfo(UserCompleteInfoVo userCompleteInfoVo, HttpServletRequest request);

    String uploadIcon(MultipartFile multipartFile, HttpServletRequest request);

    UserInfoVo getUserInfo(HttpServletRequest request);

    List<Text2ImageResult> getHistoryWithText2ImageBase(HttpServletRequest request);

    String getBaiduAuthUrl();

    Map<String, Object> userLoginByBaidu(String code, String state);

    List<Text2ImageResult> getHistoryWithText2ImageAdvanced(HttpServletRequest request);

    List<Text2ImageResult> getHistoryWithText2ImageAll(HttpServletRequest request);

    List<ImageClassifyResult> getHistoryWithImageClassifyAll(HttpServletRequest request);

    List<OcrResult> getHistoryWithOcrAll(HttpServletRequest request);

    Boolean userLogout(HttpServletRequest request);

    Boolean deleteUserInfo(HttpServletRequest request);

    void deleteText2ImageById(String id, HttpServletRequest request);

    List<ImageProcessResult> getHistoryWithImageProcessAll(HttpServletRequest request);

    void deleteImageClassifyById(String id, HttpServletRequest request);

    void deleteOcrById(String id, HttpServletRequest request);

    void deleteImageProcessById(String id, HttpServletRequest request);

    String getUserNameByUid(String uid);

//    Map<String, Object> weixinLogin(WeiXinLoginVo weiXinLoginVo);
}
