package com.smarttraffic.pojo;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class RESTUtil {
    public static Gson gson = new Gson();

    private static Logger log = Logger.getLogger(RESTUtil.class.getSimpleName());

    public static String parseStringInputStream(InputStream in) {
        StringBuilder builder = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
        try {
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
        }catch (IOException e){
            log.warning("Invalid input string!");
        }
        log.info("Input String: " + builder.toString());
        return builder.toString();
    }
}
