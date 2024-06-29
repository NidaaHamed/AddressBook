package org.development;

import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Database connection details
        String dbUrl = "jdbc:mysql://localhost:3306/addressbook";
        String driver = "com.mysql.cj.jdbc.Driver";
        String user = "developer";
        String pass = "121!@!DevEnv343#$#";

        // Initialize the DAO
        ContactDAO contactDAO = new ContactDAO(dbUrl, driver, user, pass);

        // Create the ContactPerson table
        if (contactDAO.createContactTable()) {
            System.out.println("ContactPerson table created successfully.");
        } else {
            System.out.println("Failed to create ContactPerson table.");
        }

        // Create a new ContactPerson object
        ContactPerson contactPerson = new ContactPerson();
        contactPerson.setName("John Doe");
        contactPerson.setNickName("Johnny");
        contactPerson.setHomePhone("1234567890");
        contactPerson.setWorkPhone("0987654321");
        contactPerson.setCellPhone("1122334455");
        contactPerson.setAddress("123 Main St, Anytown, USA");
        contactPerson.setWebsite("http://johndoe.com");
        contactPerson.setProfession("Software Engineer");
        contactPerson.setBirthDate(new Date());

        // Insert the ContactPerson into the database
        if (contactDAO.insertContactPerson(contactPerson)) {
            System.out.println("ContactPerson inserted successfully.");
        } else {
            System.out.println("Failed to insert ContactPerson.");
        }

        // Retrieve and display all contacts
        List<ContactPerson> contacts = contactDAO.getContacts();
        for (ContactPerson cp : contacts) {
            System.out.println("ID: " + cp.getId());
            System.out.println("Name: " + cp.getName());
            System.out.println("NickName: " + cp.getNickName());
            System.out.println("HomePhone: " + cp.getHomePhone());
            System.out.println("WorkPhone: " + cp.getWorkPhone());
            System.out.println("CellPhone: " + cp.getCellPhone());
            System.out.println("Address: " + cp.getAddress());
            System.out.println("Website: " + cp.getWebsite());
            System.out.println("Profession: " + cp.getProfession());
            System.out.println("BirthDate: " + cp.getBirthDate());
            System.out.println("-------------------------------");
        }
    }
}