package com.qa.orangeHRM.test;

import com.qa.orangeHRM.api.API_Requests;
import com.qa.orangeHRM.config.BaseTest;
import com.qa.orangeHRM.config.Constants;
import com.qa.orangeHRM.miscFunctions.Sorting;
import com.qa.orangeHRM.pages.BasePage;
import com.qa.orangeHRM.pages.LoginPage;
import com.qa.orangeHRM.pages.PIMPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PIMTest extends BaseTest {
    LoginPage loginPage;
    SoftAssert softAssert = new SoftAssert();
    API_Requests api_requests = new API_Requests(page);
    PIMPage pimPage;


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
        basePage.navBarNavigations("PIM");
    }

    @Test(priority = 12, description = "Verify that an employee can be added successfully.")
    public void AddEmployee() throws InterruptedException {
        pimPage = new PIMPage(page);
        pimPage.addEmployee();
    }

    @Test(priority = 13, description = "Verify that an employee can be added successfully.")
    public void EditEmployee() throws InterruptedException {
        pimPage = new PIMPage(page);
        pimPage.editEmployeePersonalDetails();
    }

    @Test(priority = 14, description = "Verify that an user is able to search an employee successfully.")
    public void SearchEmployee() throws InterruptedException {
        pimPage = new PIMPage(page);
        pimPage.searchEmployee();
    }

    @Test(priority = 15, description = "Verify that contact details of an employee can be updated.")
    public void UpdateContactDetails() throws InterruptedException {
        pimPage = new PIMPage(page);
        pimPage.editContactDetails();
    }

    @Test(priority = 16, description = "Verify that Emergency contact of an employee can be added.")
    public void AddEmergencyContacts(){
        pimPage = new PIMPage(page);
        pimPage.addEmergencyContacts();
    }

    @Test(priority = 17, description = "Verify that Emergency contact of an employee can be updated.")
    public void UpdateEmergencyContacts(){
        pimPage = new PIMPage(page);
        pimPage.editEmergencyContacts();
    }

    @Test(priority = 18, description = "Verify that Emergency contact of an employee can be deleted.")
    public void DeleteEmergencyContacts(){
        pimPage = new PIMPage(page);
        pimPage.deleteEmergencyContacts();
    }

    @Test(priority = 18, description = "Verify that an employee can be deleted successfully.")
    public void DeleteEmployee(){
        pimPage = new PIMPage(page);
        pimPage.deleteEmployee();
    }

    @Test(priority = 18, description = "Verify that sorting is working properly on the Employee table.")
    public void VerifySorting1() throws InterruptedException {
        Sorting sorting = new Sorting(page);
        sorting.verifySorting(0);
    }



}
