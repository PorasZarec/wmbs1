/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clients;

import SMS.sms_p1rww;
import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


/**
 *
 * @author Khyrsean
 */
public class addclient extends javax.swing.JFrame {
    
    SerialPort port;
    OutputStream out;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String uniqueClientCode;
  
 
public addclient() {
    
    initComponents();
    String status;
    setUniqueCodeTextView();

}
    
    
private void refresh() {
    last.setText("");
    first.setText("");
    num.setText("");
    add.setText("");
}
 
public int getUniqueId(Connection con) throws SQLException {
    int uniqueId = 1;
    String query = "SELECT MAX(id) as maxId FROM clients";
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(query);
    if (rs.next()) {
        uniqueId = rs.getInt("maxId") + 1;
    }
    return uniqueId;
}


private void setUniqueCodeTextView(){
    
    // Getting the new account ID on the Database
    
    try {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
        Statement stmt = con.createStatement();
        String query = "SELECT COUNT(*) AS total FROM client_list";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int count = rs.getInt("total");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String currentDate = dateFormat.format(new Date());

        String code = currentDate + String.format("%04d", count + 1);
       
        
        uniqueClientCode = code;

        rs.close();
        con.close();

    } catch (SQLException ex) {
        
        ex.printStackTrace();
        
    }
    
    tv_clientUniqueID.setText(uniqueClientCode);

}

private void activityClientIncrement() {
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
        String incrementQuery = "UPDATE administrator_activity SET add_clients_act = add_clients_act + 1 WHERE id = ?";
        PreparedStatement incrementStatement = con.prepareStatement(incrementQuery);
        incrementStatement.setInt(1, maxId);
        incrementStatement.executeUpdate();
        incrementStatement.close();

        con.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to increment add_clients_act column!");
    }
}




private void uploadClientList(){
    
    try {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
        
//        String code = currentDate + String.format("%04d", count + 1);

        String insertQuery = "INSERT INTO client_list (code, firstname, lastname, contact, address,status) VALUES (?, ?, ?, ?, ?,'Connected')";
        PreparedStatement pst = con.prepareStatement(insertQuery);
        pst.setString(1, uniqueClientCode);
        pst.setString(2, first.getText());
        pst.setString(3, last.getText());
        pst.setString(4, num.getText());
        pst.setString(5, add.getText());
        int insert = pst.executeUpdate();

        
        activityClientIncrement();
        
        JOptionPane.showMessageDialog(this, "Client added successfully with code: " + uniqueClientCode);
        setUniqueCodeTextView();
        refresh();
        
        
        
    } catch (SQLException ex) {
        
        Logger.getLogger(addclient.class.getName()).log(Level.SEVERE, null, ex);
        
    }
}


private void checkPort(){
    
    // Getting the phone number and setting the credentials
    out = port.getOutputStream();
    String data = "";
    String name = "";

    name = first.getText();
    data = num.getText();
    
    String endline = "NO LINE ENDING";
    
    try {
        out.write(data.getBytes());
        
        // Prompt user for successful recipient number set
        JOptionPane.showMessageDialog(this, "Recipient number set successfully.", "Success", JOptionPane.INFORMATION_MESSAGE); 
        
    } catch (IOException e) {
        // Display error dialog with the exception message
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); 
        
    }

    // Retrieve the current month using SimpleDateFormat
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String month = dateFormat.format(new Date());
     
     String message = "Magandang Araw, "+name+".\n" +
        "\n" +
        "	\n" +
        "	Malugod naming ipinaaabot sa inyo ang aming mainit na pagbati!\n" +
        "ang inyong account number ay " + uniqueClientCode + ".\n" +
        "Ang billings ay pina-paalala namin tuwing 10th ng buwan samantala naman \n" +
        "ang  pagbabayad ay tuwing ikaw 15th at 18th ng buwan. Paalala upang hindi\n" +
        "masuspende ang inyong water service, pina-paalala namin na magbayad bago ang \n" +
        "itinakdang araw ng pagbabayad.\n" +
        "\n" +
        "Kung mayroon po kayong mga katanungan o mga hiling, huwag po kayong \n" +
        "mag-atubiling makipag-ugnayan sa aming tanggapan. Handa po kaming tumugon at \n" +
        "magbigay ng tulong sa anumang oras. Maraming salamat po!";
    
    if (message.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a message.", "Error", JOptionPane.ERROR_MESSAGE);
    } else {
        try {
            // Write the message to the serial port
            port.getOutputStream().write(message.getBytes());

            JOptionPane.showMessageDialog(this, "Message Sent!");
            
        } catch (Exception ex) {
            
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            
        }
    } try {
        
        out.write(message.getBytes());
        
        JOptionPane.showMessageDialog(this, "Message Sent");
        
    } catch (IOException e) {
        
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        
    }
}

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tv_clientUniqueID = new javax.swing.JLabel();
        last = new javax.swing.JTextField();
        first = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        num = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        add = new javax.swing.JTextField();
        myButton1 = new button.MyButton();
        myButton2 = new button.MyButton();
        jLabel7 = new javax.swing.JLabel();
        tv_clientID1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel2.setBackground(java.awt.SystemColor.controlLtHighlight);
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.black, java.awt.Color.white, java.awt.Color.black, java.awt.Color.white));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel1.setText("Add New Client");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/cancel (2) (1).png"))); // NOI18N
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        tv_clientUniqueID.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        tv_clientUniqueID.setText("00000");

        last.setFont(new java.awt.Font("Bookman Old Style", 0, 18)); // NOI18N

        first.setFont(new java.awt.Font("Bookman Old Style", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        jLabel4.setText("Firstname:");
        jLabel4.setToolTipText("");

        jLabel5.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        jLabel5.setText("Contact Number:");

        num.setFont(new java.awt.Font("Bookman Old Style", 0, 18)); // NOI18N
        num.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        jLabel6.setText("Address:");
        jLabel6.setToolTipText("");

        add.setFont(new java.awt.Font("Bookman Old Style", 0, 18)); // NOI18N

        myButton1.setBackground(new java.awt.Color(0, 153, 255));
        myButton1.setForeground(new java.awt.Color(255, 255, 255));
        myButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/user (1).png"))); // NOI18N
        myButton1.setText("  Save");
        myButton1.setBorderColor(new java.awt.Color(0, 153, 255));
        myButton1.setColor(new java.awt.Color(0, 153, 255));
        myButton1.setColorClick(new java.awt.Color(0, 153, 255));
        myButton1.setColorOver(new java.awt.Color(0, 153, 255));
        myButton1.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        myButton1.setRadius(50);
        myButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myButton1ActionPerformed(evt);
            }
        });

        myButton2.setForeground(new java.awt.Color(255, 255, 255));
        myButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/cancel (2) (1).png"))); // NOI18N
        myButton2.setText("  Cancel");
        myButton2.setBorderColor(new java.awt.Color(226, 77, 74));
        myButton2.setColor(new java.awt.Color(226, 77, 74));
        myButton2.setColorClick(new java.awt.Color(255, 102, 102));
        myButton2.setColorOver(new java.awt.Color(255, 51, 51));
        myButton2.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        myButton2.setRadius(50);
        myButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myButton2ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        jLabel7.setText("Lastname:");

        tv_clientID1.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        tv_clientID1.setText("Client ID :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 409, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(23, 23, 23))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(num, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(myButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(first, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(57, 57, 57)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(last, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(myButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(38, 38, 38))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(tv_clientID1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tv_clientUniqueID, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(381, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(188, 188, 188)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tv_clientID1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tv_clientUniqueID, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(first, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(last, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(num, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(myButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(myButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(127, 127, 127)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(317, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void myButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myButton1ActionPerformed
    
    
        
        // Checking if the textField are empty or the requirements are met
        if (last.getText().isEmpty() || first.getText().isEmpty() || num.getText().isEmpty() || add.getText().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, "Please fill up all required fields.");
            
        } else {
            
            String cnum = num.getText().trim();
            
            if (cnum.length() != 11 || !cnum.matches("\\d+")) {
                
                JOptionPane.showMessageDialog(this, "Please enter a valid Contact Number");
                
            } else {
                
                if (last.getText().matches(".*\\d+.*") || first.getText().matches(".*\\d+.*")) {
                    
                    JOptionPane.showMessageDialog(this, "Invalid input: name should not contain numbers");
                    
                } else {
                    
                    if (port == null || !port.isOpen()) {
                        
                        JOptionPane.showMessageDialog(this, "Port is not connected.", "Error", JOptionPane.ERROR_MESSAGE);
                        
                    } else {
                        
                       checkPort();
                       
                    }
                    
                    uploadClientList();
                }
            }
        }
    }//GEN-LAST:event_myButton1ActionPerformed

    private void myButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myButton2ActionPerformed
      
    client cl = new client();
    cl.show();
    dispose();
    }//GEN-LAST:event_myButton2ActionPerformed

    private void numActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numActionPerformed

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
            java.util.logging.Logger.getLogger(addclient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(addclient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(addclient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(addclient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                
                new addclient().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField add;
    private javax.swing.JTextField first;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField last;
    private button.MyButton myButton1;
    private button.MyButton myButton2;
    private javax.swing.JTextField num;
    private javax.swing.JLabel tv_clientID1;
    private javax.swing.JLabel tv_clientUniqueID;
    // End of variables declaration//GEN-END:variables
}
