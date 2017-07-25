package dev.sanskar.com.notes;

/**
 * Created by Sanskar on 4/14/2017.
 */
public class NoteModel {


    String createdAt;
    String thenote;
    String userMobile;

    public NoteModel(String createdAt, String thenote, String userMobile) {
        this.thenote = thenote;
        this.createdAt = createdAt;
        this.userMobile=userMobile;
    }

    public NoteModel(){

    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getThenote() {
        return thenote;
    }

    public void setThenote(String thenote) {
        this.thenote = thenote;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
