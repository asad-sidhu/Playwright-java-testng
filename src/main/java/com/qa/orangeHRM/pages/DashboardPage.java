package com.qa.orangeHRM.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.List;

public class DashboardPage {
    private Page page;
    private static final String dashboardwidgets = "//div[contains(@class,'orangehrm-dashboard-widget')]";
    private static final String dashboardChartLegends = "//ul[@class ='oxd-chart-legend']//li";
    private static final String dashboardPieCharts = "div.emp-distrib-chart canvas";
    private static final String dashboardPieChartTooltipCount = "span.oxd-pie-chart-tooltip b";
    private static final String dashboardPieChartTooltipName = "span.oxd-pie-chart-tooltip span:nth-child(2)";
    private static final String dashboardToDoItems = "div.orangehrm-todo-list p";
    private static final String dashboardToDoEmployeesOnLeave = "//*[contains(@class , 'emp-leave-chart')]//div[@class = 'orangehrm-leave-card']";
    private static final String dashboardToDoEmployeesLeaveDetails = "//p[contains(@class,'orangehrm-leave-card')]";
    private static final String dashboardToDoEmployeesPosts = "//div[contains(@class,'orangehrm-buzz-widget-card')]";
    private static final String dashboardToDoEmployeesPostDetails = "//p[not(contains(@class,'time'))]";

    public DashboardPage(Page page) {
        this.page = page;
    }

    public boolean allWidgetsVisible(String widgetName) {
        System.out.println(page.locator(dashboardwidgets).locator("//p[text() = '" + widgetName + "']").textContent());
        return page.locator(dashboardwidgets).locator("//p[text() = '" + widgetName + "']").isVisible();
    }

    public void clickQuickLaunchOptions(String option) {
        page.locator(dashboardwidgets).locator("//button[contains(@title,'" + option + "')]").click();
    }

    public void unselectAllLegends() {
        int totalCount = page.locator(dashboardChartLegends).count();
        for (int i = 0; i < totalCount; i++) {
            page.locator(dashboardChartLegends).nth(i).click();
        }
    }

    public void selectLegends(int i) {
        page.locator(dashboardChartLegends).nth(i).click();
    }

    public String readSubunitChartTooltip() {
        page.locator(dashboardPieCharts).first().hover();
        return page.locator(dashboardPieChartTooltipCount).textContent();
    }

    public List<String> getToDoList() {
        List<String> toDoList = new ArrayList<>();
        page.waitForSelector(dashboardToDoItems);
        Locator items = page.locator(dashboardToDoItems);
        for (int i = 0; i < items.count(); i++) {
            toDoList.add(items.nth(i).textContent()
                    .replace("Candidate","Candidates")
                    .replace("(","")
                    .replace(")","")
                    .toLowerCase()
            );
        }
        return toDoList;
    }

    public List<String> getEmployeesOnLeave() {
        List<String> employeesOnLeave = new ArrayList<>();
        page.waitForSelector(dashboardToDoEmployeesOnLeave);
        Locator employees = page.locator(dashboardToDoEmployeesOnLeave);
        for (int i = 0; i < employees.count(); i++) {
            employeesOnLeave.add(i,"");
            Locator employeeDetails = page.locator(dashboardToDoEmployeesLeaveDetails);
            for (int j = 0; j < employeeDetails.count(); j++) {
                employeesOnLeave.set(
                i, employeesOnLeave.get(i)+ " " + employees.nth(i).locator(dashboardToDoEmployeesLeaveDetails).nth(j).textContent()
                );
                employeesOnLeave.set(i, employeesOnLeave.get(i).trim());
            }
        }
        return employeesOnLeave;
    }

    public List<String> getEmployeesPosts() {
        List<String> employeesPosts = new ArrayList<>();
        page.waitForSelector(dashboardToDoEmployeesPosts);
        Locator posts = page.locator(dashboardToDoEmployeesPosts);
        for (int i = 0; i < posts.count(); i++) {
            employeesPosts.add(i,"");
            Locator postDetails = posts.nth(i).locator(dashboardToDoEmployeesPostDetails);
            for (int j = 0; j < postDetails.count(); j++) {
                employeesPosts.set(
                i, employeesPosts.get(i)+ " " + posts.nth(i).locator(dashboardToDoEmployeesPostDetails).nth(j).textContent()
                );
                employeesPosts.set(i, employeesPosts.get(i).trim());
            }
        }
        return employeesPosts;
    }
}
