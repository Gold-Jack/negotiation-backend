package com.negotiation.question.service.impl;

import com.negotiation.question.pojo.Question;
import com.negotiation.question.mapper.QuestionMapper;
import com.negotiation.question.service.IQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 题目表 服务实现类
 * </p>
 *
 * @author Gold_Jack
 * @since 2022-08-18
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

}
