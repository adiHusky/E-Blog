package in.org.eonline.eblog.Models;

public class UserModel {

    public String userId;
    public String userFName;
    public String userLName;
    public String userImageUrl;
    public String userEmail;
    public String userContact;
    public String userReadBlogsId;
    public String isLikeTrue;
    public String userButtonValue;

    public String getUserButtonValue() {
        return userButtonValue;
    }

    public void setUserButtonValue(String userButtonValue) {
        this.userButtonValue = userButtonValue;
    }




    public String getLikeTrue() {
        return isLikeTrue;
    }

    public void  setLikeTrue(String likeTrue) {
        this.isLikeTrue = likeTrue;
    }



    public String getUserReadBlogsId() {
        return userReadBlogsId;
    }

    public void setUserReadBlogsId(String userReadBlogsId) {
        this.userReadBlogsId = userReadBlogsId;
    }




    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFName() {
        return userFName;
    }

    public void setUserFName(String userFName) {
        this.userFName = userFName;
    }

    public String getUserLName() {
        return userLName;
    }

    public void setUserLName(String userLName) {
        this.userLName = userLName;
    }

    public String getUserImage() {
        return userImageUrl;
    }

    public void setUserImage(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

}
