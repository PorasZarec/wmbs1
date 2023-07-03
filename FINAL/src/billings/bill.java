package billings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

public class bill {
    private int id;
    private int client_id;
    private Date bill_date;
    private Date payment_date;
    private Date pay_date;
    private Date due_date;
   private double amount;
    private double r_amount;
    private String status;

    public bill(int id, int client_id, Date bill_date, Date payment_date, Date pay_date, Date due_date , double amount, double r_amount, String status) {
        this.id = id;
        this.client_id = client_id;
        this.bill_date = bill_date;
        this.payment_date = payment_date;
        this.pay_date = pay_date;
        this.due_date = due_date;
        this.amount = amount;
        this.r_amount = r_amount;
        this.status = status;
        
        
    }
public bill(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.client_id = rs.getInt("client_id");
        this.bill_date = rs.getDate("bill_date");
        this.payment_date = rs.getDate("payment_date");
        this.pay_date = rs.getDate("pay_date");
        this.due_date = rs.getDate("due_date");
        this.amount =  rs.getDouble("amount");
        this.r_amount = rs.getDouble("r_amount");
        this.status = rs.getString("status");
    }

    bill(int aInt, int aInt0, LocalDate object, LocalDate object0, LocalDate object1, LocalDate object2, double aDouble, double aDouble0, boolean aBoolean) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public int getId() {
        return id;
    }

    public int getClientId() {
        return client_id;
    }
      public Date getBillDate() {
    return bill_date;
}

public Date getPaymentDate() {
    return payment_date;
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
   public double getR_amount() {
    return r_amount;
}

    public String getStatus() {
        return status;
    }

   Object getbill_date() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
}

Object getpayment_date() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
}

Object getpay_date() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
}

Object getdue_date() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
}
Object getamount() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
}

Object getr_amount() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
}


    Object getstatus() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    Object getid() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
