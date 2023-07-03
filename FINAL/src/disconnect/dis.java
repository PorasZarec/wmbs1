
package disconnect;

import SMS.sms_p1rww;
import billings.bhistory;
import billings.billpayment;
import billings.invoice;
import billings.receipt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import wbms1.dashboard;
import billings.reconnectionReceipt;
import clients.client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;


public class dis extends javax.swing.JFrame {

    
    public dis() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String formattedDate = dateFormat.format(new Date());
                datetime.setText(formattedDate);
            }
        });
        timer.start();
        initComponents();
        Connect();
        update_table();
        show_table();

        TableColumn column = null;
        column = jTable1.getColumnModel().getColumn(0); // id
        column.setPreferredWidth(10);
        column = jTable1.getColumnModel().getColumn(1); // client_name
        column.setPreferredWidth(20);
        column = jTable1.getColumnModel().getColumn(3); // reading_date
        column.setPreferredWidth(50);
        column = jTable1.getColumnModel().getColumn(4); // reading_date
        column.setPreferredWidth(50);
        column = jTable1.getColumnModel().getColumn(5); // due_date
        column.setPreferredWidth(50);
        column = jTable1.getColumnModel().getColumn(6); // total
        column.setPreferredWidth(25);

        
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = jTable1.getSelectedRow();

                if (selectedRow != -1) {
                    // Get the values from the selected row
                    Object selectedIDObj = jTable1.getValueAt(selectedRow, jTable1.getColumnModel().getColumnIndex("ID"));
                    Object selectedNameObj = jTable1.getValueAt(selectedRow, jTable1.getColumnModel().getColumnIndex("Client Name"));

                    // Convert the values to strings
                    String selectedID = String.valueOf(selectedIDObj);
                    String selectedName = String.valueOf(selectedNameObj);

                    reconnectionReceipt.someFunction(selectedID, selectedName);
                    }
                }
            }
        });
    
    }
 Connection con;
    PreparedStatement pst;
    ResultSet rs;

    public void Connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
            System.out.println("Connected to database.");
        } catch (SQLException ex) {
            Logger.getLogger(dis.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Failed to connect to database.");
        }
    }
    
public ArrayList<DisconnectedUser> disconnectedUserList() {
    ArrayList<DisconnectedUser> userList = new ArrayList<>();
    try {
        if (con != null) {
            String query1 = "SELECT * FROM client_list WHERE status = 'Disconnected'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            while (rs.next()) {
                int id = rs.getInt("id");
                String code = rs.getString("code");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String contact = rs.getString("contact");
                String address = rs.getString("address");
                String status = rs.getString("status");
                double amount = 0.0;
                String query2 = "SELECT SUM(amount) AS total_amount FROM billing_list WHERE client_id = "+id;
                Statement st2 = con.createStatement();
                ResultSet rs2 = st2.executeQuery(query2);
                if(rs2.next()){
                    amount = rs2.getDouble("amount");
                }
                rs2.close();
                DisconnectedUser newUser = new DisconnectedUser(id, code, firstname, lastname, contact, address, status, amount);
                userList.add(newUser);
            }
            rs.close(); // Close the result set
        }
    } catch (SQLException ex) {
        Logger.getLogger(disco.class.getName()).log(Level.SEVERE, null, ex);
    }
    return userList;
}


  public void update_table() {
    try {
        String sql = "SELECT client_list.id, client_list.code, client_list.firstname, client_list.lastname, client_list.contact, client_list.address, client_list.status, billing_list.amount FROM client_list JOIN billing_list ON client_list.id = billing_list.client_id WHERE client_list.status = 'Disconnected'";
        pst = con.prepareStatement(sql);
        rs = pst.executeQuery();
        DefaultTableModel tblModel = (DefaultTableModel) jTable1.getModel();
        tblModel.setRowCount(0); // Clear the table
        Object[] row = new Object[7]; // Create a new row
        while (rs.next()) {
            row[0] = rs.getInt("id");
            row[1] = rs.getString("code");
            row[2] = rs.getString("firstname") + ", " + rs.getString("lastname");
            row[3] = rs.getString("contact");
            row[4] = rs.getString("address");
            String status = rs.getString("status");
            if (status.equals("Disconnected")) {
                for (int i = 0; i < 5; i++) {
                    row[i] = "<html><font color='red'>" + row[i] + "</font></html>";
                }
            }
            row[6] = rs.getDouble("amount");
            row[5] = status;
            if (status.equals("Disconnected")) {
                row[5] = "<html><font color='red'>" + status + "</font></html>";
            }
            tblModel.addRow(row); // Add the row to the table
        }
        rs.close(); // Close the result set
    } catch (SQLException ex) {
        Logger.getLogger(dis.class.getName()).log(Level.SEVERE, null, ex);
    }
}
public void show_table() {
    try {
        String userListSql = "SELECT * FROM client_list WHERE status = 'Disconnected'";
        pst = con.prepareStatement(userListSql);
        rs = pst.executeQuery();
        DefaultTableModel tblModel = (DefaultTableModel) jTable1.getModel();
        tblModel.setRowCount(0); // Clear the table
        while (rs.next()) {
            Object[] row = new Object[7]; // Create a new row inside the loop
            int client_id = rs.getInt("id");
            String code = rs.getString("code");
            String userName = rs.getString("firstname") + ", " + rs.getString("lastname");
            String Contact = rs.getString("contact");
            String Address = rs.getString("address");
            String Status = rs.getString("status");
            row[0] = client_id;
            row[1] = code;
            row[2] = userName;
            row[3] = Contact;
            row[4] = Address;
            row[5] = Status;
            row[6] = 0.0;

            if (Status.equals("Disconnected")) {
                row[5] = "<html><font color='red'>" + Status + "</font></html>";

                // Query the billing_list to get the total amount
                String billingSql = "SELECT SUM(amount) AS totalAmount FROM billing_list WHERE client_id = ?";
                PreparedStatement pstBilling = con.prepareStatement(billingSql);
                pstBilling.setInt(1, client_id);
                ResultSet rsBilling = pstBilling.executeQuery();
                if (rsBilling.next()) {
                    double totalAmount = rsBilling.getDouble("totalAmount");
                    row[6] = "<html><font color='red'>" + totalAmount + "</font></html>";
                }
                rsBilling.close();
            }
            tblModel.addRow(row); // Add the row to the table
        }
        rs.close(); // Close the result set
    } catch (SQLException ex) {
        Logger.getLogger(dis.class.getName()).log(Level.SEVERE, null, ex);
    }
}

private void activityReconnectIncrement() {
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
        String incrementQuery = "UPDATE administrator_activity SET reconnect_act = reconnect_act + 1 WHERE id = ?";
        PreparedStatement incrementStatement = con.prepareStatement(incrementQuery);
        incrementStatement.setInt(1, maxId);
        incrementStatement.executeUpdate();
        incrementStatement.close();

        con.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to increment reconnect_act column!");
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        myButton4 = new button.MyButton();
        color3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        datetime = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        dash1 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTable1.setFont(new java.awt.Font("Bookman Old Style", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Code", "Client Name", "Contact Number", "Address", "Status", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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

        jLabel4.setFont(new java.awt.Font("Bookman Old Style", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Disconnected Clients");

        myButton4.setBackground(new java.awt.Color(102, 204, 255));
        myButton4.setForeground(new java.awt.Color(51, 51, 51));
        myButton4.setText("Reconnection");
        myButton4.setBorderColor(new java.awt.Color(102, 204, 255));
        myButton4.setColor(new java.awt.Color(102, 204, 255));
        myButton4.setColorClick(new java.awt.Color(153, 204, 255));
        myButton4.setColorOver(new java.awt.Color(102, 204, 255));
        myButton4.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        myButton4.setRadius(50);
        myButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 925, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(388, 388, 388)
                        .addComponent(myButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel4)
                .addGap(19, 19, 19)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(myButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        color3.setBackground(new java.awt.Color(51, 204, 255));

        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/logo (2).png"))); // NOI18N
        jLabel7.setText("P1RWW");

        jLabel27.setBackground(java.awt.Color.lightGray);
        jLabel27.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel27.setText("Clients");
        jLabel27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel27MouseClicked(evt);
            }
        });

        jLabel31.setBackground(java.awt.Color.lightGray);
        jLabel31.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel31.setText("SMS");
        jLabel31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel31MouseClicked(evt);
            }
        });

        datetime.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        datetime.setText("0");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel30.setBackground(java.awt.Color.lightGray);
        jLabel30.setFont(new java.awt.Font("Bahnschrift", 0, 20)); // NOI18N
        jLabel30.setText("Disconnected Clients     >");
        jLabel30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel30MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        dash1.setBackground(java.awt.Color.lightGray);
        dash1.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        dash1.setText("Dashboard");
        dash1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dash1MouseClicked(evt);
            }
        });

        jLabel32.setBackground(new java.awt.Color(0, 0, 0));
        jLabel32.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel32.setText("Bill Payments");
        jLabel32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel32MouseClicked(evt);
            }
        });

        jLabel28.setBackground(java.awt.Color.lightGray);
        jLabel28.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel28.setText("Bill History");
        jLabel28.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel28MouseClicked(evt);
            }
        });

        jLabel29.setBackground(java.awt.Color.lightGray);
        jLabel29.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel29.setText("Invoice");
        jLabel29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel29MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout color3Layout = new javax.swing.GroupLayout(color3);
        color3.setLayout(color3Layout);
        color3Layout.setHorizontalGroup(
            color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(color3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color3Layout.createSequentialGroup()
                        .addGap(0, 4, Short.MAX_VALUE)
                        .addGroup(color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(dash1)
                                .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                                .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(color3Layout.createSequentialGroup()
                        .addComponent(datetime)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        color3Layout.setVerticalGroup(
            color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color3Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(dash1)
                .addGap(18, 18, 18)
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83)
                .addComponent(datetime)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(color3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(color3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void myButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myButton4ActionPerformed
int rowIndex = jTable1.getSelectedRow();
if (rowIndex < 0) {
    JOptionPane.showMessageDialog(this, "Please select a row.");
    return;
}

int clientId = (int) jTable1.getValueAt(rowIndex, jTable1.getColumnModel().getColumnIndex("ID"));
int reconnectionFee = 500;
try {
    int unpaidCount = 0;
    Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
    String sql1 = "SELECT COUNT(*) FROM billing_list WHERE client_id = ? AND status = 'Unpaid'";
    PreparedStatement countStmt = con.prepareStatement(sql1);
    countStmt.setInt(1, clientId);
    ResultSet resultSet = countStmt.executeQuery();
    if (resultSet.next()) {
    unpaidCount = resultSet.getInt(1);
}
    if (unpaidCount >= 1) {
    JOptionPane.showMessageDialog(null, "There's still unpaid balance left!");
}   else {
        double receivedAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount to pay:"));
    
    if (reconnectionFee == receivedAmount) {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
    String sql = "UPDATE billing_list SET status = 'Paid' WHERE client_id = ? AND status = 'Unpaid'";
    PreparedStatement stmt = con.prepareStatement(sql);
    stmt.setInt(1, clientId);
    stmt.executeUpdate(); 
    
    // update client status to connected
    
    sql = "UPDATE client_list SET status = 'Connected' WHERE id = ?";
    stmt = con.prepareStatement(sql);
    stmt.setInt(1, clientId);
    stmt.executeUpdate();
    show_table();
    
        
    JOptionPane.showMessageDialog(this, "Reconnection successful.");
    
    // admin activity method
    activityReconnectIncrement();
    
    reconnectionReceipt r = new reconnectionReceipt();
    r.setVisible(true);
    }
    }
    
} catch (SQLException ex) {
    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
}




    }//GEN-LAST:event_myButton4ActionPerformed

    private void jLabel27MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel27MouseClicked
        client cl = new client();
        cl.show();
        dispose();
    }//GEN-LAST:event_jLabel27MouseClicked

    private void jLabel30MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel30MouseClicked
        dis dis = new dis();
        dis.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel30MouseClicked

    private void jLabel31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseClicked
   sms_p1rww sms = new sms_p1rww();
        sms.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel31MouseClicked

    private void jLabel29MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel29MouseClicked
        invoice in = new invoice();
        in.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel29MouseClicked

    private void dash1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dash1MouseClicked
        // TODO add your handling code here:
        dashboard dash = new dashboard();
        dash.show();
        dispose();
    }//GEN-LAST:event_dash1MouseClicked

    private void jLabel32MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel32MouseClicked
        billpayment bill = new billpayment();
        bill.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel32MouseClicked

    private void jLabel28MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel28MouseClicked
        bhistory b = new bhistory();
        b.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel28MouseClicked

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
            java.util.logging.Logger.getLogger(dis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dis().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel color3;
    private javax.swing.JLabel dash1;
    private javax.swing.JLabel datetime;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private button.MyButton myButton4;
    // End of variables declaration//GEN-END:variables
}
