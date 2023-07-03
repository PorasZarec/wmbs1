package billings;

import java.util.Date;

public class paid {
    private int id;
    private String code;
    private String firstname;
    private String lastname;
    private Date bill_date;
    private Date payment_date;
    private String status;
    
    public paid(int id, String code, String firstname, String lastname, Date bill_date, Date payment_date, String status) {
        this.id = id;
        this.code = code;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bill_date = bill_date;
        this.payment_date = payment_date;
        this.status = status;
    }

    paid(int id, String code, String firstName, String lastName, String bill_date, String payment_date, String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getId() {
        return id;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getFirstname() {
        return firstname;
    }
    
    public String getLastname() {
        return lastname;
    }
    
    public Date getBill_date() {
        return bill_date;
    }
    
    public Date getPayment_date() {
        return payment_date;
    }
    
    public String getStatus() {
        return status;
    }
}
