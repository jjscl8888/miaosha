package com.jjs.miaosha.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * @author jjs
 * @Version 1.0 2020/5/3
 */
public class DBUtils {
    private static Properties properties;

    static {
        try {
            InputStream in = DBUtils.class.getClassLoader().getResourceAsStream("application.properties");
            properties = new Properties();
            properties.load(in);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() throws Exception {
        String url = properties.getProperty("spring.datasource.url");
        String username = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");
        String driver = properties.getProperty("spring.datasource.driver-class-name");
        Class.forName(driver);
        return DriverManager.getConnection(url,username,password);
    }
}
