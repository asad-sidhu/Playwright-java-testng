package com.qa.orangeHRM.miscFunctions;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Sorting {
    private static Page page;
    private static final String sortingIcon = ".oxd-table-header-sort-icon";
    private static final String ascendingSorting = "span:has-text('Ascending')";
    private static final String descendingSorting = "span:has-text('Descending')";
    private static final String tableHeaders = ".oxd-table-header-cell";
    private static final String tableBody = "div.oxd-table-body";
    private static final String tableRows = "div.oxd-table-row";
    private static final String tableCells = "div.oxd-table-cell";
    private static final String sortableColumnHeaders = "//i[contains(@class ,'oxd-table-header-sort-icon')]/../..";

    public Sorting(Page page) {
        this.page = page;
    }


//    public void verifySorting(int skipColumns) throws InterruptedException {
//        SoftAssert softAssert = new SoftAssert();
//        Thread.sleep(5000);
//        int columnsCount = page.locator(tableHeaders).count();
//        String columnName = null;
//
//        for (int i = 0; i < columnsCount; i++) {
//            try {
//                List<String> ascList = new ArrayList<>();
//                List<String> descList = new ArrayList<>();
//                if (page.locator(tableBody).nth(i).locator(sortingIcon).isVisible()) {
//                columnName = page.locator(sortableColumnHeaders).nth(i).textContent().split("Asc")[0];
//                    page.locator(sortingIcon).nth(i).click();
//                page.locator(ascendingSorting).nth(i).click();
//                page.locator(tableBody).waitFor();
//                int totalRecords = page.locator(tableBody).locator(tableRows).count();
//                for (int j = 0; j < totalRecords; j++) {
//                    ascList.add(page.locator(tableBody).locator(tableRows).nth(j).locator(tableCells).nth(i + 1).textContent());
//                }
////                System.out.println("Actual result :" + ascList);
//                List<String> sortedAscList = Sorting.alpabeticalSorting(ascList, "ASC", columnName);
////                System.out.println("Expected result :" + sortedAscList);
//                softAssert.assertEquals(ascList, sortedAscList, "Ascending order sorting not working correctly on " + columnName);
//                page.locator(sortingIcon).nth(i).click();
//                page.locator(descendingSorting).nth(i).click();
//                page.locator(tableBody).waitFor();
//                for (int j = 0; j < totalRecords; j++) {
//                    descList.add(page.locator(tableBody).locator(tableRows).nth(j).locator(tableCells).nth(i + 1).textContent());
//                }
////                System.out.println("Actual result :" + descList);
//                List<String> sortedDescList = Sorting.alpabeticalSorting(descList, "DESC", columnName);
////                System.out.println("Expected result :" + sortedDescList);
//                softAssert.assertEquals(descList, sortedDescList, "Descending order sorting not working correctly on " + columnName);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new RuntimeException(e);
//            } catch (AssertionError e) {
//                // Log the assertion error but continue with the next iteration
//                System.err.println("Assertion failed for column: " + columnName);
//                e.printStackTrace();
//            }
//        }
//
//        // Make sure all assertions are checked
//        softAssert.assertAll();
//    }
public void verifySorting(int skipColumns) throws InterruptedException {
    SoftAssert softAssert = new SoftAssert();
    Thread.sleep(5000); // Consider using WebDriverWait instead of Thread.sleep

    int columnsCount = page.locator(tableHeaders).count(); // Count of all columns
    for (int i = skipColumns; i < columnsCount; i++) {
        try {
            List<String> ascList = new ArrayList<>();
            List<String> descList = new ArrayList<>();

            // Check if the sorting icon is visible for the current column
            if (page.locator(tableHeaders).nth(i).locator(sortingIcon).isVisible()) {
                String columnName = page.locator(tableHeaders).nth(i).textContent().split("Asc")[0];

                // Click to sort ascending
                page.locator(tableHeaders).nth(i).locator(sortingIcon).click();
                page.locator(tableHeaders).nth(i).locator(ascendingSorting).click();
                page.locator(tableBody).waitFor(); // Consider waiting for specific elements instead

                // Get sorted data
                int totalRecords = page.locator(tableBody).locator(tableRows).count();
                for (int j = 0; j < totalRecords; j++) {
                    ascList.add(page.locator(tableBody).locator(tableRows).nth(j).locator(tableCells).nth(i).textContent());
                }

                // Check sorting
                List<String> sortedAscList = Sorting.alpabeticalSorting(ascList, "ASC", columnName);
                softAssert.assertEquals(ascList, sortedAscList, "Ascending order sorting not working correctly on " + columnName);

                // Click to sort descending
                page.locator(tableHeaders).nth(i).locator(sortingIcon).click();
                page.locator(tableHeaders).nth(i).locator(descendingSorting).click();
                page.locator(tableBody).waitFor(); // Consider waiting for specific elements instead

                // Get sorted data
                for (int j = 0; j < totalRecords; j++) {
                    descList.add(page.locator(tableBody).locator(tableRows).nth(j).locator(tableCells).nth(i).textContent());
                }

                // Check sorting
                List<String> sortedDescList = Sorting.alpabeticalSorting(descList, "DESC", columnName);
                softAssert.assertEquals(descList, sortedDescList, "Descending order sorting not working correctly on " + columnName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (AssertionError e) {
            // Log the assertion error but continue with the next iteration
            System.err.println("Assertion failed for column: " + page.locator(sortableColumnHeaders).nth(i).textContent());
            e.printStackTrace();
        }
    }

    // Make sure all assertions are checked
    softAssert.assertAll();
}



    public static List<String> alpabeticalSorting(List<String> list, String order, String columnName){
        List<String> sortedList = new ArrayList<>(list);

        switch (order){
            case "ASC":
                sortedList = sortedList.stream().sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
                break;
            case "DESC":
                sortedList = sortedList.stream().sorted(String.CASE_INSENSITIVE_ORDER.reversed()).collect(Collectors.toList());
                break;
            default:
                System.out.println("Invalid Option");
                break;
        }
        return sortedList;
    }


    public static void numericalSorting(List<Integer> list, String order, String columnName){
        List<Integer> sortedList = new ArrayList<>(list);

        switch (order){
            case "ASC":
                Collections.sort(sortedList);
                break;
            case "DESC":
                Collections.sort(sortedList, Collections.reverseOrder());
                break;
            default:
                System.out.println("Invalid Option");
                break;
        }

    }

    public static String normalizeString(String input) {
        if (input == null) {
            return null;
        }
        // Replace all \r\n with \n
        input = input.replace("\r\n", "\n");
        // Replace all \r with \n
        input = input.replace("\r", "\n");
        // Trim leading and trailing whitespace
        input = input.trim();
        // Replace multiple spaces with a single space
        input = input.replaceAll("\\s+", " ");
        return input;
    }
}
