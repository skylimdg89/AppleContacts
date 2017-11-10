package edu.mnstate.mg0483fp.applecontacts;

/**
 * Created by dongkyulim on 11/2/17.
 */

public class User {
    private String firstName, lastName, address, phone, email;

    /**
     *  constructor with no parameters
     */
    public User(){
        this.firstName = null;
        this.lastName = null;
        this.address = null;
        this.phone = null;
        this.email = null;
    }

    /**
     * constructor with 5 string parameters
     * @param fn
     * @param ln
     * @param ad
     * @param ph
     * @param em
     */
    public User(String fn, String ln, String ad, String ph, String em){
        this.firstName = fn;
        this.lastName = ln;
        this.address = ad;
        this.phone = ph;
        this.email = em;
    }

    /**
     *
     * @param tmp
     */
    public User(User tmp){
        this.firstName = tmp.getFirstName();
        this.lastName = tmp.getLastName();
        this.address = tmp.getAddress();
        this.phone = tmp.getPhone();
        this.email = tmp.getEmail();
    }

    /**
     *
     * @return first name
     */
    public String getFirstName(){
        return this.firstName;
    }

    /**
     *
     * @param fn first name
     */
    public void setFirstName(String fn){
        this.firstName = fn;
    }

    /**
     *
     * @return last name
     */
    public String getLastName(){
        return this.lastName;
    }

    /**
     *
     * @param ln last name
     */
    public void setLastName(String ln){
        this.lastName = ln;
    }

    /**
     *
     * @return address
     */
    public String getAddress(){
        return this.address;
    }

    /**
     *
     * @param ad address
     */
    public void setAddress(String ad){
        this.address = ad;
    }

    /**
     *
     * @return phone number
     */
    public String getPhone(){
        return this.phone;
    }

    /**
     *
     * @param ph phone  number
     */
    public void setPhone(String ph){
        this.phone = ph;
    }

    /**
     *
     * @return email address
     */
    public String getEmail(){
        return this.email;
    }

    /**
     *
     * @param em email address
     */
    public void setEmail(String em){
        this.email = em;
    }

    /**
     *
     * @param user
     * @return result String
     */
    public String toString(User user){
        String result = user.getFirstName() + ", " + user.getLastName() + ", " +
                user.getAddress() + ", '" + user.getPhone() + ", " + user.getEmail();
        return result;
    }
}
