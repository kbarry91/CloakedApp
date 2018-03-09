package kevcon.ie.cloaked;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Intel Build on 09/03/2018.
 */

public class Message {
    public String message;
    FirebaseUser sender;
    long createdAt;

    //null constructor
    public Message() {

    }

    public Message(String message, FirebaseUser sender, long createdAt) {
        this.message = message;
        this.sender = sender;
        this.createdAt = createdAt;
    }
    // getter and setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FirebaseUser getSender() {
        return sender;
    }

    public void setSender(FirebaseUser sender) {
        this.sender = sender;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}

