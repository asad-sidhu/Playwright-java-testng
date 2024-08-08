package com.qa.orangeHRM.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.qa.orangeHRM.config.Constants;
import com.qa.orangeHRM.miscFunctions.Sorting;
import com.qa.orangeHRM.utils.DateUtil;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PIMPage {
    private Page page;
    SoftAssert softAssert;
    private static String firstName;
    private static String middleName;
    private static String lastName;
    private static String employeeID;
    Random rand = new Random();
    BasePage basePage = new BasePage(page);

    private static final String employeeListTab = "a:has-text('Employee List')";
    private static final String addEmployeeButton = "button:has-text(' Add ')";
    private static final String addEmployeeFirstNameField = "[name='firstName']";
    private static final String addEmployeeMiddleNameField = "[name='middleName']";
    private static final String addEmployeeLastNameField = "[name='lastName']";
    private static final String addEmployeeEmployeeIDField = "input:below(:has-text(\"Employee Id\"))";
    private static final String uploadPhotoButton = "input[type='file']";
    private static final String saveButton = "button:has-text(' Save ')";
    private static final String cancelButton = "button:has-text(' Cancel ')";
    private static final String successSaveAlert = "p:has-text(' Successfully Saved ')";
    private static final String editEmployeeForm = "div.orangehrm-edit-employee";
    private static final String editEmployeePersonalDetailsTab = "a:has-text('Personal Details')";
    private static final String editEmployeeContactDetailsTab = "a:has-text('Contact Details')";
    private static final String editEmployeeEmergencyContactsTab = "a:has-text('Emergency Contacts')";
    private static final String editEmployeeDependentsTab = "a:has-text('Dependents')";
    private static final String editEmployeeImmigrationTab = "a:has-text('Immigration')";
    private static final String editEmployeeJobTab = "a:has-text('Job')";
    private static final String editEmployeeSalaryTab = "a:has-text('Salary')";
    private static final String editEmployeeReportToTab = "a:has-text('Report-to')";
    private static final String editEmployeeQualificationsTab = "a:has-text('Qualifications')";
    private static final String editEmployeeMembershipsTab = "a:has-text('Memberships')";
    private static final String otherIDField = "input:below(:has-text(\"Other Id\"))";
    private static final String licenseNumberField = "input:below(:has-text(\"License Number\"))";
    private static final String licenseExpiryField = "input:below(:has-text(\"License Expiry Date\"))";
    private static final String DOBField = "input:below(:has-text(\"Date of Birth\"))";
    private static final String nationalitySelect = "div.oxd-select-text--active:below(:has-text(\"Nationality\"))";
    private static final String genderRadioButton = "label:text(\"Male\")";
    private static final String successUpdateAlert = "p:has-text(' Successfully Updated ')";
    private static final String successDeleteAlert = "p:has-text(' Successfully Deleted ')";
    private static final String searchEmployeeNameField = "input:below(:has-text(\"Employee Name\"))";
    private static final String searchButton = "button:has-text('Search')";
    private static final String employeeTable = "div.oxd-table-body";
    private static final String employeeTableRows = "div.oxd-table-row";
    private static final String employeeTableCells = "div.oxd-table-cell";
    private static final String employeeTableHeaders = ".oxd-table-header-cell";
    private static final String contactDetailsStreet1 = "input:below(:has-text(\"Street 1\"))";
    private static final String contactDetailsStreet2 = "input:below(:has-text(\"Street 2\"))";
    private static final String contactDetailsCity = "input:below(:has-text(\"City\"))";
    private static final String contactDetailsState = "input:below(:has-text(\"State/Province\"))";
    private static final String contactDetailsZip = "input:below(:has-text(\"Zip/Postal Code\"))";
    private static final String contactDetailsTelephoneHome = "input:below(:has-text(\"Home\"))";
    private static final String contactDetailsTelephoneMobile = "input:below(:has-text(\"Mobile\"))";
    private static final String contactDetailsTelephoneWork = "input:below(:has-text(\"Work\"))";
    private static final String contactDetailsWorkEmail = "input:below(:has-text(\"Work Email\"))";
    private static final String contactDetailsOtherEmail = "input:below(:has-text(\"Other Email\"))";
    private static final String emergencyContactsName = "input:below(:has-text(\"Name\"))";
    private static final String emergencyContactsRelation = "input:below(:has-text(\"Relationship\"))";
    private static final String emergencyContactsTelephoneHome = "input:below(:has-text(\"Home Telephone\"))";
    private static final String emergencyContactsTelephoneMobile = "input:below(:has-text(\"Mobile\"))";
    private static final String emergencyContactsTelephoneWork = "input:below(:has-text(\"Work Telephone\"))";
    private static final String emergencyContactsRecords = ".orangehrm-edit-employee-content span.oxd-text";
    private static final String recordsFound = "span:has-text('Records Found')";
    private static final String editButton = "i.bi-pencil-fill";
    private static final String deleteButton = "i.bi-trash";
    private static final String confirmDeleteButton = ":has-text(\"Yes, Delete\")";
    private static final String confirmationPopup = "//*[contains(@class,'orangehrm-dialog-popup')]";
    private static final String sortingIcon = ".oxd-table-header-sort-icon";
    private static final String ascendingSorting = "span:has-text('Ascending')";
    private static final String descendingSorting = "span:has-text('Descending')";

    public PIMPage(Page page) {
        this.page = page;
    }

    public void addEmployee() {
        try {
            softAssert = new SoftAssert();
            firstName = Constants.FIRST_NAMES[rand.nextInt(Constants.FIRST_NAMES.length)];
            middleName = Constants.MIDDLE_NAMES[rand.nextInt(Constants.MIDDLE_NAMES.length)];
            lastName = Constants.LAST_NAMES[rand.nextInt(Constants.LAST_NAMES.length)];
            employeeID = String.valueOf(100000 + rand.nextInt(900000));

            page.click(addEmployeeButton);
            page.fill(addEmployeeFirstNameField, firstName);
            page.fill(addEmployeeMiddleNameField, middleName);
            page.fill(addEmployeeLastNameField, lastName);
            page.fill(addEmployeeEmployeeIDField, employeeID);
            page.locator(uploadPhotoButton).setInputFiles(Paths.get("A.png"));
            page.click(saveButton);
            page.waitForSelector(successSaveAlert);
            Assert.assertTrue(page.locator(successSaveAlert).isVisible());
            page.locator(editEmployeeForm).locator("h6").first().waitFor();
            softAssert.assertEquals(page.locator(editEmployeeForm).locator("h6").first().textContent(), firstName + " " + lastName);
            page.locator(addEmployeeLastNameField).waitFor();
            Thread.sleep(2000);
            softAssert.assertEquals(page.locator(addEmployeeFirstNameField).inputValue(), firstName);
            softAssert.assertEquals(page.locator(addEmployeeMiddleNameField).inputValue(), middleName);
            softAssert.assertEquals(page.locator(addEmployeeLastNameField).inputValue(), lastName);
            softAssert.assertAll();
            System.out.println(firstName + " " + middleName + " " + lastName);
        } catch (AssertionError e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }catch (SkipException e) {
            throw e;
        }

    }

    public void editEmployeePersonalDetails() {
        try {
            softAssert = new SoftAssert();
            searchEmployee();
            page.locator(employeeTableCells).nth(2).click();
            page.locator(otherIDField).first().waitFor();
            Thread.sleep(2000);
            page.locator(otherIDField).first().fill(String.valueOf(100 + rand.nextInt(900)));
            page.locator(licenseNumberField).first().waitFor();
            page.fill(licenseNumberField, String.valueOf(10000000 + rand.nextInt(900000000)));
            page.locator(licenseExpiryField).first().waitFor();
            page.fill(licenseExpiryField, DateUtil.getCurrentDateYYYYMMDD());
            page.fill(DOBField, DateUtil.getCurrentDateYYYYMMDD().replace("202", "199"));
            page.click(genderRadioButton);
            page.click(saveButton);
            page.waitForSelector(successUpdateAlert);
            Assert.assertTrue(page.locator(successUpdateAlert).isVisible());
            page.locator(addEmployeeLastNameField).waitFor();
            Thread.sleep(5000);
            softAssert.assertTrue(Integer.parseInt(page.locator(otherIDField).first().inputValue()) >= 100 && Integer.parseInt(page.locator(otherIDField).first().inputValue()) <= 900, "Wrong Value showing in Other Id field.");
            softAssert.assertTrue(Integer.parseInt(page.locator(licenseNumberField).first().inputValue()) >= 10000000 && Integer.parseInt(page.locator(licenseNumberField).first().inputValue()) <= 900000000, "Wrong Value showing in license Number field.");
            softAssert.assertEquals(page.locator(licenseExpiryField).first().inputValue(), DateUtil.getCurrentDateYYYYMMDD(), "Wrong Value showing in license Expiry field.");
            softAssert.assertEquals(page.locator(DOBField).first().inputValue(), DateUtil.getCurrentDateYYYYMMDD().replace("202", "199"), "Wrong Value showing in DOB field.");
            softAssert.assertAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (AssertionError e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }

    public void searchEmployee() {
        try {
            softAssert = new SoftAssert();
            page.locator(employeeListTab).click();
            page.locator(searchEmployeeNameField).first().waitFor();
            page.fill(searchEmployeeNameField, firstName + " " + middleName + " " + lastName);
            page.keyboard().press("ArrowDown");
            page.keyboard().press("Enter");
            page.click(searchButton);
            page.waitForSelector("span:has-text('(1) Record Found')");
            Assert.assertTrue(page.isVisible("span:has-text('(1) Record Found')"));
            String actualID = page.locator(employeeTable).locator(employeeTableRows).first().locator(employeeTableCells).nth(1).textContent();
            String actualFirstMiddleName = page.locator(employeeTable).locator(employeeTableRows).first().locator(employeeTableCells).nth(2).textContent();
            String actualLastName = page.locator(employeeTable).locator(employeeTableRows).first().locator(employeeTableCells).nth(3).textContent();
            Assert.assertEquals(actualFirstMiddleName, firstName + " " + middleName);
            Assert.assertEquals(actualLastName, lastName);
            Assert.assertEquals(actualID, employeeID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (AssertionError e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }

    public void editContactDetails() {
        try {
            softAssert = new SoftAssert();
            searchEmployee();
            page.locator(employeeTableCells).nth(2).click();
            page.locator(editEmployeeContactDetailsTab).first().waitFor();
            page.click(editEmployeeContactDetailsTab);
            page.locator(contactDetailsStreet1).first().waitFor();
            Thread.sleep(1000);
            page.fill(contactDetailsStreet1, "Street 1");
            page.fill(contactDetailsStreet2, "Street 2");
            page.fill(contactDetailsCity, "Lahore");
            page.fill(contactDetailsState, "Punjab");
            page.fill(contactDetailsZip, "123");
            page.fill(contactDetailsTelephoneHome, "123456789");
            page.fill(contactDetailsTelephoneMobile, "123456789");
            page.fill(contactDetailsTelephoneWork, "123456789");
            page.fill(contactDetailsWorkEmail, firstName + middleName + lastName + "@example.com");
            page.fill(contactDetailsOtherEmail, firstName + lastName + "@example.com");
            Thread.sleep(2000);
            page.click(saveButton);
            page.waitForSelector(successUpdateAlert);
            Assert.assertTrue(page.isVisible(successUpdateAlert));
            page.locator(contactDetailsStreet1).first().waitFor();
            Thread.sleep(2000);
            softAssert.assertEquals(page.locator(contactDetailsStreet1).first().inputValue(), "Street 1", "Street 1 value does not match the expected value.");
            softAssert.assertEquals(page.locator(contactDetailsStreet2).first().inputValue(), "Street 2", "Street 2 value does not match the expected value.");
            softAssert.assertEquals(page.locator(contactDetailsCity).first().inputValue(), "Lahore", "City value does not match the expected value.");
            softAssert.assertEquals(page.locator(contactDetailsState).first().inputValue(), "Punjab", "State value does not match the expected value.");
            softAssert.assertEquals(page.locator(contactDetailsZip).first().inputValue(), "123", "ZIP code value does not match the expected value.");
            softAssert.assertEquals(page.locator(contactDetailsTelephoneHome).first().inputValue(), "123456789", "Home telephone number value does not match the expected value.");
            softAssert.assertEquals(page.locator(contactDetailsTelephoneMobile).first().inputValue(), "123456789", "Mobile telephone number value does not match the expected value.");
            softAssert.assertEquals(page.locator(contactDetailsTelephoneWork).first().inputValue(), "123456789", "Work telephone number value does not match the expected value.");
            softAssert.assertEquals(page.locator(contactDetailsWorkEmail).first().inputValue(), firstName + middleName + lastName + "@example.com", "Work email value does not match the expected value.");
            softAssert.assertEquals(page.locator(contactDetailsOtherEmail).first().inputValue(), firstName + lastName + "@example.com", "Other email value does not match the expected value.");
            softAssert.assertAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (AssertionError e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }

    public void addEmergencyContacts() {
        try {
            if (!page.locator(employeeTableCells).nth(1).textContent().equals(employeeID)) {
                searchEmployee();
            }
            softAssert = new SoftAssert();
            page.locator(employeeTableCells).nth(1).click();
            page.locator(editEmployeeEmergencyContactsTab).first().waitFor();
            page.click(editEmployeeEmergencyContactsTab);
            Thread.sleep(3000);
            String recordsBefore = page.locator(emergencyContactsRecords).first().textContent();
            if (recordsBefore.equals("No Records Found")) {
                recordsBefore = "0";
            } else {
                recordsBefore = recordsBefore.split("\\)")[0].replace("(", "");
            }
//            System.out.println(recordsBefore);
            page.locator(addEmployeeButton).first().click();
            page.locator(emergencyContactsName).first().waitFor();
            Thread.sleep(1000);
            page.fill(emergencyContactsName, "Street 1");
            page.fill(emergencyContactsRelation, "Street 2");
            page.fill(emergencyContactsTelephoneHome, "123456789");
            page.fill(emergencyContactsTelephoneMobile, "123456789");
            page.fill(emergencyContactsTelephoneWork, "123456789");
            Thread.sleep(2000);
            page.click(saveButton);
            page.waitForSelector(successSaveAlert);
            Assert.assertTrue(page.isVisible(successSaveAlert));
            page.locator(emergencyContactsName).first().waitFor();

            Locator cells = page.locator(employeeTable).first().locator(employeeTableRows).first().locator(employeeTableCells);

            softAssert.assertEquals(cells.nth(1).textContent(), "Street 1", "Emergency Contact Name does not match the expected value.");
            softAssert.assertEquals(cells.nth(2).textContent(), "Street 2", "Emergency Contact Relation does not match the expected value.");
            softAssert.assertEquals(cells.nth(3).textContent(), "123456789", "Emergency Contact Telephone Home value does not match the expected value.");
            softAssert.assertEquals(cells.nth(4).textContent(), "123456789", "Emergency Contact Telephone Mobile value does not match the expected value.");
            softAssert.assertEquals(cells.nth(5).textContent(), "123456789", "Emergency Contact Telephone Work value does not match the expected value.");
            Thread.sleep(2000);
            String recordsAfter = page.locator(emergencyContactsRecords).first().textContent();
            recordsAfter = recordsAfter.split("\\)")[0].replace("(", "");
            softAssert.assertEquals(Integer.parseInt(recordsAfter.trim()), Integer.parseInt(recordsBefore.trim()) + 1);
            softAssert.assertAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (AssertionError e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }

    public void editEmergencyContacts() {
        try {
            if (!page.locator(employeeTableCells).nth(1).textContent().equals(employeeID)) {
                searchEmployee();
            }
            softAssert = new SoftAssert();
            page.locator(employeeTableCells).nth(1).click();
            page.locator(editEmployeeEmergencyContactsTab).first().waitFor();
            page.click(editEmployeeEmergencyContactsTab);
            Thread.sleep(3000);

            page.locator(employeeTable).first().locator(employeeTableRows).first().locator(editButton).click();
            page.locator(emergencyContactsName).first().waitFor();
            Thread.sleep(1000);
            page.fill(emergencyContactsName, "Street 1 Updated");
            page.fill(emergencyContactsRelation, "Street 2 Updated");
            page.fill(emergencyContactsTelephoneHome, "000000000");
            page.fill(emergencyContactsTelephoneMobile, "000000000");
            page.fill(emergencyContactsTelephoneWork, "000000000");
            Thread.sleep(2000);
            page.click(saveButton);
            page.waitForSelector(successUpdateAlert);
            Assert.assertTrue(page.isVisible(successUpdateAlert));
            page.locator(emergencyContactsName).first().waitFor();
            Thread.sleep(3000);

            Locator cells = page.locator(employeeTable).first().locator(employeeTableRows).first().locator(employeeTableCells);

            softAssert.assertEquals(cells.nth(1).textContent(), "Street 1 Updated", "Emergency Contact Name does not match the expected value.");
            softAssert.assertEquals(cells.nth(2).textContent(), "Street 2 Updated", "Emergency Contact Relation does not match the expected value.");
            softAssert.assertEquals(cells.nth(3).textContent(), "000000000", "Emergency Contact Telephone Home value does not match the expected value.");
            softAssert.assertEquals(cells.nth(4).textContent(), "000000000", "Emergency Contact Telephone Mobile value does not match the expected value.");
            softAssert.assertEquals(cells.nth(5).textContent(), "000000000", "Emergency Contact Telephone Work value does not match the expected value.");
            Thread.sleep(2000);
            softAssert.assertAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (AssertionError e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }

    public void deleteEmergencyContacts() {
        try {
            if (!page.locator(employeeTableCells).nth(1).textContent().equals(employeeID)) {
                searchEmployee();
            }
            softAssert = new SoftAssert();
            page.locator(employeeTableCells).nth(1).click();
            page.locator(editEmployeeEmergencyContactsTab).first().waitFor();
            page.click(editEmployeeEmergencyContactsTab);
            Thread.sleep(3000);
            String recordsBefore = page.locator(emergencyContactsRecords).first().textContent();
            if (recordsBefore.equals("No Records Found")) {
                throw new SkipException("No Records to Delete");
            } else {
                recordsBefore = recordsBefore.split("\\)")[0].replace("(", "");
            }
            System.out.println(recordsBefore);
            page.locator(employeeTable).first().locator(employeeTableRows).first().locator(deleteButton).click();
            page.locator(confirmationPopup).waitFor();
            page.locator(confirmationPopup).locator(deleteButton).click();
            Thread.sleep(2000);
            page.locator(successDeleteAlert).waitFor();
            softAssert.assertTrue(page.locator(successDeleteAlert).isVisible(),"Alert not visible");
            Thread.sleep(2000);
            String recordsAfter = page.locator(emergencyContactsRecords).first().textContent();
            recordsAfter = recordsAfter.split("\\)")[0].replace("(", "");
            System.out.println(recordsAfter);
            if (recordsAfter.equals("No Records Found")) {
                recordsAfter = "0";
            } else {
                recordsAfter = recordsAfter.split("\\)")[0].replace("(", "");
            }
            Assert.assertEquals(Integer.parseInt(recordsAfter.trim()),Integer.parseInt(recordsBefore.trim())-1,"Records do not match");
            softAssert.assertAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (AssertionError e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }

    public void deleteEmployee() {
        try {
            if (!page.locator(employeeTableCells).nth(1).textContent().equals(employeeID)) {
                searchEmployee();
            }
            String recordsBefore = page.locator(recordsFound).first().textContent();
            if (recordsBefore.equals("No Records Found")) {
                throw new SkipException("No Records to Delete");
            } else {
                recordsBefore = recordsBefore.split("\\)")[0].replace("(", "");
            }
            System.out.println(recordsBefore);

            softAssert = new SoftAssert();
            page.locator(employeeTable).locator(employeeTableRows).first().locator(deleteButton).click();
            page.locator(confirmationPopup).waitFor();
            page.locator(confirmationPopup).locator(deleteButton).click();
            Thread.sleep(2000);

            String recordsAfter = page.locator(recordsFound).first().textContent();
            recordsAfter = recordsAfter.split("\\)")[0].replace("(", "");
            System.out.println(recordsAfter);
            if (recordsAfter.equals("No Records Found")) {
                recordsAfter = "0";
            } else {
                recordsAfter = recordsAfter.split("\\)")[0].replace("(", "");
            }
            Assert.assertEquals(Integer.parseInt(recordsAfter.trim()),Integer.parseInt(recordsBefore.trim())-1,"Records do not match");
            softAssert.assertAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } catch (AssertionError e) {
            e.printStackTrace();
            throw new AssertionError(e);
        }
    }

}