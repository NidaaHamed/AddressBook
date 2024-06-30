package org.development;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        JFrame f= new JFrame("Address Book");
        // Initialize the DAO
        ContactDAO contactDAO = new ContactDAO();

        // Create the ContactPerson table
        if (contactDAO.createContactTable()) {
            System.out.println("ContactPerson table created successfully.");
        } else {
            System.out.println("Failed to create ContactPerson table.");
        }

        // Create a new ContactPerson object
        ContactPerson contactPerson = new ContactPerson();
        contactPerson.setName("Janet Doe");
        contactPerson.setNickName("Jane");
        contactPerson.setHomePhone("1234567890");
        contactPerson.setWorkPhone("0987654321");
        contactPerson.setCellPhone("1122334455");
        contactPerson.setAddress("153 Main St, Anytown, UK");
        contactPerson.setWebsite("http://Janedoe.com");
        contactPerson.setProfession("Software Engineer");
        contactPerson.setBirthDate(new Date());

        // Insert the ContactPerson into the database
        if (contactDAO.insertContactPerson(contactPerson)) {
            System.out.println("ContactPerson inserted successfully.");
        } else {
            System.out.println("Failed to insert ContactPerson.");
        }
        JLabel l1, l2,l3;
        l1 = new JLabel("Name:");
        l1.setBounds(50, 50, 100, 30);
        l2 = new JLabel("CellPhone:");
        l2.setBounds(50, 100, 100, 30);
        l3 = new JLabel("Address:");
        l3.setBounds(50, 150, 100, 30);
        f.add(l1);
        f.add(l2);
        f.add(l3);

        JTextField t1, t2,t3;
        t1 = new JTextField();
        t1.setBounds(150, 50, 200, 30);
        t2 = new JTextField();
        t2.setBounds(150, 100, 200, 30);
        t3 = new JTextField();
        t3.setBounds(150, 150, 200, 30);
        f.add(t1);
        f.add(t2);
        f.add(t3);

        JButton nextButton = new JButton("Next");
        nextButton.setBounds(300, 200, 95, 30);
        f.add(nextButton);
        JButton prevButton = new JButton("Previous");
        prevButton.setBounds(100, 200, 95, 30);
        f.add(prevButton);

        // Retrieve all contacts
        List<ContactPerson> contacts = contactDAO.getContacts();
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }

        // Initialize the index
        final int[] currentIndex = {0};

        // Display the first contact
        displayContact(contacts.get(currentIndex[0]), t1, t2,t3);

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentIndex[0] = (currentIndex[0] + 1) % contacts.size();
                displayContact(contacts.get(currentIndex[0]), t1, t2,t3);
            }
        });

        prevButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentIndex[0] = (currentIndex[0] - 1 + contacts.size()) % contacts.size();
                displayContact(contacts.get(currentIndex[0]), t1, t2,t3);
            }
        });

        f.setSize(500, 500);
        f.setLayout(null);
        f.setVisible(true);

    }
    private static void displayContact(ContactPerson cp, JTextField t1, JTextField t2, JTextField t3) {
        t1.setText(cp.getName());
        t2.setText(cp.getCellPhone());
        t3.setText(cp.getAddress());
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