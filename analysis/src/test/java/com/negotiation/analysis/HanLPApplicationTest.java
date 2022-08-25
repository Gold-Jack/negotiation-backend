package com.negotiation.analysis;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.func.VoidFunc0;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.hankcs.hanlp.HanLP;
import org.checkerframework.checker.formatter.FormatUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

//@SpringBootTest
public class HanLPApplicationTest {

    @Test
    public void testHanLPConnection() {
        File file = new File("C:\\Users\\GoldJack\\Desktop\\谈判案例\\迪士尼建设工程款谈判.txt");
        List<String> document = FileUtil.readLines(file, "UTF-8");
        System.out.println("文件内容：" + document);
        System.out.println("------------------------------------------------------------\n");
        List<String> keywords = HanLP.extractKeyword(document.toString(), 4);
        List<String> summary = HanLP.extractSummary(document.toString(), 25);
        System.out.println("keywords = " + keywords);
        System.out.println("summary = " + summary);
    }
}
