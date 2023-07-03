/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billings;


import java.util.ArrayList;
import java.util.Date;

public class billhis {
    private int id;
    private String client_name;
    private Date bill_date;
    private Date payment_date;
    private Date due_date;
    private double amount;
    private String status;

    public billhis(int id, String firstname, String lastname, Date bill_date, Date payment_date, Date due_date, double amount, String status) {
        this.id = id;
        this.client_name = String.format("%s, %s", lastname, firstname);
        this.bill_date=bill_date;
        this.payment_date = payment_date;
        this.due_date = due_date;
        this.amount = amount;
        this.status = status;
    }

    billhis(int id, String client_name, Date bill_date, Date payment_date, Date due_date, double amount, String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    public int getId() {
        return id;
    }

    public String getClientName() {
        return client_name;
    }
    public Date getBillDate() {
        return bill_date;
    }
    public Date getPaymentDate() {
        return payment_date;
    }

    public Date getDueDate() {
        return due_date;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
}