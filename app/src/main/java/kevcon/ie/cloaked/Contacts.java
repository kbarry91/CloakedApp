package kevcon.ie.cloaked;

import java.io.Serializable;

/**
 * <h1>Contacts</h1>
 * This class is used to get & set details about each contact.
 * It contains variables, constructors, getters & setters, along with
 * a toString method.
 *
 * @author Conor Raftery
 * @since 08/03/2018
 */
public class Contacts implements Serializable{

    //Member Variables
    private String mName;
    private String mNumber;
    private String key;
    private boolean isKeySet;

    //Constructor
    public Contacts(String name, String number, String key, boolean isKeySet) {
        this.mName = name;
        this.mNumber = number;
        this.isKeySet = isKeySet;
        this.key = key;
    }

    //Constructor
    public Contacts(String name, String number) {
        this.mName = name;
        this.mNumber = number;
        this.isKeySet = false;
        this.key = null;
    }

    //
    //ToString, Getters & Setters methods below here
    //

    public String getName() {

        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getNumber() {

        return mNumber;
    }

    public void setNumber(String number) {

        this.mNumber = number;
    }

    public void setKeySet(boolean isKeySet){
        this.isKeySet = isKeySet;
    }

    public boolean getKeySet(){
        return isKeySet;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "Contacts{" +
                "mName='" + mName + '\'' +
                ", mNumber='" + mNumber + '\'' +
                ", key='" + key + '\'' +
                ", isKeySet=" + isKeySet +
                '}';
    }
}