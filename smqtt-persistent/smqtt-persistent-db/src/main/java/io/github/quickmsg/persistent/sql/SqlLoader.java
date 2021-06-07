package io.github.quickmsg.persistent.sql;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * @author luxurong
 */
@Slf4j
public class SqlLoader {

    private final static String DEFAULT_TABLE_INIT_SQL_DIR = "sql";

    public static List<String> loadSql() {
        List<String> sql = new ArrayList<>();
        Enumeration<URL> urlEnumeration = null;
        try {
            urlEnumeration = SqlLoader.class.getClassLoader().getResources(DEFAULT_TABLE_INIT_SQL_DIR);
            File file = new File(urlEnumeration.nextElement().getPath());
            if (file.isDirectory() && file.listFiles() != null && Objects.requireNonNull(file.listFiles()).length > 0) {
                for (File path : Objects.requireNonNull(file.listFiles())) {
                    StringBuilder stringBuilder = new StringBuilder();
                    BufferedReader br = new BufferedReader(new FileReader(path));
                    String len;
                    while ((len = br.readLine()) != null) {
                        stringBuilder.append(len);
                    }
                    sql.add(stringBuilder.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sql;
    }
}
