package com.elysia.demoApp.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.elysia.demoApp.Repository.GalleryRepository;
import com.elysia.demoApp.Repository.Text2ImageRepository;
import com.elysia.demoApp.exception.WenXinException;
import com.elysia.demoApp.model.entity.system.Gallery;
import com.elysia.demoApp.model.result.Text2ImageResult;
import com.elysia.demoApp.model.vo.GalleryVo;
import com.elysia.demoApp.service.AccountService;
import com.elysia.demoApp.service.AiPlatformApiService;
import com.elysia.demoApp.service.GalleryService;
import com.elysia.demoApp.utils.AccessTokenUtils;
import com.elysia.demoApp.utils.FileUtils;
import com.elysia.demoApp.utils.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author cxb
 * @ClassName GalleryServiceImpl
 * @date 17/7/2023 上午10:43
 */

@Service
public class GalleryServiceImpl implements GalleryService {
    @Resource
    private AiPlatformApiService aiPlatformApiService;
    @Resource
    private AccountService accountService;
    @Resource
    private GalleryRepository galleryRepository;
    @Resource
    private Text2ImageRepository text2ImageRepository;
    @Resource
    private AccessTokenUtils accessTokenUtils;
    @Override
    public List<GalleryVo> getAllGallery() {
        List<Gallery> galleries = galleryRepository.findAll();
        if (galleries.size() == 0) {
            return null;
        } else {
            List<GalleryVo> galleryVoList = new ArrayList<>();
            for (int i = 0;i < galleries.size(); i++) {
                GalleryVo galleryVo = new GalleryVo();
                galleryVo.setAuthor(galleries.get(i).getAuthor());
                galleryVo.setImageUrl(galleries.get(i).getImageUrl());
                galleryVo.setJsonObject(galleries.get(i).getJsonObject());
                galleryVo.setType(galleries.get(i).getType());
                galleryVo.setShareTime(galleries.get(i).getCreateTime());
                galleryVoList.add(galleryVo);
            }
            return galleryVoList;
        }
    }
    @Override
    public Boolean shareAiPainting(String id, HttpServletRequest request) {
        // 先要检查id是否已经被分享了
        boolean isExist = galleryRepository.existsById(id);
        if (isExist) {
            throw new WenXinException("画作已分享，请勿重复操作", 201);
        }
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        // 通过id获取到画作具体详情
        Text2ImageResult result = new Text2ImageResult();
        result.setUid(uid);
        result.setIsDeleted(0);
        result.setId(id);
        Example<Text2ImageResult> example = Example.of(result);
        Optional<Text2ImageResult> temp = text2ImageRepository.findOne(example);
        if (!temp.isPresent()) {
            throw new WenXinException("获取画作信息失败，请刷新后重试！", 201);
        }
        Text2ImageResult imageResult = temp.get();
        String type = imageResult.getType();
        Gallery check = null;
        if ("base".equals(type)) {
            // 处理基础版的作画结果
            JSONObject output = imageResult.getOutput();
            String taskId = output.getJSONObject("data").getString("taskId");
            String data = aiPlatformApiService.checkText2Image(taskId);
            JSONObject jsonObject = JSON.parseObject(data);
            String bdUrl = jsonObject.getJSONObject("data").getJSONArray("imgUrls").getJSONObject(0).getString("image");
            String url = FileUtils.downloadImageByUrl(bdUrl, "/data/wenxin/text2image/Share");
            Gallery gallery = new Gallery();
            gallery.setId(id);
            gallery.setUid(uid);
            String userName = accountService.getUserNameByUid(uid);
            gallery.setAuthor(userName);
            gallery.setCreateTime(new Date());
            gallery.setUpdateTime(new Date());
            gallery.setIsDeleted(0);

            JSONObject input = imageResult.getInput();
            gallery.setImageUrl(url);
            gallery.setJsonObject(input);
            gallery.setType(type);
            check = galleryRepository.insert(gallery);

        } else {
            // 处理高级版的作画的结果
            Gallery gallery = new Gallery();
            gallery.setId(id);
            gallery.setUid(uid);
            String userName = accountService.getUserNameByUid(uid);
            gallery.setAuthor(userName);
            gallery.setCreateTime(new Date());
            gallery.setUpdateTime(new Date());
            gallery.setIsDeleted(0);

            String imageUrl = imageResult.getOutput().getString("ai_painting_result");
            JSONObject input = imageResult.getInput();
            gallery.setImageUrl(imageUrl);
            gallery.setJsonObject(input);
            gallery.setType(type);
            check = galleryRepository.insert(gallery);
        }

        if (StringUtils.isEmpty(check.getId())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<GalleryVo> getSharedAiPaintingHistory(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        Gallery gallery = new Gallery();
        gallery.setUid(uid);
        Example<Gallery> example = Example.of(gallery);
        List<Gallery> galleries = galleryRepository.findAll(example);
        if (galleries.size() == 0) {
            return null;
        } else {
            List<GalleryVo> galleryVoList = new ArrayList<>();
            for (int i = 0;i < galleries.size(); i++) {
                GalleryVo galleryVo = new GalleryVo();
                galleryVo.setAuthor(galleries.get(i).getAuthor());
                galleryVo.setImageUrl(galleries.get(i).getImageUrl());
                galleryVo.setJsonObject(galleries.get(i).getJsonObject());
                galleryVo.setType(galleries.get(i).getType());
                galleryVo.setShareTime(galleries.get(i).getCreateTime());
                galleryVoList.add(galleryVo);
            }
            return galleryVoList;
        }
    }

    @Override
    public Boolean userDeleteSharedAiPaintingById(String id, HttpServletRequest request) {
        // 要注意通过id删除，必须要通过id和uid两个共同来确定
        String token = request.getHeader("Authorization");
        String uid = accessTokenUtils.getUid(token);
        Gallery temp = new Gallery();
        temp.setUid(uid);
        temp.setId(id);
        temp.setIsDeleted(0);
        Example<Gallery> example = Example.of(temp);
        Optional<Gallery> target = galleryRepository.findOne(example);
        if (target.isPresent()) {
            Gallery gallery = target.get();
            // 此时为直接删除
            galleryRepository.delete(gallery);
            return true;
        } else {
            return false;
        }
    }
}
