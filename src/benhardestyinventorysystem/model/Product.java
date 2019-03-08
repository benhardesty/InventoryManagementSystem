/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyinventorysystem.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Ben Hardesty
 */
public class Product {
    
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private final IntegerProperty productID;
    private final StringProperty name;
    private final DoubleProperty price;
    private final IntegerProperty inStock;
    private final IntegerProperty min;
    private final IntegerProperty max;
    
    /**
     * Default constructor.
     * 
     * @param productID
     * @param name
     * @param price
     * @param inStock
     * @param min
     * @param max
     * @param associatedParts 
     */
    public Product(int productID, String name, double price, int inStock, int min, int max, ObservableList associatedParts) {
        this.productID = new SimpleIntegerProperty(productID);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.inStock = new SimpleIntegerProperty(inStock);
        this.min = new SimpleIntegerProperty(min);
        this.max = new SimpleIntegerProperty(max);
        this.associatedParts.addAll(associatedParts);
    }
    
    public void setProductID(int productID) {
        this.productID.set(productID);
    }
    
    public int getProductID() {
        return productID.get();
    }
    
    public IntegerProperty productIDProperty() {
        return productID;
    }
    
    public void setName(String name) {
        this.name.set(name);
    }
    
    public String getName() {
        return name.get();
    }
    
    public StringProperty nameProperty() {
        return name;
    }
    
    public void setPrice(double price) {
        this.price.set(price);
    }
    
    public double getPrice() {
        return price.get();
    }
    
    public DoubleProperty priceProperty() {
        return price;
    }
    
    public void setInStock(int inStock) {
        this.inStock.set(inStock);
    }
    
    public int getInStock() {
        return inStock.get();
    }
    
    public IntegerProperty inStockProperty() {
        return inStock;
    }
    
    public void setMin(int min) {
        this.min.set(min);
    }
    
    public int getMin() {
        return min.get();
    }
    
    public IntegerProperty minProperty() {
        return min;
    }
    
    public void setMax(int max) {
        this.max.set(max);
    }
    
    public int getMax() {
        return max.get();
    }
    
    public IntegerProperty maxProperty() {
        return max;
    }
    
    public void addAssociatedPart(Part part) {
        this.associatedParts.add(part);
    }
    
    public boolean removeAssociatedPart(int index) {
        if (index >= 0 && index < this.associatedParts.size()) {
            this.associatedParts.remove(index);
            return true;
        } else {
            return false;
        }
    }
    
    public Part lookupAssociatedPart(int index) {
        if (index >= 0 && index < this.associatedParts.size()) {
            return associatedParts.get(index);
        } else {
            return null;
        }
    }
    
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }
}
