/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyinventorysystem.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Ben Hardesty
 */
public class Outsourced extends Part {
    
    // Name of the company that made the part.
    private final StringProperty companyName;
    
    /**
     * Default constructor.
     * 
     * @param partID
     * @param name
     * @param price
     * @param inStock
     * @param min
     * @param max
     * @param companyName 
     */
    public Outsourced(int partID, String name, double price, int inStock, int min, int max, String companyName) {
        super(partID, name, price, inStock, min, max);
        
        this.companyName = new SimpleStringProperty(companyName);
    }
    
    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }
    
    public String getCompanyName() {
        return companyName.get();
    }
    
    public StringProperty companyNameProperty() {
        return companyName;
    }
}
