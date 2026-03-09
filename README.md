# MO-IT101-Group2
MotorPH Payroll System

Team Details and Contribution:
Brixter Colipano
  -Corrected and finalized the code to match Milestone 2 submission. Created the repository for everyone, lead the group and facilated task for each members. Checked each members task and corrected and revised the whole project plan.

Gerald Labini
  -Revised Milestone 1 output to correct and match for Milestone 2 submission. Created codes that can be integrated to the main code and edited the README file.

Rachelda Honrado
  -Created codes for early basis of the code that can be integrated to the main code.

Angeline Visitacion
  -Project timeline and project plan facilator


Program Details:
Here's how the MotorPH Payroll System works:

Authentication
-The program starts with a login screen that accepts two roles:
-Employee (view personal info) or payroll_staff (process payroll).
-Both use the hardcoded password 12345. Invalid credentials terminate the program immediately.

Employee Role
-Employees get a simple menu to look up their own record by employee number.
-It reads the employee CSV and prints their ID, full name, and birthdate.
-Nothing else is accessible to this role.

Payroll Staff Role
-Payroll staff can process payroll either for a single employee (by ID) or for all employees (IDs 10001–10034 in sequence).
For each employee, it prints basic details then runs the full payroll calculation.

Payroll Calculation Logic
-For each employee, the system loops through months June–December 2024 and splits each month into two cutoff periods: the 1st–15th and 16th–end of month.

Hours computation follows these rules per day:
-Login after 8:10 AM loses the grace period and counts actual time
-Logout is capped at 5:00 PM (no overtime)
-A 1-hour lunch break is deducted if worked more than 60 minutes
-Maximum counted hours per day is 8

Gross income = total hours worked × hourly rate
Deductions (applied on the second half only):

SSS — tiered contribution based on gross income, capped at ₱1,125
PhilHealth — 3% of basic salary, floored at ₱300 and capped at ₱1,800
Pag-IBIG — 2% of basic salary, capped at ₱100
Withholding Tax — progressive bracket tax on taxable income (gross minus SSS, half of PhilHealth, and Pag-IBIG)

First half payout = gross for days 1–15 with no deductions applied yet
Second half payout = gross for days 16–end, minus all deductions, plus non-taxable allowances (rice subsidy, phone, clothing)
The results are printed to the console per cutoff period, showing hours worked, gross salary, itemized deductions, and net salary.
