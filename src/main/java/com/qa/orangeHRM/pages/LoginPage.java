package com.qa.orangeHRM.pages;

import com.microsoft.playwright.*;

public class LoginPage {
    private Page page;
    private String userNameField = "input[name=\"username\"]";
    private String passwordField = "input[name=\"password\"]";
    private String loginBtn = " button[type=\"submit\"]";
    private String invalidCredsAlert  = "//p[text() = 'Invalid credentials']";
    private String forgotPasswordLink  = "Forgot your password? ";
    private String resetPasswordLinkMessage  = "Reset Password link sent successfully";


    public LoginPage(Page page){
        this.page = page;
    }

    public void navigate(String url){
        page.navigate(url);
    }

    public BasePage login(String userName, String password){
        page.fill(userNameField,userName);
        page.fill(passwordField,password);
        page.click(loginBtn);
        return new BasePage(page);
    }

    public boolean resetPassword(String userName){
        page.getByText(forgotPasswordLink).click();
        page.fill(userNameField,userName);
        page.click(loginBtn);
        try {
            page.getByText(resetPasswordLinkMessage).waitFor(new Locator.WaitForOptions().setTimeout(2000));
            return page.getByText(resetPasswordLinkMessage).isVisible();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isInavlidCredsAlertPresent(){
        try {
            page.waitForSelector(invalidCredsAlert,new Page.WaitForSelectorOptions().setTimeout(2000));
            return page.isVisible(invalidCredsAlert);
        } catch (Exception e) {
            return false;
        }
    }
}
