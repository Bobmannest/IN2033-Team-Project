# IN2033 Team Project
This is the java code for Team 33's team project.

Steps to run the program:
1) Make sure you are using Java SDK 17
2) Make sure you have JavaFX 21 installed (it will probably prompt you to auto-install anyways)
3) For the JavaFX app, run login.java file (FX>src>main>java>com.example>fx>Login.java)
4) For REST APIs, run the SpringApp file (FX>src>main>java>com.example>email>SpringApp.java) or Catalogue.java (it auto starts SpringApp) 

Testing:
- To test email service use in CMD: 
- For Test email) curl -X POST "http://localhost:8080/api/emails/sendTest?recipient_email=recipient@gmail.com"
- For Purchase email)
  curl -X POST "http://localhost:8080/api/emails/sendPurchase?recipientName=John&recipientEmail=example@gmail.com&recipientAddress=address&trackId=TRK123456789"
- For Registration email) curl -X POST "http://localhost:8080/api/emails/sendTest?recipient_email=recipient@gmail.com&generated_password=generatedpass123"
- For Password Reset email) curl -X POST "http://localhost:8080/api/emails/sendPassReset?recipient_email=recipient@gmail.com&tempPass=temppassword123"

- To test Payment service run SpringApp then PaymentTester.java
- To test Catalogue service run Catalogue.java then CatalogueTester.java

Database Setup:

This project uses MySQL. Follow these steps to set up the database locally:

1) Install MySQL if you haven't already
2) Open MySQL Workbench or any MySQL client
3) Connect to localhost:3306 with your root credentials
4) Open and run the SQL file located at 'DB/ipos_pu.sql'
5) This creates the 'ipos_pu' db and all required tables.
6) Will also insert sample data including test accounts

**Database credentials used by the app:**
- Host: localhost:3306
- Database: ipos_pu
- Password: Ipos2026@

## Test Accounts

| Email | Password | Role |
|-------|----------|------|
| curatecht33+manager@gmail.com | GetPU it done | Admin |
| curatecht33+sysdba@gmail.com | masterkey | Admin |
| curatecht33+pu0001@gmail.com | 12ss_56_SS | Non-commercial |
| curatecht33+pu0002@gmail.com | 34pp_78_LL | Non-commercial |
| curatecht33+pu0003@gmail.com | changeme | Commercial |

Important notes:

- **SpringApp must be running** before:
  - Creating a new account (registration email won't send without it)
  - Using the forgot password feature (reset email won't send without it)
- Start SpringApp first: `FX > src > main > java > com.example > SpringApp.java`
- Then run the JavaFX app via Login.java