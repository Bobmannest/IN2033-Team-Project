package com.example.members;

import java.time.LocalDateTime;

public class CommercialApplication {
    private String applicationId;
    private String companyName;
    private String companiesHouseNo;
    private String directorName;
    private String businessType;
    private String address;
    private String email;
    private LocalDateTime submittedAt;
    private String status;

    public CommercialApplication(String applicationId, String companyName, String companiesHouseNo,
                                 String directorName, String businessType, String address,
                                 String email, LocalDateTime submittedAt, String status) {
        this.applicationId = applicationId;
        this.companyName = companyName;
        this.companiesHouseNo = companiesHouseNo;
        this.directorName = directorName;
        this.businessType = businessType;
        this.address = address;
        this.email = email;
        this.submittedAt = submittedAt;
        this.status = status;
    }

    public String getApplicationId() { return applicationId; }
    public String getCompanyName() { return companyName; }
    public String getCompaniesHouseNo() { return companiesHouseNo; }
    public String getDirectorName() { return directorName; }
    public String getBusinessType() { return businessType; }
    public String getAddress() { return address; }
    public String getEmail() { return email; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public String getStatus() { return status; }
}