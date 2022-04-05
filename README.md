# CoLo (Corporate Logistics Workplace Management)

## Overview
Our group has decided to move forward with an Android Application. This app will allow its users to clock-in/out, be assigned projects, and have managers who can control certain aspects of the employees. The initial plan is to use a database like Firebase to hold all the user’s information and use Android Studio to develop the actual application. Languages used will most likely be Java or Kotlin depending on the needs of the application as we develop. 

Upon launching the app, the user would be met by a login page. They can then register a company and login as admin. The admin can then create manager for the comapny. Then the manager will be able to create employees for the company. After entering the user’s login and password, the application would check Firebase to see if the user is an admin, employee or manager. Depending on the status of the login the user will be sent to their respected hub page. Displayed on this page would be buttons to navigate to a Clock-in page, a project page, an announcement page and settings. Managers and Admins can view Employees of the company and assign them projects. 

---

### USE CASES FOR OUR APPLICATION:
#### From CoLo App Domain:
####  1
- Name: Setup company (Nguyen and Austin)
-  Scope: CoLo (Corporate Logistics) App
- Level: Admin goal
- Primary Actor: Admin
- Stakeholders and Interests:
	- Company: Wants to set up the app to be used by its Managers and Employees.
	- Admin: Wants to register the company quickly to later set up CoLo for Managers
and Employees.
	- Managers: Wants to be able to accurately track hours worked and collaborate
with employees.
- Preconditions:
	- Company is not yet registered
	- Admin has downloaded CoLo
	- Managers and employees to be added to the app
- Postconditions:
- Company is saved to DB
- Admin account is created
- Admin is given privileges to oversee company app operations
- Manager account(s) is/are created
- Main Success Scenario:
	1) Admin opens CoLo app and proceeds to registration
	2) Admin enters company information and their own account information
	3) System stores admin and company information, generates account, and presents
Administrator Hub
	4) Admin registers Managers and gives them temporary password
- Extension(s):
	- Invalid data entered at any point:
		- Inform Admin at point of confirmation that a field is invalid
		- Require Admin to re-enter applicable field’s data until valid upon
confirmation
	- No response from database upon confirmation:
		- Display error message and ask to try again later or when connection is
reestablished
	- After registration, Admin tries to use other app features before creating first
manager account
		- Display message that at least one Manager account must be created first
- Man-Hours
	- Expected: 25 hours
	- Completed: 25 hours 

####  2
- Name: Register employees/manager setup (Caleb and Eduardo)
- Scope: CoLo Appt
- Level: Manager goals
- Primary Actor: Manager
- Stakeholders and Interests:
	- Manager: Wants to register Employees under them so as to manage them and
assign Projects.
	- Employee: Wants to have an account that they can then use to collaborate and
track their time with.
	- Company: Wants to organize and consolidate employees under a Manager.
- Preconditions:
	- Admin and Company have been registered
	- Admin registered Manager account
	- Manager has downloaded CoLo
- Postconditions:
	- Manager finishes account setup
	- Manager given access to view Employee hours and Project information
	- Manager given access to create and assign Projects for subordinates
	- Employee accounts are created and accessible for those Employees
- Main Success Scenario:
	1) Manager opens app and proceeds to first time login, changes temporary
password
	2) Uses Manager Hub to create Employee accounts and give them temporary
password
- Extension(s):
	- Invalid data entered at any point:
		- Inform Manager at point of confirmation that a field is invalid
		- Require Manager to re-enter applicable field’s data until valid upon
confirmation
	- No response from database upon confirmation:
		- Display error message and ask to try again later or when connection is
reestablished
	- Temporary password is forgotten or does not work:
		- Email can be sent to Manager to reset password
- Man-Hours
	- Expected: 10 hours
	- Completed: 9.5 hours (being finalized)

####  3
- Name: Employee clock-in/out (Bradley and Nirdesh)
- Scope: CoLo App
- Level: Employee goals
- Primary Actor: Employee
- Stakeholders and Interests:
	- Manager: Wants the hours tracked so that they can know their employees are
coming into work and leaving when they are supposed to.
	- Employee: Wants to be compensated appropriately for the time they worked.
	- Company: Wants to properly compensate employees for the time they worked.
- Preconditions:
	- Manager registered Employee account
	- Employee has downloaded CoLo
	- Employee has reset temporary password on first login
- Postconditions:
	- Employee hours worked information is stored and viewable
	- Manager can view Employee’s recorded hours worked.
- Main Success Scenario:
	1) Employee clocks-in at beginning of work day
	2) Employee clocks-out at end of work day
- Extension(s):
	- Employee attempts to press the clock-in and presses clock-out the incorrect time
		- Employee is prompted when clocking-in or out to confirm action
		- Only one clock-in and clock-out allowed per day
		- Admin can override the hours worked of a day
	- Employee has a break during working hours:
		- Separate option is provided upon clock-in to take a break and set the
duration of that break
	- Employee never presses clock-out
		- Automatically clocked-out by CoLo after a period of time
		- Manually clocked-out by Admin
- Man-Hours
	- Expected: 10 hours
	- Completed: 11 hours

### 4
- Name: Creating and editing Projects (Bradley and Michael)
- Scope: CoLo App
- Level: Manager goals
- Primary Actor: Manager
- Stakeholders and Interests:
	- Manager: Wants to be able to assign tasks to an individual or a group to be
completed for the Company.
	- Employee: Wants to be easily informed of work that needs to be done.
	- Company: Wants to profit from organized and quickly completed Projects.
- Preconditions:
	- Manager has a registered account and has registered Employees
- Postconditions:
	- Project is stored in the DB
	- A Project is created which can be seen by a group of, or individual, employees it
is assigned to
	- A Project is updated in a way that a Manager specifies
- Main Success Scenario:
	1) Manager creates a Project and sets attributes of the Project
	2) Manager assigns employee(s) to the Project
	3) Manager confirms Project and makes it visible to relevant people
- Extension(s):
	- Project is incorrectly entered:
		- Manager can toggle visibility of a completed Project
		- Manager can edit any attribute of the Project and it will show as updated
		- Manager can delete the Project
- Man-Hours
	- Expected: 15 hours
	- Completed: 10 hours (still being worked on)

#### 5
- Name: Working with Projects (Bradley and Michael)
- Scope: CoLo App
- Level: Employee goals
- Primary Actor: Employee
- Stakeholders and Interests:
	- Manager: Wants their Projects to be visible to relevant Employees. Wants to be
able to see how the projects are going.
	- Employee: Wants to be able to see what has been assigned to them. Wants to
be able to easily show they have been working on assigned projects.
	- Company: Wants to profit from organized and quickly completed Projects.
- Preconditions:
	- Manager has a registered account and has registered Employees
	- Employee has an assigned Project
- Postconditions:
	- Project is presented to the Employee
	- Employee applied a change to the state of a project
- Main Success Scenario:
	1) Employee reviews all currently assigned Projects
	2) Employee indicates that a Project that showed as “not started” is now in progress
	3) Employee marks a project as done when they have completed it
- Extension(s):
	- Employee miss inputs when working with a selected project
		- Reselect project being worked on
- Man-Hours
	- Expected: 10 hours
	- Completed: 10 hours 

#### 6
- Name: Creating Announcements (Caleb and Michael)
- Scope: CoLo App
- Level: Manager goals
- Primary Actor: Manager
- Stakeholders and Interests:
	- Manager: Wants to be able to inform their Employees about general changes,
notices, or announcements.
	- Employee: Wants to be able to easily check company or department-wide (the
Employees registered under a single Manager) messages.
	- Company: Wants to be able to easily inform a variety of personnel
- Preconditions:
	- Manager has a registered account
- Postconditions:
	- Announcement is stored in DB
	- Announcement is posted to relevant people in the Company
- Main Success Scenario:
	1) Manager shares an Announcement to the entire Company.
	2) Manager edits the Announcement to convey their intended message.
	3) Manager confirms their Announcement to be displayed.
- Extension(s):
	- Announcement was incorrectly format or contains the incorrect message:
		- Manager or Admin can delete the Announcement
	- Manager not allowed to send Announcement to entire Company:
		-	Admin can revoke that privilege
- Man-Hours
	- Expected: 10 hours
	- Completed: 10 hours 

#### 7
- Name: Viewing Announcements (Caleb and Michael)
- Scope: CoLo App
- Level: Employee goals
- Primary Actor: Employee
- Stakeholders and Interests:
	- Manager: Wants to be able to inform their Employees about general changes,
notices, or announcements.
	- Employee: Wants to be able to easily check company or department-wide (the
Employees registered under a single Manager) messages.
	- Company: Wants to be able to easily inform a variety of personnel
- Preconditions:
	- Employee has a registered account
	- An Announcement is posted
- Postconditions:
	- Announcement is viewed
- Main Success Scenario:
	1) Employee selects an Announcement to see further details
- Extension(s):
	- Announcement was unintentionally marked as read
		- Employee can unmark it
- Man-Hours
	- Expected: 10 hours
	- Completed: 10 hours
---
### MAIN TASKS FOR ITERATION 3
- One time pop up temporary password for managers and employees
Main Success Scenarios:
- When created a new account by admin, manager and employees get a temporary password and they should be able to change their password.
- Complete Settings page for all 3 users

Main Success Scenarios:
- Be able to reset their password
- Be able to change their email
- Request for missed clock in/out

- View Activity logs

Main Success Scenarios:
- Be able to view their clock in/out time
- be able to see past worked projects


Finish Up with the XMLs

### GENERAL FOLLOW PATTERN FOR OUR APP:
Login Page &#8594; Register a company `&#8594; Login as admin &#8594; Admin Page &#8594; Create Manager &#8594; Manager Login &#8594; Create Employees &#8594; Employees Login &#8594; Dashboard for all 3 users

----
### CURRENT SCREENSHOTS
 Login page:
 
![image](https://user-images.githubusercontent.com/98622327/161833511-2225596f-1c63-4437-ab9a-aa6a8d7f810c.png)
 
 Register a Company: 
 
 ![image](https://user-images.githubusercontent.com/98622327/161833679-7c2923d2-0442-45e4-a6b2-41ad18ca3f93.png)
 
 Employee Page:      
 
 ![image](https://user-images.githubusercontent.com/98622327/161833740-c4a51bdd-bbf6-4996-8748-ef1f0c6ae2fa.png)
 
 Clock In/Out :      
 
![image](https://user-images.githubusercontent.com/98622327/161833859-a434e852-9797-4f67-91ee-4300392e3708.png)
 
 Announcement:      
 
![image](https://user-images.githubusercontent.com/98622327/161834046-d2355122-d01d-4273-ba6e-4619a50227d7.png)
 
 Manager page:    
 
![image](https://user-images.githubusercontent.com/98622327/161834287-7a72e111-b3eb-4741-8bd9-92e78d6491d5.png)
 
 Projects page:   
 
![image](https://user-images.githubusercontent.com/98622327/161834343-974443b6-3828-49f3-b968-4b063c3164e8.png)
 
 Create Announcements:
 
![image](https://user-images.githubusercontent.com/98622327/161834404-85195673-2875-4fd5-baa1-6ced37d0e53b.png)
 
 Create Employees:    
 	
![image](https://user-images.githubusercontent.com/98622327/161834479-08ff40fb-469f-45d6-9324-e6844a889d89.png)
 
 Admin Page:          
 
![image](https://user-images.githubusercontent.com/98622327/161834650-f71055fb-6310-4e90-9417-ab875ef501c7.png)
 
 --------

