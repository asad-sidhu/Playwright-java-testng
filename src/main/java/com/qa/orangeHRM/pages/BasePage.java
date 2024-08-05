package com.qa.orangeHRM.pages;

import com.microsoft.playwright.Page;
import com.qa.orangeHRM.api.API_Requests;

public class BasePage {
    private Page page;
    private String profilePicture = "img[alt = 'profile picture']";
    private String logoutLink = "a:text('Logout')";
    private String pageTitle = "//span/h6";
    private String sideMenu = "ul.oxd-main-menu";
    private String topBarHeader = "div.oxd-topbar-header";
    private String topBarBody = "div.oxd-topbar-body";
    private String sideMenuExpandButton = "button.oxd-main-menu-button";


    public BasePage(Page page) {
        this.page = page;
    }

    public boolean isAccountLinkVisible() {
        page.waitForSelector(profilePicture);
        return page.isVisible(profilePicture);
    }

    public void logout() {
        page.click(profilePicture);
        page.click(logoutLink);
    }

    public String getPageTitle() {
        return page.textContent(pageTitle);
    }

    public void navBarNavigations(String screen) {
        if (!page.locator(sideMenu).locator("span:has-text('"+screen+"')").isVisible()) {
            page.click(sideMenuExpandButton);
        }
        page.locator(sideMenu).locator("span:has-text('"+screen+"')").click();
    }

    public void topBarBodyNavigations(String screen) {
        if (!page.locator(topBarBody).locator("span:has-text('"+screen+"')").isVisible()) {
            page.click(sideMenuExpandButton);
        }
        page.locator(sideMenu).locator("span:has-text('"+screen+"')").click();
    }

    public boolean istopBarNavigationSelected(String option) {
        return page.locator(topBarBody).locator("//li[.//*[contains(text(), '"+option.trim()+"')]]").getAttribute("class").contains("visited");
    }
}
