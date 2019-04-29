/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BBDD;

import Pojo.Galaxia;
import Pojo.Planeta;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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
            String query = "create table if not exists planetas(planetaID int auto_increment not null primary key, nombrePlaneta varchar(255) not null, edadPlaneta int not null, radioPlaneta double not null);";
            Statement st = conn.createStatement();
            st.executeUpdate(query);
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
            int myResult = st.executeUpdate("CREATE DATABASE IF NOT EXISTS galaxia");
            System.out.println("Database created !");
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
//solo una tabla planeta, tener pojo planeta con id pero no obligatorio
    //pool ya funciona

    public Galaxia crearGalaxia(Galaxia galaxia) throws NamingException, SQLException {
        InitialContext initialcontext = new InitialContext();
        DataSource datasource;
        datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
        Connection conn = datasource.getConnection();

        String query = "insert into galaxias (nombreGalaxia) values('" + galaxia.getNombre() + "');";
        Statement st = conn.createStatement();
        st.executeUpdate(query);

        String query2 = "select galaxiaId from galaxias where nombreGalaxia ='" + galaxia.getNombre() + "';";
        Statement st2 = conn.createStatement();
        ResultSet rs = st2.executeQuery(query2);
        int galaxiaId = 0;
        if (rs.next()) {
            galaxiaId = rs.getInt(1);
        }
        Galaxia galaxiaRes = obtenerGalaxia(galaxiaId);
        conn.close();
        return galaxiaRes;
    }

    public Galaxia obtenerGalaxia(int galaxiaId) {
        Galaxia galaxia = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            Connection conn = datasource.getConnection();
            String query = "select * from galaxias where galaxiaId =" + galaxiaId + ";";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            String nombreGalaxia = "";
            if (rs.next()) {
                nombreGalaxia = rs.getString(1);
            }
            galaxia = new Galaxia(nombreGalaxia, obtenerPlanetas(galaxiaId));
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return galaxia;
    }

    public List<Planeta> obtenerPlanetas(int galaxiaId) {
        List<Planeta> listaPlanetas = new ArrayList();
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            Connection conn = datasource.getConnection();
            String query = "select * from planetas where galaxiaId ='" + galaxiaId + "';";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {

                Planeta planeta = new Planeta(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4), rs.getString(6));
                listaPlanetas.add(planeta);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return listaPlanetas;
    }

    public Galaxia crearPlaneta(Planeta planeta, int galaxiaId) throws NamingException, SQLException {
        InitialContext initialcontext = new InitialContext();
        DataSource datasource;
        datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
        Connection conn = datasource.getConnection();
        String query = "insert into planetas (nombrePlaneta,edadPlaneta,radioPlaneta,galaxiaId) values('" + planeta.getNombre() + "'," + planeta.getEdad() + "," + planeta.getRadio() + "," + galaxiaId + ");";
        Statement st = conn.createStatement();
        st.executeUpdate(query);

        annadirLink(planeta);
        Galaxia galaxiaRes = obtenerGalaxia(galaxiaId);
        conn.close();
        return galaxiaRes;
    }

    private static void annadirLink(Planeta planeta) throws NamingException, SQLException {
        InitialContext initialcontext = new InitialContext();
        DataSource datasource;
        datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
        Connection conn = datasource.getConnection();

        String query2 = "select planetaId from planetas where nombrePlaneta ='" + planeta.getNombre() + "';";
        Statement st2 = conn.createStatement();
        ResultSet rs = st2.executeQuery(query2);
        int planetaId = 0;
        if (rs.next()) {
            planetaId = rs.getInt(1);
        }
        String query = "update planetas set link = '" + planeta.crearLink(planetaId) + "' where planetaId = " + planetaId + ";";
        Statement st = conn.createStatement();
        st.executeUpdate(query);
        conn.close();
    }

    public Planeta obtenerPlaneta(int galaxiaId, int planetaId) {
        Planeta planeta = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            Connection conn = datasource.getConnection();
            String query = "select * from planetas where galaxiaId =" + galaxiaId + " and planetaId = " + planetaId + ";";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                planeta = new Planeta(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4), rs.getString(6));
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return planeta;
    }
}
