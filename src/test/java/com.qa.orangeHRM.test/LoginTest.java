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
        loginPage = new LoginPage(getPage());
        username = props.getProperty("username");
        password = props.getProperty("password");
    }

    @Test(priority = 3, description = "Verify that user logs in with valid credentials.")
    public void Login() {
        loginPage.navigate(props.getProperty("url"));
        basePage = loginPage.login(username,password);
        Assert.assertTrue(basePage.isAccountLinkVisible());
        System.out.println(page.context().cookies());
        playwrightFactory.storeLoginInfo(page.context());
    }

    @DataProvider(parallel = true)
    public Object[][] LoginNegativeData() {
        return new Object[][]{
                {"a", "z"},
                {"", ""},
                {"s", "a"}
        };
    }

    @Test(dataProvider = "LoginNegativeData", priority = 1, description = "Verify that user cannot log in with invalid credentials.")
    public void LoginNegativeCases(String uname, String pwd) throws IOException {
        try {

            loginPage.login(uname, pwd);
            Assert.assertTrue(loginPage.isInavlidCredsAlertPresent());
        } catch (AssertionError e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 2, description = "Verify that user can reset the password.")
    public void ForgotPassword() throws IOException {
        try {
            Assert.assertTrue(loginPage.resetPassword("Admin"));
        } catch (AssertionError e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }
}
