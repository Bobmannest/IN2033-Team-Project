# IN2033 Team Project
This is the java code for Team 33's team project.

Steps to run the program:
1) Make sure you are using Java SDK 17
2) Make sure you have JavaFX 17 installed (it will probably prompt you to auto-install anyways)
3) For the JavaFX app, run the App.java file (FX>src>main>java>com.example>fx>App.java)
4) For email service REST API, run the SpringApp.java file (FX>src>main>java>com.example>email>SpringApp.java) 

Testing:
- To test email service use in CMD: curl -X POST "http://localhost:8080/api/emails/send?recipient_email=recipient@gmail.com"
