package kevcon.ie.cloaked;

/**
 * Created by Intel Build on 09/03/2018.
 */
/*
Represents a Message
 */
public class Message {
    private String message;
    private String sender;
    private String body;
    private int type;

    //null constructor
    public Message() {

    }

    public Message(String message, String sender, String body, int type) {

        this.message = message;
        this.sender = sender;
        this.body = body;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}

