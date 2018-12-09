/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankaccountsfx.utils;

import java.util.List;
import bankaccountsfx.model.*;
import static bankaccountsfx.utils.MessageUtils.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.*;

/**
 *
 * @author Jose J. Pardines Garcia
 */
public class FileUtils {
    
    /**
     * Get all of accounts from DB (accounts.txt)
     * @return List<Account>
     * @throws FileNotFoundException, IOException 
     */
    
    public static List<Account> loadAccounts() throws FileNotFoundException, IOException{
        
        String accountsFile = "accounts.txt";
        FileReader _file;
        BufferedReader _bufferedReader;
        String _lineReaded;
        String[] _splitDataAccount;
        List<Account> listAccounts = new ArrayList<>();
        Account newAccount;
        
        _file = new FileReader( accountsFile );
        _bufferedReader = new BufferedReader( _file );
        _lineReaded = _bufferedReader.readLine();
        
        do{

            _splitDataAccount = _lineReaded.split( ";" );
            listAccounts.add( 
                    new Account(
                            _splitDataAccount[ 0 ],
                            _splitDataAccount[ 1 ]
                    ) 
            );
            _lineReaded = _bufferedReader.readLine();
            
        }while( _lineReaded != null );
        
        return listAccounts;
        
    }
    
    /**
     * Save a list of new accounts to DB (accounts.txt)
     * @param listAccounts
     * @throws FileNotFoundException, IOException 
     */
    
    public static void saveAccounts(List<Account> listAccounts) throws FileNotFoundException, IOException{
        
        String accountsFile = "accounts.txt";
        FileWriter _file;
        BufferedWriter _bufferedWriter;
            
        _file = new FileWriter( accountsFile ); 
        _bufferedWriter = new BufferedWriter( _file );
        _bufferedWriter.flush();

        listAccounts.forEach( ( Account dataAccount ) -> {

            try {

                _bufferedWriter.write(
                    dataAccount.toString()
                );
                _bufferedWriter.newLine();

            } catch (IOException e) {

                Logger
                .getLogger( FileUtils.class.getName() )
                .log( Level.SEVERE, null, e );
                showError( FileUtils.class.getName(), e.getMessage() );

            }

        } );

        _bufferedWriter.close();
        
    }
    
    /**
     * Get all of transactions from DB (transactions.txt)
     * @return List<Transaction>
     * @throws FileNotFoundException, IOException 
     */
    
    public static List<Transaction> loadTransactions() throws FileNotFoundException, IOException{
        
        String transactionsFile = "transactions.txt";
        FileReader _file;
        BufferedReader _bufferedReader;
        String _lineReaded;
        String[] _splitDataTransaction;
        List<Transaction> listTransactions = new ArrayList<>();
            
        _file = new FileReader( transactionsFile );
        _bufferedReader = new BufferedReader( _file );
        _lineReaded = _bufferedReader.readLine();
        
        do{

            _splitDataTransaction = _lineReaded.split( ";" );
            listTransactions.add( 
                    new Transaction(
                            _splitDataTransaction[ 0 ],
                            new Date( _splitDataTransaction[ 1 ] ),
                            _splitDataTransaction[ 2 ],
                           Float.parseFloat( _splitDataTransaction[ 3 ] )
                    ) 
            );
            _lineReaded = _bufferedReader.readLine();
            
        }while( _lineReaded != null );
        
        return listTransactions;
        
    }
    
    
    /**
     * Get all of transactions for an account from DB (transactions.txt)
     * @param account
     * @param typeParams
     * @param params
     * @return List<Transaction>
     * @throws FileNotFoundException, IOException 
     */
    
    public static List<Transaction> loadTransactionsForAccount( Account account, String typeParams, String params ) throws FileNotFoundException, IOException{
        
        String transactionsFile = "transactions.txt";
        FileReader _file;
        BufferedReader _bufferedReader;
        String _lineReaded;
        String[] _splitDataTransaction;
        String[] splitDate;
        List<Transaction> listTransactions = new ArrayList<>();
        Boolean isOk = false;
            
        _file = new FileReader( transactionsFile );
        _bufferedReader = new BufferedReader( _file );
        _lineReaded = _bufferedReader.readLine();
        
        do{

            _splitDataTransaction = _lineReaded.split( ";" );
            if( account.getAccountNumber().equals( _splitDataTransaction[ 0 ] ) ){
                splitDate = _splitDataTransaction[ 1 ].split( "/" );
                if( typeParams != null && params != null  ){
                    
                    switch( typeParams ){
                        case "date":

                            isOk = _splitDataTransaction[ 1 ].equals( params );
                            
                        break;
                        case "description":
                            
                            isOk = _splitDataTransaction[ 2 ].contains( params );
                            
                        break;
                        case "amount":
                            
                            isOk = Float.parseFloat( _splitDataTransaction[ 3 ] ) >= Float.parseFloat( params );
                            
                        break;
                        case "+":
                            
                            isOk = Float.parseFloat( _splitDataTransaction[ 3 ] ) >= 0;
                            
                        break;
                        case "-":
                            
                            isOk = Float.parseFloat( _splitDataTransaction[ 3 ] ) < 0;
                            
                        break;
                    }
                    
                    if( isOk ){
                        
                        _splitDataTransaction[ 1 ] = splitDate[ 1 ] + "/" + splitDate[ 0 ] + " / " + splitDate[ 2 ];
                        listTransactions.add( 
                            new Transaction(
                                    _splitDataTransaction[ 0 ],
                                    new Date( _splitDataTransaction[ 1 ] ),
                                    _splitDataTransaction[ 2 ],
                                   Float.parseFloat( _splitDataTransaction[ 3 ] )
                            ) 
                        );
                        
                    }
                    
                    isOk = false;
                    
                }else{   

                    _splitDataTransaction[ 1 ] = splitDate[ 1 ] + "/" + splitDate[ 0 ] + " / " + splitDate[ 2 ];
                    listTransactions.add( 
                            new Transaction(
                                    _splitDataTransaction[ 0 ],
                                    new Date( _splitDataTransaction[ 1 ] ),
                                    _splitDataTransaction[ 2 ],
                                   Float.parseFloat( _splitDataTransaction[ 3 ] )
                            ) 
                    );
                
                }
                
            }
            _lineReaded = _bufferedReader.readLine();
            
        }while( _lineReaded != null );
        
        return listTransactions;
        
    }
    
    /**
     * Save a list of new transactions to DB (transactions.txt)
     * @param listTransactions
     * @throws FileNotFoundException, IOException
     */
    
    public static void saveTransactions(List<Transaction> listTransactions) throws FileNotFoundException, IOException{
        
        String transactionsFile = "transactions.txt";
        FileWriter _file;
        BufferedWriter _bufferedWriter;

        _file = new FileWriter( transactionsFile ); 
        _bufferedWriter = new BufferedWriter( _file );
        _bufferedWriter.flush();
        
        listTransactions.forEach( ( Transaction dataTransaction ) -> {

            try {

                _bufferedWriter.write(
                    dataTransaction.toString()
                );
                _bufferedWriter.newLine();

            } catch (IOException e) {

                Logger
                .getLogger( FileUtils.class.getName() )
                .log( Level.SEVERE, null, e );
                showError( FileUtils.class.getName(), e.getMessage() );

            }

        } );

        _bufferedWriter.close();
            
    }
    
    /**
     * Get total amount from transactions list
     * @param listTransactions
     * @return 
     */
    public static Float getTotalAmount( List<Transaction> listTransactions ){
        float result = 0;
        
        for( Transaction dataTransaction: listTransactions ){

            result += dataTransaction.getAmount();

        }
        
        return result;
    }
}
