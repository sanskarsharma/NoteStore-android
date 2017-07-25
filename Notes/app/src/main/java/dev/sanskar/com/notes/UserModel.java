package dev.sanskar.com.notes;

/**
 * Created by Sanskar on 4/15/2017.
 */
public class UserModel {

    String mobileNumber;
    String password;
    String name;

    public UserModel(String mobileNumber, String name, String password) {
        this.mobileNumber = mobileNumber;
        this.name = name;
        this.password = password;
    }
    public UserModel(){

    }

    public UserModel(String mobileNumber, String password) {
        this.mobileNumber = mobileNumber;
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
