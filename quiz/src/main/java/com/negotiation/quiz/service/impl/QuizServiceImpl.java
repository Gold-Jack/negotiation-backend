package com.negotiation.quiz.service.impl;

import com.negotiation.quiz.pojo.Quiz;
import com.negotiation.quiz.mapper.QuizMapper;
import com.negotiation.quiz.service.IQuizService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 测试表
一个测试表中包含多道题目 服务实现类
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-18
 */
@Service
@Primary
public class QuizServiceImpl extends ServiceImpl<QuizMapper, Quiz> implements IQuizService {

}
