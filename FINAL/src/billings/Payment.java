/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package billings;
import AdminLogin.*;
import wbms1.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.text.DateFormat;

    /**
    * Author Patrick
    */

public final class Payment extends javax.swing.JFrame {
    
    private static String idVariable;
    private static String nameVariable;
    private static String dateFromp;
    private static String dateTop;
    private BufferedImage image;
    private static String client_name;
    
    int billId = Integer.parseInt(idVariable);
    
      public static void payFunction(String id, String firstName, String dateFrom, String dateTo) {
          
        Payment.idVariable = id;
        Payment.nameVariable = firstName;
        Payment.dateFromp = dateFrom;
        Payment.dateTop = dateTo;
}
    
    public Payment() {
        
        initComponents();
        // Set up date and time display
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        
        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String formattedDate = dateFormat.format(new Date());
                datetime.setText(formattedDate);
            }
        });
        
        
        displayData();
        timer.start();
        
        addComponentListener(new ComponentAdapter() {
        @Override
        public void componentShown(ComponentEvent e) {          
        }
    });
        
        Object R_ID = idVariable;
        Object R_BillDate = dateFromp;
        Object R_PaymentDate = dateTop;
        Object R_ClientName = nameVariable;
        
        String receipt_ID = String.valueOf(R_ID);
        String receipt_BillDate = String.valueOf(R_BillDate);
        String receipt_PaymentDate = String.valueOf(R_PaymentDate);
        String receipt_ClientName = String.valueOf(R_ClientName);
        
        receipt.payFunction(receipt_ID, receipt_BillDate, receipt_PaymentDate, receipt_ClientName);
    }
    
    Connection con;
    
    public void displayData(){
        
        accNum.setText(idVariable);
        clientName.setText(nameVariable);
        balance.setText("180.0");
        
        
    }
    
    private void activityPayBillIncrement() {
    try {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");

        // Select the maximum ID
        String getMaxIdQuery = "SELECT MAX(id) FROM administrator_activity";
        PreparedStatement getMaxIdStatement = con.prepareStatement(getMaxIdQuery);
        ResultSet maxIdResultSet = getMaxIdStatement.executeQuery();

        int maxId = 0;
        if (maxIdResultSet.next()) {
            maxId = maxIdResultSet.getInt(1);
        }

        maxIdResultSet.close();
        getMaxIdStatement.close();

        // Increment the add_clients_act column by 1 for the row with the maximum ID
        String incrementQuery = "UPDATE administrator_activity SET pay_bill_act = pay_bill_act + 1 WHERE id = ?";
        PreparedStatement incrementStatement = con.prepareStatement(incrementQuery);
        incrementStatement.setInt(1, maxId);
        incrementStatement.executeUpdate();
        incrementStatement.close();

        con.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to increment pay_bill_act column!");
    }
}
    

    
 
    
    
    
   

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        color16 = new GradientPanel.color1();
        clients = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btn_back = new button.MyButton();
        balance = new javax.swing.JTextField();
        payAmount = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        datetime = new javax.swing.JLabel();
        clientName = new javax.swing.JLabel();
        accNum = new javax.swing.JLabel();
        btn_pay1 = new button.MyButton();

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/logo (2).png"))); // NOI18N
        jLabel3.setText("P1RWW");

        clients.setFont(new java.awt.Font("SansSerif", 1, 40)); // NOI18N
        clients.setForeground(new java.awt.Color(255, 255, 255));
        clients.setText("0");

        jLabel8.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/customer (1) (1).png"))); // NOI18N
        jLabel8.setText("Total Clients");

        javax.swing.GroupLayout color16Layout = new javax.swing.GroupLayout(color16);
        color16.setLayout(color16Layout);
        color16Layout.setHorizontalGroup(
            color16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color16Layout.createSequentialGroup()
                .addGap(24, 359, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addGap(22, 22, 22))
            .addGroup(color16Layout.createSequentialGroup()
                .addGroup(color16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(color16Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel8))
                    .addGroup(color16Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(clients)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        color16Layout.setVerticalGroup(
            color16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color16Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addGap(15, 15, 15)
                .addComponent(clients)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btn_back.setForeground(new java.awt.Color(51, 51, 51));
        btn_back.setText("BACK");
        btn_back.setBorderColor(new java.awt.Color(0, 0, 0));
        btn_back.setColor(new java.awt.Color(51, 204, 255));
        btn_back.setColorClick(new java.awt.Color(255, 0, 0));
        btn_back.setColorOver(new java.awt.Color(0, 255, 255));
        btn_back.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        btn_back.setRadius(50);
        btn_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_backActionPerformed(evt);
            }
        });

        balance.setBackground(new java.awt.Color(255, 255, 255));
        balance.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        balance.setForeground(new java.awt.Color(0, 0, 0));

        payAmount.setBackground(new java.awt.Color(255, 255, 255));
        payAmount.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        payAmount.setForeground(new java.awt.Color(0, 0, 0));

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 20)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Account Number :");

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Amount to pay :");

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 20)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText(" Client Name  :");

        jLabel9.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 20)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Payment :");

        datetime.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        datetime.setForeground(new java.awt.Color(0, 0, 0));
        datetime.setText("Month/Day/Year");

        clientName.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 18)); // NOI18N
        clientName.setForeground(new java.awt.Color(0, 0, 0));
        clientName.setText("Account Number :");

        accNum.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 18)); // NOI18N
        accNum.setForeground(new java.awt.Color(0, 0, 0));
        accNum.setText("Account Number :");

        btn_pay1.setForeground(new java.awt.Color(51, 51, 51));
        btn_pay1.setText("PAY");
        btn_pay1.setBorderColor(new java.awt.Color(0, 0, 0));
        btn_pay1.setColor(new java.awt.Color(51, 204, 255));
        btn_pay1.setColorClick(new java.awt.Color(255, 0, 0));
        btn_pay1.setColorOver(new java.awt.Color(0, 255, 255));
        btn_pay1.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        btn_pay1.setRadius(50);
        btn_pay1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pay1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel9)
                                    .addComponent(btn_pay1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel4)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(clientName)
                            .addComponent(accNum)
                            .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(balance, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(payAmount))
                        .addGap(0, 25, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(datetime, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accNum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clientName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(payAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(balance, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_back, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_pay1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addComponent(datetime)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_backActionPerformed
        billpayment bp = new billpayment();
        bp.show();
        dispose();
    }//GEN-LAST:event_btn_backActionPerformed

    private void btn_pay1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pay1ActionPerformed
                String amount = payAmount.getText();
        double rAmount = Double.parseDouble(amount);

        if (rAmount < 0) {
            
            JOptionPane.showMessageDialog(this, "Payment amount must be at least 180");
            
        } else {
        // get current date
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date paymentDate = new Date();

        try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
        String sql = "INSERT INTO payments (bill_id, r_amount, payment_date) VALUES (?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, billId);
        stmt.setDouble(2, rAmount);
        stmt.setString(3, dateFormat.format(paymentDate));
        stmt.executeUpdate();
        // update status to paid in billing list
        sql = "UPDATE billing_list SET status = 'Paid' WHERE id = ?";
        stmt = con.prepareStatement(sql);
        stmt.setInt(1, billId);
        stmt.executeUpdate();

        // admin activity
        activityPayBillIncrement();

        JOptionPane.showMessageDialog(this, "Payment successful.");
        receipt r = new receipt();
        r.show();
        } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }    
        }

        
    }//GEN-LAST:event_btn_pay1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Payment().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel accNum;
    private javax.swing.JTextField balance;
    private button.MyButton btn_back;
    private button.MyButton btn_pay1;
    private javax.swing.JLabel clientName;
    private javax.swing.JLabel clients;
    private GradientPanel.color1 color16;
    private javax.swing.JLabel datetime;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField payAmount;
    // End of variables declaration//GEN-END:variables
}
