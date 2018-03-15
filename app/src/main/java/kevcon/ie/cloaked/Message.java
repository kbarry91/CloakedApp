package kevcon.ie.cloaked;

/**
 * <h1>Message</h1>
 * The Message class represents a sms message
 *
 * @author kevin barry
 * @since 15/3/18
 */
public class Message {
    private String message;
    private String sender;
    private String time;
    private int type;

    //null constructor
    public Message() {

    }

    public Message(String message, String sender, String time, int type) {

        this.message = message;
        this.sender = sender;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "M:" + message + " S: " + sender + " T: " + type + " D: " + time;
    }
}

