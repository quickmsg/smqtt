package io.github.quickmsg.persistent.registry;

import io.github.quickmsg.common.environment.EnvContext;
import io.github.quickmsg.common.message.MessageRegistry;
import io.github.quickmsg.common.message.RetainMessage;
import io.github.quickmsg.common.message.SessionMessage;
import io.github.quickmsg.persistent.DbConnectionHolder;
import io.github.quickmsg.persistent.config.DruidConnectionProvider;
import io.github.quickmsg.persistent.sql.SqlLoader;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class DbMessageRegistry implements MessageRegistry {

    private static final Integer INIT_SIZE_DB = 10;

    private static final Integer MAX_ACTIVE_DB = 300;

    private static final Integer MAX_WAIT_DB = 60000;

    private static final Integer MIN_IDLE_DB = 2;

    @Override
    public void startUp(EnvContext envContext) {
        String path = DbMessageRegistry.class.getClassLoader().getResource("/dbChangelog.xml").getPath();
        Properties properties = new Properties();
        properties.putAll(envContext.getEnvironments());
        DruidConnectionProvider
                .singleTon()
                .init(properties)
             /*   .then(Mono.when(SqlLoader.loadSql().stream().map(sql ->
                        DbConnectionHolder.getDslContext().doOnNext(dslContext -> dslContext.execute(sql))
                ).collect(Collectors.toList())))*/
                .subscribe();

        DruidConnectionProvider.singleTon().getConnection().subscribe(conn -> {
                    Database database = null;
                    try {
                        database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
                    } catch (DatabaseException e) {
                        e.printStackTrace();
                    }
                    Liquibase liquibase = new Liquibase(path, new FileSystemResourceAccessor(), database);
                    try {
                        liquibase.update("db");
                    } catch (LiquibaseException e) {
                        e.printStackTrace();
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
}
