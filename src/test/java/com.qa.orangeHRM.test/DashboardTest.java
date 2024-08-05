package com.qa.orangeHRM.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.BrowserContext;
import com.qa.orangeHRM.api.API_Requests;
import com.qa.orangeHRM.config.BaseTest;
import com.qa.orangeHRM.config.Constants;
import com.qa.orangeHRM.pages.BasePage;
import com.qa.orangeHRM.pages.DashboardPage;
import com.qa.orangeHRM.pages.LoginPage;
import com.qa.orangeHRM.utils.DateUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;

public class DashboardTest extends BaseTest {
    DashboardPage dashboardPage;
    SoftAssert softAssert = new SoftAssert();
    API_Requests api_requests = new API_Requests(page);


    @BeforeClass
    public void navigate() {
        dashboardPage = new DashboardPage(getPage());
        basePage = new BasePage(getPage());
//        basePage.navBarNavigations("Dashboard");
    }

    @Test(priority = 4, description = "Verify that correct title shows on the Dashboard page.")
    public void VerifyTitle() {
        basePage = new BasePage(page);
        Assert.assertNotEquals(basePage.getPageTitle(), Constants.DASHBOARD_TITLE, "Wrong Title showing on the page.");
    }

    @Test(priority = 5, description = "Verify that all the widgets are visible on the Dashboard page.")
    public void VerifyWidgetsVisible() {
        String[] widgetNames = {"Time at Work", "My Actions", "Quick Launch", "Buzz Latest Posts", "Employees on Leave Today", "Employee Distribution by Sub Unit", "Employee Distribution by Location"};

        for (String widgetName : widgetNames) {
            boolean isWidgetVisible = dashboardPage.allWidgetsVisible(widgetName);
            Assert.assertTrue(isWidgetVisible, "Widget '" + widgetName + "' is not visible on the Dashboard page.");
        }
    }

    @Test(priority = 5, description = "Verify that the quick launch items redirect to the correct pages.")
    public void VerifyQuickLaunch() {
        basePage = new BasePage(page);
        String[] options = {"Assign Leave",
//                "Leave List",
//                "Timesheets",
//                "Apply",
//                "My Leave",
//                " Timesheet",
        };

        for (String option : options) {
            dashboardPage.clickQuickLaunchOptions(option);
            softAssert.assertFalse(basePage.istopBarNavigationSelected(option));
            page.goBack();
        }
        softAssert.assertAll();
    }

    @Test(priority = 6, enabled = false, description = "Verify that the employee distribution by subunit chart is displayed correctly.")
    public void EmployeeDistributionSubunit() {
        try {
            String jsonObject = api_requests.get_request(props.getProperty("api_url") + Constants.DASHBOARD_SUBUNIT_API);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonObject);
            JsonNode dataArrayNode = rootNode.path("data");
            Map<String,String> apiData = new HashMap<>();
            Map<String,String> actualData = new HashMap<>();
            for (int i = 0; i < dataArrayNode.size() ; i++) {
                apiData.put(dataArrayNode.get(i).path("subunit").path("name").toString(),dataArrayNode.get(i).path("count").toString());
            }
            dashboardPage.unselectAllLegends();
            for (int i = 0; i < dataArrayNode.size() ; i++) {
                String key = new ArrayList<>(apiData.keySet()).get(i);
                dashboardPage.selectLegends(i);
                actualData.put(key,dashboardPage.readSubunitChartTooltip());
                dashboardPage.selectLegends(i);
            }
            System.out.println(actualData);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test(priority = 7, description = "Verify that the My Actions widget displays the correct tasks as per the API response.")
    public void VerifyMyActions() {
        try {
            String jsonObject = api_requests.get_request(props.getProperty("api_url") + Constants.DASHBOARD_ACTIONS_API);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonObject);
            JsonNode dataArrayNode = rootNode.path("data");

            List<String> apiData = new ArrayList<>();
            for (int i = 0; i < dataArrayNode.size(); i++) {
                apiData.add(dataArrayNode.get(i).path("pendingActionCount").toString() + " " +
                        dataArrayNode.get(i).path("group").toString().replace("\"", "").toLowerCase());
            }
            List<String> actualData = dashboardPage.getToDoList();
            Assert.assertEquals(actualData, apiData, "Data does not match");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (AssertionError e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 8, description = "Verify that Employees on Leave Today are displayed on the dashboard as per the API response.")
    public void EmployeeLeaves() {
        try {
            Map<String,String> params = new HashMap<>();
            params.put("date", DateUtil.getCurrentDateYYYYMMDD());
            String jsonObject = api_requests.get_request(props.getProperty("api_url") + Constants.DASHBOARD_LEAVES_API,params);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonObject);
            JsonNode dataArrayNode = rootNode.path("data");
            List<String> apiData = new ArrayList<>();
            for (int i = 0; i < dataArrayNode.size(); i++) {
                JsonNode employeeNode = dataArrayNode.get(i).path("employee");
                JsonNode leaveTypeNode = dataArrayNode.get(i).path("leaveType");
                apiData.add(
                        employeeNode.path("firstName").toString().replace("\"","") + " " +
                        employeeNode.path("lastName").toString().replace("\"","").toLowerCase() + " " +
                                leaveTypeNode.path("name").toString().replace("\"","") + " " +
                        employeeNode.path("employeeId").toString().replace("\"","")
                );
                apiData.get(i).toLowerCase();
            }
            List<String> actualData = dashboardPage.getEmployeesOnLeave();
            Assert.assertEquals(actualData, apiData, "Data does not match");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (AssertionError e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 8, description = "Verify that Employees on Leave Today are displayed on the dashboard as per the API response.")
    public void BuzzLatestPosts() {
        try {
            String jsonObject = api_requests.get_request(props.getProperty("api_url") + Constants.DASHBOARD_BUZZ_API);
            System.out.println(jsonObject);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonObject);
            JsonNode dataArrayNode = rootNode.path("data");
            List<String> apiData = new ArrayList<>();
            for (int i = 0; i < dataArrayNode.size(); i++) {
                JsonNode employeeNode = dataArrayNode.get(i).path("employee");
                apiData.add(
                        employeeNode.path("firstName").toString().replace("\"","").toLowerCase() + " " +
                        employeeNode.path("middleName").toString().replace("\"","").toLowerCase() + " " +
                        employeeNode.path("lastName").toString().replace("\"","").toLowerCase() + " " +
                                dataArrayNode.get(i).path("text").toString().replace("\"","")
                );
                apiData.set(i, apiData.get(i).toLowerCase().replaceAll("\r\n","\n"));
                apiData.set(i, apiData.get(i).toLowerCase().replaceAll("\r","\n"));
            }
            List<String> actualData = dashboardPage.getEmployeesPosts();
            System.out.println(apiData);
            System.out.println(actualData);
            Assert.assertEquals(actualData, apiData, "Data does not match");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (AssertionError e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
