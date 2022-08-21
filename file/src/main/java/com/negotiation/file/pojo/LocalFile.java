package com.negotiation.file.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 文件管理表
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("file")
@ApiModel(value = "File对象", description = "文件管理表")
public class LocalFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文件ID")
    @TableId(value = "file_id", type = IdType.AUTO)
    private Integer fileId;

    @ApiModelProperty("文件名称")
    private String filename;

    @ApiModelProperty("文件类型（后缀名）")
    private String fileType;

    @ApiModelProperty("文件大小（单位：kb）")
    private Long fileSize;

    @ApiModelProperty("文件md5码")
    private String md5;

    @ApiModelProperty("文件uuid")
    private String fileUuid;

    @ApiModelProperty("文件下载url")
    private String downloadUrl;

    @ApiModelProperty("文件是否可用：默认1可用")
    private Integer isEnable;

    @ApiModelProperty("文件创建时间")
    private Date gmtCreate;

    @ApiModelProperty("文件修改或更新时间")
    private Date gmtModified;

    @ApiModelProperty("逻辑删除 默认0 未删除")
    @TableLogic
    private Integer isDeleted;


}
