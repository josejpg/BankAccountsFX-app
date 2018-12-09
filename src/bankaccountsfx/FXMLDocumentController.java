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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
    @FXML
    private Label textTotal;
    private PieChart blanceChart;
    @FXML
    private AnchorPane panePie;
    @FXML
    private AnchorPane paneData;
    @FXML
    private VBox vBox;
    @FXML
    private PieChart blanceChartPositive;
    @FXML
    private PieChart blanceChartNegative;
    
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
                        
                        filterTransactions();
                         
                    }
                    
                }catch(IOException | NullPointerException ex){
                     Logger
                            .getLogger( FileUtils.class.getName() )
                            .log( Level.SEVERE, null, ex );
                    showError( FileUtils.class.getName(), ex.getMessage() );
                }
                
            });
            
            /**
             * Listener: Shows all of Transactions for an account in a table
             * with a selected data
             */
            transactionDate.setOnAction( ( ActionEvent e ) -> {
                
                try {
                    
                    if( cmbFilter.getValue() != null &&
                        cmbFilter.getValue().trim().equals( "Date" ) ) {
                    
                        filterTransactions();

                    }
                    
                } catch ( IOException | NullPointerException ex ) {
                    
                    Logger
                            .getLogger( FileUtils.class.getName() )
                            .log( Level.SEVERE, null, ex );
                    showError( FileUtils.class.getName(), ex.getMessage() );
                    
                }
                
            });
            
            /**
             * Listener: Shows all of Transactions for an account in a combo box and a table
             * with a wrote description
             */
            transactionDescription.setOnAction( ( ActionEvent e ) -> {
                
                try {
                    
                    if( cmbFilter.getValue() != null &&
                        cmbFilter.getValue().trim().equals( "Description" ) ) {
                    
                        filterTransactions();

                    }
                    
                } catch ( IOException | NullPointerException ex ) {
                    
                    Logger
                            .getLogger( FileUtils.class.getName() )
                            .log( Level.SEVERE, null, ex );
                    showError( FileUtils.class.getName(), ex.getMessage() );
                    
                }
                
            });
            /**
             * Listener: Shows all of Transactions for an account in a combo box and a table
             * with a wrote amount
             */
            transactionAmount.setOnAction( ( ActionEvent e ) -> {
                
                try {
                    
                    if( cmbFilter.getValue() != null &&
                        cmbFilter.getValue().trim().equals( "Amount" )) {
                    
                        filterTransactions();

                    }
                    
                } catch ( IOException | NullPointerException ex ) {
                    
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
                if( cmbAccount.getValue() == null || cmbAccount.getValue().toString().trim().equals( "" ) ) {
                    
                    showError( "Error!", "An account has to be selected." );

                }else if( transactionDate.getValue() == null || transactionDate.getValue().toString().trim().equals( "" ) ){

                    showError( "Error!", "Transaction's date is required." );

                }else if( transactionDescription.getText().trim().equals( "" ) ){

                    showError( "Error!", "Transaction's description is required." );

                }else if( transactionAmount.getText().trim().equals( "" ) ){

                    showError( "Error!", "Transaction's amount is required." );

                }else{
                    
                    saveNewTransaction( cmbAccount.getValue(), transactionDate, transactionDescription.getText().trim(), Float.parseFloat( transactionAmount.getText().trim() ) );
                    
                }
                
            });
            
            btnTransactionChart.setOnAction( ( ActionEvent e ) -> {

                try{
                    
                    ObservableList<PieChart.Data> pieChartDataPositive = FXCollections.observableArrayList();
                    ObservableList<PieChart.Data> pieChartDataNegative = FXCollections.observableArrayList();
                    HashMap<String,Float> hashChart = new HashMap<>();

                    listTransactions.forEach( transaction -> {
                        
                        if( !hashChart.isEmpty() &&
                            hashChart.containsKey( transaction.getDescription() ) 
                        ){
                            
                            hashChart.replace( transaction.getDescription() , hashChart.get( transaction.getDescription() ) , hashChart.get( transaction.getDescription() ) + transaction.getAmount() );
                           
                        }else{
                            
                            hashChart.put(transaction.getDescription(), transaction.getAmount() );
                            
                        }

                    });
                    
                    hashChart.forEach( ( keyHash, valueHash ) -> {
                        
                        float amount = valueHash;
                        if( amount < 0 ){ 
                            amount = amount * -1; 
                            pieChartDataNegative.add( new PieChart.Data( keyHash, amount ) );
                        }else{
                            pieChartDataPositive.add( new PieChart.Data( keyHash, amount ) );
                        }
                        

                    });
                    blanceChartPositive.setTitle( "Income" );
                    blanceChartNegative.setTitle( "Expenses" );
                    blanceChartPositive.setData(pieChartDataPositive);
                    blanceChartNegative.setData(pieChartDataNegative);
                    
                }catch(NullPointerException ex){
                    Logger
                    .getLogger( FileUtils.class.getName() )
                    .log( Level.SEVERE, null, e );
                    showError( FileUtils.class.getName(), ex.getMessage() );
                }
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
        

        tableTransactions.getColumns().clear();
        tableTransactions.getItems().clear();
        tableTransactions.getColumns().addAll( dateColumn, descriptionColumn, amountColumn );
        tableTransactions.getItems().addAll( listTransactions );
        
        // Set new balance
        totalSalary.setText( FileUtils.getTotalAmount( listTransactions ).toString() + "€" );
        
        // Set background colors in case negative or positive amount
        amountColumn.setCellFactory(e -> new TableCell<Transaction, Float>() {
            
            @Override
            public void updateItem(Float item, boolean empty) {
                
                // Always invoke super constructor.
                super.updateItem(item, empty);

                if (item == null || empty) {
                    
                    setText(null);
                    
                } else {
                    
                    setText(item.toString());
                   
                    if(item >= 0 ){
                     
                        this.setStyle( "-fx-text-fill: white; -fx-background-color: green; -fx-alignment: center;" );
                    
                    }else{
                        
                        this.setStyle( "-fx-text-fill: white; -fx-background-color: red; -fx-alignment: center-right;" );
                    
                    }
                    
                }
            }
            
        });
        
        // Cancel transactions with positive amount when row is clicked
        // with secondary button
        tableTransactions.setRowFactory(tv -> {
            TableRow<Transaction> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if ( !row.isEmpty() && 
                    event.getButton()==MouseButton.SECONDARY ) {

                    Transaction clickedRow = row.getItem();
                    System.out.println( clickedRow );
                    if( clickedRow.getAmount() > 0 ){
                        
                        try {
                            List<Transaction> searchCanels = loadTransactionsForAccount( cmbAccount.getValue(), "description", "Canceled: " + clickedRow.getDescription() );
                            
                            if( searchCanels.isEmpty() ){
                                
                                if( showConfirmMessage( "Do you really want cancel this transaction?", "Amount canceled has this amount: " + clickedRow.getAmount() ) ){
                                    
                                    saveNewTransaction( cmbAccount.getValue(), new DatePicker( LocalDate.now() ), "Canceled: " + clickedRow.getDescription(), clickedRow.getAmount() * -1 );
                                    
                                }
                                
                            }else{
                                
                                showMessage( "Transaction previously canceled", "This transaction was canceled before." );
                                
                            }
                        } catch ( IOException | NullPointerException ex ) {

                            Logger
                                .getLogger( FileUtils.class.getName() )
                                .log( Level.SEVERE, null, ex );
                            showError( FileUtils.class.getName(), ex.getMessage() );

                        }
                    }
                    
                }
            });
            return row ;
        });
        
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
    private void saveNewTransaction( Account account, DatePicker date, String description, float amount ){

        try {

            Instant instant = Instant.from(
                    date.getValue().atStartOfDay( ZoneId.systemDefault() )
            );

            Transaction newTransaction = new Transaction(
                    account.getAccountNumber(),
                    Date.from( instant ),
                    description,
                    amount
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
    
    /**
     * Filter transactions with combo box and inputs 
     * @throws IOException
     * @throws NullPointerException 
     */
    private void filterTransactions() throws IOException, NullPointerException{
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
    
}
