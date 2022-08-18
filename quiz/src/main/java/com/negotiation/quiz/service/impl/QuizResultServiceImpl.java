package com.negotiation.quiz.service.impl;

import com.negotiation.quiz.pojo.QuizResult;
import com.negotiation.quiz.mapper.QuizResultMapper;
import com.negotiation.quiz.service.IQuizResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 测试结果表
用于存放用户的测试结果 服务实现类
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-18
 */
@Service
public class QuizResultServiceImpl extends ServiceImpl<QuizResultMapper, QuizResult> implements IQuizResultService {

}
