/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package billings;

public class Client {
    private int id;
    private String clientName;

    public Client(int id, String clientName) {
        this.id = id;
        this.clientName = clientName;
    }

    public int getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    @Override
    public String toString() {
        return clientName;
    }

    Object getClientID() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    Object getFirstName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    Object getLastName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    Object getAddress() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

