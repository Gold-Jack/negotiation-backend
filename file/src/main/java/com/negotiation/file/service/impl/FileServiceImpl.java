package com.negotiation.file.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.negotiation.file.mapper.FileMapper;
import com.negotiation.file.pojo.LocalFile;
import com.negotiation.file.service.IFileService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件管理表 服务实现类
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-18
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, LocalFile> implements IFileService {

    public LocalFile getFileByMd5(String md5) {
        return this.getOne(Wrappers.<LocalFile>lambdaQuery()
                .eq(LocalFile::getMd5, md5));
    }
}
