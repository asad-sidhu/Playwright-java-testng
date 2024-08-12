package com.qa.orangeHRM.test;

import com.qa.orangeHRM.config.BaseTest;
import com.qa.orangeHRM.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTest extends BaseTest {
    LoginPage loginPage;
    private String username = "";
    private String password = "";

    @BeforeClass
    public void navigate() {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        loginPage = new LoginPage(getPage());
        username = props.getProperty("username");
        password = props.getProperty("password");
//        loginPage.login(username,password);
    }


    @DataProvider()
    public Object[][] LoginNegativeData() {
        return new Object[][]{
                {"a", "z"},
                {"", ""},
                {"s", "a"}
        };
    }

    @Test(dataProvider = "LoginNegativeData", priority = 1, enabled = true, description = "Verify that user cannot log in with invalid credentials.")
    public void LoginNegativeCases(String uname, String pwd){
        try {
            System.out.println("THE USER NAME IS :"+uname);
            System.out.println("THE PASSWORD IS :"+pwd);
            loginPage.login(uname, pwd);
            Assert.assertTrue(loginPage.isInavlidCredsAlertPresent());
        } catch (AssertionError | Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 2, description = "Verify that user can reset the password.")
    public void ForgotPassword() throws IOException {
        try {
            Assert.assertTrue(loginPage.resetPassword("Admin"));
        } catch (AssertionError | Exception e) {
            throw e;
        }
    }

    @Test(priority = 3, description = "Verify that user logs in with valid credentials.")
    public void Login() {
        loginPage.navigate(props.getProperty("url"));
        basePage = loginPage.login(username,password);
        Assert.assertTrue(basePage.isAccountLinkVisible());
        System.out.println(page.context().cookies());
        playwrightFactory.storeLoginInfo(page.context());
    }
}
