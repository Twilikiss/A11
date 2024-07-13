package com.elysia.demoApp.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elysia.demoApp.Repository.*;
import com.elysia.demoApp.exception.WenXinException;
import com.elysia.demoApp.mapper.UserInfoMapper;
import com.elysia.demoApp.model.entity.system.Gallery;
import com.elysia.demoApp.model.entity.user.MessageStatus;
import com.elysia.demoApp.model.entity.user.UserInfo;
import com.elysia.demoApp.model.result.ImageClassifyResult;
import com.elysia.demoApp.model.result.ImageProcessResult;
import com.elysia.demoApp.model.result.OcrResult;
import com.elysia.demoApp.model.result.Text2ImageResult;
import com.elysia.demoApp.model.result.utils.ResultCodeEnum;
import com.elysia.demoApp.model.vo.LoginVo;
import com.elysia.demoApp.model.vo.UserCompleteInfoVo;
import com.elysia.demoApp.model.vo.UserInfoVo;
import com.elysia.demoApp.service.AccountService;
import com.elysia.demoApp.utils.*;
import com.elysia.demoApp.utils.helper.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author cxb
 * @ClassName AccountServiceImpl
 * @date 21/5/2023 下午7:13
 */
@Service
@Slf4j
public class AccountServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements AccountService {
    @Resource
    private MessageUtils messageUtils;
    @Resource
    private AccessTokenUtils accessTokenUtils;
    @Resource
    private Text2ImageRepository text2ImageRepository;
    @Resource
    private ImageClassifyRepository imageClassifyRepository;
    @Resource
    private OcrRepository ocrRepository;
    @Resource
    private ImageProcessRepository imageProcessRepository;
    @Resource
    private GalleryRepository galleryRepository;
    @Resource
    private RedisUtils redisUtils;
    @Value("${user.file.icons_path}")
    private String iconsFilePath;
    @Value("${user.baidu.auth.api-key}")
    private String authClientId;
    @Value("${user.baidu.auth.secret-key}")
    private String authSecretKey;
    @Value("${user.baidu.auth.redirect-url}")
    private String redirectUrl;
    @Value("${user.baidu.auth.api-url}")
    private String baiduAuthApiUrl;
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    /**
     * 用户手机+验证码注册模块（未注册自动注册）
     *
     * @param loginVo 登录对象
     * @return 相关数据（用户名+token）的哈希表
     */
    @Override
    public Map<String, Object> userLoginByWeiXin(LoginVo loginVo) {
        // 从loginVo里面获取到手机号码和验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();

        //************************测试号************************//
        if (Objects.equals(phone, "Elysia")) {
            Map<String, Object> testMap = JwtHelper.createToken("114514", "Elysia");
            Map<String, Object> resultMap = new HashMap<>();
            String accessToken = "Bearer " + testMap.get("access_token");
            resultMap.put("name", "Elysia");
            resultMap.put("access_token", accessToken);
            resultMap.put("refresh_token", testMap.get("refresh_token"));
            resultMap.put("isNew", false);
            return resultMap;
        }
        //************************测试号************************//

        // 判断手机号码+验证码是否为空
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            // 抛出参数异常
            throw new WenXinException(ResultCodeEnum.PARAM_ERROR);
        }
        // 判断手机验证码与输入的验证码是否一致
        boolean isCorrect = messageUtils.checkMessage(phone, code);

        if (!isCorrect) {
            // 验证码错误，直接抛出异常
            throw new WenXinException(ResultCodeEnum.CODE_ERROR);
        }

        // 判断是否为新用户，是则注册，否则直接登录
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        queryWrapper.eq("is_deleted", 0);
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        boolean isNew = false;
        if (userInfo == null) {
            // 未注册，则设置相关参数然后添加到我们的数据库中
            userInfo = new UserInfo();
            String uid = UUID.randomUUID().toString();
            userInfo.setUid(uid);
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            baseMapper.insert(userInfo);
            isNew = true;
        }
        // 用户存在
        // 1.校验用户状态是否可用
        if (userInfo.getStatus() == 0) {
            throw new WenXinException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        // 返回相关信息：用户名、token
        Map<String, Object> map = new HashMap<>();
        // 先查看是否有昵称
        String name = userInfo.getNickName();
        if (StringUtils.isEmpty(name)) {
            // 如果没有的昵称话返回手机号码
            name = userInfo.getPhone();
        }
        // 通过jwt生成token字符串
        UserInfo user = baseMapper.selectOne(queryWrapper);
        Map<String, Object> token = JwtHelper.createToken(user.getUid(), name);
        String accessToken = "Bearer " + token.get("access_token");
        map.put("name", name);
        map.put("access_token", accessToken);
        map.put("refresh_token", token.get("refresh_token"));
        map.put("isNew", isNew);
        return map;
    }

    @Override
    public MessageStatus checkIntervalTime(String phone) {
        return messageUtils.checkIntervalTime(phone);
    }

    @Override
    public boolean completeInfo(UserCompleteInfoVo userCompleteInfoVo, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userCompleteInfoVo, userInfo);
        userInfo.setUid(uid);
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        int i = baseMapper.update(userInfo, queryWrapper);
        return i > 0;
    }

    @Override
    public String uploadIcon(MultipartFile file, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(uid);
        //判断文件夹是否存在，不存在时，创建文件夹
        File directoryFile = new File(iconsFilePath);
        if (!directoryFile.exists()) {
            //创建多个文件夹
            boolean isOk = directoryFile.mkdirs();
            if (!isOk) {
                throw new WenXinException(ResultCodeEnum.FILE_LOAD_ERROR);
            }
        }
        try {
            //判断文件是否为空，不为空时，保存文件
            if (!file.isEmpty()) {
                String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String name = UUID.randomUUID() + fileSuffix;
                // 拼接资源路径
                String filename = iconsFilePath + "/" + name;
                userInfo.setUserIcon("http://www.elysialove.xyz/wenxin/icons/" + name);
                file.transferTo(new File(filename));
                QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("uid", uid);
                int i = baseMapper.update(userInfo, queryWrapper);
                if (i <= 0) {
                    throw new WenXinException(ResultCodeEnum.FILE_LOAD_ERROR);
                }
                return "http://www.elysialove.xyz/wenxin/icons/" + name;
            }
            return null;
        } catch (IOException e) {
            throw new WenXinException(ResultCodeEnum.FILE_LOAD_ERROR);
        }
    }

    @Override
    public UserInfoVo getUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        UserInfo result = baseMapper.selectOne(queryWrapper);
        if (result == null) {
            throw new WenXinException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(result, userInfoVo);
        return userInfoVo;
    }

    @Override
    public String getBaiduAuthUrl() {
        StringBuilder authUrl = new StringBuilder(baiduAuthApiUrl);
        // 将相关参数拼接到url上
        authUrl.append("?response_type=code");
        authUrl.append("&redirect_uri=").append(redirectUrl);
        authUrl.append("&client_id=").append(authClientId);
        authUrl.append("&display=").append("mobile");
        authUrl.append("&confirm_login=1");
        // 通过uuid生成随机字符串
        UUID temp = UUID.randomUUID();
        // 重定向后会带上state参数。建议开发者利用state参数来防止CSRF攻击。
        String state = "BD_OAUTH2_" + temp;
        // 将该state存入redis
        redisUtils.set(state, -1);
        authUrl.append("&state=").append(temp);
        return authUrl.toString();
    }

    @Override
    public Map<String, Object> userLoginByBaidu(String code, String state) {
        if (!redisUtils.hasKey("BD_OAUTH2_" + state)) {
            throw new WenXinException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        } else {
            // 判断存在对应的state后，删去对应的state
            redisUtils.del("BD_OAUTH2_" + state);
        }
        try {
            Request request01 = new Request.Builder()
                    .url("https://openapi.baidu.com/oauth/2.0/token?grant_type=authorization_code&code=" + code + "&client_id=" + authClientId + "&client_secret=" + authSecretKey + "&redirect_uri=" + redirectUrl)
                    .get()
                    .build();
            Response response01 = HTTP_CLIENT.newCall(request01).execute();
            if (response01.body() != null) {
                String result01 = response01.body().string();
                JSONObject temp01 = JSON.parseObject(result01);
                String authToken = temp01.getString("access_token");
                Request request02 = new Request.Builder()
                        .url("https://openapi.baidu.com/rest/2.0/passport/users/getInfo?access_token=" + authToken)
                        .get()
                        .build();
                Response response02 = HTTP_CLIENT.newCall(request02).execute();
                String result02 = response02.body().string();
                JSONObject temp02 = JSON.parseObject(result02);
                String openid = temp02.getString("openid");
                String username = temp02.getString("username");
                String portrait = temp02.getString("portrait");
                String birthday = temp02.getString("birthday");

                // 检查
                log.info("检查" + birthday);

                // 判断是否为新用户，是则注册，否则直接登录
                QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("uid", openid);
                UserInfo userInfo = baseMapper.selectOne(queryWrapper);
                boolean isNew = false;
                if (userInfo == null) {
                    // 未注册，则设置相关参数然后添加到我们的数据库中
                    userInfo = new UserInfo();
                    userInfo.setUid(openid);
                    userInfo.setNickName(username);
                    userInfo.setStatus(1);
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
////                    Date date = sdf.parse(birthday);
//                    //检查
////                    log.info(date.toString());
                    // 如果百度授权获取到对应的生日就设置
                    if (!"0000-00-00".equals(birthday)) {
                        String[] date = birthday.split("-");
                        log.info("年=" + date[0] + "月=" + date[1] + "日=" + date[2]);
                        userInfo.setBirth(new Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])));
                    }
                    userInfo.setUserIcon("http://tb.himg.baidu.com/sys/portrait/item/" + portrait);
                    userInfo.setUserType("baidu");
                    baseMapper.insert(userInfo);
                    isNew = true;
                }
                // 用户存在
                // 1.校验用户状态是否可用
                if (userInfo.getStatus() == 0) {
                    throw new WenXinException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
                }
                // 返回相关信息：用户名、token
                Map<String, Object> map = new HashMap<>();
                // 先查看是否有昵称
                String name = userInfo.getNickName();
                // 通过jwt生成token字符串
                UserInfo user = baseMapper.selectOne(queryWrapper);
                Map<String, Object> token = JwtHelper.createToken(user.getUid(), name);
                String accessToken = "Bearer " + token.get("access_token");
                map.put("name", name);
                map.put("access_token", accessToken);
                map.put("refresh_token", token.get("refresh_token"));
                map.put("isNew", isNew);
                return map;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public List<Text2ImageResult> getHistoryWithText2ImageBase(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        Text2ImageResult result = new Text2ImageResult();
        result.setUid(uid);
        result.setIsDeleted(0);
        result.setType("base");
        Example<Text2ImageResult> example = Example.of(result);
        List<Text2ImageResult> history = text2ImageRepository.findAll(example);
        return history;
    }

    @Override
    public List<Text2ImageResult> getHistoryWithText2ImageAdvanced(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        Text2ImageResult result = new Text2ImageResult();
        result.setUid(uid);
        result.setIsDeleted(0);
        result.setType("advanced");
        Example<Text2ImageResult> example = Example.of(result);
        List<Text2ImageResult> history = text2ImageRepository.findAll(example);
        return history;
    }

    @Override
    public List<Text2ImageResult> getHistoryWithText2ImageAll(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        Text2ImageResult result = new Text2ImageResult();
        result.setUid(uid);
        result.setIsDeleted(0);
        Example<Text2ImageResult> example = Example.of(result);
        List<Text2ImageResult> history = text2ImageRepository.findAll(example);
        return history;
    }

    @Override
    public List<ImageClassifyResult> getHistoryWithImageClassifyAll(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        ImageClassifyResult imageClassifyResult = new ImageClassifyResult();
        imageClassifyResult.setUid(uid);
        imageClassifyResult.setIsDeleted(0);
        Example<ImageClassifyResult> example = Example.of(imageClassifyResult);
        List<ImageClassifyResult> history = imageClassifyRepository.findAll(example);
        return history;
    }

    @Override
    public List<OcrResult> getHistoryWithOcrAll(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        OcrResult ocrResult = new OcrResult();
        ocrResult.setUid(uid);
        ocrResult.setIsDeleted(0);
        Example<OcrResult> example = Example.of(ocrResult);
        List<OcrResult> history = ocrRepository.findAll(example);
        return history;
    }


    @Override
    public List<ImageProcessResult> getHistoryWithImageProcessAll(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        ImageProcessResult imageProcessResult = new ImageProcessResult();
        imageProcessResult.setUid(uid);
        imageProcessResult.setIsDeleted(0);
        Example<ImageProcessResult> example = Example.of(imageProcessResult);
        List<ImageProcessResult> history = imageProcessRepository.findAll(example);
        return history;
    }


    @Override
    public Boolean userLogout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        redisUtils.setList("Access_Token_" + uid, token);
        String refreshToken = request.getHeader("RefreshToken");
        redisUtils.setList("Refresh_Token_" + uid, refreshToken);
        return true;
    }

    @Override
    public Boolean deleteUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        redisUtils.setList("Access_Token_" + uid, token);
        String refreshToken = request.getHeader("RefreshToken");
        redisUtils.setList("Refresh_Token_" + uid, refreshToken);
        // 查询并进行逻辑删除
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("uid", uid);
        UserInfo userInfo = baseMapper.selectOne(userInfoQueryWrapper);
        userInfo.setIsDeleted(1);
        int i = baseMapper.updateById(userInfo);
        return i > 0;
    }

    @Override
    public void deleteText2ImageById(String id, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        Text2ImageResult text2ImageResult = new Text2ImageResult();
        text2ImageResult.setUid(uid);
        text2ImageResult.setId(id);
        // 被删除对象应当是”未删除“的
        text2ImageResult.setIsDeleted(0);
        Example<Text2ImageResult> example = Example.of(text2ImageResult);
        if (!text2ImageRepository.findOne(example).isPresent()) {
            throw new WenXinException(ResultCodeEnum.DELETE_ERROR);
        }
        Text2ImageResult temp = text2ImageRepository.findOne(example).get();
        temp.setIsDeleted(1);
    }

    @Override
    public void deleteImageClassifyById(String id, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        ImageClassifyResult imageClassifyResult = new ImageClassifyResult();
        imageClassifyResult.setUid(uid);
        imageClassifyResult.setId(id);
        // 被删除对象应当是”未删除“的
        imageClassifyResult.setIsDeleted(0);
        Example<ImageClassifyResult> example = Example.of(imageClassifyResult);
        if (!imageClassifyRepository.findOne(example).isPresent()) {
            throw new WenXinException(ResultCodeEnum.DELETE_ERROR);
        }
        ImageClassifyResult temp = imageClassifyRepository.findOne(example).get();
        temp.setIsDeleted(1);
    }

    @Override
    public void deleteOcrById(String id, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        OcrResult ocrResult = new OcrResult();
        ocrResult.setUid(uid);
        ocrResult.setId(id);
        // 被删除对象应当是”未删除“的
        ocrResult.setIsDeleted(0);
        Example<OcrResult> example = Example.of(ocrResult);
        if (!ocrRepository.findOne(example).isPresent()) {
            throw new WenXinException(ResultCodeEnum.DELETE_ERROR);
        }
        OcrResult temp = ocrRepository.findOne(example).get();
        temp.setIsDeleted(1);
    }

    @Override
    public void deleteImageProcessById(String id, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        ImageProcessResult imageProcessResult = new ImageProcessResult();
        imageProcessResult.setUid(uid);
        imageProcessResult.setId(id);
        // 被删除对象应当是”未删除“的
        imageProcessResult.setIsDeleted(0);
        Example<ImageProcessResult> example = Example.of(imageProcessResult);
        if (!imageProcessRepository.findOne(example).isPresent()) {
            throw new WenXinException(ResultCodeEnum.DELETE_ERROR);
        }
        ImageProcessResult temp = imageProcessRepository.findOne(example).get();
        temp.setIsDeleted(1);
    }

    @Override
    public String getUserNameByUid(String uid) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        String nickName = userInfo.getNickName();
        if (!StringUtils.isEmpty(nickName)) {
            return nickName;
        } else {
            String phone = userInfo.getPhone();
            if (phone.length() == 11) {
                String data = phone.substring(0,2) + "*****" + phone.substring(7);
                return data;
            } else {
                return null;
            }
        }
    }


    @Override
    public boolean sendMessage(String phone) {
        return messageUtils.sendMessage(phone);
    }

}
