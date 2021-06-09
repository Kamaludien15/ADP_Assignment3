/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.cput.adpassignment3;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author Kamaludien
 */
public class runAssign3 {
    
    private ObjectInputStream input;
    private FileWriter fileWriter;
    private BufferedWriter bw;
    static ArrayList<Customer> customerList = new ArrayList<Customer>();
    static ArrayList<Supplier> supplierList = new ArrayList<Supplier>();
    
    private int canRent;
    private int cantRent;
    
    //Opening the serialized file
    public void openFile(){
        try{
            input = new ObjectInputStream(new FileInputStream("stakeholder.ser"));
        }
        catch(IOException ioe){
            System.out.println("Error opening file" + ioe.getMessage());
        }
    }
    
    //Closes the file
    public void closeFile(){
        try{
            input.close();
        }
        catch(IOException ioe){
            System.out.println("Error closing file "+ ioe.getMessage());
        }
    }
    
    //Reading the ser file and putting objects in arraylists.
    public void arrayListPopulator(){
        try{
            while(true){
                Object temp = input.readObject();
                if(temp instanceof Customer){
                    customerList.add((Customer)temp);
                }
                else{
                    supplierList.add((Supplier)temp);
                }
   
            }
        }
        
        catch(EOFException ioe){
            System.out.println("End of file reached");
        }
        catch(ClassNotFoundException ioe){
            System.out.println("Error reading ser file" + ioe.getMessage());
        }

        catch(IOException ioe){
            System.out.println("Error io file"+ ioe.getMessage());
        }
        finally{
            closeFile();
        }
    }
    
    //Get customer age
    public int getAge(int i){
        try{
            String dob = customerList.get(i).getDateOfBirth();
        
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            Date d = sdf.parse(dob);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int date = calendar.get(Calendar.DATE);
            LocalDate dateOfBirth = LocalDate.of(year, month, date);
            LocalDate currentDate = LocalDate.now();
            Period age = Period.between(dateOfBirth, currentDate);
            return age.getYears();
        }
        catch(ParseException pee){
            System.out.println("Parsing error " + pee.getMessage());
            return 0;
        }
    }
    
    //Checks if customer can rent
    public void rentCheck(){
        for(int i=0;i<customerList.size();i++){
            if(customerList.get(i).getCanRent()){
                canRent++;
            }
            else{
                cantRent++;
            }
        }
    }
    
    //Format date of birth
    public void dobFormat(){
        
        try{
            for(int i=0; i<customerList.size();i++){
                String date_s = customerList.get(i).getDateOfBirth();
        
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(date_s);
        
                SimpleDateFormat dt1 = new SimpleDateFormat("dd MMM yyyy");
                customerList.get(i).setDateOfBirth(dt1.format(date));
            }
        }
        catch(ParseException pee){
            System.out.println("Error"+ pee.getMessage());
        }
    }
    
    //Sort method for customer list
    public static Comparator<Customer> stakeHolderIDComparator = new Comparator<Customer>(){
        public int compare(Customer cus1, Customer cus2){
            String stakeHolderID1 = cus1.getStHolderId();
            String stakeHolderID2 = cus2.getStHolderId();
            
            return stakeHolderID1.compareTo(stakeHolderID2);
        }
    };
    //Sort method for supplier list
    public static Comparator<Supplier> supplierNameComparator = new Comparator<Supplier>(){
        public int compare(Supplier sup1, Supplier sup2){
            String supplierName1 = sup1.getName();
            String supplierName2 = sup2.getName();
            
            return supplierName1.compareTo(supplierName2);
        }
    };
    
    //Writes customer details to txt file
    public void writeCustomerInfo(){
        try{
            fileWriter = new FileWriter("customerOutFile.txt");  
            bw = new BufferedWriter(fileWriter);
            bw.write("=========================== CUSTOMERS ================================\n");
            bw.write(String.format("%-10s\t%-15s\t%-15s\t%-15s\t%-15s\n", "ID", "Name", "Surname", "Date of birth", "Age"));
            bw.write("======================================================================\n");
            for(int i=0; i<customerList.size(); i++){
                bw.write(String.format("%-10s\t%-15s\t%-15s\t%-15s\t%-15s\n", customerList.get(i).getStHolderId(), customerList.get(i).getFirstName(), customerList.get(i).getSurName(), customerList.get(i).getDateOfBirth(), getAge(i)));  
            }
            bw.write("\nNumber of customers who can rent: "+ canRent);
            bw.write("\nNumber of customers who cannot rent " + cantRent); 
            bw.close();  
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    //Writes supplier details to txt file
    public void writeSupplierInfo(){
        try{
            fileWriter = new FileWriter("supplierOutFile.txt");  
            bw = new BufferedWriter(fileWriter);
            bw.write("======================= SUPPLIERS ==================================\n");
            bw.write(String.format("%-10s\t%-20s\t%-15s\t%-15s\n", "ID", "Name", "Prod Type", "Description"));
            bw.write("======================================================================\n");
            for(int i=0; i<supplierList.size(); i++){
                bw.write(String.format("%-10s\t%-20s\t%-15s\t%-15s\n", supplierList.get(i).getStHolderId(), supplierList.get(i).getName(), supplierList.get(i).getProductType(), supplierList.get(i).getProductDescription()));  
            }
            bw.close();  
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    
    
    
    
    
    
    public static void main(String[] args) {
        runAssign3 obj = new runAssign3();
        obj.openFile();
        obj.arrayListPopulator();
        Collections.sort(customerList, stakeHolderIDComparator);
        Collections.sort(supplierList, supplierNameComparator);
        obj.writeSupplierInfo();
        obj.dobFormat();
        obj.rentCheck();
        obj.writeCustomerInfo();
    }
    
}
