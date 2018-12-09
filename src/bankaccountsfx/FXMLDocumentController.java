/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankaccountsfx;

import bankaccountsfx.model.*;
import bankaccountsfx.utils.*;
import static bankaccountsfx.utils.FileUtils.*;
import static bankaccountsfx.utils.MessageUtils.*;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.logging.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;

/**
 *
 * @author Jose J. Pardines Garcia
 */
public class FXMLDocumentController implements Initializable {
    
    private Label label;
    @FXML
    private TextField accountNumber;
    @FXML
    private TextField owner;
    @FXML
    private Label totalSalary;
    @FXML
    private Button btnAccountAdd;
    @FXML
    private ComboBox<Account> cmbAccount;
    @FXML
    private TextField transactionDescription;
    @FXML
    private TextField transactionAmount;
    @FXML
    private DatePicker transactionDate;
    @FXML
    private Button btnTransactionAdd;
    @FXML
    private Button btnTransactionChart;
    @FXML
    private ComboBox<String> cmbFilter;
    @FXML
    private TableView<Transaction> tableTransactions;
    private List<String> params;
    public List<Account> listAccounts;
    public List<Transaction> listTransactions;
    
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            /**
             * Styles
             */
            btnAccountAdd.setStyle("-fx-text-fill: white; -fx-background-color: #000000");
            btnTransactionAdd.setStyle("-fx-text-fill: white; -fx-background-color: #000000");
            btnTransactionChart.setStyle("-fx-text-fill: white; -fx-background-color: #000000");
            
            btnTransactionAdd.setDisable( true );
            btnTransactionChart.setDisable( true );
            transactionDate.setDisable( true );
            transactionDescription.setDisable( true );
            transactionAmount.setDisable( true );
            cmbFilter.setDisable( true );
            
            /**
             * Add filters
             */
            cmbFilter.getItems().add( "Filter" );
            cmbFilter.getItems().add( "Date" );
            cmbFilter.getItems().add( "Description" );
            cmbFilter.getItems().add( "Amount" );
            cmbFilter.getItems().add( "Income" );
            cmbFilter.getItems().add( "Expenses" );
            cmbFilter.getSelectionModel().selectFirst();
            
            /**
             * Listener: Shows all of Transactions filtered for an account in a table
             */
            cmbFilter.setOnAction((ActionEvent e) -> {
                
                try{
                     if( ( cmbAccount.getValue() == null || 
                        cmbAccount.getValue().toString().trim().equals( "" ) ) &&
                        !cmbFilter.getValue().equals( "Filter" ) ) {
                    
                        showError( "Error!", "An account has to be selected." );

                    }else{
                        switch( cmbFilter.getValue() ){
                            case "Date":

                                if( transactionDate.getValue() == null ||
                                    transactionDate.getValue().toString().trim().equals( "" ) ){
                                    
                                    showError( "Error!", "A date has to be selected." );
                                    
                                }else{

                                    DateFormat dateFormat = new SimpleDateFormat( "dd/MM/yyyy" );
                                    String date = dateFormat.format( 
                                        Date.from( 
                                            Instant.from(
                                                transactionDate
                                                .getValue()
                                                .atStartOfDay(
                                                    ZoneId.systemDefault()
                                                )
                                            ) 
                                        ) 
                                    );



                                    listTransactions = loadTransactionsForAccount( cmbAccount.getValue(), "date", date );
                                }
                                break;
                            case "Description":
                                
                                if( transactionDescription.getText().trim().equals( "" ) ){
                                    
                                    showError( "Error!", "A description has to be write." );
                                    
                                }else{
                                
                                    listTransactions = loadTransactionsForAccount( cmbAccount.getValue(), "description", transactionDescription.getText() );
                                    
                                }
                                break;
                                
                            case "Amount":
                                
                                if( transactionAmount.getText().trim().equals( "" ) ){
                                    
                                    showError( "Error!", "An amount has to be write." );
                                    
                                }else{
                                    
                                    listTransactions = loadTransactionsForAccount( cmbAccount.getValue(), "amount", transactionAmount.getText() );
                                    
                                }
                                break;
                                
                            case "Income":
                                
                                listTransactions = loadTransactionsForAccount( cmbAccount.getValue(), "+", "" );

                                break;
                                
                            case "Expenses":
                                
                                listTransactions = loadTransactionsForAccount( cmbAccount.getValue(), "-", "" );

                                break;
                            default:
                                
                                listTransactions = loadTransactionsForAccount( cmbAccount.getValue(), null, null );
                                
                                break;
                        }
                        createTable();
                    }
                    
                }catch(IOException | NullPointerException ex){
                     Logger
                            .getLogger( FileUtils.class.getName() )
                            .log( Level.SEVERE, null, ex );
                    showError( FileUtils.class.getName(), ex.getMessage() );
                }
                
            });
            
            /**
             * Shows all of Accounts in a combo box
             */
            listAccounts = loadAccounts();
            cmbAccount.getItems().addAll( listAccounts );

            /**
             * Listener: Shows all of Transactions for an account in a combo box and a table
             */
            cmbAccount.setOnAction((ActionEvent e) -> {
                
                try {
                    
                    listTransactions = loadTransactionsForAccount( cmbAccount.getValue(), null, null );

                    createTable();
                    
                    btnTransactionAdd.setDisable( false );
                    btnTransactionChart.setDisable( false );
                    transactionDate.setDisable( false );
                    transactionDescription.setDisable( false );
                    transactionAmount.setDisable( false );
                    cmbFilter.setDisable( false );
                    
                } catch ( IOException | NullPointerException ex ) {
                    
                    Logger
                            .getLogger( FileUtils.class.getName() )
                            .log( Level.SEVERE, null, ex );
                    showError( FileUtils.class.getName(), ex.getMessage() );
                    
                }
            });
            
            /**
             * Listener: Save a new account with form data
             */
            btnAccountAdd.setOnAction( ( ActionEvent e ) -> {
                
                saveNewAccount();
                
            });
            
            /**
             * Listener: Save a new transaction with form data
             */
            btnTransactionAdd.setOnAction( ( ActionEvent e ) -> {
                
                saveNewTransaction();
                
            });
            
        }catch( FileNotFoundException e ){
            
            Logger
            .getLogger( FileUtils.class.getName() )
            .log( Level.SEVERE, null, e );
            showError( FileUtils.class.getName(), e.getMessage() );
                    
        }catch( IOException | NullPointerException e ){
            
            Logger
            .getLogger( FileUtils.class.getName() )
            .log( Level.SEVERE, null, e );
            showError( FileUtils.class.getName(), e.getMessage() );
            
        }
    }    
    
    /**
     * Create a Table with a transaction's list
     */
    private void createTable(){
        
        tableTransactions.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        TableColumn<Transaction, String> dateColumn = new TableColumn( "Date" );
        dateColumn.setPrefWidth( 113.2 );
        dateColumn.setSortable( true );
        dateColumn.setResizable( false );
        dateColumn.setCellValueFactory(
            new PropertyValueFactory( "date" )
        );
        TableColumn<Transaction, Float> descriptionColumn = new TableColumn( "Description" );
        descriptionColumn.setPrefWidth( 339.4 );
        descriptionColumn.setSortable( true );
        descriptionColumn.setResizable( false );
        descriptionColumn.setCellValueFactory(
            new PropertyValueFactory( "description" )
        );
        TableColumn amountColumn = new TableColumn( "Amount" );
        amountColumn.setPrefWidth( 113.2 );
        amountColumn.setSortable( true );
        amountColumn.setResizable( false );
        amountColumn.setCellValueFactory(
            new PropertyValueFactory( "amount" )
        );
        
        tableTransactions.setOnMouseClicked( (e) -> {
        
        } );

        tableTransactions.getColumns().clear();
        tableTransactions.getItems().clear();
        tableTransactions.getColumns().addAll( dateColumn, descriptionColumn, amountColumn );
        tableTransactions.getItems().addAll( listTransactions );
        
        
        totalSalary.setText( FileUtils.getTotalAmount( listTransactions ).toString() + "€" );
    }
    
    /**
     * Save a new account with form data in DB
     */
    private void saveNewAccount(){
        
        if( accountNumber.getText().trim().equals( "" ) ){
                    
            showError( "Error!", "Account number is required." );

        }else if( owner.getText().trim().equals( "" ) ){

            showError( "Error!", "Owner is required." );

        }else{

            try {
                Account newAccount = new Account(
                        accountNumber.getText(),
                        owner.getText()
                );
                listAccounts.add(
                        newAccount
                );
                saveAccounts( listAccounts );
                cmbAccount.getItems().add( newAccount );
                showMessage( "Succesful!", "New account was registered successfully." );

            } catch ( IOException | NullPointerException ex ) {

                Logger
                        .getLogger( FileUtils.class.getName() )
                        .log( Level.SEVERE, null, ex );
                showError( FileUtils.class.getName(), ex.getMessage() );

            }

        }
        
    }
    
    /**
     * Save a new transaction with form data in DB
     */
    
    private void saveNewTransaction(){
        
        if( cmbAccount.getValue() == null || cmbAccount.getValue().toString().trim().equals( "" ) ) {
                    
            showError( "Error!", "An account has to be selected." );

        }else if( transactionDate.getValue() == null || transactionDate.getValue().toString().trim().equals( "" ) ){

            showError( "Error!", "Transaction's date is required." );

        }else if( transactionDescription.getText().trim().equals( "" ) ){

            showError( "Error!", "Transaction's description is required." );

        }else if( transactionAmount.getText().trim().equals( "" ) ){

            showError( "Error!", "Transaction's amount is required." );

        }else{

            try {

                Account accountSelected = cmbAccount.getValue();
                Instant instant = Instant.from(
                        transactionDate
                                .getValue()
                                .atStartOfDay(
                                        ZoneId.systemDefault()
                                )
                );

                Transaction newTransaction = new Transaction(
                        accountSelected.getAccountNumber(),
                        Date.from( instant ),
                        transactionDescription.getText(),
                        Float.parseFloat( transactionAmount.getText() )
                );

                listTransactions.add(
                        newTransaction
                );
                saveTransactions( listTransactions );
                showMessage( "Succesful!", "New account was registered successfully." );
                
                createTable();

            } catch ( IOException | NullPointerException | NumberFormatException ex ) {

                Logger
                        .getLogger( FileUtils.class.getName() )
                        .log( Level.SEVERE, null, ex );
                showError( FileUtils.class.getName(), ex.getMessage() );

            }

        }
        
    }
    
}
