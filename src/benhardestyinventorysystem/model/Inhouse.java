/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyinventorysystem.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Ben Hardesty
 */
public class Inhouse extends Part {
    
    // ID of the machine that built the part.
    private final IntegerProperty machineID;
    
    /**
     * Default constructor.
     * 
     * @param partID
     * @param name
     * @param price
     * @param inStock
     * @param min
     * @param max
     * @param machineID 
     */
    public Inhouse(int partID, String name, double price, int inStock, int min, int max, int machineID) {
        super(partID, name, price, inStock, min, max);
        
        this.machineID = new SimpleIntegerProperty(machineID);
    }
    
    public void setMachineID(int machineID) {
        this.machineID.set(machineID);
    }
    
    public int getMachineID() {
        return machineID.get();
    }
    
    public IntegerProperty machineIDProperty() {
        return machineID;
    }
    
}
