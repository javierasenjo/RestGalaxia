/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BBDD;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.sql.DataSource;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author javie
 */
public class DataBaseHandler {
//
//    public ResultSet loginear(String username, String password) throws SQLException {
//        ResultSet rs = null;
//        try {
//            InitialContext initialcontext = new InitialContext();
//            DataSource datasource;
//            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
//            Connection conn = datasource.getConnection();
//            String query = "select * from tb_users where email ='" + username + "' and password ='" + password + "';";
//            Statement st;
//            st = conn.createStatement();
//            rs = st.executeQuery(query);
//            conn.close();
//        } catch (NamingException ex) {
//            Logger.getLogger(LoginChat.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return rs;
//    }
//
//    public ResultSet comprobaciónUsuarioExiste(String username) throws SQLException {
//        ResultSet rs = null;
//        try {
//            InitialContext initialcontext = new InitialContext();
//            DataSource datasource;
//            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
//            Connection conn = datasource.getConnection();
//            String query = "select * from tb_users where email ='" + username + "';";
//            Statement st;
//            st = conn.createStatement();
//            rs = st.executeQuery(query);
//            conn.close();
//        } catch (NamingException ex) {
//            Logger.getLogger(LoginChat.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return rs;
//    }
//
//    public ResultSet register(String username, String password) throws SQLException, NamingException {
//        ResultSet rs = null;
//        try {
//            InitialContext initialcontext = new InitialContext();
//            DataSource datasource;
//            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
//            Connection conn = datasource.getConnection();
//            String query = "select * from tb_users where email ='" + username + "' and password ='" + password + "';";
//            Statement st;
//            st = conn.createStatement();
//            rs = st.executeQuery(query);
//            if (!rs.next()) {
//                username = "'" + username + "'";
//                String contrasena = "'" + password + "'";
//                query = "insert into tb_users values (" + username + ",sha1(" + contrasena + "));";
//
//                st = conn.createStatement();
//                st.execute(query);
//                conn.close();
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(LoginChat.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return rs;
//    }
//
//    public void recuperarContrasena(String username, String password) throws SQLException {
//        try {
//            InitialContext initialcontext = new InitialContext();
//            DataSource datasource;
//            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
//            Connection conn = datasource.getConnection();
//            String query = "update tb_users set password =sha1('" + password + "') where email='" + username + "';";
//            Statement st;
//            st = conn.createStatement();
//            st.executeUpdate(query);
//            conn.close();
//        } catch (NamingException ex) {
//            Logger.getLogger(LoginChat.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void borrarUsuario(String username) throws SQLException {
//        try {
//            InitialContext initialcontext = new InitialContext();
//            DataSource datasource;
//            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
//            Connection conn = datasource.getConnection();
//            String query = " delete from tb_users where email='" + username + "';";
//            Statement st;
//            st = conn.createStatement();
//            st.executeUpdate(query);
//            conn.close();
//        } catch (NamingException ex) {
//            Logger.getLogger(LoginChat.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public String hash(String pass) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        try {
//            MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
//            msdDigest.update(pass.getBytes("UTF-8"), 0, pass.length());
//            pass = DatatypeConverter.printHexBinary(msdDigest.digest());
//            return pass;
//        } catch (Exception Ex) {
//
//        }
//        return pass;
//    }

    public void createTable() throws NamingException {
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            Connection conn = datasource.getConnection();
            String query = "create table if not exists galaxiaPrueba (galaxiaID int auto_increment not null primary key, nombreGalaxia varchar(255) not null);";
            String query2 = "create table if not existst planeta(planetaID int auto_increment not null primary key, nombrePlaneta varchar(255) not null, edadPlaneta int not null, radioPlaneta double not null, galaxiaID int not null, foreign key(galaxiaID) references galaxia(galaxiaID));";
            Statement st = conn.createStatement();
            st.executeUpdate(query);
            st.executeUpdate(query2);
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void createDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/galaxia?zeroDateTimeBehavior=convertToNull");
            Statement st = conn.createStatement();
            int myResult = st.executeUpdate("CREATE DATABASE IF NOT EXISTS galaxia2");
            System.out.println("Database created !");
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
//solo una tabla planeta, tener pojo planeta con id pero no obligatorio
    //pool ya funciona
    

    public void prueba() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/galaxia?useSSL=false", "root", "root");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from planeta");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getInt(3));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
