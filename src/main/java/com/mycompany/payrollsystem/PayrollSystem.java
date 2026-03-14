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
        //Store employee details csv file to a variable named empFile
        final String empFile = "src\\main\\java\\com\\mycompany\\payrollsystem\\MotorPH_Employee Data - Employee Details.csv";
        //Store attendace records csv file to a variable named attendaceFile
        final String attendaceFile = "src\\main\\java\\com\\mycompany\\payrollsystem\\MotorPH_Employee Data - Attendance Record.csv";
        
        Scanner sc = new Scanner(System.in);
        // -- Credentials -----------------------------------------------------------
        final String validPassword = "12345";

        System.out.println("============================================");
        System.out.println("        MotorPH PAYROLL SYSTEM");
        System.out.println("============================================");
        System.out.print("Username: ");
        String username = sc.nextLine().trim().toLowerCase();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        boolean validUser = username.equals("employee") || username.equals("payroll_staff");

        if (!validUser || !password.equals(validPassword)) {
            System.out.println("Incorrect username and/or password.");
            System.out.println("Program terminated.");
            sc.close();
            return;
        }

        if (username.equals("employee")) {
            runEmployeeRole(sc, empFile);
        } else {
            runPayroll(sc,attendaceFile, empFile);
        }

        sc.close();
    }
    // a method that returns printed responses base on input choices of the employee
    public static void runEmployeeRole(Scanner sc, String empFile) {
        while (true){ //while true print the following choices and ask for an input choice
            System.out.println("1. Enter your Employee Number");
            System.out.println("2. Exit the program");
            System.out.println("    ");
            System.out.print("Enter your choice (1-2): "); 
            String inputChoice = sc.nextLine();
            
            //if statement to evaluate employee's choice
            if (inputChoice.equals("1")){ //if choice is 1, ask for employee number the runs then moves to the "printEmpDetails" method.
                System.out.print("Enter your Employee Number: ");
                
                    String inputEmpId = sc.nextLine().toLowerCase();          
                    printEmpDetails (empFile, inputEmpId);
            }
            else if(inputChoice.equals("2")){ //else if choice is 2, returns to the main method to terminate the program
                return;
            }
            else //error response if invalid choice is inputed
                System.out.println("Invalid Choice");
                System.out.println("    ");
        }       
        
    }
    //method that returns printed empdetails specifically ID, name and birthdate with error handling logic
    public static void printEmpDetails(String empFile, String inputEmpId){
        //initiallizing the following String variables that will be used.
        String id="";
        String lastName="";
        String firstName="";
        String birthDate="";
        try { //use BufferedReader to read through the employee CSV file
            BufferedReader br = new BufferedReader(new FileReader(empFile)); 
            String line;
            
            while((line = br.readLine()) != null) { 
                String[] data = line.split(",");// create an array called data from the line read split by comma.
                if (data[0].equals(inputEmpId)){//if condition so that the method below will work once input data equals inputEmpId.
                    id = data[0];//declare the id in the first position of the data array.
                    lastName = data[1];
                    firstName = data[2];
                    birthDate = data[3];

                    System.out.println("============================");
                    System.out.println("Employee #: "+ id);
                    System.out.println("Employee Name: "+lastName+", "+firstName);
                    System.out.println("Birthday: "+ birthDate);
                    System.out.println("============================");
                return;// Return immediately after finding the employee
                }
            }
            br.close();
        } 
        catch (FileNotFoundException ex) {
            System.getLogger(PayrollSystem.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } 
        catch (IOException ex) {
            System.getLogger(PayrollSystem.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        if (!inputEmpId.equals(id)) {
            System.out.println("Employee does not exit.");
        }
          
    }
    // a method that returns printed responses base on input choices of the payroll staff
    public static void runPayroll(Scanner sc,String attendaceFile, String empFile){
        boolean payrollIsRunning = false;
        while (!payrollIsRunning) { //display the payroll staff menu and the choices for the payroll staff
            System.out.println("\n============================================");
            System.out.println("        PAYROLL STAFF MENU");
            System.out.println("============================================");
            System.out.println("  [1] Process Payroll");
            System.out.println("  [2] Exit");
            System.out.print("Enter your choice (1-2): "); // print the following message at the beggining of the while loop to present payroll staff with their options.
            String choice = sc.nextLine().trim();

            if (choice.equals("1")) {//if 1 is chosen, run the runProcessPayroll method.
                runProcessPayroll(empFile,attendaceFile, sc);
            } 
            else if (choice.equals("2")) {//if 2 is chosen, print program terminated then returns to the main method to terminate the program.
                System.out.println("Program terminated.");
                return;
            } 
            else {//error handling notification then moves back to the beginning of the loop.
                System.out.println("Invalid choice. Please enter 1-2.");
            }
        }
    }
    //a method that returns printed responses base on input choices of the payroll staff, which is called within the runPayroll method.
    public static void runProcessPayroll(String empFile, String attendaceFile, Scanner sc){
            while (true) {
                System.out.println("\n--- Process Payroll ---");
                System.out.println("  [1] One employee");
                System.out.println("  [2] All employees");
                System.out.println("  [3] exit");
                System.out.print("Choice: "); // print the following message at the beggining of the while loop to present payroll staff with their options.
                String choice = sc.nextLine().trim();

                if (choice.equals("1")) {//if 1 is chosen,to print one employee payslip details, ask for employee id number
                    System.out.print("Enter Employee Number: ");
                    String inputEmpId = sc.nextLine().trim();
                    printEmpDetails(empFile,inputEmpId);//after inputing the id number, moves to the printEmpDetails method.
                    printWorkedDetails(attendaceFile,empFile, inputEmpId);//after printing from the printEmpDetails, run the printWorkedDetails method.
                } 
                else if (choice.equals("2")) {
                    //if 2 is chosen, print all employee details
                        BufferedReader br;
                    try {
                        br = new BufferedReader(new FileReader(empFile));//use BufferedReader to read through the emp file
                        String line;
                        while ((line = br.readLine()) != null) {
                            if (line.trim().isEmpty()) continue;
                            String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                            String inputEmpId = data[0];//store employee id in the data array
                            if (!inputEmpId.equals("Employee #")){//set a conditional statement for it to skip the string "Employee #" written on the first column of the emp csv file.
                            printEmpDetails(empFile,inputEmpId);//run printEmpDetails to print employees details.
                            printWorkedDetails(attendaceFile,empFile, inputEmpId);//run printWorkedDetails to print payslip details.
                            }
                        }
                        br.close();
                    } catch (FileNotFoundException ex) {
                        System.getLogger(PayrollSystem.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    } catch (IOException ex) {
                        System.getLogger(PayrollSystem.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
                }
                else if (choice.equals("3")) {//if 3 is chosen, terminate the program.
                    System.exit(0);
                } 
                else {//error handling notification then moves back to the beginning of the while loop.
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            }
    }
    //a method that returns printed payslip details which is called within the runProcessPayroll Method.
    public static void printWorkedDetails(String attendaceFile,String empFile, String inputEmpId){
        String id ="";//initialize the following variables as they will be needed for calculations in the method.
        double basicSalary = 0;
        double hourlyRate = 0;
        

        try {
            BufferedReader br = new BufferedReader(new FileReader(empFile));//read through the csv empfile to extract the variable needed.
            String line;

            while((line = br.readLine()) != null) {  //use BufferedReader to read through the employee CSV file
                String[] data = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");// create an array called data from the line read split by this regex (",(?=([^\"]*\"[^\"]*\")*[^\"]*$)") to split a string by commas that are outside of double quotation marks.
                if (data[0].equals(inputEmpId)){//if condition so that the method below will work once input data equals inputEmpId.
                    id = data[0];
                    basicSalary = Double.parseDouble(data[13].replace("\"", "").replace(",", ""));
                    hourlyRate = Double.parseDouble(data[18]);
                break;
                }
            }
        } catch (FileNotFoundException ex) {
            System.getLogger(PayrollSystem.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } catch (IOException ex) {
            System.getLogger(PayrollSystem.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        if (!inputEmpId.equals(id)) {
            return;
        }
    
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
        for (int month = 6; month <= 12; month++) {
            double firstHalf = 0;
            double secondHalf = 0;
            int daysInMonth = YearMonth.of(2024, month).lengthOfMonth();
        try {//use BufferedReader to read through the employee CSV file
            BufferedReader br = new BufferedReader(new FileReader(attendaceFile));
            String line;
            
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",");

                if (!data[0].equals(inputEmpId)) continue;

                String[] dateParts = data[3].split("/");
                int recordMonth = Integer.parseInt(dateParts[0]);
                int day = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);

                if (year != 2024 || recordMonth != month) 
                    continue;

                LocalTime login = LocalTime.parse(data[4].trim(), timeFormat);
                LocalTime logout = LocalTime.parse(data[5].trim(), timeFormat);

                double hours = computeHours(login, logout);

                if (day <= 15) firstHalf += hours;
                else secondHalf += hours;
                }
            br.close();
        } 
        catch (FileNotFoundException ex) {
            System.getLogger(PayrollSystem.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        } 
        catch (IOException ex) {
            System.getLogger(PayrollSystem.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
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
            //monrhlt GrossIncome is equal to sum of firstHalf hours and secondhalf hours multiplied by the hourldRate.
            double monthlyGrossIncome = (firstHalf+secondHalf)*hourlyRate;
            //taxable Income is equal to the monthly gross income deducted by sss, pagibig and half of the philhealth declared deduction.
            double taxableIncome = monthlyGrossIncome - calculateSSSDeduction(monthlyGrossIncome) -(computePhilHealth(basicSalary)/2)-computePagibigEmployee(basicSalary);
            //secondHalf net salary is equal to the gross of the second half minus withholding tax and deductions
            double secondHalfnetSalary = (secondHalf*hourlyRate) - computeWithholdingTax(taxableIncome)- calculateSSSDeduction(monthlyGrossIncome) -(computePhilHealth(basicSalary)/2)-computePagibigEmployee(basicSalary);
            System.out.println("========="+monthName+"=========");
            System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
            System.out.println("Total Hours Worked : " + firstHalf);
            System.out.println("Gross Salary: "+ firstHalf*hourlyRate);
            System.out.println("Net Salary: "+firstHalf*hourlyRate);
            System.out.println("=========================");

            System.out.println("\nCutoff Date: " + monthName + " 16 to " + daysInMonth);
            System.out.println("Total Hours Worked : " + secondHalf);
            System.out.println("Gross Salary: "+secondHalf*hourlyRate);
            System.out.println("Deductions: ");
            System.out.println("    SSS: "+calculateSSSDeduction(monthlyGrossIncome));
            System.out.println("    PhilHealth: "+computePhilHealth(basicSalary)/2);
            System.out.println("    Pag-IBIG: "+computePagibigEmployee(basicSalary));
            System.out.println("    Tax: "+computeWithholdingTax(taxableIncome));
            System.out.println("    Total Deductions: " + (computeWithholdingTax(taxableIncome)+ calculateSSSDeduction(monthlyGrossIncome) +(computePhilHealth(basicSalary)/2)+computePagibigEmployee(basicSalary)));
            System.out.println("Net Salary: "+secondHalfnetSalary);
            System.out.println("=========================");
        }
       
    }
    //method to computeHours worked
    static double computeHours(LocalTime login, LocalTime logout) {
        //Lets set 8:10 as the grace time as per the motorPH requirements
        LocalTime graceTime = LocalTime.of(8, 10);
        //Lets set 17:00 as the cut off time as per the motorPH requirements
        LocalTime cutoffTime = LocalTime.of(17, 0);
        //Lets set 8:00 as the official start time for any logins before the grace time.
        LocalTime startTime = LocalTime.of(8,0);

        // Apply 17:00 cutoff
        if (logout.isAfter(cutoffTime)) {
            logout = cutoffTime;
        }
        //Set 8:00 as the official start if login is not after grace time.
        if (!login.isAfter(graceTime)) {
            login = startTime;
        }
        
        long minutesWorked = Duration.between(login, logout).toMinutes();

        if (minutesWorked > 60) {
            minutesWorked -= 60;
        } else {
            minutesWorked = 0;
        }
        double hours = minutesWorked / 60.0;
        // grace period
        return Math.min(hours, 8.0);
    }   
    //method to calculateSSSDeduction
    public static double calculateSSSDeduction(double monthlyGrossIncome) {
        //declare variable for initial contribution on the top most portion of the sss bracket for those grossincome bellow 3250.
        double initialContribution = 135;
        double increment =22.5;//each contribution is increased by 22.5 depending on their bracket position
        double increasePerSalaryRange = 500;//there is a 500 increase per bracket in the salary range.
        
        
        if (monthlyGrossIncome < 3250)//if gross is bellow 3250 will return the initial contribution as per the table
            return initialContribution;
        //declare a n variable that will store the bracket position of the gross salary using the Math.ceil method.
        //by deducting 3250 on the grossincome the dividing it by 500, we will have 
        //as long as the "monthlyGrossIncome -3250)/500" has an integer with decimal value, it means it is inside the bracket of its celing value, hence ceiling is used
        double n =Math.ceil((monthlyGrossIncome -3250)/500);
        //to get the SSS deduction, multiply the increment by its number bracket then add to the initial contribution, but the value must be cap at 1125 as per the sss table.
        return Math.min(initialContribution + increment*n, 1125);
    }
    //method to computePhilHealth deduction
    static double computePhilHealth(double basicSalary) {
        if(basicSalary <= 10000) {
            return 300;
        }
        else if (basicSalary <= 59999.99) {
            return basicSalary*0.03;
        }
        else {
            return 1800;
        }
    }
    //method to computePagibigd deduction
    static double computePagibigEmployee(double basicSalary){
        if (basicSalary < 1500){
            return basicSalary*0.01;
        }
        else {
        return Math.min(basicSalary*0.02, 100.00);
        }
    }
    //method to compute tax deduction
    static double computeWithholdingTax(double taxableIncome) {
        if      (taxableIncome < 20833)  
            return 0;
        else if (taxableIncome < 33333)  
            return (taxableIncome - 20833) * 0.20;
        else if (taxableIncome < 66667)  
            return 2500 + ((taxableIncome - 33333)* 0.25);
        else if (taxableIncome < 1666667) 
            return 10833 + ((taxableIncome - 66667)* 0.30);
        else if (taxableIncome < 6666667) 
            return 40833.33 + ((taxableIncome - 166667)* 0.32);
        else                        
            return 200833.33 + ((taxableIncome - 666667)* 0.35);
    }  
    
}