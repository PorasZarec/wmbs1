package billings;

import java.util.Date;

public class billing {
    private int id;
    private String code;
    private String firstname;
    private String lastname;
    
       private Date bill_date;
    private Date pay_date;
    private Date due_date;
        private double r_amount;
    private double amount;
    private String status;

    public billing(int id, String code, String firstname, String lastname, Date bill_date, Date pay_date, Date due_date, double amount, double r_amount, String status) {
        this.id = id;
        this.code = code;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bill_date = bill_date;
        this.pay_date = pay_date;
        this.due_date = due_date;
        this.amount = amount;
        this.r_amount=r_amount;
        this.status = status;
    }

    billing(int id, String code, String string, Date bill_date, Date pay_date, Date due_date, double amount,  String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    billing(int id, String code, String string, Date pay_date, Date due_date, double total,  String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    billing(int id, String code, String string, Date bill_date, Date pay_date, Date due_date, double amount, double r_amount, String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return firstname + " " + lastname;
    }
    public Date getBillDate() {
        return bill_date;
    }
    public Date getPayDate() {
        return pay_date;
    }

    public Date getDueDate() {
        return due_date;
    }

    public double getAmount() {
        return amount;
    }
   public double getR_Amount() {
        return r_amount;
    }
    public String getStatus() {
        return status;
    }

    public String getCodeName() {
        return code + " - " + firstname + " " + lastname;
    }
}
