
package billings;

import java.util.concurrent.TimeUnit;
import SMS.sms_p1rww;
import clients.User;
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

import clients.client;
import disconnect.dis;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import wbms1.dashboard;


public class invoice extends javax.swing.JFrame {

        /**
     * Creates new form invoice
     */
    public invoice() {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
    Timer timer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String formattedDate = dateFormat.format(new Date());
            datetime2.setText(formattedDate);
        }
    });
    timer.start();

        initComponents();
         Connect();
    update_table();
    showTable();
    
                 TableColumn column = null;
        column = table.getColumnModel().getColumn(0); // id
        column.setPreferredWidth(10);
         column = table.getColumnModel().getColumn(1); // client_name
        column.setPreferredWidth(200);
         column = table.getColumnModel().getColumn(2); // reading_date
        column.setPreferredWidth(50);
        column = table.getColumnModel().getColumn(3); // reading_date
        column.setPreferredWidth(50);
        column = table.getColumnModel().getColumn(4); // due_date
        column.setPreferredWidth(50);
      
        
    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int selectedRow = table.getSelectedRow();

            if (selectedRow != -1) {
                // Get the values from the selected row
                Object selectedIDObj = table.getValueAt(selectedRow, 0);
                Object selectedNameObj = table.getValueAt(selectedRow, 1);
                Object selectedDateFromObj = table.getValueAt(selectedRow, 2);
                Object selectedDateToObj = table.getValueAt(selectedRow, 3);

                // Convert the values to strings
                String selectedID = String.valueOf(selectedIDObj);
                String selectedName = String.valueOf(selectedNameObj);
                String selectedDateFrom = String.valueOf(selectedDateFromObj);
                String selectedDateTo = String.valueOf(selectedDateToObj);
//                JOptionPane.showMessageDialog(rootPane, selectedID);

                receipt.someFunction(selectedID, selectedName, selectedDateFrom, selectedDateTo);
            }
        }
    }
});
        
    
    }
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
     
    
   public void Connect(){
    try {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db","root","");
        System.out.println("Connected to database.");
    } catch (SQLException ex) {
        Logger.getLogger(invoice.class.getName()).log(Level.SEVERE, null, ex);
        System.out.println("Failed to connect to database.");
    }
}
public void update_table() {
    try {
        String sql = "SELECT b.id, CONCAT(c.code, ' - ', c.firstname, ' ', c.lastname) AS client_name, b.bill_date, p.payment_date, b.status "
                   + "FROM billing_list b "
                   + "INNER JOIN client_list c ON b.client_id = c.id "
                   + "LEFT JOIN payments p ON b.id = p.bill_id "
                   + "WHERE b.status = 'Paid'";
        pst = con.prepareStatement(sql);
        rs = pst.executeQuery();

        // clear the table before populating it with new data
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        // iterate through the result set and populate the table
        while (rs.next()) {
            int id = rs.getInt("id");
            String client_name = rs.getString("client_name");
            String bill_date = rs.getString("bill_date");
            String payment_date = rs.getString("payment_date");
            String status = rs.getString("status");

            Object[] row = {id, client_name, bill_date, payment_date, status};
            model.addRow(row);
        }
    } catch (SQLException ex) {
        Logger.getLogger(invoice.class.getName()).log(Level.SEVERE, null, ex);
    }
}
public ArrayList<paid> paidList() {
    ArrayList<paid> userList = new ArrayList<>();
    try {
        if (con != null) {
            String query1 = "SELECT * FROM billing_list WHERE status = 'Paid'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query1);
            while (rs.next()) {
                int id = rs.getInt("id");
                String code = rs.getString("code");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String bill_date = rs.getString("bill_date");
                String payment_date = rs.getString("payment_date");
                String status = rs.getString("status");
              
                paid newUser = new paid(id, code, firstname, lastname, bill_date, payment_date, status);
                userList.add(newUser);
            }
            rs.close(); // Close the res ult set
        }
    } catch (SQLException ex) {
        Logger.getLogger(invoice.class.getName()).log(Level.SEVERE, null, ex);
    }
    return userList;
}
public void showTable() {
    try {
        String userListSql = "SELECT bl.id, CONCAT(cl.code, ' - ', cl.firstname, ' ', cl.lastname) AS client_name, bl.bill_date, p.payment_date, bl.status "
                           + "FROM billing_list bl "
                           + "JOIN client_list cl ON bl.client_id = cl.id "
                           + "JOIN payments p ON bl.id = p.bill_id "
                           + "WHERE bl.status = 'Paid'";
        pst = con.prepareStatement(userListSql);
        rs = pst.executeQuery();
        DefaultTableModel tblModel = (DefaultTableModel) table.getModel();
        tblModel.setRowCount(0); // Clear the table
        while (rs.next()) {
            Object[] row = new Object[5]; // Create a new row inside the loop
            int client_id = rs.getInt("id");
            String client_name = rs.getString("client_name");
            String bill_date = rs.getString("bill_date");
            String payment_date = rs.getString("payment_date");
            String status = rs.getString("status");
            row[0] = client_id;
            row[1] = client_name;
            row[2] = bill_date;
            row[3] = payment_date;
            row[4] = status;
            tblModel.addRow(row); // Add the row to the table
        }
        rs.close(); // Close the result set
    } catch (SQLException ex) {
        Logger.getLogger(invoice.class.getName()).log(Level.SEVERE, null, ex);
    }
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
    String status = "Unpaid";

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
        Logger.getLogger(invoice.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    
    public void selectRowToPrint(){
        
    }

private void activityCreateBillIncrement() {
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
        String incrementQuery = "UPDATE administrator_activity SET create_bill_act = create_bill_act + 1 WHERE id = ?";
        PreparedStatement incrementStatement = con.prepareStatement(incrementQuery);
        incrementStatement.setInt(1, maxId);
        incrementStatement.executeUpdate();
        incrementStatement.close();

        con.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to increment create_bill_act column!");
    }
}
            // Disable the myButton3 button initially

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        unpaid = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jMonthChooser1 = new com.toedter.calendar.JMonthChooser();
        myButton2 = new button.MyButton();
        myButton3 = new button.MyButton();
        color3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        datetime2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        dash1 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Bookman Old Style", 1, 24)); // NOI18N
        jLabel4.setText("Bill Invoice");

        table.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        table.setFont(new java.awt.Font("Bookman Old Style", 0, 14)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Bill Id", "Client Name", "Bill Date", "Payment Date", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setGridColor(new java.awt.Color(255, 255, 255));
        table.setRowHeight(50);
        table.setSelectionBackground(new java.awt.Color(0, 153, 153));
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(table);

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 31)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/filter (1) (1).png"))); // NOI18N
        jLabel1.setText("Filter");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        myButton2.setBackground(new java.awt.Color(102, 204, 255));
        myButton2.setForeground(new java.awt.Color(255, 255, 255));
        myButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/bill (1).png"))); // NOI18N
        myButton2.setText("Create bill");
        myButton2.setBorderColor(new java.awt.Color(102, 204, 255));
        myButton2.setColor(new java.awt.Color(102, 204, 255));
        myButton2.setColorClick(new java.awt.Color(102, 204, 255));
        myButton2.setColorOver(new java.awt.Color(102, 204, 255));
        myButton2.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        myButton2.setRadius(50);
        myButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myButton2ActionPerformed(evt);
            }
        });

        myButton3.setBackground(new java.awt.Color(102, 204, 255));
        myButton3.setForeground(new java.awt.Color(255, 255, 255));
        myButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/printing (1).png"))); // NOI18N
        myButton3.setText("Print");
        myButton3.setBorderColor(new java.awt.Color(102, 204, 255));
        myButton3.setColor(new java.awt.Color(102, 204, 255));
        myButton3.setColorClick(new java.awt.Color(153, 153, 153));
        myButton3.setColorOver(new java.awt.Color(102, 204, 255));
        myButton3.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        myButton3.setRadius(50);
        myButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                myButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(26, 26, 26)
                            .addComponent(jMonthChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(515, 515, 515)
                            .addComponent(myButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addComponent(unpaid))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 955, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(400, 400, 400)
                        .addComponent(myButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel4)
                .addGap(19, 19, 19)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jMonthChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(myButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unpaid)
                .addGap(11, 11, 11)
                .addComponent(myButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
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

        jLabel30.setBackground(java.awt.Color.lightGray);
        jLabel30.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel30.setText("Disconnected Clients");
        jLabel30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel30MouseClicked(evt);
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

        datetime2.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        datetime2.setText("0");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel29.setBackground(java.awt.Color.lightGray);
        jLabel29.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel29.setText("Invoice                    >");
        jLabel29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel29MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        javax.swing.GroupLayout color3Layout = new javax.swing.GroupLayout(color3);
        color3.setLayout(color3Layout);
        color3Layout.setHorizontalGroup(
            color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(color3Layout.createSequentialGroup()
                        .addComponent(datetime2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(dash1)
                                .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        color3Layout.setVerticalGroup(
            color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color3Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                .addComponent(dash1)
                .addGap(18, 18, 18)
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83)
                .addComponent(datetime2)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(color3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(color3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void myButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myButton3ActionPerformed
                receipt r = new receipt();
                r.setVisible(true);
                
                
                
//                r.dispose();
    }//GEN-LAST:event_myButton3ActionPerformed

    private void myButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myButton2ActionPerformed
try {
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost/wbill_db", "root", "");
    String query = "SELECT * FROM client_list";
    Statement st = con.createStatement();
    ResultSet rs = st.executeQuery(query);
    int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to create bills for all clients?", "Confirmation", JOptionPane.YES_NO_OPTION);

    if (confirmed == JOptionPane.YES_OPTION) {
        while (rs.next()) {
            int clientId = rs.getInt("id");
            String client_id = rs.getString("code");
            String clientName = rs.getString("code") + " - " + rs.getString("firstname") + " " + rs.getString("lastname");

            // Check if the client has three or more unpaid bills
            query = "SELECT COUNT(*) AS unpaid_bills_count FROM billing_list WHERE client_id = ? AND status = 'Unpaid'";
            PreparedStatement pstCheck = con.prepareStatement(query);
            pstCheck.setInt(1, clientId);
            ResultSet rsCheck = pstCheck.executeQuery();
            rsCheck.next();
            int unpaidBillsCount = rsCheck.getInt("unpaid_bills_count");
            if (unpaidBillsCount >= 3) {
                JOptionPane.showMessageDialog(null, clientName + " has three or more unpaid bills. A new bill was not created.");
            } else {
                // Set the default value for the amount
                double amount = 180.0;

                // Set the default value for the status to unpaid
                String status = "Unpaid";

                // Set the pay date to the first day of the next month
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DATE, 15);
                Date payDate = calendar.getTime();

                // Set the due date to the 8th day of the next month
                calendar.set(Calendar.DATE, 18);
                Date dueDate = calendar.getTime();

                // Insert the new bill into the billing_list table
                
//                client_id
                query = "INSERT INTO billing_list (client_id, bill_date, pay_date, due_date, amount, status) VALUES (?, ?, ?, ?, ?,?)";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setInt(1, clientId);
                pst.setDate(2, new java.sql.Date(new Date().getTime()));
                pst.setDate(3, new java.sql.Date(payDate.getTime()));
                pst.setDate(4, new java.sql.Date(dueDate.getTime()));
                pst.setDouble(5, amount);
                pst.setString(6, status);
                pst.executeUpdate();
                
                activityCreateBillIncrement();

              
            }
        }
        JOptionPane.showMessageDialog(null, "Bills have been created for all clients.");

    } else {
        JOptionPane.showMessageDialog(null, "Bills creation cancelled.");
    }
    con.close();
} catch (SQLException ex) {
    Logger.getLogger(invoice.class.getName()).log(Level.SEVERE, null, ex);
}

       
    }//GEN-LAST:event_myButton2ActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
     
int selectedMonth = jMonthChooser1.getMonth() + 1; // add 1 since January is month 0

try {
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost/wbill_db", "root", "");
    String query = "SELECT bl.id, CONCAT(cl.code, ' - ', cl.firstname, ' ', cl.lastname) AS client_name, bl.bill_date, p.payment_date, bl.status "
                           + "FROM billing_list bl "
                           + "JOIN client_list cl ON bl.client_id = cl.id "
                           + "JOIN payments p ON bl.id = p.bill_id "
                           + "WHERE MONTH(p.payment_date) = ?";
    PreparedStatement pst = con.prepareStatement(query);
    pst.setInt(1, selectedMonth);
    ResultSet rs = pst.executeQuery();

    // Save the current column widths
    int[] columnWidths = new int[table.getColumnCount()];
    for (int i = 0; i < table.getColumnCount(); i++) {
        columnWidths[i] = table.getColumnModel().getColumn(i).getWidth();
    }

    DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Client Name", "Bill Date", "Payment Date",  "Status"}, 0);

    while (rs.next()) {
        int id = rs.getInt("id");
        String client_name = rs.getString("client_name");
        Date bill_date = rs.getDate("bill_date");
        Date payment_date = rs.getDate("payment_date");
        String status = rs.getString("status");
        model.addRow(new Object[]{id, client_name, bill_date, payment_date, status});
    }

    table.setModel(model);

    // Set the saved column widths
    for (int i = 0; i < table.getColumnCount(); i++) {
        table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
    }

    con.close();
} catch (SQLException ex) {
    Logger.getLogger(invoice.class.getName()).log(Level.SEVERE, null, ex);
}
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jMonthChooser1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMonthChooser1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jMonthChooser1MouseClicked

    private void jLabel27MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel27MouseClicked
        client cl = new client();
        cl.show();
        dispose();
    }//GEN-LAST:event_jLabel27MouseClicked

    private void jLabel29MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel29MouseClicked
        invoice in = new invoice();
        in.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel29MouseClicked

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

    private void jLabel28MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel28MouseClicked
        bhistory b = new bhistory();
        b.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel28MouseClicked

    private void jLabel32MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel32MouseClicked
        billpayment bill = new billpayment();
        bill.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel32MouseClicked

    private void dash1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dash1MouseClicked
        // TODO add your handling code here:
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
            java.util.logging.Logger.getLogger(invoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(invoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(invoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(invoice.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new invoice().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel color3;
    private javax.swing.JLabel dash1;
    private javax.swing.JLabel datetime2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private com.toedter.calendar.JMonthChooser jMonthChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private button.MyButton myButton2;
    private button.MyButton myButton3;
    private javax.swing.JTable table;
    private javax.swing.JLabel unpaid;
    // End of variables declaration//GEN-END:variables

    
    
}
