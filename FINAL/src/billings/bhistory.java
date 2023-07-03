
package billings;

import SMS.sms_p1rww;
import clients.client;
import com.toedter.calendar.JMonthChooser;
import disconnect.dis;
import java.awt.AWTException;
import wbms1.dashboard;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Robot;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class bhistory extends javax.swing.JFrame {
    
    

    public bhistory() { 
        
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
    Timer timer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String formattedDate = dateFormat.format(new Date());
            datetime2.setText(formattedDate);
        }
    });
    timer.start();
    initComponents();


TableColumn column = null;

column = jTable1.getColumnModel().getColumn(0); // id
column.setPreferredWidth(4);

column = jTable1.getColumnModel().getColumn(1); // client
column.setPreferredWidth(200);

column = jTable1.getColumnModel().getColumn(2); // bill date
column.setPreferredWidth(15);

column = jTable1.getColumnModel().getColumn(3); // payment date
column.setPreferredWidth(15);

column = jTable1.getColumnModel().getColumn(4); // due date
column.setPreferredWidth(15);

column = jTable1.getColumnModel().getColumn(5); // amount
column.setPreferredWidth(15);

column = jTable1.getColumnModel().getColumn(6); // received amount
column.setPreferredWidth(15);

column = jTable1.getColumnModel().getColumn(7); // status
column.setPreferredWidth(15);

    Connect();
    update_table();
    show_table();
    
    
        
    JMonthChooser monthChooser = new JMonthChooser();
    JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"All", "Paid", "Unpaid"});

    // Add PropertyChangeListener to JMonthChooser
    monthChooser.addPropertyChangeListener("month", new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            // Get the selected month
            int month = monthChooser.getMonth();
            // Do something with the selected month
            System.out.println("Selected month: " + month);
        }
    });

    // Add ActionListener to JComboBox
    statusComboBox.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the selected status
            String status = (String) statusComboBox.getSelectedItem();
            // Do something with the selected status
            System.out.println("Selected status: " + status);
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
        Logger.getLogger(bhistory.class.getName()).log(Level.SEVERE, null, ex);
        System.out.println("Failed to connect to database.");
    }
}

   
public void update_table() {
    try {
        String sql = "SELECT c.id, CONCAT(c.code) AS client_name, b.bill_date, b.pay_date, CONCAT(c.code, ' - ', c.firstname, ' ', c.lastname) AS client_name, b.due_date,b.amount, p.r_amount, b.status "
                   + "FROM billing_list b "
                   + "INNER JOIN client_list c ON b.client_id = c.id "
                   + "LEFT JOIN payments p ON b.id = p.bill_id";
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
            double r_amount = rs.getDouble("r_amount");
            String status = rs.getString("status");

            Object[] row = {id, client_name, bill_date, pay_date, due_date, amount,r_amount, status};
            model.addRow(row);            
        }
    } catch (SQLException ex) {
        Logger.getLogger(bhistory.class.getName()).log(Level.SEVERE, null, ex);
    }
}


public ArrayList<billing> billingList() {
    ArrayList<billing> billingList = new ArrayList<>();
    try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/wbill_db", "root", "");
        String query = "SELECT bl.id, c.code, c.firstname, c.lastname, bl.bill_date, bl.pay_date, bl.due_date, bl.status,bl.amount, p.r_amount "
             + "FROM billing_list bl "
             + "INNER JOIN client_list c ON bl.client_id = c.id "
             + "LEFT JOIN payments p ON bl.id = p.bill_id";

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
            double r_amount = rs.getDouble("r_amount"); // replace amount with r_amount from payments table
            String status = rs.getString("status");
            billing newBilling = new billing(id, code, firstname + " " + lastname, bill_date, pay_date, due_date, amount, r_amount, status);
            billingList.add(newBilling);
        }
        con.close();
    } catch (SQLException ex) {
        Logger.getLogger(bhistory.class.getName()).log(Level.SEVERE, null, ex);
    }
    return billingList;
}

public void show_table() {
    try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/wbill_db", "root", "");
        String query = "SELECT b.id, CONCAT(c.code) AS client_name, b.bill_date, b.pay_date, b.due_date , b.amount, p.r_amount, b.status FROM billing_list b INNER JOIN client_list c ON b.client_id = c.id LEFT JOIN payments p ON b.id = p.bill_id";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Client Name", "Bill Date", "Pay Date", "Due Date", "Amount", "Received Amount", "Status"}, 0);
        while (rs.next()) {
            int id = rs.getInt("id");
            String client_name = rs.getString("client_name");
            Date bill_date = rs.getDate("bill_date");
            Date pay_date = rs.getDate("pay_date");
            Date due_date = rs.getDate("due_date");
            double amount = rs.getDouble("amount");
            double r_amount = rs.getDouble("r_amount");
            String status = rs.getString("status");
            model.addRow(new Object[]{id, client_name, bill_date, pay_date, due_date, amount, r_amount, status});
        }
        jTable1.setModel(model);
        con.close();
    } catch (SQLException ex) {
        Logger.getLogger(bhistory.class.getName()).log(Level.SEVERE, null, ex);
    }
}

private void filterData(String status, int month) {
    
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    jTable1.setRowSorter(sorter);
    
    List<RowFilter<Object,Object>> filters = new ArrayList<>();
    filters.add(RowFilter.regexFilter(status, 6)); // 6 is the column index where the status is located
    
    if (month != 0) {
        filters.add(RowFilter.dateFilter(
            ComparisonType.AFTER, 
            new GregorianCalendar(2023, month - 1, 1).getTime(), 
            4)); // 4 is the column index where the date is located
        filters.add(RowFilter.dateFilter(
            ComparisonType.BEFORE, 
            new GregorianCalendar(2023, month, 1).getTime(), 
            4)); // 4 is the column index where the date is located
    }
    
    sorter.setRowFilter(RowFilter.andFilter(filters));
}

private void activityPrintIncrement() {
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
        String incrementQuery = "UPDATE administrator_activity SET print_act = print_act + 1 WHERE id = ?";
        PreparedStatement incrementStatement = con.prepareStatement(incrementQuery);
        incrementStatement.setInt(1, maxId);
        incrementStatement.executeUpdate();
        incrementStatement.close();

        con.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to increment print_act column!");
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        JComboBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        printBtn = new button.MyButton();
        JMonthChooser = new com.toedter.calendar.JMonthChooser();
        color3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        datetime2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        dash1 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTable1.setFont(new java.awt.Font("Bookman Old Style", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Client Code", "Bill Date", "Payment Date", "Due Date", "Amount", "Received Amount", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
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
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 31)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/filter (1) (1).png"))); // NOI18N
        jLabel1.setText("Filter");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        JComboBox.setFont(new java.awt.Font("SansSerif", 0, 18)); // NOI18N
        JComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Paid", "Unpaid", " " }));
        JComboBox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JComboBoxMouseClicked(evt);
            }
        });
        JComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JComboBoxActionPerformed(evt);
            }
        });
        JComboBox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JComboBoxKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bookman Old Style", 1, 24)); // NOI18N
        jLabel4.setText("Bill History");

        printBtn.setBackground(new java.awt.Color(0, 153, 255));
        printBtn.setForeground(new java.awt.Color(255, 255, 255));
        printBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water/printing (1).png"))); // NOI18N
        printBtn.setText("Print");
        printBtn.setBorderColor(new java.awt.Color(0, 153, 255));
        printBtn.setColor(new java.awt.Color(0, 153, 255));
        printBtn.setColorClick(new java.awt.Color(0, 153, 255));
        printBtn.setColorOver(new java.awt.Color(0, 153, 255));
        printBtn.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        printBtn.setRadius(50);
        printBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printBtnActionPerformed(evt);
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
                        .addGap(29, 29, 29)
                        .addComponent(JMonthChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82)
                        .addComponent(JComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(482, 482, 482)
                        .addComponent(printBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1141, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(19, 19, 19)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(JMonthChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(printBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39))
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

        jLabel29.setBackground(java.awt.Color.lightGray);
        jLabel29.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel29.setText("Invoice");
        jLabel29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel29MouseClicked(evt);
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

        jLabel28.setBackground(java.awt.Color.lightGray);
        jLabel28.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel28.setText("Bill History               >");
        jLabel28.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel28MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
                        .addGroup(color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dash1)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        color3Layout.setVerticalGroup(
            color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color3Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                .addComponent(dash1)
                .addGap(18, 18, 18)
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(color3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 718, Short.MAX_VALUE)
            .addComponent(color3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void myButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_myButton3ActionPerformed

    }//GEN-LAST:event_myButton3ActionPerformed

    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBtnActionPerformed

        MessageFormat header = new MessageFormat("Billing Month");
        MessageFormat footer = new MessageFormat("Page {0, number, integer}");
        
        try
        {
            activityPrintIncrement();
            jTable1.print(JTable.PrintMode.FIT_WIDTH,header,footer);
            
        }
        
        catch(java.awt.print.PrinterException e)
        {
            System.err.format("No Printer found", e.getMessage());
        }
    

    }//GEN-LAST:event_printBtnActionPerformed

    private void JComboBoxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JComboBoxKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_JComboBoxKeyPressed

    private void JComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JComboBoxActionPerformed

    }//GEN-LAST:event_JComboBoxActionPerformed

    private void JComboBoxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JComboBoxMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_JComboBoxMouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        int selectedMonth = JMonthChooser.getMonth() + 1; // add 1 since January is month 0
        String selectedStatus = (String) JComboBox.getSelectedItem();

        String query;
        if (selectedStatus.equals("All")) {
            query = "SELECT b.id, c.code, b.bill_date, b.pay_date, b.due_date, b.amount,p.r_amount, b.status "
            + "FROM billing_list b "
            + "INNER JOIN client_list c ON b.client_id = c.id "
            + "LEFT JOIN payments p ON b.id = p.bill_id "
            + "WHERE MONTH(b.bill_date) = ?";
        } 
        else {
            query = "SELECT b.id,c.code, b.bill_date, b.pay_date, b.due_date, b.amount,p.r_amount, b.status "
            + "FROM billing_list b "
            + "INNER JOIN client_list c ON b.client_id = c.id "
            + "LEFT JOIN payments p ON b.id = p.bill_id "
            + "WHERE MONTH(b.bill_date) = ? AND b.status = ?";
        }

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/wbill_db", "root", "");
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, selectedMonth);
            if (!selectedStatus.equals("All")) {
                pst.setString(2, selectedStatus);
            }
            ResultSet rs = pst.executeQuery();

            // Save the current column widths
            int[] columnWidths = new int[jTable1.getColumnCount()];
            for (int i = 0; i < jTable1.getColumnCount(); i++) {
                columnWidths[i] = jTable1.getColumnModel().getColumn(i).getWidth();
            }

            DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Client Code", "Bill Date", "Payment Date", "Due Date", "Amount", "Received Amount", "Status"}, 0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String client_name = rs.getString("code");
                Date bill_date = rs.getDate("bill_date");
                Date pay_date = rs.getDate("pay_date");
                Date due_date = rs.getDate("due_date");
                double amount = rs.getDouble("amount");
                double r_amount = rs.getDouble("r_amount");
                String status = rs.getString("status");
                model.addRow(new Object[]{id, client_name, bill_date, pay_date, due_date, amount, r_amount, status});
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

    }//GEN-LAST:event_jLabel1MouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

    }//GEN-LAST:event_jTable1MouseClicked

    private void jLabel27MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel27MouseClicked
        client cl = new client();
        cl.show();
        dispose();
    }//GEN-LAST:event_jLabel27MouseClicked

    private void jLabel28MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel28MouseClicked
        bhistory b = new bhistory();
        b.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel28MouseClicked

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

    private void jLabel32MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel32MouseClicked
        billpayment bill = new billpayment();
        bill.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel32MouseClicked

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
            java.util.logging.Logger.getLogger(bhistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(bhistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(bhistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(bhistory.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new bhistory().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> JComboBox;
    private com.toedter.calendar.JMonthChooser JMonthChooser;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private button.MyButton printBtn;
    // End of variables declaration//GEN-END:variables
}
