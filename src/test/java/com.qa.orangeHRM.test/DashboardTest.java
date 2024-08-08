package com.qa.orangeHRM.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.orangeHRM.api.API_Requests;
import com.qa.orangeHRM.config.BaseTest;
import com.qa.orangeHRM.config.Constants;
import com.qa.orangeHRM.pages.BasePage;
import com.qa.orangeHRM.pages.DashboardPage;
import com.qa.orangeHRM.pages.LoginPage;
import com.qa.orangeHRM.utils.DateUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardTest extends BaseTest {
    DashboardPage dashboardPage;
    LoginPage loginPage;
    SoftAssert softAssert = new SoftAssert();
    API_Requests api_requests = new API_Requests(page);


    @BeforeClass
    public void Login() {
        dashboardPage = new DashboardPage(page);
        basePage = new BasePage(page);
        loginPage = new LoginPage(page);
        if (page.url().contains(Constants.LOGIN_LINK)) {
            loginPage.login(props.getProperty("username"), props.getProperty("password"));
        }
//        basePage.navBarNavigations("Dashboard");
    }

    @BeforeMethod
    public void Navigate() {
        basePage = new BasePage(page);
        basePage.navBarNavigations("Dashboard");
    }

    @Test(priority = 4, description = "Verify that correct title shows on the Dashboard page.")
    public void VerifyTitle() {
        basePage = new BasePage(page);
        Assert.assertEquals(basePage.getPageTitle(), Constants.DASHBOARD_TITLE, "Wrong Title showing on the page.");
    }

    @Test(priority = 5, description = "Verify that all the widgets are visible on the Dashboard page.")
    public void VerifyWidgetsVisible() {
        try {
            String[] widgetNames = {"Time at Work", "My Actions", "Quick Launch", "Buzz Latest Posts", "Employees on Leave Today", "Employee Distribution by Sub Unit", "Employee Distribution by Location"};

            for (String widgetName : widgetNames) {
                boolean isWidgetVisible = dashboardPage.allWidgetsVisible(widgetName);
                Assert.assertTrue(isWidgetVisible, "Widget '" + widgetName + "' is not visible on the Dashboard page.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (AssertionError e) {
            e.printStackTrace();
            throw new AssertionError(e);
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
            softAssert.assertTrue(basePage.istopBarNavigationSelected(option));
            page.goBack();
        }
        softAssert.assertAll();
    }

    @Test(priority = 6, enabled = false, description = "Verify that the employee distribution by subunit chart is displayed correctly.")
    public void EmployeeDistributionSubunit() {
        try {
            Map<String, String>  jsonObject = api_requests.get_request(props.getProperty("api_url") + Constants.DASHBOARD_SUBUNIT_API);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonObject.get("body"));
            JsonNode dataArrayNode = rootNode.path("data");
            Map<String, String> apiData = new HashMap<>();
            Map<String, String> actualData = new HashMap<>();
            for (int i = 0; i < dataArrayNode.size(); i++) {
                apiData.put(dataArrayNode.get(i).path("subunit").path("name").toString(), dataArrayNode.get(i).path("count").toString());
            }
            dashboardPage.unselectAllLegends();
            for (int i = 0; i < dataArrayNode.size(); i++) {
                String key = new ArrayList<>(apiData.keySet()).get(i);
                dashboardPage.selectLegends(i);
                actualData.put(key, dashboardPage.readSubunitChartTooltip());
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
            Map<String, String>  jsonObject = api_requests.get_request(props.getProperty("api_url") + Constants.DASHBOARD_ACTIONS_API);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonObject.get("body"));
            JsonNode dataArrayNode = rootNode.path("data");

            List<String> apiData = new ArrayList<>();
            if (dataArrayNode.size() != 0) {

                System.out.println(rootNode);
            for (int i = 0; i < dataArrayNode.size(); i++) {
                apiData.add(dataArrayNode.get(i).path("pendingActionCount").toString() + " " +
                        dataArrayNode.get(i).path("group").toString().replace("\"", "").toLowerCase());
            }
            List<String> actualData = dashboardPage.getToDoList();
            Assert.assertEquals(actualData, apiData, "Data does not match");
            } else {
                Assert.assertTrue(dashboardPage.verifyNoActionsMessage(), "No Actions Message is not shown");
            }
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
            Map<String, String> params = new HashMap<>();
            params.put("date", DateUtil.getCurrentDateYYYYMMDD());
            Map<String, String> jsonObject = api_requests.get_request(props.getProperty("api_url") + Constants.DASHBOARD_LEAVES_API, params);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonObject.get("body"));
            JsonNode dataArrayNode = rootNode.path("data");
            List<String> apiData = new ArrayList<>();
            if (dataArrayNode.size() != 0) {
                System.out.println(dataArrayNode);
                for (int i = 0; i < dataArrayNode.size(); i++) {
                    JsonNode employeeNode = dataArrayNode.get(i).path("employee");
                    JsonNode leaveTypeNode = dataArrayNode.get(i).path("leaveType");
                    apiData.add(
                            employeeNode.path("firstName").toString().replace("\"", "") + " " +
                                    employeeNode.path("lastName").toString().replace("\"", "").toLowerCase() + " " +
                                    leaveTypeNode.path("name").toString().replace("\"", "") + " " +
                                    employeeNode.path("employeeId").toString().replace("\"", "")
                    );
                    apiData.set(i, apiData.get(i).toLowerCase());
                }
                List<String> actualData = dashboardPage.getEmployeesOnLeave();
                Assert.assertEquals(actualData, apiData, "Data does not match");
            } else {
                Assert.assertTrue(dashboardPage.verifyNoLeavesMessage(), "No Leaves Message is not shown");
            }

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

    @Test(priority = 9, description = "Verify that Employees on Leave Today are displayed on the dashboard as per the API response.")
    public void BuzzLatestPosts() {
        try {
            // Fetching the API response and parsing the JSON data
            SoftAssert softAssert = new SoftAssert();
            Map<String, String> jsonObject = api_requests.get_request(props.getProperty("api_url") + Constants.DASHBOARD_BUZZ_API);
            ObjectMapper objectMapper = new ObjectMapper();
            String responseCode = objectMapper.readTree(jsonObject.get("code")).toString();
            if (!responseCode.equals("403")) {
                System.out.println(responseCode);
                JsonNode rootNode = objectMapper.readTree(jsonObject.get("body"));
                JsonNode dataArrayNode = rootNode.path("data");

                List<String> apiData = new ArrayList<>();
                for (JsonNode node : dataArrayNode) {
                    JsonNode employeeNode = node.path("employee");
                    String employeeDetails = String.join(" ",
                            employeeNode.path("firstName").asText().toLowerCase(),
                            employeeNode.path("middleName").asText().toLowerCase(),
                            employeeNode.path("lastName").asText().toLowerCase(),
                            node.path("text").asText().toLowerCase()
                    ).replaceAll("\r\n", "").replaceAll("\r", "");
                    apiData.add(employeeDetails.lines().collect(Collectors.joining("\n")));
                }

                // Fetching the actual data from the dashboard
                List<String> actualData = dashboardPage.getEmployeesPosts();

                // Comparing the actual data with the expected data from the API
                for (int i = 0; i < actualData.size(); i++) {
                    String[] actualWords = actualData.get(i).split(" ");
                    String[] expectedWords = apiData.get(i).split(" ");
                    for (int j = 0; j < actualWords.length; j++) {
                        softAssert.assertTrue(expectedWords[j].contains(actualWords[j]),
                                "Mismatch at index " + i + ", word position " + j + ": expected '" + expectedWords[j] + "' but found '" + actualWords[j] + "'");
                    }
                }
            }
            softAssert.assertAll();

        } catch (InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        } catch (AssertionError e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 10, description = "Verify that only employees who have access are shown Buzz section.")
    public void BuzzSectionRights() {

        try {
            Map<String, String> jsonObject = api_requests.get_request(props.getProperty("api_url") + Constants.DASHBOARD_BUZZ_API);
            ObjectMapper objectMapper = new ObjectMapper();
            String responseCode = objectMapper.readTree(jsonObject.get("code")).toString();
            if (responseCode.equals("403")) {
                Assert.assertFalse(dashboardPage.buzzWidgetRights(),"Buzz Section should not show to the user");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (AssertionError e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }

    @Test(priority = 11, description = "Verify that Time At Work Widget is showing correct data as per the API response.")
    public void TimeAtWorkSection() {

        try {
            Map<String, String> params = new HashMap<>();
            params.put("currentDate", DateUtil.getCurrentDateYYYYMMDD());
            params.put("timezoneOffset", "5");
            params.put("currentTime", "23:15");
            Map<String, String> jsonObject = api_requests.get_request(props.getProperty("api_url") + Constants.DASHBOARD_TIME_API, params);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responsebody = objectMapper.readTree(jsonObject.get("body"));
            JsonNode metaNode = responsebody.path("meta");
            JsonNode lastActionNode = metaNode.path("lastAction");
            JsonNode currentDayNode = metaNode.path("currentDay");
            JsonNode currentWeekNode = metaNode.path("currentWeek");
            List<String> apiData = new ArrayList<>();
            apiData.add(lastActionNode.path("state").toString().replace("\"",""));
            apiData.add(DateUtil.convertTo12HourFormat(lastActionNode.path("userTime").toString().replace("\"","")));
            apiData.add(currentDayNode.path("totalTime").path("hours").toString()+"h " + currentDayNode.path("totalTime").path("minutes").toString()+"m");
            apiData.add(currentWeekNode.path("totalTime").path("hours").toString()+"h " + currentWeekNode.path("totalTime").path("minutes").toString()+"m");
            System.out.println(apiData);
            List<String> actualData = dashboardPage.getTimeAtWork();
            Assert.assertEquals(actualData,apiData, "Data Does not match");
            System.out.println(actualData);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (AssertionError e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }

}
