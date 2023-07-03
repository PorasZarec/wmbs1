/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

public class User {
    
    private int id;
    private String code;
    private String firstname;
    private String lastname;
    private String contact;
    private String address;
      private String status;


    public User(int id, String code, String firstname, String lastname, String contact, String address,String status) {
        this.id = id;
        this.code=code;
        this.firstname = firstname;
        this.lastname = lastname;
        this.contact = contact;
        this.address = address;
        this.status=status;
    }
    
    public int getId() {
        return id;
    }
    
    public String getcode(){
        return code;
    }
    
    public String getFirstname() {
        return firstname;
    }
    
    public String getLastname() {
        return lastname;
    }
    
    public String getContact() {
        return contact;
    }
    
    public String getAddress() {
        return address;
    }
  public String getStatus() {
        return status;
    }
    public String getid() {
        return Integer.toString(id);
    }

    public String firstname() {
       return firstname;
    }

    public String lastname() {
        return lastname;
    }

    public String contact() {
       return contact;
    }

    public String address() {
        return address;
    }

    Object status() {
        return status;
     
    }

    public Object getCode() {
        return code;
    }

    
}

