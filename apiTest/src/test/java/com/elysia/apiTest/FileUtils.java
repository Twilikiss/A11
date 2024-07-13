package com.elysia.apiTest;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

/**
 * @author cxb
 * @ClassName FileUtils
 * @date 1/7/2023 下午4:17
 */
public class FileUtils {
//    public static String upload(MultipartFile file, String path, String server, String folderName) {
//        //判断文件夹是否存在，不存在时，创建文件夹
//        File directoryFile = new File(path);
//        if (!directoryFile.exists()) {
//            //创建多个文件夹
//            boolean isOk = directoryFile.mkdirs();
//            if (!isOk) {
//                throw new Exception();
//            }
//        }
//        try {
//            //判断文件是否为空，不为空时，保存文件
//            if (!file.isEmpty()) {
//                String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//                String name = UUID.randomUUID() + fileSuffix;
//                // 拼接资源路径
//                String filename = path + "/" + name;
//                file.transferTo(new File(filename));
//                return "http://" + server + "/wenxin/" + folderName + "/" + name;
////                return "http://www.elysialove.xyz/wenxin/ocr/test.jpg";
//            }
//            return null;
//        } catch (IOException e) {
//            throw new WenXinException(ResultCodeEnum.FILE_LOAD_ERROR);
//        }
//    }

    /**
     * 通过MultipartFile获取文件base64编码
     *
     * @param file      MultipartFile
     * @param urlEncode 如果Content-Type是application/x-www-form-urlencoded时,传true
     * @return base64编码信息，不带文件头
     * @throws IOException IO异常
     */
    public static String getFileAsBase64(MultipartFile file, boolean urlEncode) throws IOException {
        byte[] b = file.getBytes();
        String base64 = Base64.getEncoder().encodeToString(b);
        if (urlEncode) {
            base64 = URLEncoder.encode(base64, "utf-8");
        }
        return base64;
    }
    /**
     * 通过文件路径获取文件base64编码
     *
     * @param filePath      文件路径
     * @param urlEncode 如果Content-Type是application/x-www-form-urlencoded时,传true
     * @return base64编码信息，不带文件头
     * @throws IOException IO异常
     */
    public static String getFileContentAsBase64(String filePath, boolean urlEncode) throws IOException {
        byte[] b = Files.readAllBytes(Paths.get(filePath));
        String base64 = Base64.getEncoder().encodeToString(b);
        if (urlEncode) {
            base64 = URLEncoder.encode(base64, "utf-8");
        }
        return base64;
    }
    /**
     * 通过url将图片下载到服务器
     *
     * @param url 网络资源的url（图片）
     * @return 服务器提供的url，便于永久保存
     */
    public static String downloadImageByUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            // 打开连接
            URLConnection connection = url.openConnection();
            // 设置超时时间
            connection.setConnectTimeout(10000);
            // 这里使用字节流
            byte[] data = new byte[1024 * 1024];
            int len = 0;
            BufferedInputStream bi = new BufferedInputStream(connection.getInputStream());
            File file = new File("C:/Users/mlp52/Desktop");
            if (!file.exists()) {
                file.mkdirs();
            }
            String fileName = UUID.randomUUID().toString() + ".jpg";
            BufferedOutputStream bo = new BufferedOutputStream(new FileOutputStream(file.getPath() + "\\" + fileName));
            while ((len = bi.read(data)) != -1) {
                bo.write(data, 0, len);
            }
            bi.close();
            bo.close();
            System.out.println("文件输出成功，路径为" + file.getPath() + "\\" + fileName);
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
