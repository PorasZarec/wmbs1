/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billings;

import java.util.concurrent.TimeUnit;
import SMS.sms_p1rww;
import clients.client;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import wbms1.dashboard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.table.TableModel;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JDayChooser;
import disconnect.dis;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class billpayment extends javax.swing.JFrame {

    public billpayment() {
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
         
         
//    update_table();
//    show_table();
    
    
             TableColumn column = null;
        column = jTable1.getColumnModel().getColumn(0); // id
        column.setPreferredWidth(10);
         column = jTable1.getColumnModel().getColumn(1); // client_name
        column.setPreferredWidth(300);
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
        loadData();
    }
    


 Connection con;
    PreparedStatement pst;
    ResultSet rs;
     
    
   public void Connect(){
    try {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db","root","");
        System.out.println("Connected to database.");
    } catch (SQLException ex) {
        Logger.getLogger(billpayment.class.getName()).log(Level.SEVERE, null, ex);
        System.out.println("Failed to connect to database.");
    }
}
   
   private void loadData(){
        int selectedMonth = JMonthChooser.getMonth() + 1; // add 1 since January is month 0

try {
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost/wbill_db", "root", "");
    String query = "SELECT b.id, CONCAT(c.code, ' - ', c.firstname, ' ', c.lastname) AS client_name, b.bill_date, b.due_date, b.amount, b.status "
            + "FROM billing_list b "
            + "INNER JOIN client_list c ON b.client_id = c.id "
            + "WHERE MONTH(b.bill_date) = ? AND b.status = 'Unpaid'";
    PreparedStatement pst = con.prepareStatement(query);
    pst.setInt(1, selectedMonth);
    ResultSet rs = pst.executeQuery();

    // Save the current column widths
    int[] columnWidths = new int[jTable1.getColumnCount()];
    for (int i = 0; i < jTable1.getColumnCount(); i++) {
        columnWidths[i] = jTable1.getColumnModel().getColumn(i).getWidth();
    }

    DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Client Name", "Bill Date", "Due Date", "Amount", "Status"}, 0);

    while (rs.next()) {
        int id = rs.getInt("id");
        String client_name = rs.getString("client_name");
        Date bill_date = rs.getDate("bill_date");
        Date due_date = rs.getDate("due_date");
        double amount = rs.getDouble("amount");
        String status = rs.getString("status");
        model.addRow(new Object[]{id, client_name, bill_date, due_date, amount, status});
    }

    jTable1.setModel(model);

    // Set the saved column widths
    for (int i = 0; i < jTable1.getColumnCount(); i++) {
        jTable1.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
    }

    con.close();
} catch (SQLException ex) {
    Logger.getLogger(bhistory.class.getName()).log(Level.SEVERE, null, ex);
}
    }
   
   
   
   
   public void update_table() {
    try {
        String sql = "SELECT b.id, CONCAT(c.code, ' - ', c.firstname, ' ', c.lastname) AS client_name, b.bill_date, b.pay_date, CONCAT(c.code, ' - ', c.firstname, ' ', c.lastname) AS client_name, b.due_date, b.amount, b.status "
                   + "FROM billing_list b "
                   + "INNER JOIN client_list c ON b.client_id = c.id";
        pst = con.prepareStatement(sql);
        rs = pst.executeQuery();

        // clear the table before populating it with new data
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        // iterate through the result set and populate the table
        while (rs.next()) {
            int id = rs.getInt("id");
            String client_name = rs.getString("client_name");
            String bill_date = rs.getString("bill_date");
            String pay_date = rs.getString("pay_date");
            String due_date = rs.getString("due_date");
            double amount = rs.getDouble("amount");
            String status = rs.getString("status");

            Object[] row = {id, client_name, bill_date, pay_date, due_date, amount, status};
            model.addRow(row);
            
            
        }
    } catch (SQLException ex) {
        Logger.getLogger(billpayment.class.getName()).log(Level.SEVERE, null, ex);
    }
}


public ArrayList<billing> billingList() {
    ArrayList<billing> billingList = new ArrayList<>();
    try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/wbill_db", "root", "");
        String query = "SELECT billing_list.*, client_list.code, client_list.firstname, client_list.lastname " 
             + "FROM billing_list " 
             + "INNER JOIN client_list ON billing_list.client_id = client_list.id";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            int id = rs.getInt("id");
            String code = rs.getString("code");
            String firstname = rs.getString("firstname");
            String lastname = rs.getString("lastname");
            Date bill_date = rs.getDate("bill_date");
            Date pay_date = rs.getDate("pay_date");
            Date due_date = rs.getDate("due_date");
            double amount = rs.getDouble("amount");
            String status = rs.getString("status");
           billing newBilling = new billing(id, code, firstname + " " + lastname, bill_date, pay_date, due_date, amount, status);
            billingList.add(newBilling);
        }
        con.close();
    } catch (SQLException ex) {
        Logger.getLogger(billpayment.class.getName()).log(Level.SEVERE, null, ex);
    }
    return billingList;
}
public void show_table() {
    try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/wbill_db", "root", "");
        String query = "SELECT b.id, CONCAT(c.code, ' - ', c.firstname, ' ', c.lastname) AS client_name, b.bill_date, b.pay_date, b.due_date, b.amount, b.status FROM billing_list b INNER JOIN client_list c ON b.client_id = c.id WHERE b.status != 'Paid'";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Client Name", "Bill Date", "Pay Date", "Due Date", "Amount", "Status"}, 0);
        while (rs.next()) {
            int id = rs.getInt("id");
            String client_name = rs.getString("client_name");
            Date bill_date = rs.getDate("bill_date");
            Date pay_date = rs.getDate("pay_date");
            Date due_date = rs.getDate("due_date");
            double amount = rs.getDouble("amount");
            String status = rs.getString("status");
            model.addRow(new Object[]{id, client_name, bill_date, pay_date, due_date, amount, status});
        }
        jTable1.setModel(model);
        con.close();
    } catch (SQLException ex) {
        Logger.getLogger(billpayment.class.getName()).log(Level.SEVERE, null, ex);
    }
}


private ResultSet getUnpaidBills(Object clientID) throws SQLException {
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
    Statement stmt = con.createStatement();
    String query = "SELECT id, name, amount, status FROM billing_list WHERE status <> 'Paid'";
    return stmt.executeQuery(query);
}


public void createBillsForAllClients() {
    try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/wbill_db", "root", "");
        String query = "INSERT INTO billing_list (client_id, bill_date, pay_date, due_date, amount, status) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(query);
        
        // Get current date
        LocalDate currentDate = LocalDate.now();
        
        // Set due date to 8th day of next month
        LocalDate dueDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth().plus(1), 8);
        
        // Get all clients from client_list table
        String getClientsQuery = "SELECT * FROM client_list";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(getClientsQuery);
        
        // Loop through all clients and create a bill for each one
        while (rs.next()) {
    int clientId = rs.getInt("id");
    java.sql.Date bill_date = java.sql.Date.valueOf(currentDate);
    java.sql.Date pay_date = java.sql.Date.valueOf(currentDate);
    java.sql.Date dueDateSql = java.sql.Date.valueOf(dueDate);
    double amount = 180.00;
    String status = "unpaid";

    // Insert bill into billing_list table
    pst.setInt(1, clientId);
    pst.setDate(2, bill_date);
    pst.setDate(3, pay_date);
    pst.setDate(4, dueDateSql);
    pst.setDouble(5, amount);
    pst.setString(6, status);
    pst.executeUpdate();
}

        // Close connections
        rs.close();
        st.close();
        pst.close();
        con.close();
        
    } catch (SQLException ex) {
        Logger.getLogger(billpayment.class.getName()).log(Level.SEVERE, null, ex);
    }
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

        dash = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        myButton2 = new button.MyButton();
        cname = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        JMonthChooser = new com.toedter.calendar.JMonthChooser();
        color1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        datetime = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        dash1 = new javax.swing.JLabel();

        dash.setBackground(java.awt.Color.lightGray);
        dash.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        dash.setText("Dashboard                 >");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTable1.setFont(new java.awt.Font("Bookman Old Style", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Client Name", "Bill Date", "Pay Date", "Due Date", "Amount", "Status"
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
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(400);
        }

        jLabel4.setFont(new java.awt.Font("Bookman Old Style", 1, 24)); // NOI18N
        jLabel4.setText("Bill Payment");

        myButton2.setBackground(new java.awt.Color(102, 204, 255));
        myButton2.setForeground(new java.awt.Color(255, 255, 255));
        myButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/pay (1).png"))); // NOI18N
        myButton2.setText("Pay BIll");
        myButton2.setBorderColor(new java.awt.Color(102, 204, 255));
        myButton2.setColor(new java.awt.Color(102, 204, 255));
        myButton2.setColorOver(new java.awt.Color(102, 204, 255));
        myButton2.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        myButton2.setRadius(50);
        myButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myButton2ActionPerformed(evt);
            }
        });

        cname.setFont(new java.awt.Font("Bookman Old Style", 0, 24)); // NOI18N

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/filter (1) (1).png"))); // NOI18N
        jLabel1.setText("Filter");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        JMonthChooser.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cname)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(JMonthChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(48, 48, 48)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(541, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 426, Short.MAX_VALUE)
                .addComponent(myButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(378, 378, 378))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addGap(23, 23, 23))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel4)
                .addGap(19, 19, 19)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JMonthChooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(myButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cname))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        color1.setBackground(new java.awt.Color(51, 204, 255));

        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/logo (2).png"))); // NOI18N
        jLabel5.setText("P1RWW");

        jLabel15.setBackground(java.awt.Color.lightGray);
        jLabel15.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel15.setText("Clients");
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
        });

        jLabel19.setBackground(java.awt.Color.lightGray);
        jLabel19.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel19.setText("Bill History              >");
        jLabel19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel19MouseClicked(evt);
            }
        });

        jLabel18.setBackground(java.awt.Color.lightGray);
        jLabel18.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel18.setText("Invoice                     >");
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
        jLabel21.setText("SMS                         >");
        jLabel21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel21MouseClicked(evt);
            }
        });

        datetime.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        datetime.setText("0");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel16.setBackground(new java.awt.Color(0, 0, 0));
        jLabel16.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel16.setText("Payment                   >");
        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        dash1.setBackground(java.awt.Color.lightGray);
        dash1.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        dash1.setText("Dashboard          ");
        dash1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dash1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout color1Layout = new javax.swing.GroupLayout(color1);
        color1.setLayout(color1Layout);
        color1Layout.setHorizontalGroup(
            color1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(color1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(color1Layout.createSequentialGroup()
                        .addComponent(datetime)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(color1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dash1)
                            .addGroup(color1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        color1Layout.setVerticalGroup(
            color1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color1Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dash1)
                .addGap(18, 18, 18)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83)
                .addComponent(datetime)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(color1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(color1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void myButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myButton2ActionPerformed

        

        Payment p = new Payment();
        p.show();
        p.dispose();
        
        

//        int rowIndex = jTable1.getSelectedRow();
//        
//        if (rowIndex < 0) {
//            JOptionPane.showMessageDialog(this, "Please select a row.");
//            return;
//        }
//
//        int billId = (int) jTable1.getValueAt(rowIndex, jTable1.getColumnModel().getColumnIndex("ID"));
//
//        double billAmount = (double) jTable1.getValueAt(rowIndex, jTable1.getColumnModel().getColumnIndex("Amount"));
        

//
//        double rAmount = Double.parseDouble(JOptionPane.showInputDialog("Enter amount to pay:"));
//
//        if (rAmount < 0) {
//            
//            JOptionPane.showMessageDialog(this, "Payment amount must be at least " + billAmount);
//            
//            return;
//            
//        } else if (rAmount != billAmount){
//        
//            JOptionPane.showMessageDialog(this, "Payment amount must be at least " + billAmount);
//            
//            return;
//        }
//
//
//        // get current date
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date paymentDate = new Date();
//
//        try {
//        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
//        String sql = "INSERT INTO payments (bill_id, r_amount, payment_date) VALUES (?, ?, ?)";
//        PreparedStatement stmt = con.prepareStatement(sql);
//        stmt.setInt(1, billId);
//        stmt.setDouble(2, rAmount);
//        stmt.setString(3, dateFormat.format(paymentDate));
//        stmt.executeUpdate();
//        // update status to paid in billing list
//        sql = "UPDATE billing_list SET status = 'Paid' WHERE id = ?";
//        stmt = con.prepareStatement(sql);
//        stmt.setInt(1, billId);
//        stmt.executeUpdate();
//        // delete row in bill payment table
//        DefaultTableModel tblModel = (DefaultTableModel)jTable1.getModel();
//        tblModel.removeRow(rowIndex);
//
//        // admin activity
//        activityPayBillIncrement();
//
//        JOptionPane.showMessageDialog(this, "Payment successful.");
//        } catch (SQLException ex) {
//        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
//        }       
    }//GEN-LAST:event_myButton2ActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
            loadData();



//        try {
//            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/wbill_db", "root", "");
//            String query = "SELECT b.id, CONCAT(c.code, ' - ', c.firstname, ' ', c.lastname) AS client_name, b.bill_date, b.pay_date, b.amount, b.status "
//                    + "FROM billing_list b "
//                    + "INNER JOIN client_list c ON b.client_id = c.id "
//                    + "WHERE MONTH(b.bill_date) = ? AND b.status = 'Unpaid'";
//            PreparedStatement pst = con.prepareStatement(query);
//
//            // Retrieve the selected date when the JLabel is clicked
//            Date selectedDate = dateChooser.getDate();
//
//            // Check if a date is selected
//            if (selectedDate != null) {
//                // Set the selected month as a parameter
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(selectedDate);
//                int selectedMonth = cal.get(Calendar.MONTH) + 1;
//                pst.setInt(1, selectedMonth);
//
//                ResultSet rs = pst.executeQuery();
//
//                // Save the current column widths
//                int[] columnWidths = new int[jTable1.getColumnCount()];
//                for (int i = 0; i < jTable1.getColumnCount(); i++) {
//                    columnWidths[i] = jTable1.getColumnModel().getColumn(i).getWidth();
//                }
//
//                DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Client Name", "Bill Date", "Due Date", "Amount", "Status"}, 0);
//
//                while (rs.next()) {
//                    int id = rs.getInt("id");
//                    String client_name = rs.getString("client_name");
//                    Date bill_date = rs.getDate("bill_date");
//                    Date due_date = rs.getDate("pay_date");
//                    double amount = rs.getDouble("amount");
//                    String status = rs.getString("status");
//                    model.addRow(new Object[]{id, client_name, bill_date, due_date, amount, status});
//                }
//
//                jTable1.setModel(model);
//
//                // Set the saved column widths
//                for (int i = 0; i < jTable1.getColumnCount(); i++) {
//                    jTable1.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
//                }
//
//                con.close();
//            } else {
//                // Handle case when no date is selected
//                System.out.println("No date selected");
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(bhistory.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        client cl = new client();
        cl.show();
        dispose();
    }//GEN-LAST:event_jLabel15MouseClicked

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        billpayment bill = new billpayment();
        bill.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel16MouseClicked

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

    private void jLabel20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel20MouseClicked
        dis dis = new dis();
        dis.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel20MouseClicked

    private void jLabel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseClicked
   sms_p1rww sms = new sms_p1rww();
        sms.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel21MouseClicked

    private void dash1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dash1MouseClicked
        dashboard dash = new dashboard();
        dash.show();
        dispose();
    }//GEN-LAST:event_dash1MouseClicked

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
            java.util.logging.Logger.getLogger(billpayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(billpayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(billpayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(billpayment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new billpayment().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JMonthChooser JMonthChooser;
    private javax.swing.JLabel cname;
    private javax.swing.JPanel color1;
    private javax.swing.JLabel dash;
    private javax.swing.JLabel dash1;
    private javax.swing.JLabel datetime;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private button.MyButton myButton2;
    // End of variables declaration//GEN-END:variables

    private List<Client> getClients() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
