package com.negotiation.quiz.feign;

import com.negotiation.common.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@FeignClient("question-service")
public interface QuestionFeignService {

    @GetMapping("/question/get/{questionId}")
    R getById(@PathVariable Integer questionId);

    @GetMapping("/question/getId")
    public R<Integer> getId(@RequestBody Object question);

    @GetMapping("/question/getType")
    public R<String> getType(@RequestBody Object question);

    @GetMapping("/question/getRule")
    public R<String> getRule(@RequestBody Object question);

    @PostMapping(value = "/question/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R addQuestion(@RequestParam(defaultValue = "") String description,   // 默认无注释
                 @RequestParam(defaultValue = "multiple-choice") String type,   // 默认单项选择
                 @RequestParam(defaultValue = "Any") String rule,       // 默认所有答案正确
                 @RequestParam(defaultValue = "ADMIN") String creator,
                 @RequestPart(value = "file") MultipartFile file) throws IOException;
}
