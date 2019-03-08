/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyinventorysystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Ben Hardesty
 */
public class Inventory {
    
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> products = FXCollections.observableArrayList();
    
    /**
     * Default constructor.
     */
    public Inventory() {
        
    }
    
    /**
     * Additional constructor. Not currently used, but conceivable that it might 
     * be needed in the future.
     * 
     * @param parts
     * @param products 
     */
    public Inventory(ObservableList<Part> parts, ObservableList<Product> products) {
        this.allParts = parts;
        this.products = products;
    }
    
    public void addProduct(Product product) {
        this.products.add(product);
    }
    
    public boolean removeProduct(int index) {
        if (index >= 0 && index < this.products.size()) {
            products.remove(index);
            return true;
        } else {
            return false;
        }
    }
    
    public Product lookupProduct(int index) {
        if (index >= 0 && index < this.products.size()) {
            return this.products.get(index);
        } else {
            return null;
        }
    }
    
    public void updateProduct(int index, Product product) {
        if (index >= 0 && index < products.size()) {
            products.set(index, product);
        }
    }
    
    public void addPart(Part part) {
        this.allParts.add(part);
    }
    
    public boolean deletePart(Part part) {
        if (part == null) {
            return false;
        }
        return allParts.remove(part);
    }
    
    public Part lookupPart(int index) {
        if (index >= 0 && index < this.allParts.size()) {
            return allParts.get(index);
        } else {
            return null;
        }
    }
    
    public void updatePart(int index, Part part) {
        if (index >= 0 && index < this.allParts.size()) {
            allParts.set(index, part);
        }
    }
    
    /**
     * Returns the parts in the inventory as an ObservableList.
     * @return 
     */
    public ObservableList<Part> getParts() {
        return allParts;
    }
    
    /**
     * Returns the products in the inventory as an ObservableList.
     * @return 
     */
    public ObservableList<Product> getProducts() {
        return products;
    }
}
