package com.mykotlin.kasakaid.mykotlin

import lombok.SneakyThrows
import lombok.extern.slf4j.Slf4j
import org.dbunit.DatabaseUnitException
import org.dbunit.database.DatabaseConfig
import org.dbunit.database.DatabaseConnection
import org.dbunit.dataset.csv.CsvDataSet
import org.dbunit.ext.mysql.MySqlDataTypeFactory
import org.dbunit.operation.DatabaseOperation
import org.junit.rules.ExternalResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DataSourceUtils
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.stereotype.Component
import java.io.File
import java.sql.SQLException
import java.util.logging.Logger
import javax.annotation.Resource


@Component
@Slf4j
class MyResource : ExternalResource() {

    /** ApplicationContext  */
    @Autowired
    protected var context: ApplicationContext? = null

    /** データソース  */
    @Resource
    private val masterDataSource: TransactionAwareDataSourceProxy? = null

    @Autowired
    private val environment: Environment? = null

    override fun before() {
//        log.info(this.javaClass.name + "before start.")
    }

    override fun after() {
//        log.info(this.javaClass.name + "after start")
    }

    /**
     * テストデータ投入メソッド
     *
     * @param testData テストデータ
     * @throws Exception 例外
     */
    @Throws(Exception::class)
    fun insertData(vararg testData: String) {
        var dbConn: DatabaseConnection? = null
        try {
            // DatabaseConnectionの作成
            dbConn = openDbConn()
            for (data in testData) {
                // データセットの取得
                val dataSet = CsvDataSet(File(CSV_DIRECTORY + data))
                // セットアップ実行
                DatabaseOperation.CLEAN_INSERT.execute(dbConn, dataSet)
            }
        } finally {
            // DatabaseConnectionの破棄
            closeDbConn(dbConn)
        }
    }

    /**
     * テストデータ削除メソッド
     *
     * @throws Exception 例外
     */
    @SneakyThrows
    @Throws(Exception::class)
    fun truncateTable() {
        val dbConn = openDbConn()
        executeStatement(dbConn, truncateReference("FALSE"))
        try {
            // データセットの取得
            val dataSet = CsvDataSet(File(CSV_DIRECTORY + TRUNCATE_DIRECTORY))
            DatabaseOperation.TRUNCATE_TABLE.execute(dbConn, dataSet)
        } finally {
            // DatabaseConnectionの破棄
            executeStatement(dbConn, truncateReference("TRUE"))
            closeDbConn(dbConn)
        }
    }

    private val isMySQL: Boolean
        get() = environment!!.getProperty("spring.datasource.url").contains("mysql")

    private fun truncateReference(value: String): String {
        return if (isMySQL) "set foreign_key_checks = $value;" else "SET REFERENTIAL_INTEGRITY $value;"
    }

    @Throws(DatabaseUnitException::class)
    fun openDbConn(): DatabaseConnection {
        val databaseConnection = DatabaseConnection(DataSourceUtils.getConnection(masterDataSource!!))
        val config = databaseConnection.getConfig()
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, MySqlDataTypeFactory())
        return databaseConnection
    }

    @Throws(SQLException::class)
    fun closeDbConn(dbConn: DatabaseConnection?) {
        if (dbConn != null) {
            dbConn.close()
        }
    }

    @Throws
    private fun executeStatement(dbConnection: DatabaseConnection, statement: String) {
        val st = dbConnection.getConnection().createStatement()
        st.execute(statement)
    }

    companion object {

        /** CSVデータ格納ディレクトリ  */
        private val CSV_DIRECTORY = "src/test/resources/testData/"


        private val TRUNCATE_DIRECTORY = "/truncate"
    }
}
