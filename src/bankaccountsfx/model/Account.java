/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankaccountsfx.model;

import bankaccountsfx.utils.FileUtils;
import static bankaccountsfx.utils.MessageUtils.showError;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jose J. Pardines Garcia
 */
public class Account {
    
    String accountNumber;
    String owner;

    /**
     * Init a Account object with all of its data
     * @param accountNumber
     * @param owner 
     */
    public Account( 
        String accountNumber,
        String owner 
    ){
        
        this.accountNumber = accountNumber;
        this.owner = owner;
        
    }
    
    /**
     * Save a new account number in Account object
     * @param newAccountNumber 
     */
    public void setAccountNumber( String newAccountNumber ){ this.accountNumber = newAccountNumber; }
    
    /**
     * Get the account number from Account object
     * @return String
     */
    
    public String getAccountNumber() { return this.accountNumber; } 
    
    /**
     * Save a new owner in Account object
     * @param newOwner 
     */
    
    public void setOwner( String newOwner ){ this.owner = newOwner; }
    
    /**
     * Get the owner from Account object
     * @return String
     */
    
    public String getOwner() { return this.owner; }
    
    @Override
    public String toString(){ 
                    
       return this.accountNumber + 
               ";" + 
               this.owner;
       
    }
    
}
