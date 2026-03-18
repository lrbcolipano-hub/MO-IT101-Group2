/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
 
package com.mycompany.payrollsystem;
 
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
 
/**
 *
 * @author Anya
 */
public class PayrollSystem {
 
    public static void main(String[] args) {

        // File path for the CSV file that contains employee details
        final String empFile = "src\\main\\java\\com\\mycompany\\payrollsystem\\MotorPH_Employee Data - Employee Details.csv";

        // File path for the CSV file that contains employee attendance records
        final String attendaceFile = "src\\main\\java\\com\\mycompany\\payrollsystem\\MotorPH_Employee Data - Attendance Record.csv";

        // Scanner object to read input from the user (keyboard)
        Scanner sc = new Scanner(System.in);

        // Stored system password for login authentication
        final String validPassword = "12345";

        // Display system header
        System.out.println("============================================");
        System.out.println("        MotorPH PAYROLL SYSTEM");
        System.out.println("============================================");

        // Ask user to enter their username
        System.out.print("Username: ");

        // Read username input, remove extra spaces, and convert to lowercase
        String username = sc.nextLine().trim().toLowerCase();

        // Ask user to enter password
        System.out.print("Password: ");

        // Read password input and remove extra spaces
        String password = sc.nextLine().trim();

        // Check if the username is one of the valid system roles
        boolean validUser = username.equals("employee") || username.equals("payroll_staff");

        // Validate both username and password
        if (!validUser || !password.equals(validPassword)) {
            // If credentials are incorrect, show error message
            System.out.println("Incorrect username and/or password.");
            System.out.println("Program terminated.");

            // Close scanner before exiting program
            sc.close();
            return;
        }

        // If login is successful, determine which role the user selected
        if (username.equals("employee")) {
            // Run employee menu and functions
            runEmployeeRole(sc, empFile);
        } else {
            // Run payroll staff menu and payroll processing functions
            runPayroll(sc, attendaceFile, empFile);
        }

        // Close the scanner to prevent resource leaks
        sc.close();
    }
    // Method that handles the employee role menu and actions
    public static void runEmployeeRole(Scanner sc, String empFile) {

        // Infinite loop to continuously show the employee menu until the user chooses to exit
        while (true){ 

            // Display menu options for the employee
            System.out.println("1. Enter your Employee Number");
            System.out.println("2. Exit the program");
            System.out.println("    ");

            // Ask the user to choose an option
            System.out.print("Enter your choice (1-2): "); 

            // Read the user's menu choice
            String inputChoice = sc.nextLine();

            // Evaluate the user's choice
            if (inputChoice.equals("1")){ 

                // If the user selects option 1, ask for their employee number
                System.out.print("Enter your Employee Number: ");

                // Read the employee number entered by the user
                // Convert to lowercase for consistent comparison/searching
                String inputEmpId = sc.nextLine().toLowerCase();          

                // Call the method that searches and prints employee details
                // using the employee file and the entered employee ID
                printEmployee(empFile, inputEmpId);
            }

            // If the user selects option 2, exit the employee menu
            // and return to the main method
            else if(inputChoice.equals("2")){ 
                return;
            }

            // If the user enters an invalid option, show an error message
            else {
                System.out.println("Invalid Choice");
                System.out.println("    ");
            }
        }         
    }
    // Method that retrieves specific employee details from the employee CSV file
    // It returns an array containing selected employee information
    public static String[] empDetails(String empFile, String inputEmpId){

        // Create an array to store the employee details that will be returned
        // Index meaning:
        // [0] = Employee ID
        // [1] = Last Name
        // [2] = First Name
        // [3] = Birth Date
        // [4] = Basic Salary
        // [5] = Hourly Rate
        String[] employeeDetails = new String[6];

        try {

            // Create a BufferedReader to read the employee CSV file
            BufferedReader br = new BufferedReader(new FileReader(empFile));

            // Variable that will store each line of the CSV file
            String line;

            // Loop through the CSV file line by line until the end of the file
            while((line = br.readLine()) != null) {

                // Split the line into columns using a regex
                // This regex ensures that commas inside quotation marks are not treated as separators
                String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                // Check if the employee ID in the CSV file matches the input employee ID
                if (data[0].equals(inputEmpId)){

                    // Extract needed information from the CSV columns
                    String id = data[0];
                    String lastName = data[1];
                    String firstName = data[2];
                    String birthDate = data[3];

                    // Remove quotation marks and commas from the salary value
                    String basicSalary = data[13].replace("\"", "").replace(",", "");

                    // Get the hourly rate value
                    String hourlyRate = data[18];

                    // Store extracted values in the employeeDetails array
                    employeeDetails[0] = id;
                    employeeDetails[1] = lastName;
                    employeeDetails[2] = firstName;
                    employeeDetails[3] = birthDate;
                    employeeDetails[4] = basicSalary;
                    employeeDetails[5] = hourlyRate;

                    // Stop searching once the matching employee is found
                    break;
                }
            }

        } 
        // Handle error if the employee file cannot be found
        catch (FileNotFoundException ex) {
            System.getLogger(PayrollSystem.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        } 

        // Handle other input/output errors while reading the file
        catch (IOException ex) {
            System.getLogger(PayrollSystem.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        } 

        // Return the collected employee details
        return employeeDetails;  
    }
    
    // Method that prints the employee's basic information
    public static void printEmployee(String empFile, String inputEmpId){

        // Call the empDetails method to retrieve employee information from the CSV file
        // The method returns an array containing the employee details
        String[] employeeDetail = empDetails(empFile, inputEmpId);

        // Extract specific details from the returned array
        String id = employeeDetail[0];        // Employee ID
        String lastName = employeeDetail[1];  // Employee Last Name
        String firstName = employeeDetail[2]; // Employee First Name
        String birthDate = employeeDetail[3]; // Employee Birth Date

        // Check if the entered employee ID matches the ID retrieved from the file
        // If it does not match, it means the employee does not exist in the file
        if (!inputEmpId.equals(id)) {
            System.out.println("Employee does not exist.");
            return; // Stop the method if employee is not found
        }

        // Display the employee information in a formatted output
        System.out.println("============================");
        System.out.println("Employee #: "+ id); // Print employee ID
        System.out.println("Employee Name: "+lastName+", "+firstName); // Print employee full name
        System.out.println("Birthday: "+ birthDate); // Print employee birth date
        System.out.println("============================");
    } 
    
    // Method that computes the total hours worked by an employee based on attendance records
    // It separates the total hours into two parts: first half (days 1–15) and second half (days 16–end)
    static double[] computeHoursAttendace(String attendaceFile, String inputEmpId, int month, int daysInMonth){

        // Formatter used to parse time values from the CSV file (example format: 8:30, 17:00)
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");

        // Array that will store the total hours worked
        // Index [0] = hours worked during the first half of the month
        // Index [1] = hours worked during the second half of the month
        double[] attendaceHours = new double [2];

        // Variables to store computed hours
        double hours = 0;
        double firstHalf = 0;
        double secondHalf = 0;

        try {

            // Use BufferedReader to read the attendance CSV file
            BufferedReader br = new BufferedReader(new FileReader(attendaceFile));
            String line;

            // Loop through the file line by line
            while ((line = br.readLine()) != null) {

                // Skip empty lines in the file
                if (line.trim().isEmpty()) continue;

                // Split the CSV line into columns
                String[] data = line.split(",");

                // Skip records that do not belong to the entered employee ID
                if (!data[0].equals(inputEmpId)) continue;

                // Extract and split the date field (format: MM/DD/YYYY)
                String[] dateParts = data[3].split("/");

                // Convert date parts into integers
                int recordMonth = Integer.parseInt(dateParts[0]);
                int day = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);

                // Only process records that match the selected year and month
                if (year != 2024 || recordMonth != month) 
                    continue;

                // Convert login and logout time strings into LocalTime objects
                LocalTime login = LocalTime.parse(data[4].trim(), timeFormat);
                LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);

                // Calculate hours worked for that day using the computeHours() method
                hours = computeHours(login, logout);

                // Separate hours depending on the day of the month
                // Days 1–15 are counted in the first half
                // Days 16 onwards are counted in the second half
                if (day <= 15) 
                    firstHalf += hours;
                else 
                    secondHalf += hours;

                // Store accumulated hours in the result array
                attendaceHours[0] = firstHalf;
                attendaceHours[1] = secondHalf;
            }

            // Close the file after reading
            br.close();

        } 
        // Handle error if the attendance file cannot be found
        catch (FileNotFoundException ex) {
            System.getLogger(PayrollSystem.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        } 

        // Handle other input/output errors while reading the file
        catch (IOException ex) {
            System.getLogger(PayrollSystem.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        }

        // Return the computed attendance hours for both halves of the month
        return attendaceHours;
    }  
    // a method that returns printed responses base on input choices of the payroll staff
    // Method that handles the payroll staff menu and available actions
    public static void runPayroll(Scanner sc, String attendaceFile, String empFile){
        // Infinite loop to continuously display the payroll processing menu
        // until the user chooses to exit the program
        while (true) { 

            // Display payroll staff menu header
            System.out.println("\n============================================");
            System.out.println("        PAYROLL STAFF MENU");
            System.out.println("============================================");

            // Display available options for payroll staff
            System.out.println("  [1] Process Payroll");
            System.out.println("  [2] Exit");

            // Ask the user to enter their menu choice
            System.out.print("Enter your choice (1-2): ");

            // Read and trim the user input
            String choice = sc.nextLine().trim();

            // Check the user's choice
            if (choice.equals("1")) {

                // If option 1 is selected, run the payroll processing method
                // This will compute employee payroll based on employee and attendance files
                runProcessPayroll(empFile, attendaceFile, sc);

            } 
            else if (choice.equals("2")) {

                // If option 2 is selected, terminate the program
                System.out.println("Program terminated.");

                // Return to the main method and exit
                return;

            } 
            else {

                // If the input is not 1 or 2, display an error message
                // and loop back to show the menu again
                System.out.println("Invalid choice. Please enter 1-2.");
            }
        }
    }
    // Method that allows payroll staff to process payroll
    // It provides options to process payroll for one employee or for all employees
    public static void runProcessPayroll(String empFile, String attendaceFile, Scanner sc){

        // Infinite loop to continuously display the payroll processing menu
        while (true) {

            // Display payroll processing menu options
            System.out.println("\n--- Process Payroll ---");
            System.out.println("  [1] One employee");
            System.out.println("  [2] All employees");
            System.out.println("  [3] exit");

            // Ask the payroll staff to choose an option
            System.out.print("Choice: ");
            String choice = sc.nextLine().trim();

            // Option 1: Process payroll for a single employee
            if (choice.equals("1")) {

                // Ask the user to enter the employee number
                System.out.print("Enter Employee Number: ");
                String inputEmpId = sc.nextLine().toLowerCase();

                // Call method that prints the worked hours and payroll details
                // for the specified employee
                printWorkedDetails(attendaceFile, empFile, inputEmpId);

            } 

            // Option 2: Process payroll for all employees in the employee CSV file
            else if (choice.equals("2")) {

                BufferedReader br;

                try {
                    // Open the employee file for reading
                    br = new BufferedReader(new FileReader(empFile));

                    String line;

                    // Read the employee file line by line
                    while ((line = br.readLine()) != null) {

                        // Skip empty lines
                        if (line.trim().isEmpty()) continue;

                        // Split CSV line into columns while preserving quoted commas
                        String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                        // Extract employee ID from the first column
                        String inputEmpId = data[0];

                        // Skip the header row (which contains the text "Employee #")
                        if (!inputEmpId.equals("Employee #")){

                            // Print payroll / worked hours / payslip details
                            printWorkedDetails(attendaceFile, empFile, inputEmpId);
                        }
                    }

                    // Close the file after reading
                    br.close();

                } 
                // Handle error if employee file cannot be found
                catch (FileNotFoundException ex) {
                    System.getLogger(PayrollSystem.class.getName())
                          .log(System.Logger.Level.ERROR, (String) null, ex);
                } 
                // Handle other file reading errors
                catch (IOException ex) {
                    System.getLogger(PayrollSystem.class.getName())
                          .log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }

            // Option 3: Exit the program completely
            else if (choice.equals("3")) {
                System.exit(0);
            } 

            // Error handling for invalid menu input
            else {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }
    // Method that calculates and prints the employee's worked hours, salary, deductions,
    // and net pay for each payroll cutoff. This method is called inside runProcessPayroll().
    public static void printWorkedDetails(String attendaceFile,String empFile, String inputEmpId){

            // Retrieve employee details from the employee CSV file
            String[] employeeDetail = empDetails(empFile, inputEmpId);

            // Check if the employee ID entered exists in the file
            if (!inputEmpId.equals(employeeDetail[0])) {
                System.out.println("Employee does not exist.");
                return; // stop the method if employee is not found
            }

            // Extract employee information from the returned array
            String id = employeeDetail[0];
            String lastName = employeeDetail[1];
            String firstName = employeeDetail[2];
            String birthDate = employeeDetail[3];

            // Convert salary and hourly rate from String to double for computation
            double basicSalary = Double.parseDouble(employeeDetail[4]);
            double hourlyRate = Double.parseDouble(employeeDetail[5]);

            // Loop through months from June to December (months with attendance records)
            for (int month = 6; month <= 12; month++) {

                // Determine number of days in the current month
                int daysInMonth = YearMonth.of(2024, month).lengthOfMonth();

                // Get the hours worked for first half and second half of the month
                double[] attendaceHours = computeHoursAttendace(attendaceFile, inputEmpId,  month, daysInMonth);

                // First half (days 1–15)
                double firstHalf = attendaceHours[0];

                // Second half (days 16–end of month)
                double secondHalf = attendaceHours[1];

                // Convert month number to readable month name
                String monthName = switch (month) {
                    case 6 -> "June";
                    case 7 -> "July";
                    case 8 -> "August";
                    case 9 -> "September";
                    case 10 -> "October";
                    case 11 -> "November";
                    case 12 -> "December";
                    default -> "Month " + month;
                };

                // Monthly gross income = total hours worked multiplied by hourly rate
                double monthlyGrossIncome = (firstHalf + secondHalf) * hourlyRate;

                // Taxable income = gross income minus mandatory deductions
                // (SSS + half of PhilHealth + Pag-IBIG)
                double taxableIncome = monthlyGrossIncome 
                        - calculateSSSDeduction(monthlyGrossIncome)
                        - (computePhilHealth(basicSalary) / 2)
                        - computePagibigEmployee(basicSalary);

                // Net salary for the second cutoff after deductions and tax
                double secondHalfnetSalary = (secondHalf * hourlyRate)
                        - computeWithholdingTax(taxableIncome)
                        - calculateSSSDeduction(monthlyGrossIncome)
                        - (computePhilHealth(basicSalary) / 2)
                        - computePagibigEmployee(basicSalary);

                // Print the month header
                System.out.println("=========" + monthName + "=========");

                // Print employee details
                printEmployee(empFile, inputEmpId);

                // Print payroll details for the first cutoff (1–15)
                System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
                System.out.println("Total Hours Worked : " + firstHalf);
                System.out.println("Gross Salary: " + firstHalf * hourlyRate);

                // No deductions applied in the first cutoff
                System.out.println("Net Salary: " + firstHalf * hourlyRate);
                System.out.println("=========================");

                // Print payroll details for the second cutoff (16–end of month)
                System.out.println("\nCutoff Date: " + monthName + " 16 to " + daysInMonth);
                System.out.println("Total Hours Worked : " + secondHalf);
                System.out.println("Gross Salary: " + secondHalf * hourlyRate);

                // Display deductions
                System.out.println("Deductions: ");
                System.out.println("    SSS: " + calculateSSSDeduction(monthlyGrossIncome));
                System.out.println("    PhilHealth: " + computePhilHealth(basicSalary) / 2);
                System.out.println("    Pag-IBIG: " + computePagibigEmployee(basicSalary));
                System.out.println("    Tax: " + computeWithholdingTax(taxableIncome));

                // Calculate and display total deductions
                System.out.println("    Total Deductions: " +
                        (computeWithholdingTax(taxableIncome)
                        + calculateSSSDeduction(monthlyGrossIncome)
                        + (computePhilHealth(basicSalary) / 2)
                        + computePagibigEmployee(basicSalary)));

                // Display final net salary after deductions
                System.out.println("Net Salary: " + secondHalfnetSalary);
                System.out.println("=========================");
            }
    }      
    // Method that computes the number of hours worked based on login and logout times
    static double computeHours(LocalTime login, LocalTime logout) {

        // Set 8:10 AM as the grace period time according to MotorPH policy
        LocalTime graceTime = LocalTime.of(8, 10);

        // Set 5:00 PM as the official logout cutoff time
        // Any logout time after this will be treated as 5:00 PM
        LocalTime cutoffTime = LocalTime.of(17, 0);

        // Set 8:00 AM as the official start time
        // Employees who log in before or within the grace period will still be counted as starting at 8:00 AM
        LocalTime startTime = LocalTime.of(8,0);

        // Apply the 5:00 PM cutoff rule
        // If an employee logs out later than 5:00 PM, use 5:00 PM as the logout time
        if (logout.isAfter(cutoffTime)) {
            logout = cutoffTime;
        }

        // Apply the grace period rule
        // If login time is before or within the grace period (8:10 AM),
        // the official login time will be considered as 8:00 AM
        if (!login.isAfter(graceTime)) {
            login = startTime;
        }

        // Safety check: if logout happens before login due to incorrect data
        // return 0 hours worked
        if (logout.isBefore(login)){
            return 0;
        }

        // Compute the total minutes worked between login and logout
        long minutesWorked = Duration.between(login, logout).toMinutes();

        // Deduct 1 hour (60 minutes) for lunch break if work duration is more than 1 hour
        if (minutesWorked > 60) {
            minutesWorked -= 60;
        } else {
            minutesWorked = 0;
        }

        // Convert minutes worked to hours
        double hours = minutesWorked / 60.0;

        // Ensure maximum payable work hours per day is 8 hours
        return Math.min(hours, 8.0);
    }
    // Method to calculate SSS (Social Security System) contribution based on monthly gross income
    public static double calculateSSSDeduction(double monthlyGrossIncome) {

        // Base contribution for employees earning below the minimum SSS bracket (₱3,250)
        double initialContribution = 135;

        // Increment added to the base contribution for each salary bracket above the minimum
        double increment = 22.5;

        // Each salary bracket increases by ₱500 in monthly gross income
        double increasePerSalaryRange = 500;

        // If monthly gross income is below ₱3,250, return the base contribution
        if (monthlyGrossIncome < 3250)
            return initialContribution;

        // Calculate which bracket the employee falls into:
        // Subtract the minimum salary (₱3,250), divide by bracket range (₱500),
        // and take the ceiling to ensure partial bracket counts as full increment
        double n = Math.ceil((monthlyGrossIncome - 3250) / 500);

        // Compute SSS deduction:
        // initial contribution + (increment * bracket position)
        // Ensure the maximum deduction does not exceed ₱1,125
        return Math.min(initialContribution + increment * n, 1125);
    }// Method to compute PhilHealth contribution based on basic salary
    // Method to compute PhilHealth contribution based on basic salary
    static double computePhilHealth(double basicSalary) {

        // If basic salary is ₱10,000 or below, PhilHealth deduction is fixed at ₱300
        if(basicSalary <= 10000) {
            return 300;
        }
        // If basic salary is between ₱10,000.01 and ₱59,999.99,
        // PhilHealth deduction is 3% of the basic salary
        else if (basicSalary <= 59999.99) {
            return basicSalary * 0.03;
        }
        // If basic salary is ₱60,000 or higher, PhilHealth deduction is capped at ₱1,800
        else {
            return 1800;
        }
    }
    // Method to compute Pag-IBIG contribution based on basic salary
    static double computePagibigEmployee(double basicSalary){

        // For employees earning less than ₱1,500, contribution is 1% of basic salary
        if (basicSalary <= 1500){
            return basicSalary * 0.01;
        }
        // For employees earning more than ₱1,500:
        // Contribution is 2% of basic salary, but capped at ₱100
        else {
            return Math.min(basicSalary * 0.02, 100.00);
        }
    }
    // Method to compute withholding tax based on taxable income (monthly)
    static double computeWithholdingTax(double taxableIncome) {

        // If taxable income is below ₱20,833 → no tax
        if (taxableIncome < 20833)  
            return 0;

        // If taxable income is ₱20,833 to ₱33,332 → 20% of the amount exceeding ₱20,833
        else if (taxableIncome < 33333)  
            return (taxableIncome - 20833) * 0.20;

        // If taxable income is ₱33,333 to ₱66,666 → ₱2,500 + 25% of the amount exceeding ₱33,333
        else if (taxableIncome < 66667)  
            return 2500 + ((taxableIncome - 33333) * 0.25);

        // If taxable income is ₱66,667 to ₱1,666,666 → ₱10,833 + 30% of the amount exceeding ₱66,667
        else if (taxableIncome < 1666667) 
            return 10833 + ((taxableIncome - 66667) * 0.30);

        // If taxable income is ₱1,666,667 to ₱6,666,666 → ₱40,833.33 + 32% of the amount exceeding ₱166,667
        else if (taxableIncome < 6666667) 
            return 40833.33 + ((taxableIncome - 166667) * 0.32);

        // If taxable income is ₱6,666,667 or more → ₱200,833.33 + 35% of the amount exceeding ₱666,667
        else                        
            return 200833.33 + ((taxableIncome - 666667) * 0.35);
    }
}
 