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
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Budget {
    
    private int userID;
    private int budgetID;
    private String budCategory;
    private BigDecimal budAmount;
    private static List<Budget> budgets = new ArrayList<>();
    
    public Budget(int userID1, int budgetID1, String budCategory1, BigDecimal budAmount1){
         this.userID = userID1;
        this.budgetID = budgetID1;
        this.budCategory = budCategory1;
        this.budAmount = budAmount1;
    }
    
    public static void loadFromFile() {
         
       File file = new File("User_budgets.txt");
       
       if (file.exists()) {
           try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
               
               String line;
               
               while ((line = reader.readLine()) != null) {
                  
                   String[] parts = line.split(", ");
                   
                   int userid = Integer.parseInt(parts[0].split(": ")[1]);
                   int id = Integer.parseInt(parts[1].split(": ")[1]);
                   String category = parts[2].split(": ")[1];      
                   BigDecimal amount = new BigDecimal(parts[3].split(": ")[1]);
                   
                   budgets.add(new Budget(userid, id, category, amount));
               }
           } catch (IOException e) {
               System.out.println("Error loading budget records: " + e.getMessage());
           }
       }
    }
     
    public static void set_budget(Budget budget) {
         
        File file = new File("User_budgets.txt");
        boolean alreadyExists = false;

        // Check for existing ID
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("UserID: " + budget.userID + ", ID: " + budget.budgetID + ",")){

                        alreadyExists = true;
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading budget file: " + e.getMessage());
            }
        }

        if (!alreadyExists) {
            
            budgets.add(budget);
            System.out.println("[The budget with UserID#" + budget.userID + " and ID#" + budget.budgetID + " has been set successfully]");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write("UserID: " + budget.userID +
                             ", ID: " + budget.budgetID +
                             ", Category: " + budget.budCategory +
                             ", Amount: " + budget.budAmount + "\n");
            } catch (IOException e) {
                System.out.println("Error writing to budget file: " + e.getMessage());
            }
        }
        else {
            System.out.println("[The budget with UserID#" + budget.userID + " and ID#" + budget.budgetID + " already exist]");
        }
    }
     
    public static void analyze_spending(int userID, BigDecimal actualSpending, int budgetID) {
        
        System.out.println();
        System.out.println("**Spending Analysis for user#" + userID + "**");
                  
        for (Budget item : budgets){
            if (item.budgetID == budgetID) {
                int compare = actualSpending.compareTo(item.budAmount);

                if (compare == 0) {
                   System.out.println("Id#"+ item.budgetID + ": in category '"+ item.budCategory + "', you are exactly on budget.");
                } else if (compare > 0) {
                   System.out.println("Id#"+ item.budgetID + ": in category '"+ item.budCategory + "', you have overspent by [" + actualSpending.subtract(item.budAmount) + "].");
                } else {
                   System.out.println("Id#"+ item.budgetID + ": in category '"+ item.budCategory + "', you are under budget by [" + item.budAmount.subtract(actualSpending) + "].");
                }
            }
        }
    }
    
    public static void display_budget(int userID) {
         
        boolean found = false; 
        System.out.println();
        System.out.println("**User#" + userID + " budgets**");
         
         for (Budget element : budgets) {
            if (element.userID == userID) {
            found = true;
            System.out.println("--------------------------");
            System.out.println("ID#" + element.budgetID);
            System.out.println("Budget Category: " + element.budCategory);
            System.out.println("Budget Amount: " + element.budAmount);
            System.out.println("--------------------------");
         }
        }
        if (!found){
            System.out.println("[No budget found for userID: " + userID + "]");
        }
    }   
}
