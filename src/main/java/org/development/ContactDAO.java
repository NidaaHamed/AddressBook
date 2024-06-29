package org.development;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class ContactDAO {

    private String DB_URL;
    private String DRIVER;
    private String PASS;
    private String USER;
    private Connection con;
    private boolean conflag;

    public ContactDAO(String dbUrl, String driver, String user, String pass) {
        this.DB_URL = dbUrl;
        this.DRIVER = driver;
        this.USER = user;
        this.PASS = pass;
    }

    private boolean connect() {
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            conflag = true;
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void closeConn() {
        if (con != null) {
            try {
                con.close();
                conflag = false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ContactPerson createContactPerson(ResultSet rs) throws SQLException {
        ContactPerson contactPerson = new ContactPerson();
        contactPerson.setId(rs.getInt("id"));
        contactPerson.setName(rs.getString("name"));
        contactPerson.setNickName(rs.getString("nick_name"));
        contactPerson.setHomePhone(rs.getString("home_phone"));
        contactPerson.setWorkPhone(rs.getString("work_phone"));
        contactPerson.setCellPhone(rs.getString("cell_phone"));
        contactPerson.setAddress(rs.getString("address"));
        contactPerson.setWebsite(rs.getString("web_site"));
        contactPerson.setProfession(rs.getString("profession"));
        contactPerson.setBirthDate(rs.getDate("birthday"));
        return contactPerson;
    }

    public boolean createContactTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS ContactPerson (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(255), " +
                "nick_name VARCHAR(255), " +
                "home_phone VARCHAR(15), " +
                "work_phone VARCHAR(15), " +
                "cell_phone VARCHAR(15), " +
                "address VARCHAR(255), " +
                "web_site VARCHAR(255), " +
                "profession VARCHAR(255), " +
                "birthday DATE)";
        if (connect()) {
            try (Statement stmt = con.createStatement()) {
                stmt.execute(createTableSQL);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeConn();
            }
        }
        return false;
    }

    public List<ContactPerson> getContacts() {
        List<ContactPerson> contactList = new ArrayList<>();
        String query = "SELECT * FROM ContactPerson";

        if (connect()) {
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    ContactPerson contact = createContactPerson(rs);
                    contactList.add(contact);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeConn();
            }
        }
        return contactList;
    }

    public List<ContactPerson> getContactsForName(String name) {
        List<ContactPerson> contactList = new ArrayList<>();
        String query = "SELECT * FROM ContactPerson WHERE name LIKE ?";

        if (connect()) {
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, "%" + name + "%");
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        ContactPerson contact = createContactPerson(rs);
                        contactList.add(contact);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeConn();
            }
        }
        return contactList;
    }

    public boolean insertContactPerson(ContactPerson cp) {
        String insertSQL = "INSERT INTO ContactPerson (name, nick_name, home_phone, work_phone, cell_phone, address, web_site, profession, birthday) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (connect()) {
            try (PreparedStatement pstmt = con.prepareStatement(insertSQL)) {
                pstmt.setString(1, cp.getName());
                pstmt.setString(2, cp.getNickName());
                pstmt.setString(3, cp.getHomePhone());
                pstmt.setString(4, cp.getWorkPhone());
                pstmt.setString(5, cp.getCellPhone());
                pstmt.setString(6, cp.getAddress());
                pstmt.setString(7, cp.getWebsite());
                pstmt.setString(8, cp.getProfession());
                pstmt.setDate(9, new java.sql.Date(cp.getBirthDate().getTime()));

                int rowsInserted = pstmt.executeUpdate();
                return rowsInserted > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeConn();
            }
        }
        return false;
    }

    public void testBatch() {
        String batchInsertSQL = "INSERT INTO ContactPerson (name, nick_name, home_phone, work_phone, cell_phone, address, web_site, profession, birthday) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        if (connect()) {
            try (PreparedStatement pstmt = con.prepareStatement(batchInsertSQL)) {
                con.setAutoCommit(false);

                for (int i = 1; i <= 5; i++) {
                    pstmt.setString(1, "Name" + i);
                    pstmt.setString(2, "NickName" + i);
                    pstmt.setString(3, "HomePhone" + i);
                    pstmt.setString(4, "WorkPhone" + i);
                    pstmt.setString(5, "CellPhone" + i);
                    pstmt.setString(6, "Address" + i);
                    pstmt.setString(7, "Website" + i);
                    pstmt.setString(8, "Profession" + i);
                    pstmt.setDate(9, new java.sql.Date(System.currentTimeMillis()));

                    pstmt.addBatch();
                }

                int[] batchResults = pstmt.executeBatch();
                con.commit();
                System.out.println("Batch insert results: " + batchResults.length);
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } finally {
                closeConn();
            }
        }
    }

    public void testTrains() {
        String insertSQL1 = "INSERT INTO ContactPerson (name, nick_name, home_phone, work_phone, cell_phone, address, web_site, profession, birthday) VALUES ('Name1', 'NickName1', 'HomePhone1', 'WorkPhone1', 'CellPhone1', 'Address1', 'Website1', 'Profession1', '2000-01-01')";
        String insertSQL2 = "INSERT INTO ContactPerson (name, nick_name, home_phone, work_phone, cell_phone, address, web_site, profession, birthday) VALUES ('Name2', 'NickName2', 'HomePhone2', 'WorkPhone2', 'CellPhone2', 'Address2', 'Website2', 'Profession2', '2000-01-01')";

        if (connect()) {
            try (Statement stmt = con.createStatement()) {
                con.setAutoCommit(false);

                stmt.executeUpdate(insertSQL1);
                stmt.executeUpdate(insertSQL2);

                con.commit();
                System.out.println("Transaction successful.");
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } finally {
                closeConn();
            }
        }

    }

}