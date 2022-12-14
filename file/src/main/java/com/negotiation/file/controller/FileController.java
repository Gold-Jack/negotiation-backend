package com.negotiation.file.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.negotiation.common.util.R;
import com.negotiation.file.pojo.LocalFile;
import com.negotiation.file.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 文件管理表 前端控制器
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-18
 */
@RestController
@RequestMapping("/file")
public class FileController {

    private static final String PROJECT_PATH = System.getProperty("user.dir");  // 这里是项目路径：/negotiation
    private static final String RELATIVE_PATH = "/localized-files";       // 这里是项目内的相对路径，从/negotiation开始
    private static final String globalFileUploadPath = PROJECT_PATH + RELATIVE_PATH + "/";

    @Value("${spring.application.name}")
    private String SERVICE_NAME;
    @Value("${server.port}")
    private String PORT;
    @Value("${project.deployment.host}")
    private String HOST;

    @Autowired
    @Qualifier("fileServiceImpl")
    private IFileService fileService;

    /**
     * 文件下载
     * @param fileUuid  文件唯一标识码
     * @param response  后端响应，此处封装字节流
     * @return 文件本身
     * @throws IOException
     */
    @GetMapping("/download/{fileUuid}")
    public R download(@PathVariable String fileUuid,
                      HttpServletResponse response) throws IOException {
        // 根据文件唯一标识码获取文件
        File targetFile = new File(globalFileUploadPath + fileUuid);
        // 设置输出流格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment; filename=" + URLDecoder.decode(fileUuid, StandardCharsets.UTF_8));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(FileUtil.readBytes(targetFile));
        os.flush();
        os.close();
        return R.success();
    }


    /**
     * 通过downloadUrl获取文件的uuid
     * @param downloadUrl 文件下载url
     * @return 文件的uuid
     */
    @GetMapping("/get-uuid")
    public R getFileUuidByUrl(@RequestParam String downloadUrl) {
        String[] urlList = downloadUrl.split("/");
        String fileUuid = urlList[urlList.length - 1];
        return R.success(fileUuid);         // TODO 暂时不知道这里会有什么error
    }

    /**
     * 通过downloadUrl获取文件类型
     * @param downloadUrl
     * @return 文件类型（小写）
     */
    @GetMapping("/get-type")
    public R getFileType(@RequestParam String downloadUrl) {
        List<String> split = StrUtil.split(downloadUrl, ".");
        String fileType = split.get(split.size() - 1).toLowerCase();
        return R.success(fileType);
    }


    /**
     * 文件上传控制
     * @param file 前端上传的文件，需要通过file.getInputStream()或者file.transferTo(File dest)转为java.util.File
     * @return 文件的downloadUrl
     * @throws IOException
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R upload(@RequestPart(value = "file") MultipartFile file) throws IOException {
        // 获取原始文件名
        String originalFileName = file.getOriginalFilename();
        String fileType = FileUtil.extName(originalFileName);
        Long fileSize = file.getSize() / 1024;  // 以kb为单位保存

        // uuid可以唯一标识文件，防止文件重名
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + fileType;
        File uploadFile = new File(globalFileUploadPath + fileUUID);

        // 检查文件上传路径是否为空，如果为空，新建文件夹
        if (!uploadFile.getParentFile().exists()) {
            uploadFile.getParentFile().mkdirs();
        }

        // 利用文件md5码，防止相同文件多次上传，挤占内存空间
        String md5 = SecureUtil.md5(file.getInputStream());     // 根据上传文件生成md5，getInputStream()是为了把MultipartFile转为java.util.File类型
        LocalFile dbFile = fileService.getFileByMd5(md5);           // 查询数据库中是否有相同的md5
        if (dbFile != null) {   // 数据库中已经有相同的文件
            return R.success(dbFile.getDownloadUrl());   // 直接返回该文件的downloadUrl
        }

        /*
         * 如果数据库中没有相同md5码的文件，说明当前上传的文件是新文件，则上传文件，生成url
         * */
        // 因为数据库类File与java的File重名，所以把数据库类改成LocalFile
        LocalFile toSaveFile = new LocalFile();
        // 将文件上传到指定位置
        file.transferTo(uploadFile);
        // 生成下载地址
        String downloadUrl = "http://" + HOST + ":" + PORT + "/file/download/" + fileUUID;
        /*
         * 将文件信息写入数据库
         * */

        // 设置文件的基本属性
        toSaveFile.setFilename(originalFileName);
        toSaveFile.setFileType(fileType);
        toSaveFile.setFileSize(fileSize);
        toSaveFile.setMd5(md5);
        toSaveFile.setFileUuid(fileUUID);
        toSaveFile.setDownloadUrl(downloadUrl);
        // 存储文件到数据库
        fileService.save(toSaveFile);

        return R.success(downloadUrl);
    }

}
