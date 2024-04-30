package com.czy.service.impl;

import com.czy.domain.ResponseResult;
import com.czy.enums.AppHttpCodeEnum;
import com.czy.handler.execption.SystemException;
import com.czy.service.UploadService;
import com.czy.utils.PathUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * ClassName: UploadServiceImpl
 * Package: com.czy.com.czy.com.czy.com.czy.com.czy.service.impl
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@Service
@ConfigurationProperties(prefix = "myoss")//指定读取application.yml文件的myoss属性的数据
@Data
public class UploadServiceImpl implements UploadService {
    @Override
    public ResponseResult upload(MultipartFile img) {
        // 判断文件类型大小等
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();

        // 获取文件大小
        long fileSize = img.getSize();

        // 判断文件大小是否超过2MB（2MB=2*1024*1024 bytes）
        if (fileSize > 2 * 1024 * 1024) {
            // 抛出文件大小超过限制的异常
            throw new SystemException(AppHttpCodeEnum.FILE_SIZE_ERROR);
        }

        //对原始文件名进行判断大小。只能上传png或jpg文件
        if(!originalFilename.endsWith(".png") && !originalFilename.endsWith(".jpg")){
            //AppHttpCodeEnum是我们在huanf-framework写的枚举类，FILE_TYPE_ERROR代表文件类型错误的提示
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        // 上传图片到OSS
        // 生成文件路径
        String filePath = PathUtils.generateFilePath(img.getOriginalFilename());
        String url = uploadOss(img, filePath);

        return ResponseResult.okResult(url);
    }

    private String accessKey;
    private String secretKey;
    private String bucket;


    private String uploadOss(MultipartFile img, String filePath) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;
        try {
//            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
//            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
            InputStream inputStream = img.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "http://sc6f3v8x7.hb-bkt.clouddn.com/"+key;
            } catch (QiniuException ex) {
                ex.printStackTrace();
                if (ex.response != null) {
                    System.err.println(ex.response);

                    try {
                        String body = ex.response.toString();
                        System.err.println(body);
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (Exception ex) {
            //ignore
        }

        return "上传文件失败";
    }
}
