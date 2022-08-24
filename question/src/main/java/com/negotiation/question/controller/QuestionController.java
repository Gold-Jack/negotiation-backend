package com.negotiation.question.controller;


import cn.hutool.core.util.StrUtil;
import com.negotiation.common.util.R;
import com.negotiation.question.feign.FileFeignService;
import com.negotiation.question.pojo.Question;
import com.negotiation.question.service.IQuestionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

import static com.negotiation.common.util.ResponseCode.CODE_101;
import static com.negotiation.common.util.ResponseCode.CODE_313;

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

    @Autowired
    private FileFeignService fileFeignService;
    @Autowired
    private IQuestionService questionService;

    /**
     * 通过questionId获取question对象
     * @param questionId
     * @return question对象
     */
    @GetMapping("/get/{questionId}")
    public R getById(@PathVariable Integer questionId) {
        Question questionById = questionService.getById(questionId);
        return R.success(questionById);
    }

    @ApiOperation("通过question对象，获取questionId")
    @GetMapping("/getId")
    public R<Integer> getId(@RequestBody Object question) {
        assert StrUtil.equals(question.getClass().getName(), Question.class.getName());
        return R.success(((Question) question).getQuestionId());
    }

    @ApiOperation("通过questionId，获取question的type")
    @GetMapping("/getType/{questionId}")
    public R<String> getTypeById(@PathVariable Integer questionId) {
        Question questionById = questionService.getById(questionId);
        return R.success(questionById.getType());
    }

    @ApiOperation("通过question对象，获取question的type")
    @GetMapping("/getType")
    public R<String> getType(@RequestBody Object question) {
        assert StrUtil.equals(question.getClass().getName(), Question.class.getName());
        return R.success(((Question) question).getType());
    }

    @ApiOperation("通过questionId，获取question的评分标准rule")
    @GetMapping("/getRule/{questionId}")
    public R<String> getRuleById(@PathVariable Integer questionId) {
        Question questionById = questionService.getById(questionId);
        return R.success(questionById.getRule());
    }

    @ApiOperation("通过question对象，获取question的评分标准rule")
    @GetMapping("/getRule")
    public R<String> getRule(@RequestBody Object question) {
        assert StrUtil.equals(question.getClass().getName(), Question.class.getName());
        return R.success(((Question) question).getRule());
    }

    /**
     * 新建question，并存储至数据库
     * @param description 问题描述
     * @param type 问题类型
     * @param rule 评分标准
     * @param creator 问题创建人
     * @param file 问题内容文件
     * @return question对象
     * @throws IOException 问题内容文件上传时可能产生的Exception
     */
    @ApiOperation("新增一个question，目前只允许管理员操作")
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R addQuestion(@RequestParam(defaultValue = "") String description,   // 默认无注释
                         @RequestParam(defaultValue = "average") String judgingAbility,
                         @RequestParam(defaultValue = "multiple-choice") String type,   // 默认单项选择
                         @RequestParam(defaultValue = "Any") String rule,       // 默认所有答案正确
                         @RequestParam(defaultValue = "ADMIN") String creator,
                         @RequestPart MultipartFile file) throws IOException {
        // 默认值处理
        if (StrUtil.isBlank(description)) {
            description = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0];
        }

        R uploadResult;
        try {
            uploadResult = fileFeignService.upload(file);
        } catch (IOException ioException) {
            return R.error(CODE_101, CODE_101.getCodeMessage());
        }
        // 确保uploadResult返回的是String类型的data
        assert StrUtil.equals(uploadResult.getData().getClass().getName(), String.class.getName());
        String questionContentUrl = (String) uploadResult.getData();

        // 创建新的question信息
        Question question = new Question();
        question.setDescription(description);
        question.setJudgingAbility(judgingAbility);
        question.setType(type);
        question.setRule(rule);
        question.setCreator(creator);
        question.setQuestionContentUrl(questionContentUrl);

        // 持久化question信息
        return questionService.save(question) ?
                R.success(question) : R.error(CODE_313, CODE_313.getCodeMessage());
    }

}
