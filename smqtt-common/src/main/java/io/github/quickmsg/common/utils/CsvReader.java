package io.github.quickmsg.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author luxurong
 */
@Slf4j
public class CsvReader {

    public static List<List<String>> readCsvValues(String filePath) {
        File csv = new File(filePath);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(csv), StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (br != null) {
            String line = "";
            List<List<String>> records = new ArrayList<>();
            try {
                while ((line = br.readLine()) != null) {
                    List<String> lines=buildLineList(line);
                    records.add(lines);
                }
                return records;
            } catch (IOException e) {
                log.error("read auth error");
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

    private static List<String> buildLineList(String line) {
        return Arrays.stream(line.split(","))
                .collect(Collectors.toList());
    }
}
