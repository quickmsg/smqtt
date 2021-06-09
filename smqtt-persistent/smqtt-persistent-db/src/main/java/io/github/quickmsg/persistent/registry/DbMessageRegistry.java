package io.github.quickmsg.persistent.registry;

import io.github.quickmsg.common.environment.EnvContext;
import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.message.RetainMessage;
import io.github.quickmsg.common.message.SessionMessage;
import io.github.quickmsg.persistent.config.DruidConnectionProvider;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.FileSystemResourceAccessor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Slf4j
public class DbMessageRegistry implements MessageRegistry {

    private static final Integer INIT_SIZE_DB = 10;

    private static final Integer MAX_ACTIVE_DB = 300;

    private static final Integer MAX_WAIT_DB = 60000;

    private static final Integer MIN_IDLE_DB = 2;

    @Override
    public void startUp(EnvContext envContext) {
        Properties properties = new Properties();
        properties.putAll(envContext.getEnvironments());
        DruidConnectionProvider
                .singleTon()
                .init(properties)
                .subscribe();

        DruidConnectionProvider.singleTon().getConnection().subscribe(conn -> {
                    Database database;
                    try {
                        database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
                        Liquibase liquibase = new Liquibase("classpath:liquibase/dbChangelog.xml", new FileSystemResourceAccessor(), database);
                        liquibase.update("db");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            conn.close();
                        } catch (SQLException e) {

                        }
                    }
                }
        );
    }

    // TODO session一个表  retain 一个表
    @Override
    public List<SessionMessage> getSessionMessages(String clientIdentifier) {
        return null;
    }

    @Override
    public void sendSessionMessages(SessionMessage sessionMessage) {

    }

    @Override
    public void saveRetainMessage(RetainMessage retainMessage) {

    }

    @Override
    public List<RetainMessage> getRetainMessage(String topic) {
        return null;
    }

    public static void main(String[] args) {
        DbMessageRegistry dbMessageRegistry = new DbMessageRegistry();
        dbMessageRegistry.startUp(new EnvContext());
    }
}
