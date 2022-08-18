package com.negotiation.file.service;

import com.negotiation.file.pojo.LocalFile;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件管理表 服务类
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-18
 */
@Service
public interface IFileService extends IService<LocalFile> {

    LocalFile getFileByMd5(String md5);
}
