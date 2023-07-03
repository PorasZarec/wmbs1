/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package wbms1;
import billings.bhistory;
import billings.billpayment;
import billings.invoice;
import clients.client;
import clients.User;
import disconnect.disco;
import GradientPanel.color;
import GradientPanel.color1;
import SMS.sms_p1rww;

import billings.billhistory;
import clients.User;
import disconnect.dis;
import javax.swing.table.DefaultTableModel;
import raven.cell.TableActionCellEditor;
import raven.cell.TableActionCellRender;
import raven.cell.TableActionEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JOptionPane;
import java.sql.*;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;import java.math.BigDecimal;
import java.util.List;

public class dashboard extends javax.swing.JFrame {

    /**
     * Creates new form dashboard
     */
    
    public dashboard() {
        
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
        Connect();
        update_table();

        // Display client count
        int clientCount = countClients();
        clients.setText("" + clientCount);
        
               // Display paid count
        int paidCount = countPaid();
        paid.setText("" + paidCount);


     
      // Display revenue
    double revenue = getRevenue();
    rev.setText(String.format("%.2f", revenue));
    
    
   // Display disconnected clients count
        int disconnectedCount = countDisconnected();
        disco.setText("" + disconnectedCount);

    }
    
    Connection con;
    PreparedStatement pst;  
    ResultSet rs;
    
    
public void Connect(){
    try {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db","root","");
        System.out.println("Connected to database.");
    } catch (SQLException ex) {
        Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        System.out.println("Failed to connect to database.");
    }
}

public void update_table() {
    try {
        String sql = "SELECT * FROM administrator_activity";
        pst = con.prepareStatement(sql);
        rs = pst.executeQuery();

        DefaultTableModel tblModel = (DefaultTableModel) table.getModel();
        tblModel.setRowCount(0); // Clear existing rows in the table

        while (rs.next()) {
            Object[] row = new Object[9];
            
            row[0] = rs.getString("user");
            row[1] = rs.getString("log_in_act");
            row[2] = rs.getString("add_clients_act");
            row[3] = rs.getString("disconnect_clients_act");
            row[4] = rs.getString("pay_bill_act");
            row[5] = rs.getString("print_act");
            row[6] = rs.getString("create_bill_act");
            row[7] = rs.getString("reconnect_act");
            row[8] = rs.getString("sms_sent_act");

            tblModel.addRow(row);
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

public ArrayList<admin> adminList() {
    ArrayList<admin> adminList = new ArrayList<>();
    try {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
        String query = "SELECT * FROM administrator_activity";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            String use = rs.getString("user");
            String log = rs.getString("log_in_act");
            String add = rs.getString("add_clients_act");
            String discon = rs.getString("disconnect_clients_act");
            String pay = rs.getString("pay_bill_act");
            String print = rs.getString("print_act");
            String create = rs.getString("create_bill_act");
            String recon = rs.getString("reconnect_act");
            String sms = rs.getString("sms_sent_act");

            admin newAdmin = new admin(use, log, add, discon, pay, print, create, recon, sms);

            adminList.add(newAdmin);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return adminList;
}

public void show_table() {
    ArrayList<admin> list = adminList();
    DefaultTableModel tblModel = (DefaultTableModel) table.getModel();
    tblModel.setRowCount(0); // Clear existing rows in the table
    for (admin admin : list) {
        Object[] row = new Object[9];
        
        row[0] = admin.getUserActivity();
        row[1] = admin.getLogInActivity();
        row[2] = admin.getAddClientsActivity();
        row[3] = admin.getDisconnectClientsActivity();
        row[4] = admin.getPayBillActivity();
        row[5] = admin.getPrintActivity();
        row[6] = admin.getCreateBillActivity();
        row[7] = admin.getReconnectActivity();
        row[8] = admin.getSMSSentActivity();
        tblModel.addRow(row);
    }
}














public double getRevenue() {
    double revenue = 0.0;
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int month = LocalDate.now().getMonthValue();
    int year = LocalDate.now().getYear();

    try {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
        String query = "SELECT SUM(r_amount) AS revenue FROM payments WHERE MONTH(payment_date)=? AND YEAR(payment_date)=?";
        pst = con.prepareStatement(query);
        pst.setInt(1, month);
        pst.setInt(2, year);
        rs = pst.executeQuery();
        if (rs.next()) {
            revenue = rs.getDouble("revenue");
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

    return revenue;
}


      public int countClients() {
        int count = 0;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
            String query = "SELECT COUNT(*) AS count FROM client_list";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
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

        return count;
    }


  public int countPaid() {
   int count = 0;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
            String query = "SELECT COUNT(*) AS count FROM billing_list WHERE status='Paid' AND MONTH(bill_date)=? AND YEAR(bill_date)=?";
pst = con.prepareStatement(query);
pst.setInt(1, month);
pst.setInt(2, year);
rs = pst.executeQuery();
if (rs.next()) {
count = rs.getInt("count");
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
    return count;

 }



public int countDisconnected() {
    int count = 0;
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    try {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
        String query = "SELECT COUNT(*) AS count FROM client_list WHERE status='Disconnected'";
        pst = con.prepareStatement(query);
        rs = pst.executeQuery();
        if (rs.next()) {
            count = rs.getInt("count");
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

    return count;
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        color16 = new GradientPanel.color1();
        clients = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        color17 = new GradientPanel.color1();
        rev = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        color18 = new GradientPanel.color1();
        paid = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        color20 = new GradientPanel.color1();
        disco = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        datetime = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        dash = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

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
                .addContainerGap(39, Short.MAX_VALUE)
                .addGroup(color16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color16Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color16Layout.createSequentialGroup()
                        .addGroup(color16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(clients, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(49, 49, 49))))
        );
        color16Layout.setVerticalGroup(
            color16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color16Layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(clients)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addGap(67, 67, 67))
        );

        rev.setFont(new java.awt.Font("SansSerif", 1, 40)); // NOI18N
        rev.setForeground(new java.awt.Color(255, 255, 255));
        rev.setText("0");

        jLabel9.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/growth (1).png"))); // NOI18N
        jLabel9.setText("Revenue");

        javax.swing.GroupLayout color17Layout = new javax.swing.GroupLayout(color17);
        color17.setLayout(color17Layout);
        color17Layout.setHorizontalGroup(
            color17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color17Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(color17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rev, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19)
                .addComponent(jLabel12)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        color17Layout.setVerticalGroup(
            color17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color17Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rev)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        paid.setFont(new java.awt.Font("SansSerif", 1, 40)); // NOI18N
        paid.setForeground(new java.awt.Color(255, 255, 255));
        paid.setText("0");

        jLabel10.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/money (1).png"))); // NOI18N
        jLabel10.setText("Paid");

        javax.swing.GroupLayout color18Layout = new javax.swing.GroupLayout(color18);
        color18.setLayout(color18Layout);
        color18Layout.setHorizontalGroup(
            color18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color18Layout.createSequentialGroup()
                .addContainerGap(57, Short.MAX_VALUE)
                .addGroup(color18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color18Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color18Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(66, 66, 66))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color18Layout.createSequentialGroup()
                        .addComponent(paid, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(56, 56, 56))))
        );
        color18Layout.setVerticalGroup(
            color18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color18Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel14))
            .addGroup(color18Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(paid)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        disco.setFont(new java.awt.Font("SansSerif", 1, 40)); // NOI18N
        disco.setForeground(new java.awt.Color(255, 255, 255));
        disco.setText("0");

        jLabel22.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/users-avatar (1).png"))); // NOI18N
        jLabel22.setText("Client Disconnected");

        javax.swing.GroupLayout color20Layout = new javax.swing.GroupLayout(color20);
        color20.setLayout(color20Layout);
        color20Layout.setHorizontalGroup(
            color20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color20Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(color20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color20Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color20Layout.createSequentialGroup()
                        .addComponent(disco, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color20Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addGap(14, 14, 14))))
        );
        color20Layout.setVerticalGroup(
            color20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color20Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addComponent(jLabel22)
                .addGap(26, 26, 26)
                .addComponent(disco)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addGap(67, 67, 67))
        );

        jPanel5.setBackground(new java.awt.Color(51, 204, 255));

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/logo (2).png"))); // NOI18N
        jLabel1.setText("P1RWW");

        jLabel15.setBackground(java.awt.Color.lightGray);
        jLabel15.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel15.setText("Accounts");
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
        });

        jLabel16.setBackground(java.awt.Color.lightGray);
        jLabel16.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel16.setText("Payments");
        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
        });

        jLabel18.setBackground(java.awt.Color.lightGray);
        jLabel18.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel18.setText("Invoice");
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
        });

        jLabel20.setBackground(java.awt.Color.lightGray);
        jLabel20.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel20.setText("Disconnected Clients");
        jLabel20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel20MouseClicked(evt);
            }
        });

        jLabel21.setBackground(java.awt.Color.lightGray);
        jLabel21.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel21.setText("SMS");
        jLabel21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel21MouseClicked(evt);
            }
        });

        datetime.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        datetime.setText("0");

        jLabel19.setBackground(java.awt.Color.lightGray);
        jLabel19.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel19.setText("Bill History");
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        dash.setBackground(java.awt.Color.lightGray);
        dash.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        dash.setText("Dashboard                 >");
        dash.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dash, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(dash, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(datetime)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)))))
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(datetime)
                .addContainerGap())
        );

        table.setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "User", "Log-in", "Add clients", "Disconnect clients", "Pay Bill activity", "Printed activity", "Created bill", "Reconnected", "Sms sent "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setResizable(false);
            table.getColumnModel().getColumn(0).setPreferredWidth(90);
            table.getColumnModel().getColumn(1).setResizable(false);
            table.getColumnModel().getColumn(1).setPreferredWidth(165);
        }

        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/logo (2).png"))); // NOI18N
        jLabel2.setText("ADMIN ACTIVITY LOG");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(color16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(color17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(color20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(color18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(color20, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(color17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(color16, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(color18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseClicked
        sms_p1rww sms = new sms_p1rww();
        sms.show();
        dispose();
    }//GEN-LAST:event_jLabel21MouseClicked

    private void jLabel20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel20MouseClicked
        dis dis = new dis();
        dis.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel20MouseClicked

    private void jLabel19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel19MouseClicked
        bhistory b = new bhistory();
        b.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel19MouseClicked

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        invoice in = new invoice();
        in.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel18MouseClicked

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        billpayment bill = new billpayment();
        bill.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel16MouseClicked

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        client cl = new client();
        cl.show();
        dispose();
    }//GEN-LAST:event_jLabel15MouseClicked

    private void dashMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashMouseClicked
        dashboard dash = new dashboard();
        dash.show();
        dispose();
    }//GEN-LAST:event_dashMouseClicked

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
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel clients;
    private GradientPanel.color1 color16;
    private GradientPanel.color1 color17;
    private GradientPanel.color1 color18;
    private GradientPanel.color1 color20;
    private javax.swing.JLabel dash;
    private javax.swing.JLabel datetime;
    private javax.swing.JLabel disco;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel paid;
    private javax.swing.JLabel rev;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
