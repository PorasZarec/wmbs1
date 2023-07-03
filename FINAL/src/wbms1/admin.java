package wbms1;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Nokashi
 */
public class admin {
    
    private String userActivity;
    private String logInActivity;
    private String addClientsActivity;
    private String disconnectClientsActivity;
    private String payBillActivity;
    private String printActivity;
    private String createBillActivity;
    private String reconnectActivity;
    private String smsSentActivity;
    
    public admin(String userActivity, String logInActivity, String addClientsActivity, String disconnectClientsActivity, String payBillActivity, String printActivity, String createBillActivity, String reconnectActivity, String smsSentActivity) {
        
        this.userActivity = userActivity;
        this.logInActivity = logInActivity;
        this.addClientsActivity = addClientsActivity;
        this.disconnectClientsActivity = disconnectClientsActivity;
        this.payBillActivity = payBillActivity;
        this.printActivity = printActivity;
        this.createBillActivity = createBillActivity;
        this.reconnectActivity = reconnectActivity;
        this.smsSentActivity = smsSentActivity;
    }
    
    public String getUserActivity() {
        return userActivity;
    }
    
    public String getLogInActivity() {
        return logInActivity;
    }

    public String getAddClientsActivity() {
        return addClientsActivity;
    }

    public String getDisconnectClientsActivity() {
        return disconnectClientsActivity;
    }

    public String getPayBillActivity() {
        return payBillActivity;
    }

    public String getPrintActivity() {
        return printActivity;
    }

    public String getCreateBillActivity() {
        return createBillActivity;
    }

    public String getReconnectActivity() {
        return reconnectActivity;
    }

    public String getSMSSentActivity() {
        return smsSentActivity;
    }
}
    
