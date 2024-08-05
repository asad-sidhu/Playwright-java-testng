package com.qa.orangeHRM.config;

import com.microsoft.playwright.Page;
import com.qa.orangeHRM.factory.PlaywrightFactory;
import com.qa.orangeHRM.pages.BasePage;
import com.qa.orangeHRM.utils.PropertiesUtil;
import org.testng.annotations.*;

import java.util.Properties;

public class BaseTest {
    protected PlaywrightFactory playwrightFactory;
    protected Page page;
    protected PropertiesUtil propsUtil;
    protected Properties props;
    protected BasePage basePage;

    @BeforeClass
    public void setUp() {
        boolean useStoredLogin = !this.getClass().getSimpleName().equals("LoginTest");
        playwrightFactory = new PlaywrightFactory();
        propsUtil = new PropertiesUtil();
        props = propsUtil.initProperties();
        page = playwrightFactory.initBrowser(props,useStoredLogin);
    }

    @AfterClass
    public void tearDown() {
        // Teardown code
        if (page != null) {
            page.context().close();
            page = null;
        }
    }

    public Page getPage() {
        return page;
    }
}
