package com.example.fx;

public class Session {

    private static Member currentMember;

    public static void setMember(Member member) {
        currentMember = member;
    }
    public static Member getMember() {
        return currentMember;
    }
}
