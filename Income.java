/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ustory_3and4;

/**
 *
 * @author dell
 */

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;


public class Income {
    
    private int userID;
    private int incomeID;
    private String inSource;
    private BigDecimal inAmount;
    private LocalDate inDate;
    private static List<Income> incomeRecords = new ArrayList<>();
    
    public Income(int userID1,int incomeID1, String inSource1, BigDecimal inAmount1, LocalDate inDate1) {
        this.userID = userID1;
        this.incomeID = incomeID1;
        this.inSource = inSource1;
        this.inAmount = inAmount1;
        this.inDate = inDate1;
        
    }
     
    public static void loadFromFile() {
        
       File file = new File("income_records.txt");
       
       if (file.exists()) {
           try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
               
               String line;
               
               while ((line = reader.readLine()) != null) {
                  
                   String[] parts = line.split(", ");
                   
                   int userid = Integer.parseInt(parts[0].split(": ")[1]);
                   int id = Integer.parseInt(parts[1].split(": ")[1]);
                   String source = parts[2].split(": ")[1];      
                   BigDecimal amount = new BigDecimal(parts[3].split(": ")[1]);
                   LocalDate date = LocalDate.parse(parts[4].split(": ")[1]);
                   
                   incomeRecords.add(new Income(userid, id, source, amount, date));
               }
           } catch (IOException e) {
               System.out.println("Error loading income records: " + e.getMessage());
           }
       }
    }
     
    public static void add_income(Income income) {
         
          File file = new File("income_records.txt");
            boolean alreadyExists = false;

            // Check for existing ID
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("UserID: " + income.userID + ", ID: " + income.incomeID + ",")) {
                            
                            alreadyExists = true;
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error reading income file: " + e.getMessage());
                }
            }

            if (!alreadyExists) {
                incomeRecords.add(income);
                System.out.println("[Income with UserID#" + income.userID + " and ID#" + income.incomeID + " added successfully]");

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                    writer.write("UserID: " + income.userID +
                                 ", ID: " + income.incomeID +
                                 ", Source: " + income.inSource +
                                 ", Amount: " + income.inAmount +
                                 ", Date: " + income.inDate + "\n");
                } catch (IOException e) {
                    System.out.println("Error writing to income file: " + e.getMessage());
                }
            }
            else {
                System.out.println("[Income with UserID#" + income.userID + " and ID#" + income.incomeID + " already exist]");
            }
        
    }
    
    
    public static void delete_income(int userID, int incomeID) {
        boolean removed = incomeRecords.removeIf(record -> record.incomeID == incomeID && record.userID == userID);

        if (removed) {
            
            System.out.println("[Income with UserID#" + userID + " and ID#" + incomeID + " deleted successfully]");
            
            File file = new File("income_records.txt");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Income income : incomeRecords) {
                    writer.write("UserID: " + income.userID +
                                 ", ID: " + income.incomeID +
                                 ", Source: " + income.inSource +
                                 ", Amount: " + income.inAmount +
                                 ", Date: " + income.inDate + "\n");
                }
            } catch (IOException e) {
                System.out.println("Error updating income file after deletion: " + e.getMessage());
            }

        } else {
            System.out.println("[Income with UserID#" + userID + " and ID#" + incomeID + " not found to delete]");
        }
    }
    
    public static void edit_income(int userID, int incomeID, String inSource, BigDecimal inAmount, LocalDate inDate) {
        
        boolean found = false;

        for (Income record : incomeRecords) {
            if (record.incomeID == incomeID && record.userID == userID) {
                
                record.inSource = inSource;
                record.inAmount = inAmount;
                record.inDate = inDate;
                
                found = true;
                System.out.println("[Income with UserID#" + userID + " and ID#" + record.incomeID + " modified successfully]");
                break;
            }
        }

        if (found) {
            
            File file = new File("income_records.txt");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Income income : incomeRecords) {
                    writer.write("UserID: " + income.userID +
                                 ", ID: " + income.incomeID +
                                 ", Source: " + income.inSource +
                                 ", Amount: " + income.inAmount +
                                 ", Date: " + income.inDate + "\n");
                }
            } catch (IOException e) {
                System.out.println("Error updating income file after edit: " + e.getMessage());
            }
        } else {
            System.out.println("[Income with UserID#" + userID +" and ID#" + incomeID + " not found to modify]");
        }
    }
       
    public static void display_income(int userID) {
        
        boolean found = false; 
        System.out.println();
        System.out.println("**User#" + userID + " Incomes**");
            
        for (Income record : incomeRecords) {
            if (record.userID == userID) {
                found = true;
                System.out.println("--------------------------");
                System.out.println("ID#" + record.incomeID);
                System.out.println("Source: " + record.inSource);
                System.out.println("Amount: " + record.inAmount);
                System.out.println("Date: " + record.inDate);
                System.out.println("--------------------------");
            }
        }
        if (!found){
            System.out.println("[No income records found for user ID: " + userID + "]");
        }   
    }
}
