package com.negotiation.question.controller;


import com.negotiation.common.util.R;
import com.negotiation.question.feign.FileFeignService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * <p>
 * 题目表 前端控制器
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-18
 */
@RestController
@RequestMapping("/question")
public class QuestionController {

    @Resource
    private FileFeignService fileFeignService;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R addQuestion(@RequestPart(value = "file") MultipartFile file) throws IOException {
        return fileFeignService.upload(file);
    }
}
