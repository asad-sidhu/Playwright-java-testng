package com.qa.orangeHRM.test;

import com.qa.orangeHRM.api.API_Requests;
import com.qa.orangeHRM.config.BaseTest;
import com.qa.orangeHRM.config.Constants;
import com.qa.orangeHRM.miscFunctions.Sorting;
import com.qa.orangeHRM.pages.BasePage;
import com.qa.orangeHRM.pages.LoginPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class AdminTest extends BaseTest {
    LoginPage loginPage;
    SoftAssert softAssert = new SoftAssert();
    API_Requests api_requests = new API_Requests(page);


    @BeforeClass
    public void Login() {
        basePage = new BasePage(page);
        loginPage = new LoginPage(page);
        if (page.url().contains(Constants.LOGIN_LINK)) {
            loginPage.login(props.getProperty("username"), props.getProperty("password"));
        }
//        basePage.navBarNavigations("PIM");
    }

    @BeforeMethod
    public void Navigate() {
        basePage = new BasePage(page);
        basePage.navBarNavigations("Admin");
    }

    @Test(priority = 18, description = "Verify that sorting is working properly on the Admins table.")
    public void VerifySorting1() throws InterruptedException {
        Sorting sorting = new Sorting(page);
        sorting.verifySorting(0);
    }



}
