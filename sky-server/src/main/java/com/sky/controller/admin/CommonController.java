package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.constant.PathConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传");
        try {
            String originalFilename = file.getOriginalFilename();
            String exitension = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String objectName = UUID.randomUUID().toString() + exitension;
//            String filePath = aliOssUtil.upload(file.getBytes(),objectName);
            String filePath = PathConstant.IMAGE_PATH + objectName;
            log.info("图片存放于:{}",filePath);
            file.transferTo(new File(filePath));
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/")
                    .path(objectName)
                    .toUriString();//动态生成url路径便于部署
            log.info("图片访问url:{}",imageUrl);
            return Result.success(imageUrl);

        } catch (IOException e) {
            log.error("文件上传失败：{}",e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
