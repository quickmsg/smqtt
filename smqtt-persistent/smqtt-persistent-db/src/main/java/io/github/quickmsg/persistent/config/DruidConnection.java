package io.github.quickmsg.persistent.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Druid连接类
 *
 * @author zhaopeng
 * @date 2021/06/03
 */
public class DruidConnection {
    private DataSource dataSource = null;
    private volatile static DruidConnection instatce = null;

    /**
     * 私有构造函数, 防止实例化对象
     */
    private DruidConnection() {

    }

    /**
     * 用简单单例模式确保只返回一个链接对象
     *
     * @return
     */
    public static DruidConnection getInstace() {
        if (instatce == null) {
            synchronized (DruidConnection.class) {
                if (instatce == null) {
                    instatce = new DruidConnection();
                }
            }
        }
        return instatce;
    }

    /**
     * 加载数据源
     *
     * @return {@link DataSource}
     */
    public void initDatasource(Properties properties) {
        try {
			this.dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回一个数据源
     *
     * @return {@link DataSource}
     */
    public DataSource getDataSource() {
        return this.dataSource;
    }


    /**
     * 返回一个连接
     *
     * @return {@link Connection}
     */
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}