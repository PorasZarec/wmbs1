/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package AdminLogin;
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

    /**
    * Author Patrick
    */

public class AdminLogin extends javax.swing.JFrame {
    
    
    Connection con;
    String adminUsername;
    
    public AdminLogin() {
        
        
        // Set up date and time display
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        
        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String formattedDate = dateFormat.format(new Date());
                datetime.setText(formattedDate);
            }
        });
        timer.start();
        initComponents();
    }
    
private void createNewLoginAdminRow() {
    adminUsername = tf_phone_number.getText();
    
    try {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");

        // Get the highest ID
        String getMaxIdQuery = "SELECT MAX(id) FROM administrator_activity";
        PreparedStatement getMaxIdStatement = con.prepareStatement(getMaxIdQuery);
        ResultSet maxIdResultSet = getMaxIdStatement.executeQuery();
        int maxId = 0;
        if (maxIdResultSet.next()) {
            maxId = maxIdResultSet.getInt(1);
        }
        maxIdResultSet.close();
        getMaxIdStatement.close();
        
        // Increment the ID to create a new row
        int newId = maxId + 1;

        // Insert a new row with the admin username and current date and time
        String insertQuery = "INSERT INTO administrator_activity (id, user, log_in_act, add_clients_act,disconnect_clients_act, pay_bill_act,print_act,create_bill_act,reconnect_act,sms_sent_act) VALUES (?, ?, ?, 0,0,0,0,0,0,0)";
        PreparedStatement insertStatement = con.prepareStatement(insertQuery);

        insertStatement.setInt(1, newId);
        insertStatement.setString(2, adminUsername);
        
        // Get the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        String currentDateAndTime = dateFormat.format(new Date());
        
        insertStatement.setString(3, currentDateAndTime);
        
        insertStatement.executeUpdate();
        insertStatement.close();

        con.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to create new login admin row!");
    }
}
    
    
    
    
    
    
    
private void refresh() {
   tf_phone_number.setText("");
   tf_password.setText("");
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
        color17 = new GradientPanel.color1();
        jLabel14 = new javax.swing.JLabel();
        tf_phone_number = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tf_password = new javax.swing.JTextField();
        datetime = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        myButton4 = new button.MyButton();

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

        color17.setBorder(new javax.swing.border.MatteBorder(null));

        tf_phone_number.setBackground(new java.awt.Color(255, 255, 255));
        tf_phone_number.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        tf_phone_number.setForeground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Contact      :");

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText(" Password   :");

        tf_password.setBackground(new java.awt.Color(255, 255, 255));
        tf_password.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        tf_password.setForeground(new java.awt.Color(0, 0, 0));

        datetime.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        datetime.setForeground(new java.awt.Color(0, 0, 0));
        datetime.setText("Month/Day/Year");

        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/logo (2).png"))); // NOI18N
        jLabel5.setText("Admin Log In");

        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/water_background_1.png"))); // NOI18N

        myButton4.setForeground(new java.awt.Color(51, 51, 51));
        myButton4.setText("LOGIN");
        myButton4.setBorderColor(new java.awt.Color(0, 0, 0));
        myButton4.setColor(new java.awt.Color(0, 255, 0));
        myButton4.setColorClick(new java.awt.Color(255, 0, 0));
        myButton4.setColorOver(new java.awt.Color(0, 255, 255));
        myButton4.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        myButton4.setRadius(50);
        myButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout color17Layout = new javax.swing.GroupLayout(color17);
        color17.setLayout(color17Layout);
        color17Layout.setHorizontalGroup(
            color17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color17Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(color17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color17Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color17Layout.createSequentialGroup()
                        .addGroup(color17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(color17Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(tf_password, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(color17Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(tf_phone_number, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(70, 70, 70))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color17Layout.createSequentialGroup()
                        .addComponent(datetime, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(color17Layout.createSequentialGroup()
                .addGap(171, 171, 171)
                .addComponent(myButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(color17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color17Layout.createSequentialGroup()
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 473, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        color17Layout.setVerticalGroup(
            color17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(datetime)
                .addGap(19, 19, 19)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addGap(21, 21, 21)
                .addGroup(color17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_phone_number, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(color17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tf_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(myButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
            .addGroup(color17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(color17Layout.createSequentialGroup()
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(color17, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(color17, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void myButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myButton4ActionPerformed

        if (tf_phone_number.getText().isEmpty() || tf_password.getText().isEmpty()) {
            refresh();
            JOptionPane.showMessageDialog(this, "Please fill up all required fields.");
        } else {
            String username = tf_phone_number.getText().trim();
            String password = tf_password.getText().trim();

            // Check if phone number contains only numbers
            if (!username.matches("\\d+")) {
                refresh();
                JOptionPane.showMessageDialog(this, "Please enter a valid phone number (numbers only).");
                return;
            }

            try {
                // Connecting to database
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");

                String query = "SELECT * FROM administrator_login WHERE contact_number = ? AND password = ?";
                PreparedStatement statement = con.prepareStatement(query);

                // Setting the string if it matches any data from the administrator_login table
                statement.setString(1, username);
                statement.setString(2, password);

                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    // Match found
//                    JOptionPane.showMessageDialog(this, "Phone number and password match!");
                    createNewLoginAdminRow();
                    refresh();

                    // Launch the dashboard
                    dashboard dash = new dashboard();
                    dash.show();
                    dispose();
                } else {
                    // No match found, display an error message
                    refresh();
                    JOptionPane.showMessageDialog(this, "Invalid phone number or password!");
                }

                rs.close();
                statement.close();
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_myButton4ActionPerformed

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
            java.util.logging.Logger.getLogger(AdminLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel clients;
    private GradientPanel.color1 color16;
    private GradientPanel.color1 color17;
    private javax.swing.JLabel datetime;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private button.MyButton myButton4;
    private javax.swing.JTextField tf_password;
    private javax.swing.JTextField tf_phone_number;
    // End of variables declaration//GEN-END:variables
}
