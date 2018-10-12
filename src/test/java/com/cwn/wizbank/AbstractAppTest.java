package com.cwn.wizbank;

import com.cwn.wizbank.report.ReportApplication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 应用测试类
 * @author Andrew.xiao 2018/5/2
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ReportApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractAppTest {
    @Autowired
    protected TestRestTemplate restTemplate;
}
