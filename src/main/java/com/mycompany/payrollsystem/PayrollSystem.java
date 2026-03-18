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
        // FIX #3 & #4: Renamed 'attendaceFile' -> 'attendanceFile' (typo corrected)
        final String attendanceFile = "src\\main\\java\\com\\mycompany\\payrollsystem\\MotorPH_Employee Data - Attendance Record.csv";
 
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
            // FIX #3 & #4: Updated parameter name to attendanceFile
            runPayroll(sc, attendanceFile, empFile);
        }
 
        // Close the scanner to prevent resource leaks
        sc.close();
    }
 
    // Method that handles the employee role menu and actions
    public static void runEmployeeRole(Scanner sc, String empFile) {
 
        // Infinite loop to continuously show the employee menu until the user chooses to exit
        while (true) {
 
            // Display menu options for the employee
            System.out.println("1. Enter your Employee Number");
            System.out.println("2. Exit the program");
            System.out.println("    ");
 
            // Ask the user to choose an option
            System.out.print("Enter your choice (1-2): ");
 
            // Read the user's menu choice
            String inputChoice = sc.nextLine();
 
            // Evaluate the user's choice
            if (inputChoice.equals("1")) {
 
                // If the user selects option 1, ask for their employee number
                System.out.print("Enter your Employee Number: ");
 
                // Read the employee number entered by the user
                String inputEmpId = sc.nextLine().toLowerCase();
 
                // Call the method that searches and prints employee details
                printEmployee(empFile, inputEmpId);
 
            } else if (inputChoice.equals("2")) {
                // If the user selects option 2, exit the employee menu
                return;
 
            } else {
                // If the user enters an invalid option, show an error message
                System.out.println("Invalid Choice");
                System.out.println("    ");
            }
        }
    }
 
    // Method that retrieves specific employee details from the employee CSV file
    // Returns an array: [0]=ID [1]=LastName [2]=FirstName [3]=Birthday [4]=BasicSalary [5]=HourlyRate
    public static String[] empDetails(String empFile, String inputEmpId) {
 
        String[] employeeDetails = new String[6];
 
        try {
 
            // Create a BufferedReader to read the employee CSV file
            BufferedReader br = new BufferedReader(new FileReader(empFile));
            String line;
 
            // Loop through the CSV file line by line until the end of the file
            while ((line = br.readLine()) != null) {
 
                // Split the line into columns using a regex
                // This regex ensures that commas inside quotation marks are not treated as separators
                String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
 
                // Check if the employee ID in the CSV file matches the input employee ID
                if (data[0].equals(inputEmpId)) {
 
                    // Remove quotation marks and commas from the salary value
                    String basicSalary = data[13].replace("\"", "").replace(",", "");
 
                    // Store extracted values in the employeeDetails array
                    employeeDetails[0] = data[0];   // Employee ID
                    employeeDetails[1] = data[1];   // Last Name
                    employeeDetails[2] = data[2];   // First Name
                    employeeDetails[3] = data[3];   // Birthday
                    employeeDetails[4] = basicSalary;
                    employeeDetails[5] = data[18];  // Hourly Rate
 
                    // Stop searching once the matching employee is found
                    break;
                }
            }
 
            br.close();
 
        } catch (FileNotFoundException ex) {
            System.getLogger(PayrollSystem.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            System.getLogger(PayrollSystem.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        }
 
        return employeeDetails;
    }
 
    // Method that prints the employee's basic information
    public static void printEmployee(String empFile, String inputEmpId) {
 
        String[] employeeDetail = empDetails(empFile, inputEmpId);
 
        String id        = employeeDetail[0];
        String lastName  = employeeDetail[1];
        String firstName = employeeDetail[2];
        String birthDate = employeeDetail[3];
 
        // Check if the entered employee ID matches the ID retrieved from the file
        if (!inputEmpId.equals(id)) {
            // FIX #10: Corrected typo 'exit' -> 'exist'
            System.out.println("Employee does not exist.");
            return;
        }
 
        System.out.println("============================");
        System.out.println("Employee #: "    + id);
        System.out.println("Employee Name: " + lastName + ", " + firstName);
        System.out.println("Birthday: "      + birthDate);
        System.out.println("============================");
    }
 
    // Method that computes the total hours worked by an employee
    // Separates hours into first half (days 1-15) and second half (days 16-end)
    // FIX #1: Removed unused parameter 'daysInMonth'
    // FIX #3 & #4: Renamed method from 'computeHoursAttendace' -> 'computeHoursAttendance'
    //              Renamed parameter 'attendaceFile' -> 'attendanceFile'
    static double[] computeHoursAttendance(String attendanceFile, String inputEmpId, int month) {
 
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
 
        // Array: [0] = first half hours, [1] = second half hours
        double[] attendanceHours = new double[2];
 
        double firstHalf  = 0;
        double secondHalf = 0;
 
        try {
 
            BufferedReader br = new BufferedReader(new FileReader(attendanceFile));
            String line;
 
            while ((line = br.readLine()) != null) {
 
                if (line.trim().isEmpty()) continue;
 
                String[] data = line.split(",");
 
                if (!data[0].equals(inputEmpId)) continue;
 
                String[] dateParts  = data[3].split("/");
                int recordMonth     = Integer.parseInt(dateParts[0]);
                int day             = Integer.parseInt(dateParts[1]);
                int year            = Integer.parseInt(dateParts[2]);
 
                if (year != 2024 || recordMonth != month) continue;
 
                LocalTime login  = LocalTime.parse(data[4].trim(), timeFormat);
                LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);
 
                // FIX #2: Removed top-level 'double hours = 0' and declared inline here
                double hours = computeHours(login, logout);
 
                if (day <= 15) firstHalf  += hours;
                else           secondHalf += hours;
 
                attendanceHours[0] = firstHalf;
                attendanceHours[1] = secondHalf;
            }
 
            br.close();
 
        } catch (FileNotFoundException ex) {
            System.getLogger(PayrollSystem.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            System.getLogger(PayrollSystem.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        }
 
        return attendanceHours;
    }
 
    // Method that handles the payroll staff menu and available actions
    // FIX #3 & #4: Renamed parameter 'attendaceFile' -> 'attendanceFile'
    public static void runPayroll(Scanner sc, String attendanceFile, String empFile) {
 
        while (true) {
 
            System.out.println("\n============================================");
            System.out.println("        PAYROLL STAFF MENU");
            System.out.println("============================================");
            System.out.println("  [1] Process Payroll");
            System.out.println("  [2] Exit");
            System.out.print("Enter your choice (1-2): ");
 
            String choice = sc.nextLine().trim();
 
            if (choice.equals("1")) {
                // FIX #3 & #4: Updated parameter name to attendanceFile
                runProcessPayroll(empFile, attendanceFile, sc);
 
            } else if (choice.equals("2")) {
                System.out.println("Program terminated.");
                return;
 
            } else {
                System.out.println("Invalid choice. Please enter 1-2.");
            }
        }
    }
 
    // Method that allows payroll staff to process payroll for one or all employees
    // FIX #3 & #4: Renamed parameter 'attendaceFile' -> 'attendanceFile'
    public static void runProcessPayroll(String empFile, String attendanceFile, Scanner sc) {
 
        while (true) {
 
            System.out.println("\n--- Process Payroll ---");
            System.out.println("  [1] One employee");
            System.out.println("  [2] All employees");
            System.out.println("  [3] exit");
            System.out.print("Choice: ");
 
            String choice = sc.nextLine().trim();
 
            if (choice.equals("1")) {
 
                System.out.print("Enter Employee Number: ");
                String inputEmpId = sc.nextLine().toLowerCase();
 
                // FIX #3 & #4: Updated call to use attendanceFile
                printWorkedDetails(attendanceFile, empFile, inputEmpId);
 
            } else if (choice.equals("2")) {
 
                // FIX #7: Use try-with-resources so BufferedReader is always closed
                try (BufferedReader br = new BufferedReader(new FileReader(empFile))) {
 
                    String line;
 
                    while ((line = br.readLine()) != null) {
 
                        if (line.trim().isEmpty()) continue;
 
                        String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                        String inputEmpId = data[0];
 
                        if (!inputEmpId.equals("Employee #")) {
                            // FIX #3 & #4: Updated call to use attendanceFile
                            printWorkedDetails(attendanceFile, empFile, inputEmpId);
                        }
                    }
 
                } catch (FileNotFoundException ex) {
                    System.getLogger(PayrollSystem.class.getName())
                          .log(System.Logger.Level.ERROR, (String) null, ex);
                } catch (IOException ex) {
                    System.getLogger(PayrollSystem.class.getName())
                          .log(System.Logger.Level.ERROR, (String) null, ex);
                }
 
            } else if (choice.equals("3")) {
                System.exit(0);
 
            } else {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }
 
    // Method that calculates and prints the employee's worked hours, salary,
    // deductions, and net pay for each payroll cutoff (June to December)
    // FIX #3 & #4: Renamed parameter 'attendaceFile' -> 'attendanceFile'
    public static void printWorkedDetails(String attendanceFile, String empFile, String inputEmpId) {
 
        String[] employeeDetail = empDetails(empFile, inputEmpId);
 
        if (!inputEmpId.equals(employeeDetail[0])) {
            // FIX #10: Corrected typo 'exit' -> 'exist'
            System.out.println("Employee does not exist.");
            return;
        }
 
        String id        = employeeDetail[0];
        String lastName  = employeeDetail[1];
        String firstName = employeeDetail[2];
        String birthDate = employeeDetail[3];
 
        double basicSalary = Double.parseDouble(employeeDetail[4]);
        double hourlyRate  = Double.parseDouble(employeeDetail[5]);
 
        // FIX #9: Print employee header ONCE here, not repeated inside the month loop
        System.out.println("============================");
        System.out.println("Employee #: "    + id);
        System.out.println("Employee Name: " + lastName + ", " + firstName);
        System.out.println("Birthday: "      + birthDate);
        System.out.println("============================");
 
        for (int month = 6; month <= 12; month++) {
 
            int daysInMonth = YearMonth.of(2024, month).lengthOfMonth();
 
            // FIX #1: Removed daysInMonth from call (unused parameter removed)
            // FIX #3: Updated method name to computeHoursAttendance
            // FIX #4: Updated variable name to attendanceHours
            double[] attendanceHours = computeHoursAttendance(attendanceFile, inputEmpId, month);
 
            double firstHalf  = attendanceHours[0];
            double secondHalf = attendanceHours[1];
 
            String monthName = switch (month) {
                case 6  -> "June";
                case 7  -> "July";
                case 8  -> "August";
                case 9  -> "September";
                case 10 -> "October";
                case 11 -> "November";
                case 12 -> "December";
                default -> "Month " + month;
            };
 
            double monthlyGrossIncome = (firstHalf + secondHalf) * hourlyRate;
 
            // FIX #6: Extract each deduction to a named variable for clarity
            double sssDeduction       = calculateSSSDeduction(monthlyGrossIncome);
            double philHealthDeduction = computePhilHealth(basicSalary) / 2;
            // FIX #5: Renamed computePagibigEmployee -> computePagIbig
            double pagIbigDeduction   = computePagIbig(basicSalary);
 
            // FIX #6: Taxable income now clearly shows each deduction component
            double taxableIncome = monthlyGrossIncome
                    - sssDeduction
                    - philHealthDeduction
                    - pagIbigDeduction;
 
            double withholdingTax = computeWithholdingTax(taxableIncome);
 
            double totalDeductions = sssDeduction
                    + philHealthDeduction
                    + pagIbigDeduction
                    + withholdingTax;
 
            double secondHalfNetSalary = (secondHalf * hourlyRate) - totalDeductions;
 
            System.out.println("=========" + monthName + "=========");
 
            // Cutoff 1 (days 1-15): no deductions
            System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
            System.out.println("Total Hours Worked : " + firstHalf);
            System.out.println("Gross Salary: "        + firstHalf * hourlyRate);
            System.out.println("Net Salary: "          + firstHalf * hourlyRate);
            System.out.println("=========================");
 
            // Cutoff 2 (days 16-end): with all deductions
            System.out.println("\nCutoff Date: " + monthName + " 16 to " + daysInMonth);
            System.out.println("Total Hours Worked : " + secondHalf);
            System.out.println("Gross Salary: "        + secondHalf * hourlyRate);
            System.out.println("Deductions: ");
            System.out.println("    SSS: "              + sssDeduction);
            System.out.println("    PhilHealth: "       + philHealthDeduction);
            System.out.println("    Pag-IBIG: "         + pagIbigDeduction);
            System.out.println("    Tax: "              + withholdingTax);
            System.out.println("    Total Deductions: " + totalDeductions);
            System.out.println("Net Salary: "           + secondHalfNetSalary);
            System.out.println("=========================");
        }
    }
 
    // Method that computes the number of hours worked based on login and logout times
    // FIX #8: When login <= graceTime, return 8.0 immediately instead of resetting
    //         login to 8:00 and recomputing -- prevents wrong results on early logouts
    static double computeHours(LocalTime login, LocalTime logout) {
 
        // 8:10 AM grace period cutoff per MotorPH policy
        LocalTime graceTime  = LocalTime.of(8, 10);
 
        // 5:00 PM official logout cutoff
        LocalTime cutoffTime = LocalTime.of(17, 0);
 
        // Cap logout at 5:00 PM
        if (logout.isAfter(cutoffTime)) {
            logout = cutoffTime;
        }
 
        // FIX #8: Return 8.0 flat immediately when on time (login <= 08:10)
        // This correctly handles early logouts without wrong duration math
        if (!login.isAfter(graceTime)) {
            return 8.0;
        }
 
        // Safety check: if logout is before login, return 0
        if (logout.isBefore(login)) {
            return 0;
        }
 
        // Compute total minutes worked between login and logout
        long minutesWorked = Duration.between(login, logout).toMinutes();
 
        // Deduct 1 hour (60 minutes) for lunch break if worked more than 1 hour
        if (minutesWorked > 60) {
            minutesWorked -= 60;
        } else {
            minutesWorked = 0;
        }
 
        // Convert minutes to hours, capped at 8
        double hours = minutesWorked / 60.0;
        return Math.min(hours, 8.0);
    }
 
    // Method to calculate SSS contribution based on monthly gross income
    public static double calculateSSSDeduction(double monthlyGrossIncome) {
 
        double initialContribution    = 135;
        double increment              = 22.5;
        double increasePerSalaryRange = 500;
 
        if (monthlyGrossIncome < 3250)
            return initialContribution;
 
        double n = Math.ceil((monthlyGrossIncome - 3250) / increasePerSalaryRange);
        return Math.min(initialContribution + increment * n, 1125);
    }
 
    // Method to compute PhilHealth contribution based on basic salary
    static double computePhilHealth(double basicSalary) {
 
        if (basicSalary <= 10000) {
            return 300;
        } else if (basicSalary <= 59999.99) {
            return basicSalary * 0.03;
        } else {
            return 1800;
        }
    }
 
    // FIX #5: Renamed from 'computePagibigEmployee' -> 'computePagIbig'
    //         Consistent with computePhilHealth and computeWithholdingTax naming
    static double computePagIbig(double basicSalary) {
 
        if (basicSalary <= 1500) {
            return basicSalary * 0.01;
        } else {
            return Math.min(basicSalary * 0.02, 100.00);
        }
    }
 
    // Method to compute withholding tax based on taxable income (monthly)
    static double computeWithholdingTax(double taxableIncome) {
 
        if (taxableIncome < 20833) {
            return 0;
        } else if (taxableIncome < 33333) {
            return (taxableIncome - 20833) * 0.20;
        } else if (taxableIncome < 66667) {
            return 2500 + ((taxableIncome - 33333) * 0.25);
        } else if (taxableIncome < 1666667) {
            return 10833 + ((taxableIncome - 66667) * 0.30);
        } else if (taxableIncome < 6666667) {
            return 40833.33 + ((taxableIncome - 166667) * 0.32);
        } else {
            return 200833.33 + ((taxableIncome - 666667) * 0.35);
        }
    }
}
 