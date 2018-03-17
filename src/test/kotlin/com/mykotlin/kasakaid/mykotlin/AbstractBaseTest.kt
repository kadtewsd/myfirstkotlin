package com.mykotlin.kasakaid.mykotlin

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [MykotlinApplication::class, TestConfig::class])
@ActiveProfiles("test")
abstract class AbstractBaseTest {

    @Autowired
    lateinit var context: ApplicationContext
}