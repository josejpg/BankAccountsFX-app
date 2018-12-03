/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankaccountsfx.model;

import bankaccountsfx.utils.FileUtils;
import static bankaccountsfx.utils.MessageUtils.showError;
import java.io.UnsupportedEncodingException;
import java.text.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jose J. Pardines Garcia
 */
public class Transaction {
    
    String accountNumber;
    Date date;
    String description;
    float amount;
    
    /**
     * Init a Transaction object with all of its data
     * @param accountNumber
     * @param date
     * @param description
     * @param amount 
     */
    public Transaction( 
        String accountNumber,
        Date date,
        String description,
        float amount
    ){
        
        this.accountNumber = accountNumber;
        this.date = date;
        this.description = description;
        this.amount = amount;
        
    }
    
    /**
     * Save a new account number in Transaction object
     * @param newAccountNumber 
     */
    
    public void setAccountNumber( String newAccountNumber ){ this.accountNumber = newAccountNumber; }
    
    /**
     * Get the account number from Transaction object
     * @return String
     */
    
    public String getAccountNumber() { return this.accountNumber; }
    
    /**
     * Save a new transaction data in Transaction object
     * @param newDate 
     */
    
    public void setDate( Date newDate ){ this.date = newDate; }
    
    /**
     * Get the transaction data in format (dd/MM/yyyy) from Transaction object
     * @return String
     */
    
    public String getDate() { 
        
        DateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
        return dateFormat.format( this.date ); 
        
    } 
    
    /**
     * Save a new transaction description in Transaction object
     * @param newDescription 
     */
    
    public void setDescription( String newDescription ){ this.description = newDescription; }
    
    /**
     * Get the transaction description from Transaction object
     * @return String
     */
    
    public String getDescription() { return this.description; }
    
    /**
     * Save a new transaction amount in Transaction object
     * @param newAmount 
     */
    
    public void setAmount( Float newAmount ){ this.amount = newAmount; }
    
    /**
     * Get the transaction amount from Transaction object
     * @return Float
     */
    
    public Float getAmount() { return this.amount; }
    
    @Override
    public String toString(){ 
                    
       return this.accountNumber + 
               ";" + 
               getDate() + 
               ";" + 
               this.description + 
               ";" + 
               this.amount;
       
    }
    
}
