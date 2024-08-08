package com.qa.orangeHRM.config;

public class Constants {
    // >>>>> Paths
    public static final String SCREENSHOTS_PATH = "screenshots/";
    public static final String REPORTS_PATH = "reports/";

    // >>>>> Links
    public static final String LOGIN_LINK = "web/index.php/auth/login";
    public static final String DASHBOARD_LINK = "web/index.php/dashboard/index";
    public static final String PIM_LINK = "web/index.php/pim/viewEmployeeList";

    // >>>>> Page Titles
    public static final String ADMIN_TITLE = "Admin";
    public static final String DASHBOARD_TITLE = "Dashboard";

    // >>>>> Api end points
    public static final String DASHBOARD_SUBUNIT_API = "dashboard/employees/subunit";
    public static final String DASHBOARD_LOCATIONS_API = "dashboard/employees/locations";
    public static final String DASHBOARD_ACTIONS_API = "dashboard/employees/action-summary";
    public static final String DASHBOARD_LEAVES_API = "dashboard/employees/leaves";
    public static final String DASHBOARD_BUZZ_API = "buzz/feed?limit=5&offset=0&sortOrder=DESC&sortField=share.createdAtUtc";
    public static final String DASHBOARD_TIME_API = "dashboard/employees/time-at-work";

    // >>>>> Random Credentials
    public static final String[] FIRST_NAMES = {"John", "Jane", "Alex", "Emily", "Michael", "Sarah", "David", "Laura", "James", "Anna"};
    public static final String[] MIDDLE_NAMES = {"Ali", "Johnny", "Williams", "Jones", "White", "Davis", "Anderson", "Wilson", "Moore", "Taylor"};
    public static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor"};

}