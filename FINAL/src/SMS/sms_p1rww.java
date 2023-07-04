package SMS;

import billings.bhistory;
import billings.billpayment;
import billings.invoice;
import clients.client;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import disconnect.dis;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.Timer;
import wbms1.dashboard;

public class sms_p1rww extends javax.swing.JFrame {

    SerialPort port;
    OutputStream out;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    String message;

    public sms_p1rww() {
        initComponents();
        
        endline.setSelectedItem("NO LINE ENDING");
        com.setSelectedItem("");

        Timer timer = new Timer(0, (ActionEvent e) -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd / MM / yyyy HH:mm:ss");
            date_year.setText(dateFormat.format(new java.util.Date()));
            
            SimpleDateFormat monthandyear = new SimpleDateFormat("MMMM yyyy");
            String month_year = monthandyear.format(new Date());
            date_year.setText(month_year);
        });

        // Start the timer
        timer.start();
        
        // Initialize the serial port
        port = SerialPort.getCommPort(""); // Replace with the desired port index
        port.openPort();
        port.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }
            
            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    byte[] buffer = new byte[port.bytesAvailable()];
                    int numRead = port.readBytes(buffer, buffer.length);
                    String receivedData = new String(buffer, 0, numRead);
                    // Process the received data
                    processReceivedData(receivedData);
                    
                    
                }
                
            }
            
        });

        jComboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedOption = (String) jComboBox1.getSelectedItem();

                if (selectedOption.equals("ANNOUNCEMENT")){
                    btn_send.setEnabled(true);
                    textfield.setText(" ");
                    
                } else if (selectedOption.equals("DISCONNECTION NOTICE")){
                    btn_send.setEnabled(true);
                    cb_Disconnection();
                    
                } else if (selectedOption.equals("DUE DATE REMINDER")){
                    btn_send.setEnabled(true);
                    cb_DueDate();
                
                } else if (selectedOption.equals("FOR NEWLY ADDED CLIENTS")){
                    btn_send.setEnabled(true);
                    cb_GreetingAddClient();
                    
                }
            }
        });
        
    }
    
    private void cb_GreetingAddClient(){
        
        try {
            ResultSet rs;
            PreparedStatement pst = con.prepareStatement("SELECT c.firstname, c.code, b.amount FROM client_list c LEFT JOIN billing_list b ON c.id = b.client_id WHERE c.id = ?;");
            pst.setString(1, idsearch.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
                String name = rs.getString("firstname");
                String code = rs.getString("code");
                String amount = rs.getString("amount");

                // Retrieve the current date using SimpleDateFormat
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
                String month = dateFormat.format(new Date());

                message = "Magandang Araw, " + name + "! \r\r" +
                            "Malugod naming ipinaaabot sa inyo ang aming mainit na pagbati! \r\r Ang inyong account number ay " + code + ". Ang billings ay pina-paalala namin tuwing 10th ng buwan, samantala naman ang pagbabayad ay tuwing ika-15 at ika-18 ng buwan. Paalala upang hindi masuspende ang inyong water service, pina-paalala namin na magbayad bago ang itinakdang araw ng pagbabayad. Kung mayroon po kayong mga katanungan o mga hiling, huwag po kayong mag-atubiling makipag-ugnayan sa aming tanggapan. Handa po kaming tumugon at magbigay ng tulong sa anumang oras.\r\r Maraming salamat po!";
                textfield.setText(message); // Set the message to the text field
            }
        } catch (SQLException ex) {
            Logger.getLogger(sms_p1rww.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Failed to retrieve data from the database.");
        }
    }
    
    private void cb_DueDate(){
        
        try {
            ResultSet rs;
            PreparedStatement pst = con.prepareStatement("SELECT c.firstname, c.code, b.amount FROM client_list c LEFT JOIN billing_list b ON c.id = b.client_id WHERE c.id = ?;");
            pst.setString(1, idsearch.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
                String name = rs.getString("firstname");
                String code = rs.getString("code");
                String amount = rs.getString("amount");

                // Retrieve the current date using SimpleDateFormat
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
                String month = dateFormat.format(new Date());

                message = "Magandang Araw, " + name + "! \r\r" +
                                 "Good Day, This is to inform you that your current bill for this month of " + month  + " is " + amount + " pesos. Kindly settle it on or before " + month + " 18. Thank you." + "\r\r";

                textfield.setText(message); // Set the message to the text field
            }
        } catch (SQLException ex) {
            Logger.getLogger(sms_p1rww.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Failed to retrieve data from the database.");
        }
    }

    private void cb_Disconnection(){
            
        try {
            ResultSet rs;
            PreparedStatement pst = con.prepareStatement("SELECT c.firstname, c.code, b.amount FROM client_list c LEFT JOIN billing_list b ON c.id = b.client_id WHERE c.id = ?;");
            pst.setString(1, idsearch.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
                String name = rs.getString("firstname");
                String code = rs.getString("code");
                String amount = rs.getString("amount");

                // Retrieve the current date using SimpleDateFormat
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
                String month = dateFormat.format(new Date());

                message = "Magandang Araw, " + name + "! \r\r" +
                                "Ang inyong tubig ay pansamantala naming sinuspendido sa kadahilanang may natitira ka pang P" + amount + " sa inyong account " + code + ". Upang maiwasan ang ganitong situwasyon ay pinapayuhan namin kayo na magbayad bago ang petsa ng " + month + ". Mangyaring magbayad sa pinakamalapit na payment center sa inyong lugar. \r\r" + "Maraming salamat.";
                textfield.setText(message); // Set the message to the text field
            }
        } catch (SQLException ex) {
            Logger.getLogger(sms_p1rww.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Failed to retrieve data from the database.");
        }
        
    }
    
    private boolean isConnected = false;
    
    public void processReceivedData(String data) {
        // Process the received data here
        // ...
    }

    {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbill_db", "root", "");
            System.out.println("Connected to database.");
        } catch (SQLException ex) {
            Logger.getLogger(sms_p1rww.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Failed to connect to the database.");
        }
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
        String incrementQuery = "UPDATE administrator_activity SET sms_sent_act = sms_sent_act + 1 WHERE id = ?";
        PreparedStatement incrementStatement = con.prepareStatement(incrementQuery);
        incrementStatement.setInt(1, maxId);
        incrementStatement.executeUpdate();
        incrementStatement.close();

        con.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to increment sms_sent_act column!");
    }
}
    





    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        idsearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        textfield = new javax.swing.JTextArea();
        com = new javax.swing.JComboBox<>();
        date_year = new javax.swing.JLabel();
        endline = new javax.swing.JComboBox<>();
        label3 = new javax.swing.JLabel();
        label2 = new javax.swing.JLabel();
        num = new javax.swing.JLabel();
        label1 = new javax.swing.JLabel();
        label4 = new javax.swing.JLabel();
        search = new javax.swing.JButton();
        cnum = new javax.swing.JTextField();
        setcnum = new javax.swing.JButton();
        btn_send = new javax.swing.JButton();
        conn = new javax.swing.JButton();
        sms_pa = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        name = new javax.swing.JLabel();
        code = new javax.swing.JLabel();
        bal = new javax.swing.JLabel();
        clear = new javax.swing.JButton();
        all_con = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        color3 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        dash2 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel2.setBackground(java.awt.SystemColor.controlLtHighlight);
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.black, java.awt.Color.white, java.awt.Color.black, java.awt.Color.white));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        idsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idsearchActionPerformed(evt);
            }
        });
        jPanel2.add(idsearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, 223, 33));

        textfield.setBackground(java.awt.SystemColor.control);
        textfield.setColumns(20);
        textfield.setRows(5);
        textfield.setMaximumSize(new java.awt.Dimension(10, 10));
        jScrollPane1.setViewportView(textfield);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 440, 320));

        com.setForeground(new java.awt.Color(255, 255, 255));
        com.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "COM9", "\t\t\t\t\t\t\t\t\t\t" }));
        com.setToolTipText("");
        com.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                comPopupMenuWillBecomeVisible(evt);
            }
        });
        com.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comActionPerformed(evt);
            }
        });
        jPanel2.add(com, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 450, 130, 30));

        date_year.setText("Year_Current_time");
        jPanel2.add(date_year, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 610, 161, -1));

        endline.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NO LINE ENDING", "NEW LINE ", " " }));
        endline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endlineActionPerformed(evt);
            }
        });
        jPanel2.add(endline, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 490, 130, 34));

        label3.setText("Customer Code");
        jPanel2.add(label3, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 190, -1, -1));

        label2.setText("Unpaid Balance");
        jPanel2.add(label2, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 220, -1, -1));

        num.setText("******************");
        jPanel2.add(num, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 250, -1, -1));

        label1.setText("Contact Number");
        jPanel2.add(label1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 250, -1, -1));

        label4.setText("Customer Name");
        jPanel2.add(label4, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 160, -1, -1));

        search.setText("Search Client name or ID");
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });
        jPanel2.add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, 180, -1));

        cnum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cnumActionPerformed(evt);
            }
        });
        jPanel2.add(cnum, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 300, 210, 30));

        setcnum.setText("Set Client's Contact Number");
        setcnum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setcnumActionPerformed(evt);
            }
        });
        jPanel2.add(setcnum, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 340, -1, -1));

        btn_send.setText("Send");
        btn_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendActionPerformed(evt);
            }
        });
        jPanel2.add(btn_send, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 590, 80, 40));

        conn.setText("Connect");
        conn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connActionPerformed(evt);
            }
        });
        jPanel2.add(conn, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 590, 90, 40));

        sms_pa.setText("Send Public Announcement");
        sms_pa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sms_paActionPerformed(evt);
            }
        });
        jPanel2.add(sms_pa, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 590, 190, 40));

        jLabel3.setText("COM");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 460, -1, -1));

        jLabel2.setText("END LINE");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 500, -1, -1));

        name.setText("******************");
        jPanel2.add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 160, -1, -1));

        code.setText("******************");
        jPanel2.add(code, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 190, -1, -1));

        bal.setText("******************");
        jPanel2.add(bal, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 220, -1, -1));

        clear.setText("Clear");
        clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearActionPerformed(evt);
            }
        });
        jPanel2.add(clear, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 370, -1, 40));

        all_con.setText("Select  All Contacts");
        all_con.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                all_conActionPerformed(evt);
            }
        });
        jPanel2.add(all_con, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 550, 150, 40));

        jLabel15.setText("TYPE OF MESSAGE:");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 520, -1, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ANNOUNCEMENT", "DISCONNECTION NOTICE", "DUE DATE REMINDER", "FOR NEWLY ADDED CLIENTS" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel2.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, 210, -1));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 140, 220, 140));

        color3.setBackground(new java.awt.Color(51, 204, 255));

        jLabel16.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 36)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("P1RWW");

        jLabel33.setBackground(java.awt.Color.lightGray);
        jLabel33.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel33.setText("Clients");
        jLabel33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel33MouseClicked(evt);
            }
        });

        jLabel34.setBackground(java.awt.Color.lightGray);
        jLabel34.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel34.setText("Disconnected Clients");
        jLabel34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel34MouseClicked(evt);
            }
        });

        jLabel35.setBackground(java.awt.Color.lightGray);
        jLabel35.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel35.setText("Invoice");
        jLabel35.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel35MouseClicked(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jLabel36.setBackground(java.awt.Color.lightGray);
        jLabel36.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel36.setText("SMS                          >");
        jLabel36.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel36MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        dash2.setBackground(java.awt.Color.lightGray);
        dash2.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        dash2.setText("Dashboard");
        dash2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dash2MouseClicked(evt);
            }
        });

        jLabel37.setBackground(new java.awt.Color(0, 0, 0));
        jLabel37.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel37.setText("Bill Payments");
        jLabel37.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel37MouseClicked(evt);
            }
        });

        jLabel38.setBackground(java.awt.Color.lightGray);
        jLabel38.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        jLabel38.setText("Bill History");
        jLabel38.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel38MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout color3Layout = new javax.swing.GroupLayout(color3);
        color3.setLayout(color3Layout);
        color3Layout.setHorizontalGroup(
            color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, color3Layout.createSequentialGroup()
                        .addGap(0, 18, Short.MAX_VALUE)
                        .addGroup(color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(dash2)
                                .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        color3Layout.setVerticalGroup(
            color3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(color3Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(dash2)
                .addGap(18, 18, 18)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(color3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 747, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(color3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void comPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_comPopupMenuWillBecomeVisible
        com.removeAllItems();
        SerialPort []portLists = SerialPort.getCommPorts();

        for
        (SerialPort port : portLists) {
            com.addItem(port.getSystemPortName());
        }
    }//GEN-LAST:event_comPopupMenuWillBecomeVisible

    private void idsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idsearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idsearchActionPerformed

    private void comActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_comActionPerformed

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
     if (port == null || !port.isOpen()) {
    JOptionPane.showMessageDialog(this, "Port is not connected.", "Error", JOptionPane.ERROR_MESSAGE);
} else {
    if (idsearch.getText().isEmpty()) {
        name.setText("******************");
        code.setText("******************");
        bal.setText("******************");
        num.setText("******************");
        JOptionPane.showMessageDialog(new JFrame(), "The fields cannot be left blank.", "Message", JOptionPane.INFORMATION_MESSAGE);
    } else {
        try {
            ResultSet rs;
            PreparedStatement pst = con.prepareStatement("SELECT c.id, c.code, c.firstname, c.contact, b.amount FROM client_list c LEFT JOIN billing_list b ON c.id = b.client_id WHERE c.firstname = ? OR c.id = ?;");
            pst.setString(1, idsearch.getText());
            pst.setString(2, idsearch.getText());
            rs = pst.executeQuery();

      if (rs.next()) {
        name.setText(rs.getString("firstname"));
        code.setText(rs.getString("code"));
        bal.setText(rs.getString("amount"));
        num.setText(rs.getString("contact"));
        String existingContact = cnum.getText(); // Get the existing contact numbers in the cnum text field
        String newContact = rs.getString("contact");
        if (!existingContact.isEmpty()) {
            newContact = existingContact + ", " + newContact; // Append the new contact number to the existing ones
        }
        cnum.setText(newContact); // Set the retrieved or existing contact numbers in the cnum text field
    } else {
        name.setText("******************");
        code.setText("******************");
        bal.setText("******************");
        JOptionPane.showMessageDialog(new JFrame(), "Client not Found in Database.", "Message", JOptionPane.INFORMATION_MESSAGE);
    }
} catch (Exception e) {
    e.printStackTrace();
}}
     }

    }//GEN-LAST:event_searchActionPerformed

    private void cnumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cnumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cnumActionPerformed

    private void setcnumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setcnumActionPerformed

        out = port.getOutputStream();
        String data = "";

        data = cnum.getText();

        switch (endline.getSelectedIndex()) {
            case 0 -> data = cnum.getText();
        }

        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: No contact number entered.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                out.write(data.getBytes());
                JOptionPane.showMessageDialog(this, "Recipient number set successfully.", "Success", JOptionPane.INFORMATION_MESSAGE); // Prompt user for successful recipient number set
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); // Display error dialog with the exception message
            }
        }
    }//GEN-LAST:event_setcnumActionPerformed
    
    private void btn_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendActionPerformed

    if (idsearch.getText().isEmpty()) {
            JOptionPane.showMessageDialog(new JFrame(), "The ID field cannot be left blank.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
                    
            out = port.getOutputStream();
            
                // Default message if no specific option is selected
                message = textfield.getText();
            
            try {
                
                out.write(message.getBytes());
                JOptionPane.showMessageDialog(sms_p1rww.this, "Message Sent");
                
                // admin activity method DONT REMOVE
                activityClientIncrement();
                
                
            } catch (IOException ex) {
                
                JOptionPane.showMessageDialog(sms_p1rww.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                
            }
        }
    }//GEN-LAST:event_btn_sendActionPerformed

    private void connActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connActionPerformed
        if (isConnected) {
            JOptionPane.showMessageDialog(this, "Already connected to the port.", "Connection Status", JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                SerialPort[] portLists = SerialPort.getCommPorts();
                port = portLists[com.getSelectedIndex()];
                port.openPort();

                if (port.isOpen()) {
                    JOptionPane.showMessageDialog(this, port.getDescriptivePortName() + "\n\n Port Connected");
                    isConnected = true; // Update the connection status to true
                    conn.setEnabled(false); // Disable the connect button

                } else {
                    JOptionPane.showMessageDialog(this, port.getDescriptivePortName() + "\n\n Port Connection failed");
                }
            } catch (ArrayIndexOutOfBoundsException a) {
                JOptionPane.showMessageDialog(this, "Please Choose Communication Port!", "ERROR", JOptionPane.ERROR_MESSAGE);
            } catch (Exception b) {
                JOptionPane.showMessageDialog(this, b, "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_connActionPerformed

    private void sms_paActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sms_paActionPerformed

    if (port == null || !port.isOpen()) {
        JOptionPane.showMessageDialog(this, "Port is not connected.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
                String message = textfield.getText().trim(); // Get the message from the text field and remove leading/trailing whitespace

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
            }
        }
    }//GEN-LAST:event_sms_paActionPerformed

    private void clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearActionPerformed
        if (cnum.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please set the recipient number first.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (port != null && port.isOpen()) {
                try {
                    cnum.setText("");
                    String command = "clear"; // Command to clear the saved contacts
                    out.write(command.getBytes());
                    JOptionPane.showMessageDialog(this, "Contacts cleared successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Port is not connected.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_clearActionPerformed

    private void all_conActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_all_conActionPerformed
    if (port == null || !port.isOpen()) {
        JOptionPane.showMessageDialog(this, "Port is not connected.", "Error", JOptionPane.ERROR_MESSAGE);
    } else {
            try {
                StringBuilder contactNumbers = new StringBuilder(); // Store all contact numbers

                // Retrieve all contact numbers from the database
                String query = "SELECT contact FROM client_list";
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    String contact = resultSet.getString("contact");
                    contactNumbers.append(contact).append(", "); // Append each contact number with a comma and space
                }

                // Remove the last comma and space
                if (contactNumbers.length() > 0) {
                    contactNumbers.setLength(contactNumbers.length() - 2);
                }

                    // Set the contact numbers to the cnum text field
                    cnum.setText(contactNumbers.toString());

                    JOptionPane.showMessageDialog(this, "Contact numbers retrieved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_all_conActionPerformed

    private void jLabel33MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel33MouseClicked
        client cl = new client();
        cl.show();
        dispose();
    }//GEN-LAST:event_jLabel33MouseClicked

    private void jLabel34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseClicked
        dis dis = new dis();
        dis.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel34MouseClicked

    private void jLabel35MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel35MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel35MouseClicked

    private void jLabel36MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel36MouseClicked
        invoice in = new invoice();
        in.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel36MouseClicked

    private void dash2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dash2MouseClicked
        // TODO add your handling code here:
        dashboard dash = new dashboard();
        dash.show();
        dispose();
    }//GEN-LAST:event_dash2MouseClicked

    private void jLabel37MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel37MouseClicked
        billpayment bill = new billpayment();
        bill.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel37MouseClicked

    private void jLabel38MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel38MouseClicked
        bhistory b = new bhistory();
        b.show();
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jLabel38MouseClicked

    private void endlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endlineActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_endlineActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed
        
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
            java.util.logging.Logger.getLogger(sms_p1rww.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(sms_p1rww.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(sms_p1rww.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(sms_p1rww.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new sms_p1rww().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton all_con;
    private javax.swing.JLabel bal;
    private javax.swing.JButton btn_send;
    private javax.swing.JButton clear;
    private javax.swing.JTextField cnum;
    private javax.swing.JLabel code;
    private javax.swing.JPanel color3;
    private javax.swing.JComboBox<String> com;
    private javax.swing.JButton conn;
    private javax.swing.JLabel dash2;
    private javax.swing.JLabel date_year;
    private javax.swing.JComboBox<String> endline;
    private javax.swing.JTextField idsearch;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label1;
    private javax.swing.JLabel label2;
    private javax.swing.JLabel label3;
    private javax.swing.JLabel label4;
    private javax.swing.JLabel name;
    private javax.swing.JLabel num;
    private javax.swing.JButton search;
    private javax.swing.JButton setcnum;
    private javax.swing.JButton sms_pa;
    private javax.swing.JTextArea textfield;
    // End of variables declaration//GEN-END:variables

}
