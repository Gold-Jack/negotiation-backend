package com.negotiation.user.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.negotiation.common.util.R;
import com.negotiation.common.util.TypeParser;
import com.negotiation.user.feign.AnalysisFeignService;
import com.negotiation.user.feign.FileFeignService;
import com.negotiation.user.feign.QuizFeignService;
import com.negotiation.user.feign.QuizResultFeignService;
import com.negotiation.user.pojo.User;
import com.negotiation.user.service.IUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.IntConsumer;

import static com.negotiation.common.util.ResponseCode.*;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-17
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private FileFeignService fileFeignService;
    @Autowired
    private QuizFeignService quizFeignService;
    @Autowired
    private QuizResultFeignService quizResultFeignService;
    @Autowired
    private AnalysisFeignService analysisFeignService;

    /**
     * 通过ID获取某个用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/get/{userId}")
    public R getById(@PathVariable Integer userId) {
        User userById = userService.getById(userId);
        return R.success(userById);
    }

    /**
     * 用过User对象获取用户信息
     * @param user 用户对象
     * @return 用户信息
     */
    @GetMapping("/get")
    public R getByEntity(@RequestBody User user) {
        return getById(user.getUserId());
    }

    @ApiOperation("通过userId获取用户做完的quiz对象")
    @GetMapping("/getUserQuiz/{userId}")
    public R getUserQuizById(@PathVariable Integer userId) {
        User user = userService.getById(userId);
        assert ObjectUtil.isNotNull(user);
        return getUserQuiz(user.getQuizFinishedId());
    }

    /**
     * 获取用户做完的Quiz对象列表
     * @return
     */
    @ApiOperation("通过quizIdList获取用户做完的quiz对象")
    @GetMapping("/getUserQuiz")
    public R getUserQuiz(@RequestParam String quizIdList) {
        // 如果quizIdList是空的，说明用户没有做完的quiz，直接返回null
        if (StrUtil.isBlank(quizIdList)) {
            return R.success(null);
        }
        // 从quizIdList中获取Integer类型的quizIds
        int[] quizDoneId = StrUtil.splitToInt(quizIdList, ",");
        List<Object> quizDoneList = new ArrayList<>();
        try {
            Arrays.stream(quizDoneId).iterator().forEachRemaining(
                    (IntConsumer) (quizId) -> {
                        /*
                        * 通过quizFeignService获取每个quizId对应的quiz信息，并存入quizDoneList
                        * */
                        Object quizById = quizFeignService.getById(quizId).getData();
                        assert ObjectUtil.isNotNull(quizById);
                        quizDoneList.add(quizById);
                    }
            );
        } catch (Exception e) {
            return R.error(CODE_100, CODE_100.getCodeMessage().concat(": " + e.getMessage()));
        }
        return R.success(quizDoneList);
    }


    @ApiOperation("通过userId获取用户做完的quiz的result信息")
    @GetMapping("/getUserResult/{userId}")
    public R getUserResultById(@PathVariable Integer userId) {
        User userById = userService.getById(userId);
        assert ObjectUtil.isNotNull(userById);
        return getUserResult(userById.getQuizResultId());
    }

    @ApiOperation("通过resultIdList获取用户做完的quiz的result信息")
    @GetMapping("/getUserResult")
    public R getUserResult(@RequestParam String resultIdList) {
        if (StrUtil.isBlank(resultIdList)) {
            return R.success(null);
        }
        int[] resultIds = StrUtil.splitToInt(resultIdList, ",");
        List<Object> resultList = new ArrayList<>();
        try {
            Arrays.stream(resultIds).iterator().forEachRemaining(
                    (IntConsumer) (resultId) -> {
                        /*
                        * 通过quizResultFeignService由resultId获取result信息，并存入resultList
                        * */
                        Object quizResult = quizResultFeignService.getById(resultId).getData();
                        assert ObjectUtil.isNotNull(quizResult);
                        resultList.add(quizResult);
                    }
            );
        } catch (Exception e) {
            return R.error(CODE_100, CODE_100.getCodeMessage().concat(": " + e.getMessage()));
        }
        return R.success(resultList);
    }


    /**
     * 用户提交答案
     * 这里假设传入的答案是Map<题号(int), 用户答案(String)>的形式
     * @return
     */
    @ApiOperation("用户答案提交")
    @PostMapping("/submitAnswer")
    public R submitAnswer(@RequestParam Integer userId,
                          @RequestParam Integer quizId,
                          @RequestBody Map<String, String> userAnswer) {
        /*
        * TODO 通过AnalysisFeignService分析用户答案，获得用户得分，并更新quizDoneId和quizResult
        * */
        R analysisResult = analysisFeignService.doAnalysis(quizId, userAnswer);
        Integer resultId = (Integer) analysisResult.getData();
        Object result = quizResultFeignService.getById(resultId).getData();

        /*
        * 更新用户quizFinishedId和quizResultId
        * */
        User user = userService.getById(userId);
        // 更新quizFinishedId
        HashSet<Integer> quizIdHash = user.getQuizFinishedIdHash();
        if (!quizIdHash.contains(quizId)) {     // 如果是第一次做当前quiz，则在quizFinishedId中添加当前quizId
            quizIdHash.add(quizId);
            user.setQuizFinishedIdHash(quizIdHash);
        }
        // 更新quizResultId
        HashSet<Integer> resultIdHash = user.getQuizResultIdHash();
        if (resultIdHash.contains(resultId)) {      // 如果发现有相同的resultId，则说明发生了系统性错误，返回error
            return R.error(CODE_100, CODE_100.getCodeMessage().concat(": resultId重复"));
        }
        resultIdHash.add(resultId);     // 如果resultId唯一，则在quizResultId中添加当前resultId
        user.setQuizResultIdHash(resultIdHash);

        // 更新用户答题信息
        userService.updateUser(user);
        return R.success(user);
    }



    /**
     * 用户信息持久化
     * @param user 用户信息
     * @return 是否持久化成功
     */
    @PostMapping("/persistence")
    public R persistence(@RequestBody User user) {
        User deprecatedUser = userService.getOne(
                Wrappers.<User>lambdaQuery()
                        .eq(User::getUsername, user.getUsername()));

        if (deprecatedUser != null) {   // 有重复的用户名（不允许相同的用户名！）
            return R.error(CODE_301, CODE_301.getCodeMessage());
        }

        user.setAuthority("GUEST");     // 暂时默认新建用户的权限为GUEST

        return userService.save(user) ?
                R.success() : R.error(CODE_313, CODE_313.getCodeMessage());
    }


    /**
     * 用户头像上传
     * @param userId 用户ID
     * @param avatarImage 用户头像文件
     * @return 用户头像url
     * @throws IOException
     */
    @PostMapping(value = "/upload-avatar-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R uploadUserAvatarImage(@RequestParam Integer userId,
                                   @RequestPart MultipartFile avatarImage) throws IOException {
        R uploadResult;
        try {
            uploadResult = fileFeignService.upload(avatarImage);
        } catch (IOException ioException) {     // 如果文件上传失败，返回错误码
            return R.error(CODE_101, CODE_101.getCodeMessage());
        }
        // 确保upload方法封装的数据是String类型
        assert StrUtil.equals(uploadResult.getData().getClass().getName(), String.class.getName());
        // 获取avatarUrl
        String avatarImageUrl = (String) uploadResult.getData();
        // 通过userId获取user信息
        User user = userService.getById(userId);
        user.setAvatarUrl(avatarImageUrl);

        // 更新带有avatarUrl的user信息
        userService.updateById(user);

        // 返回avatarUrl
        return R.success(user.getAvatarUrl());
    }

//    /**
//     * 更新
//     * @param user 新的用户信息
//     * @return 是否更新成功
//     */
//    @PutMapping("/update")
//    public R updateUser(@RequestBody User user) {
//        userService.up
//    }

}
