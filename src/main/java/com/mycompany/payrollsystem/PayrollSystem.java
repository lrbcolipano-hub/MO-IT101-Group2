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
import java.util.ArrayList;
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
        final String attendanceFile = "src\\main\\java\\com\\mycompany\\payrollsystem\\MotorPH_Employee Data - Attendance Record.csv";
 
        // Scanner object to read input from the user
        Scanner sc = new Scanner(System.in);

        // Stored system password for login authentication
        final String validPassword = "12345";

        // Display system header
        System.out.println("============================================");
        System.out.println("        MotorPH PAYROLL SYSTEM");
        System.out.println("============================================");
 
        // Ask user to enter their username and password
        System.out.print("Username: ");
        String username = sc.nextLine().trim().toLowerCase();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        boolean validUser = username.equals("employee") || username.equals("payroll_staff");
 
        //Validate both username and password
        //If credentials are incorrect, show error message
        if (!validUser || !password.equals(validPassword)) {
            System.out.println("Incorrect username and/or password.");
            System.out.println("Program terminated.");
            sc.close();
            return;
        }
        //Calls empDetails method to load employee data from a file into a 2D array (each row = one employee).
        String[][] employees = empDetails(empFile);

        // If login is successful, determine which role the user selected
        if (username.equals("employee")) {
            runEmployeeRole(sc, employees);
        } 
        else {
            runPayroll(sc, attendanceFile, employees);
        }
        sc.close();
    }

    // Method that handles the employee role menu and actions
    public static void runEmployeeRole(Scanner sc, String[][] employees) {
        while (true) {
            // Display menu options for the employee
            System.out.println("1. Enter your Employee Number");
            System.out.println("2. Exit the program");
            System.out.println("    ");

            // Ask the user to choose an option
            System.out.print("Enter your choice (1-2): ");
            String inputChoice = sc.nextLine();
 
            // Check the user's menu choice:
            if (inputChoice.equals("1")) {                          // If the user selects "1", prompt them to enter their employee number
                System.out.print("Enter your Employee Number: ");   //and call the method to display the employee's details.
                String inputEmpId = sc.nextLine().toLowerCase();    // If the user selects "2", exit the current method.
                searchEmployeeId(employees, inputEmpId);                 // For any other input, display an "Invalid Choice" message.
            } 
            else if (inputChoice.equals("2")) {
                return;
            } 
            else {
                System.out.println("Invalid Choice");
                System.out.println("    ");
            }
        }         
    }
    // Method that searches the employee's basic information
    public static void searchEmployeeId(String[][] employees, String inputEmpId) {

        boolean found = false;

        // Loop through all employees
        for (String[] employee : employees) {
            String id = employee[0];
            String lastName = employee[1];
            String firstName = employee[2];
            String birthDate = employee[3];
            // Check if IDs match
            if (inputEmpId.equalsIgnoreCase(id)) {
                printEmployee(id, lastName, firstName, birthDate);
                found = true;
                break;
            }
        }

        // If no match found
        if (!found) {
            System.out.println("Employee does not exist.");
        }
    }
    // Method that handles the payroll staff menu and available actions
    public static void runPayroll(Scanner sc, String attendanceFile, String[][] employees) {
 
        while (true) {
            // Display payroll staff menu header and available options for payroll staff
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
            if (choice.equals("1")) {                               // If option 1 is selected, run the payroll processing method
                runProcessPayroll(employees, attendanceFile, sc);     // This will compute employee payroll based on employee and attendance file
            } 
            else if (choice.equals("2")) {                        // If option 2 is selected, terminate the program
                System.out.println("Program terminated.");          // If the input is not 1 or 2, display an error message
                return;
            } 
            else {
                System.out.println("Invalid choice. Please enter 1-2.");
            }
        }
    }
    // Method that allows payroll staff to process payroll for one or all employees
    public static void runProcessPayroll(String[][] employees, String attendanceFile, Scanner sc) {
 
        while (true) {
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
                printPayrollDetails(attendanceFile, employees, inputEmpId);
 
            } 
            else if (choice.equals("2")) {
                // The code goes through every employee, gets their ID, and uses it to show their payroll details.
                for (String[] employee : employees) {
                    String inputEmpId = employee[0];
                    printPayrollDetails(attendanceFile, employees, inputEmpId);
                }
            } 
            else if (choice.equals("3")) {
                System.exit(0);
            } 
            else {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }
    public static void printEmployee(String id, String lastName, String firstName, String birthDate){
        System.out.println("============================");
        System.out.println("Employee #: "    + id);
        System.out.println("Employee Name: " + lastName + ", " + firstName);
        System.out.println("Birthday: "      + birthDate);
        System.out.println("============================");
    }
    
    static void printPayroll(
            String id, String lastName, String firstName,String birthDate, String monthName, double firstHalf,
            double secondHalf, double hourlyRate, int daysInMonth, double sssDeduction, double philHealthDeduction,
            double pagIbigDeduction, double withholdingTax,double totalDeductions, double secondHalfNetSalary)
        {
        System.out.println("=========" + monthName + "=========");
        printEmployee(id, lastName, firstName, birthDate);
        // Cutoff 1
        System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
        System.out.println("Total Hours Worked : " + firstHalf);
        System.out.println("Gross Salary: "        + firstHalf * hourlyRate);
        System.out.println("Net Salary: "          + firstHalf * hourlyRate);
        System.out.println("=========================");

        // Cutoff 2
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

    // Method that calculates and prints the employee's worked hours, salary,
    // deductions, and net pay for each payroll cutoff (June to December)
    public static void printPayrollDetails(String attendanceFile, String[][] employees, String inputEmpId) {

        boolean found = false;

        // Find the employee first
        for (String[] employee : employees) {
            if (inputEmpId.equalsIgnoreCase(employee[0])) {
                found = true;
                // Extract values from the array
                String id = employee[0];
                String lastName = employee[1];
                String firstName = employee[2];
                String birthDate = employee[3];
                double basicSalary = Double.parseDouble(employee[4]);
                double hourlyRate = Double.parseDouble(employee[5]);
                // Process months
                for (int month = 6; month <= 12; month++) {

                    int daysInMonth = YearMonth.of(2024, month).lengthOfMonth();
                    
                    // Get the hours worked for first half and second half of the month
                    double[] attendanceHours = computeHoursAttendance(attendanceFile, inputEmpId, month);
                    
                    double firstHalf  = attendanceHours[0];
                    double secondHalf = attendanceHours[1];
                    
                    String monthName = getMonthName(month); //store month name from getMonthName method
                    // Monthly gross income
                    double monthlyGrossIncome = (firstHalf + secondHalf) * hourlyRate;
                    //deductions
                    double sssDeduction        = computeSss(monthlyGrossIncome);
                    double philHealthDeduction = computePhilHealth(basicSalary) / 2;
                    double pagIbigDeduction    = computePagIbig(basicSalary);
                    
                    double taxableIncome = computeTaxableIncome(monthlyGrossIncome, basicSalary);
                    double withholdingTax = computeWithholdingTax(taxableIncome);
                    double totalDeductions = computeDeductions(sssDeduction,philHealthDeduction, pagIbigDeduction, withholdingTax);

                    double secondHalfNetSalary = (secondHalf * hourlyRate) - totalDeductions;
                    //run printPayroll method
                    printPayroll(
                            id,  lastName,  firstName,
                            birthDate,  monthName,  firstHalf,
                            secondHalf,  hourlyRate,  daysInMonth,
                            sssDeduction,  philHealthDeduction,
                            pagIbigDeduction,  withholdingTax,
                            totalDeductions,  secondHalfNetSalary);
                }
                break; // stop looping once found
            }
        }
        // If employee not found
        if (!found) {
            System.out.println("Employee does not exist.");
        }
    }  
    // Method that retrieves specific employee details from the employee CSV file
    // Returns an array: [0]=ID [1]=LastName [2]=FirstName [3]=Birthday [4]=BasicSalary [5]=HourlyRate   
    public static String[][] empDetails(String empFile) {

        ArrayList<String[]> employeesList = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(empFile));
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                // Skip header row
                if (data[0].equals("Employee #")) {
                    continue;
                }
                if (data.length < 19) {//validation if even one row is malformed
                    continue;
                }

                String[] employeeDetails = new String[6];

                // Clean salary (remove quotes and commas)
                String basicSalary = data[13].replace("\"", "").replace(",", "");

                employeeDetails[0] = data[0];   // Employee ID
                employeeDetails[1] = data[1];   // Last Name
                employeeDetails[2] = data[2];   // First Name
                employeeDetails[3] = data[3];   // Birthday
                employeeDetails[4] = basicSalary;
                employeeDetails[5] = data[18];  // Hourly Rate

                // Add each employee to the list
                employeesList.add(employeeDetails);
            }
            br.close();
        } 
        catch (FileNotFoundException ex) {
            System.getLogger(PayrollSystem.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        } 
        catch (IOException ex) {
            System.getLogger(PayrollSystem.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        }

        // Convert ArrayList to 2D array
        String[][] employeesArray = new String[employeesList.size()][6];
        for (int i = 0; i < employeesList.size(); i++) {
            employeesArray[i] = employeesList.get(i);
        }
        return employeesArray;
    }
    
    // Method that computes the total hours worked by an employee
    // Separates hours into first half (days 1-15) and second half (days 16-end)
    static double[] computeHoursAttendance(String attendanceFile, String inputEmpId, int month) {
 
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
 
        // Array: [0] = first half hours, [1] = second half hours
        double[] attendanceHours = new double[2];
        double firstHalf  = 0;
        double secondHalf = 0;
 
        try {
            // Use BufferedReader to read the attendance CSV file
            BufferedReader br = new BufferedReader(new FileReader(attendanceFile));
            String line;
            while ((line = br.readLine()) != null) {
 
                if (line.trim().isEmpty()) continue;       // Skip empty lines in the file
                String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");  // Split the CSV line into columns
                if (!data[0].equals(inputEmpId)) continue; // Skip records that do not belong to the entered employee ID
                if (data.length < 6) continue; //validation if even one row is malformed
                // Extract and split the date field (format: MM/DD/YYYY)
                String[] dateParts  = data[3].split("/");
                int recordMonth     = Integer.parseInt(dateParts[0]);
                int day             = Integer.parseInt(dateParts[1]);
                int year            = Integer.parseInt(dateParts[2]);
 
                if (year != 2024 || recordMonth != month) continue;
 
                LocalTime login  = LocalTime.parse(data[4].trim(), timeFormat);
                LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);
 
                double hours = computeHours(login, logout); // Calculate hours worked for that day using the computeHours() method

                // Separate hours depending on the day of the month
                // Days 1–15 are counted in the first half
                // Days 16 onwards are counted in the second half
                if (day <= 15) firstHalf  += hours;
                else           secondHalf += hours;
                attendanceHours[0] = firstHalf;
                attendanceHours[1] = secondHalf;
            }
            br.close();
        } 
        catch (FileNotFoundException ex) {
            System.getLogger(PayrollSystem.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        } 
        catch (IOException ex) {
            System.getLogger(PayrollSystem.class.getName())
                  .log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return attendanceHours;
    }   
    
    // Method that computes the number of hours worked based on login and logout times
    static double computeHours(LocalTime login, LocalTime logout) {
        // Computes total work hours between login and logout:
        // Applies grace period (8:10 AM → counts as 8:00 AM),
        // enforces 5:00 PM cutoff, deducts 1-hour break,
        // and caps total work hours at 8
        LocalTime graceTime = LocalTime.of(8, 10);     
        LocalTime cutoffTime = LocalTime.of(17, 0);   
        LocalTime startTime = LocalTime.of(8,0);        
        
        if (logout.isAfter(cutoffTime)) {                   
            logout = cutoffTime;
        }
        if (!login.isAfter(graceTime)) {    
            login = startTime;              
        }
        if (logout.isBefore(login)){        
            return 0;
        }
        long minutesWorked = Duration.between(login, logout).toMinutes();

        if (minutesWorked > 60) {
            minutesWorked -= 60;
        } 
        else {
            minutesWorked = 0;
        }
        double hours = minutesWorked / 60.0; 
        return Math.min(hours, 8.0);       
    }    
    
    // Method to calculate SSS contribution based on monthly gross income
    public static double computeSss(double monthlyGrossIncome) {
 
        double initialContribution    = 135;    // Calculates SSS deduction based on monthly income:
        double increment              = 22.5;   // Starts at a base contribution, increases per ₱500 salary bracket,
        double increasePerSalaryRange = 500;    // and is capped at a maximum of ₱1,125
 
        if (monthlyGrossIncome < 3250)      
            return initialContribution;

        double n = Math.ceil((monthlyGrossIncome - 3250) / increasePerSalaryRange); 
        return Math.min(initialContribution + increment * n, 1125); 
    }
 
    // Method to compute PhilHealth contribution based on basic salary
    static double computePhilHealth(double basicSalary) {
 
        if (basicSalary <= 10000) {             // Computes PhilHealth contribution based on basic salary:
            return 300;                         // - Fixed 300 if salary ≤ 10,000
        } 
        else if (basicSalary <= 59999.99) {   // - 3% of salary if between 10,001 and 59,999.99
            return basicSalary * 0.03;          // - Capped at 1,800 if salary ≥ 60,000
        } 
        else {
            return 1800;
        }
    }
    
    // Method to compute withholding tax based on taxable income (monthly)
    static double computeWithholdingTax(double taxableIncome) {
 
        if (taxableIncome < 20833) {                        // Computes withholding tax using progressive tax brackets:
            return 0;                                       // Applies increasing tax rates based on income range,
        } 
        else if (taxableIncome < 33333) {                 // with fixed base tax plus percentage of excess income
            return (taxableIncome - 20833) * 0.20;
        } 
        else if (taxableIncome < 66667) {
            return 2500 + ((taxableIncome - 33333) * 0.25);
        } 
        else if (taxableIncome < 166667) {
            return 10833 + ((taxableIncome - 66667) * 0.30);
        } 
        else if (taxableIncome < 6666667) {
            return 40833.33 + ((taxableIncome - 166667) * 0.32);
        } 
        else {
            return 200833.33 + ((taxableIncome - 666667) * 0.35);
        }
    }
    //Method to compute Pag-ibig deduction
    static double computePagIbig(double basicSalary) {
 
        if (basicSalary <= 1500) {              // 1% for salaries ≤ 1,500, otherwise 2% capped at ₱100
            return basicSalary * 0.01;
        } 
        else {
            return Math.min(basicSalary * 0.02, 100.00);
        }
    }
    //method that computes taxable income
    static double computeTaxableIncome(double monthlyGrossIncome, double basicSalary){
        return monthlyGrossIncome - computeSss(monthlyGrossIncome)- (computePhilHealth(basicSalary) / 2)- computePagIbig(basicSalary);
    }
    //method that computes total deductions
    static double computeDeductions(double sssDeduction, double philHealthDeduction, double pagIbigDeduction, double withholdingTax ){
        return sssDeduction + philHealthDeduction + pagIbigDeduction + withholdingTax;
    }
    //Method to get Month
    public static String getMonthName(int month) {
        return switch (month) {
            case 6 -> "June";
            case 7 -> "July";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "October";
            case 11 -> "November";
            case 12 -> "December";
            default -> "";
        };
    }
}
    
