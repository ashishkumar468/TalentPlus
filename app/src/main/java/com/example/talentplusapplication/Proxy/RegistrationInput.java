package com.example.talentplusapplication.Proxy;

public class RegistrationInput {

    private  String createdBy ="";
    private  String createdOn="";
    private  String updatedBy="";
    private  String updatedOn="";
    private  String delFlag="";
    private  String userId="";
    private  String userName="";
    private  String role="";
    private  String fName="";
    private  String lName="";
    private  String dob="";
    private  String gender="";
    private  String password;
    private  String contactNo;
    private  String emailId;
    private  String oldPassword;
    private  String newPassword;

    public RegistrationInput( String userName, String fName, String lName, String password, String contactNo, String emailId, String oldPassword, String newPassword) {

        this.userName = userName;
        this.fName = fName;
        this.lName = lName;
        this.password = password;
        this.contactNo = contactNo;
        this.emailId = emailId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "RegistrationInput{" +
                "createdBy='" + createdBy + '\'' +
                ", createdOn='" + createdOn + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedOn='" + updatedOn + '\'' +
                ", delFlag='" + delFlag + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", role='" + role + '\'' +
                ", fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", password='" + password + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", emailId='" + emailId + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getRole() {
        return role;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
