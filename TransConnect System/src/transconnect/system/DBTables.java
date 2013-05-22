package transconnect.system;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Kiarie
 */
public class DBTables {

    private Connection con;
    private String sql;
    
    public DBTables() throws SQLException {
        con=new DBUtility().getConnection();
        createUsersTable();
        createVehiclesTable();
    }
    private void createUsersTable() throws SQLException{
        sql="create table users("
                + "FirstName varchar(30),"
                + "LastName varchar(30),"
                + "Surname varchar(30),"
                + "DOB Date,"
                + "Residence varchar(30),"
                + "Mobile varchar(10),"
                + "Type varchar(20),"
                + "NationalID varchar(30),"
                + "Username varchar(20),"
                + "Password varchar(20),"
                + "Note varchar(30),"
                + "Created_By varchar(25),"
                + "UserID integer not null GENERATED ALWAYS AS IDENTITY)";
        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        }
    }
    private void createVehiclesTable() throws SQLException{
        sql="CREATE TABLE VEHICLES (VEHICLEID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY, REGISTRATION VARCHAR(15), MODEL VARCHAR(15), NUMSEATS INTEGER, ENTRYDATE DATE, STATUS VARCHAR(10), COMMENT VARCHAR(300), PRIMARY KEY (VEHICLEID))";
        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        }
    }
    private void createDriversTable() throws SQLException{
        sql="CREATE TABLE DRIVERS ("
                + "DRIVERID INTEGER DEFAULT AUTOINCREMENT: start 1 increment 1  NOT NULL GENERATED ALWAYS AS IDENTITY, "
                + "FIRSTNAME VARCHAR(30), "
                + "LASTNAME VARCHAR(30), "
                + "SURNAME VARCHAR(30), "
                + "RESIDENCE VARCHAR(30), "
                + "MOBILE VARCHAR(10), "
                + "ENTRYDATE DATE, "
                + "STATUS VARCHAR(10), "
                + "PRIMARY KEY (DRIVERID));";
        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql);
        }
    }
}
