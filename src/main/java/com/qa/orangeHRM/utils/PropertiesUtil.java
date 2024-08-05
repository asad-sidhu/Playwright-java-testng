package com.qa.orangeHRM.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    Properties props;
    public Properties initProperties(){
        try {
            FileInputStream fileInputStream = new FileInputStream("./src/test/resources/config/config.properties");
            props = new Properties();
            props.load(fileInputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return props;
    }
}
