package com.qa.orangeHRM.listeners;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;

import com.aventstack.extentreports.reporter.configuration.Theme;
import com.qa.orangeHRM.config.BaseTest;
import com.qa.orangeHRM.config.Constants;
import com.qa.orangeHRM.utils.DateUtil;
import org.testng.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.qa.orangeHRM.utils.ScreenshotUtil;
import com.microsoft.playwright.Page;

public class ExtentReportListener implements ITestListener, IClassListener {

    private static ThreadLocal<ExtentReports> extentReports = new ThreadLocal<>();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ThreadLocal<String> reportName = new ThreadLocal<>();

    private static ExtentReports init(String reportName) {
        Path path = Paths.get(Constants.REPORTS_PATH);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ExtentReports extentReports = new ExtentReports();
        ExtentSparkReporter reporter = new ExtentSparkReporter(Constants.REPORTS_PATH + reportName);
        reporter.config().setReportName("OrangeHRM Automation Test Results");

        // Set the theme to DARK or STANDARD
        reporter.config().setTheme(Theme.DARK);  // Use Theme.STANDARD for light theme

        extentReports.attachReporter(reporter);
        extentReports.setSystemInfo("System", "MAC");
        extentReports.setSystemInfo("Author", "Asad Sidhu");
        extentReports.setSystemInfo("Build#", "1.1");
        extentReports.setSystemInfo("Team", "");
        extentReports.setSystemInfo("Customer Name", "OHRM");

        return extentReports;
    }

    @Override
    public synchronized void onStart(ITestContext context) {
        System.out.println("Test Suite started!");
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        System.out.println("Test Suite is ending!");
    }

    @Override
    public synchronized void onBeforeClass(ITestClass testClass) {
        System.out.println("Test Class started: " + testClass.getName());
        String className = testClass.getName();
        String dateTime = DateUtil.getCurrentTime();
//        String generatedReportName = className.split("test.")[1] + "_" + dateTime + ".html";
        String generatedReportName = "className.html";
        extentReports.set(init(generatedReportName));
        reportName.set(generatedReportName);
    }

    @Override
    public synchronized void onAfterClass(ITestClass testClass) {
        System.out.println("Test Class ended: " + testClass.getName());
        if (extentReports.get() != null) {
            extentReports.get().flush();
        }
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        String description = result.getMethod().getDescription();
        ExtentTest extentTest = extentReports.get().createTest(description != null ? description : result.getMethod().getMethodName());
        extentTest.assignCategory(result.getTestContext().getSuite().getName());
        test.set(extentTest);
        test.get().getModel().setStartTime(getTime(result.getStartMillis()));
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        test.get().pass(result.getMethod().getMethodName() + " Test passed");
        Page page = ((BaseTest) result.getInstance()).getPage();
//        String screenshotPath = ScreenshotUtil.takeScreenshot(page);
//        String relativeScreenshotPath = Paths.get(Constants.SCREENSHOTS_PATH, new File(screenshotPath).getName()).toString();
        // test.get().pass("Screenshot", MediaEntityBuilder.createScreenCaptureFromPath(relativeScreenshotPath).build());
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() + " failed!");
        Page page = ((BaseTest) result.getInstance()).getPage();
        String screenshotPath = ScreenshotUtil.takeScreenshot(page);
        String relativeScreenshotPath = Paths.get(Constants.SCREENSHOTS_PATH, new File(screenshotPath).getName()).toString();
        test.get().fail(result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromPath(relativeScreenshotPath).build());
        test.get().fail(result.getMethod().getMethodName() + " Test failed");
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        System.out.println(result.getMethod().getMethodName() + " skipped!");
        Page page = ((BaseTest) result.getInstance()).getPage();
        String screenshotPath = ScreenshotUtil.takeScreenshot(page);
        String relativeScreenshotPath = Paths.get(Constants.SCREENSHOTS_PATH, new File(screenshotPath).getName()).toString();
        test.get().skip(result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromPath(relativeScreenshotPath).build());
        test.get().fail(result.getMethod().getMethodName() + " Test skipped");
        test.get().getModel().setEndTime(getTime(result.getEndMillis()));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName());
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}
