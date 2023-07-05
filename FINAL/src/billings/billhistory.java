/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billings;

import clients.client;
import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Khyrsean
 */
public class billhistory extends javax.swing.JFrame {

      private int client_id;
        Connection con;
   PreparedStatement pst;
    ResultSet rs;
     
       private int selectedRowIndex;
        private int clientId;  
        private String client_ID;
           private int id;


        public billhistory(int client_id ) {
            initComponents();
            this.client_id = client_id;
            Connect();
            
        displayUnpaidAmount();
            populateTable();


             TableColumn column = null;
        column = jTable1.getColumnModel().getColumn(0); // bill date
        column.setPreferredWidth(10);
        column = jTable1.getColumnModel().getColumn(1); // payment date
        column.setPreferredWidth(20);
        column = jTable1.getColumnModel().getColumn(2); // pay date
        column.setPreferredWidth(20);
        column = jTable1.getColumnModel().getColumn(3); // due  date
        column.setPreferredWidth(20);
        column = jTable1.getColumnModel().getColumn(4); // amount
        column.setPreferredWidth(20); 
        column = jTable1.getColumnModel().getColumn(5); // received amount
        column.setPreferredWidth(20); 
        column = jTable1.getColumnModel().getColumn(6); // status
        column.setPreferredWidth(20); 
        
 

jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int selectedRow = jTable1.getSelectedRow();

            if (selectedRow != -1) {
                // Get the values from the selected row
                Object selectedIDObj = jTable1.getValueAt(selectedRow, 0);
                Object selectedNameObj = jTable1.getValueAt(selectedRow, 1);
                Object selectedDateFromObj = jTable1.getValueAt(selectedRow, 2);
                Object selectedDateToObj = jTable1.getValueAt(selectedRow, 3);

                // Convert the values to strings
                String selectedID = String.valueOf(selectedIDObj);
                String selectedName = String.valueOf(selectedNameObj);
                String selectedDateFrom = String.valueOf(selectedDateFromObj);
                String selectedDateTo = String.valueOf(selectedDateToObj);
//                JOptionPane.showMessageDialog(rootPane, selectedID);
                
                Payment.payFunction(selectedID, selectedName, selectedDateFrom, selectedDateTo);
            }
        }
    }
});

        }

    private billhistory() { 
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

          public void setClientID(int client_id) {
            this.client_id = client_id;
            populateTable();
        } 
 

        public void Connect() {
            try {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
                System.out.println("Connected to database.");
            } catch (SQLException ex) {
                Logger.getLogger(billhistory.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Failed to connect to database.");
            }
        }
  public List<bill> getBills(int clientId) {
    List<bill> billList = new ArrayList<>();
    try {
        String query = "SELECT b.id, b.client_id, b.bill_date, p.payment_date, b.pay_date, b.due_date, b.amount, p.r_amount, b.status "
                + "FROM billing_list b "
                + "LEFT JOIN payments p ON b.id = p.bill_id "
                + "WHERE b.client_id = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, clientId);
        ResultSet rs = pst.executeQuery();
        while(rs.next()){
            bill billObj = new bill(
                    rs.getInt("id"),
                    rs.getInt("client_id"),
                    rs.getObject("bill_date", LocalDate.class),
                    rs.getObject("payment_date", LocalDate.class),
                    rs.getObject("pay_date", LocalDate.class),
                    rs.getObject("due_date", LocalDate.class),
                    rs.getDouble("amount"),
                    rs.getDouble("r_amount"),
                    rs.getBoolean("status")
            );
            billList.add(billObj);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return billList;
}

  
  
  public void fillBillTable(int clientId) {
    List<bill> billList = getBills(clientId);
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0);
    Object[] row = new Object[7];
    for (int i = 0; i < billList.size(); i++) {
        row[0] = billList.get(i).getBillDate();
        row[1] = billList.get(i).getPaymentDate();
        row[2] = billList.get(i).getPayDate();
        row[3] = billList.get(i).getDueDate();
        row[4] = billList.get(i).getAmount();
        row[5] = billList.get(i).getR_amount();
        row[6] = billList.get(i).getStatus().equals("Paid") ? "Paid" : "Unpaid";

        model.addRow(row);
}

  }

    
    public billhistory(String client_id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  public void populateTable() {
        try {
            if (con == null) {
                // Handle null connection object here
                return;
            }
    String getClientName = "SELECT CONCAT(code, ' -  ', firstname, ' ', lastname) AS clientName, created_at, address FROM client_list WHERE id = " + client_id;
Statement stmt1 = con.createStatement();
ResultSet rs1 = stmt1.executeQuery(getClientName);
String clientName = "";
String createdAt = "";
String addressDB = "";
if (rs1.next()) {
    clientName = rs1.getString("clientName");
    createdAt = rs1.getString("created_at");
    addressDB = rs1.getString("address");
    if (getUnpaidAmount(client_id) >= 540) {
        cname.setForeground(Color.RED); // set the client name color to red
    } else {
        cname.setForeground(Color.BLACK); // set the client name color to black
    }
}


       String sql = "SELECT b.id, b.bill_date, p.payment_date, b.pay_date, b.due_date ,b.amount, p.r_amount, b.status "
                + "FROM billing_list b "
                + "LEFT JOIN payments p ON b.id = p.bill_id "
                + "WHERE b.client_id = ?";
PreparedStatement pstmt = con.prepareStatement(sql);
pstmt.setInt(1, client_id);
ResultSet rs = pstmt.executeQuery();
DefaultTableModel model = new DefaultTableModel();
model.addColumn("ID");
model.addColumn("Bill Date");
model.addColumn("Payment Date");
model.addColumn("Pay Date");
model.addColumn("Due Date");
model.addColumn("Amount");
model.addColumn("Received Amount");
model.addColumn("Status");
while (rs.next()) {
    Object[] row = new Object[8];
    row[0] = rs.getInt("id");
    row[1] = rs.getDate("bill_date");
    if (rs.getDate("payment_date") != null) {
        row[2] = rs.getDate("payment_date");
    } else {
        row[2] = "Not Paid";
    }
    row[3] = rs.getDate("pay_date");
    row[4] = rs.getDate("due_date");
    row[5] = rs.getDouble("amount");
    row[6] = rs.getDouble("r_amount");
    row[7] = rs.getString("status");
    
//    row[0] = rs.getDate("bill_date");
//    if (rs.getDate("payment_date") != null) {
//        row[1] = rs.getDate("payment_date");
//    } else {
//        row[1] = "Not Paid";
//    }
//    row[2] = rs.getDate("pay_date");
//    row[3] = rs.getDate("due_date");
//    row[4] = rs.getBigDecimal("amount");
//    row[5] = rs.getBigDecimal("r_amount");
//    row[6] = rs.getString("status");
 
    model.addRow(row);
}

            jTable1.setModel(model);

           cname.setText("Bill for: " + clientName);
           address.setText("Address : " + addressDB);
           
           
        } catch (SQLException ex) {
            Logger.getLogger(billhistory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
public double getUnpaidAmount(int client_id) {
    double unpaidAmount = 0.0;
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    try {
        con = DriverManager.getConnection("jdbc:mysql://localhost/wbill_db", "root", "");
        String query = "SELECT SUM(amount) AS unpaid_amount FROM billing_list WHERE client_id = ? AND status = 'Unpaid'";
        pst = con.prepareStatement(query);
        pst.setInt(1, client_id);
        rs = pst.executeQuery();
        if (rs.next()) {
            unpaidAmount = rs.getDouble("unpaid_amount");
        }
    } catch (SQLException ex) {
        System.out.println("Error: " + ex.getMessage());
    } finally {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    return unpaidAmount;
}
private void displayUnpaidAmount() {
    double unpaidAmount = getUnpaidAmount(client_id);
    String unpaidText = String.format("%.2f", unpaidAmount);
    if (unpaidAmount >= 540) {
        unpaid.setText("<html><font color='red'>Unpaid Amount PHP : " + unpaidText + "</font></html>");
    } else {
        unpaid.setText("Unpaid Amount PHP : " + unpaidText);
    }
    System.out.println("Client ID: " + client_id);
    System.out.println("Unpaid Amount: " + unpaidAmount);
}




    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        tv_dateCreated = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        unpaid = new javax.swing.JLabel();
        cname = new javax.swing.JLabel();
        address = new javax.swing.JLabel();
        myButton3 = new button.MyButton();
        pay_bill = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTable1.setFont(new java.awt.Font("Bookman Old Style", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Bill Date", "Payment Date", "Pay Date", "Due Date", "Amount", "Received Amount", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(255, 255, 255));
        jTable1.setRowHeight(50);
        jTable1.setSelectionBackground(new java.awt.Color(0, 153, 153));
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        tv_dateCreated.setFont(new java.awt.Font("Bookman Old Style", 0, 24)); // NOI18N
        tv_dateCreated.setText("date created :");

        jLabel4.setFont(new java.awt.Font("Bookman Old Style", 1, 24)); // NOI18N
        jLabel4.setText("Client Bill Information");

        unpaid.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        unpaid.setText("Total Unpaid Amount: ");

        cname.setFont(new java.awt.Font("Bookman Old Style", 0, 24)); // NOI18N
        cname.setText("cname");

        address.setFont(new java.awt.Font("Bookman Old Style", 0, 24)); // NOI18N
        address.setText("address :");

        myButton3.setBackground(new java.awt.Color(255, 51, 51));
        myButton3.setForeground(new java.awt.Color(255, 255, 255));
        myButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/cancel (2) (1).png"))); // NOI18N
        myButton3.setText("Back");
        myButton3.setBorderColor(new java.awt.Color(255, 51, 51));
        myButton3.setColor(new java.awt.Color(255, 51, 51));
        myButton3.setColorClick(new java.awt.Color(255, 51, 51));
        myButton3.setColorOver(new java.awt.Color(255, 51, 51));
        myButton3.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        myButton3.setRadius(50);
        myButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myButton3ActionPerformed(evt);
            }
        });

        pay_bill.setText("jButton1");
        pay_bill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pay_billActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1143, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(348, 348, 348)
                                .addComponent(pay_bill)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(unpaid)
                                .addGap(127, 127, 127)))
                        .addGap(0, 24, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(tv_dateCreated)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(address)
                                    .addComponent(cname))
                                .addGap(0, 0, Short.MAX_VALUE))))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(497, 497, 497)
                    .addComponent(myButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(501, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                .addComponent(tv_dateCreated)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cname)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(address)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addComponent(unpaid)
                        .addGap(14, 14, 14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pay_bill)
                        .addGap(29, 29, 29))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(592, Short.MAX_VALUE)
                    .addComponent(myButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(16, 16, 16)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void myButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myButton3ActionPerformed

    }//GEN-LAST:event_myButton3ActionPerformed

    private void pay_billActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pay_billActionPerformed
//        Payment p = new Payment();
//        p.setVisible(true);

//        int rowIndex = jTable1.getSelectedRow();
//    if (rowIndex < 0) {
//    JOptionPane.showMessageDialog(this, "Please select a row.");
//    return;
//    }
//
//    int billId = (int) jTable1.getValueAt(rowIndex, jTable1.getColumnModel().getColumnIndex("ID"));
//
//    double billAmount = (double) jTable1.getValueAt(rowIndex, jTable1.getColumnModel().getColumnIndex("Amount"));
//
//    double rAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount to pay:"));
//
//    if (rAmount < billAmount) {
//    JOptionPane.showMessageDialog(this, "Payment amount must be at least " + billAmount);
//    return;
//    }else if (rAmount > billAmount) {
//    JOptionPane.showMessageDialog(this, "Payment exceeds the bill amount " + billAmount);
//    return;
//    }
//
//
//    // get current date
//    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//    Date paymentDate = new Date();
//
//    try {
//    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
//    String sql = "INSERT INTO payments (bill_id, r_amount, payment_date) VALUES (?, ?, ?)";
//    PreparedStatement stmt = con.prepareStatement(sql);
//    stmt.setInt(1, billId);
//    stmt.setDouble(2, rAmount);
//    stmt.setString(3, dateFormat.format(paymentDate));
//    stmt.executeUpdate();
//    // update status to paid in billing list
//    sql = "UPDATE billing_list SET status = 'Paid' WHERE id = ?";
//    stmt = con.prepareStatement(sql);
//    stmt.setInt(1, billId);
//    stmt.executeUpdate();
//    populateTable();
//
//
//    JOptionPane.showMessageDialog(this, "Payment successful.");
//    } catch (SQLException ex) {
//    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
//    }        // TODO add your handling code here:
    }//GEN-LAST:event_pay_billActionPerformed

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
            java.util.logging.Logger.getLogger(billhistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(billhistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(billhistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(billhistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new billhistory().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel address;
    private javax.swing.JLabel cname;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private button.MyButton myButton3;
    private javax.swing.JButton pay_bill;
    private javax.swing.JLabel tv_dateCreated;
    private javax.swing.JLabel unpaid;
    // End of variables declaration//GEN-END:variables

    private ArrayList<bill> getBill(int clientId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
