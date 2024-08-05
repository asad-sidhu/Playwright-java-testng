package com.qa.orangeHRM.utils;

import com.microsoft.playwright.Page;
import com.qa.orangeHRM.config.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;


public class ScreenshotUtil {
    public static String takeScreenshot(Page page){
        try {
            String fileName = "Screenshot_"+ DateUtil.getCurrentTime()+".png";
            String filePath = Paths.get(Constants.SCREENSHOTS_PATH,fileName).toString();
            Files.createDirectories(Paths.get(Constants.SCREENSHOTS_PATH));

            byte[] buffer = page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)).setFullPage(true));
            String base64Path = Base64.getEncoder().encodeToString(buffer);
            return base64Path;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
