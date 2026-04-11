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
