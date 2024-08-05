package com.qa.orangeHRM.factory;

import com.microsoft.playwright.*;

import java.nio.file.Paths;
import java.util.Properties;

public class PlaywrightFactory {

    Playwright playwright;
    Browser browser;
    BrowserContext browserContext;
    Page page;

    public Page initBrowser(Properties props, boolean useStoredLogin){
        String browserName = props.getProperty("browser");
        String url = props.getProperty("url");
        playwright = Playwright.create();
        switch (browserName){
            case "chrome":
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
                break;
            case "firefox":
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
                break;
            case "edge":
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(false));
                break;
            case "chromium":
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
                break;
            case "safari":
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(true));
                break;
            default:
                System.out.println("PLEASE ENTER VALID BROWSER NAME");
                break;
        }
        if (useStoredLogin) {
            browserContext = browser.newContext(new Browser.NewContextOptions().setStorageStatePath(Paths.get("applogin.json")));
        }else {
            browserContext = browser.newContext();
        }
        browserContext.setDefaultTimeout(60000);
        page = browserContext.newPage();
        page.navigate(url);
        return page;
    }


    public void storeLoginInfo(BrowserContext browserContext) {
        try {
            browserContext.storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("applogin.json")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
