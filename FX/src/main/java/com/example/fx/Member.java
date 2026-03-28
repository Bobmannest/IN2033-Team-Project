package com.example.fx;

public class Member {
    private String accountNo;
    private String email;
    private String pass;
    private String memberType;
    private boolean isFirstLogin;
    private int orderCount;

    public Member(String accountNo, String email, String pass, String memberType, boolean isFirstLogin, int orderCount) {
        this.accountNo = accountNo;
        this.email = email;
        this.pass = pass;
        this.memberType = memberType;
        this.isFirstLogin = isFirstLogin;
        this.orderCount = orderCount;
    }

    // getters and setters

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }
}
