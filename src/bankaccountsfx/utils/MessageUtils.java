/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankaccountsfx.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;

/**
 *
 * @author Jose J. Pardines Garcia
 */
public class MessageUtils {
    
    /**
     * Shows an error message with the params
     * @param header
     * @param message 
     */
    public static void showError(String header, String message){
        
        Alert alert = new Alert( AlertType.ERROR );
        alert.setTitle( "Error Dialog" );
        alert.setHeaderText( header );
        alert.setContentText( message );

        alert.showAndWait();
        
    }
    
    /**
     * Shows an information message with the params
     * @param header
     * @param message 
     */
    public static void showMessage(String header, String message){
        
        Alert alert = new Alert( AlertType.INFORMATION );
        alert.setTitle( "Information Dialog" );
        alert.setHeaderText( header );
        alert.setContentText( message );

        alert.showAndWait();
        
    }
    
}
