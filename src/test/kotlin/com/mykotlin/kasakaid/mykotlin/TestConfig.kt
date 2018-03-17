package com.mykotlin.kasakaid.mykotlin

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType

@Configuration
@Profile("test")
@PropertySource("classpath:application-test.properties")
class TestConfig(private val environment:Environment) {
    @Bean
    fun dataSource(): TransactionAwareDataSourceProxy {
        if ("jdbc:log4jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_ON_EXIT=FALSE" == environment.getProperty("spring.datasource.url")) {
            // H2 の読み込みをさせないと、JPAが作成したデータベースにアプリケーションからアクセスできない。そのため、これで対処。
            val ds = EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()
            val proxyDs = net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy(ds)
            return TransactionAwareDataSourceProxy(proxyDs)
        }
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"))
        dataSource.url = environment.getProperty("spring.datasource.url")
        dataSource.username = environment.getProperty("spring.datasource.username")
        dataSource.password = environment.getProperty("spring.datasource.password")

        return TransactionAwareDataSourceProxy(dataSource)
    }
}
