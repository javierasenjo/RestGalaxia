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

        String query3 = "select * from galaxias where nombreGalaxia ='" + galaxia.getNombre() + "';";
        Statement st3 = conn.createStatement();
        ResultSet rs2 = st3.executeQuery(query3);
        if (rs2.next()) {
            return null;
        }
        String query = "insert into galaxias (nombreGalaxia) values('" + galaxia.getNombre() + "');";
        Statement st = conn.createStatement();
        st.executeUpdate(query);

        annadirLinkGalaxia(galaxia);

        String query2 = "select galaxiaId from galaxias where nombreGalaxia ='" + galaxia.getNombre() + "';";
        Statement st2 = conn.createStatement();
        ResultSet rs = st2.executeQuery(query2);
        int galaxiaId = 0;
        if (rs.next()) {
            galaxiaId = rs.getInt(1);
        }
        Planeta planeta = null;
        for (int i = 0; i < galaxia.contarPlanetas(); i++) {
            planeta = galaxia.getPlaneta(i);
            crearPlaneta(planeta, galaxiaId);
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
            String nombreGalaxia = "", linkGalaxia = "";
            int idGalaxia = 0;
            if (rs.next()) {
                idGalaxia = rs.getInt(1);
                nombreGalaxia = rs.getString(2);
                linkGalaxia = rs.getNString(3);
            }
            galaxia = new Galaxia(idGalaxia, nombreGalaxia, obtenerPlanetas(galaxiaId), linkGalaxia);
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
                System.out.println(planeta.toString());
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return listaPlanetas;
    }

    public Planeta crearPlaneta(Planeta planeta, int galaxiaId) throws NamingException, SQLException {
        InitialContext initialcontext = new InitialContext();
        DataSource datasource;
        datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
        Connection conn = datasource.getConnection();
        String query = "insert into planetas (nombrePlaneta,edadPlaneta,radioPlaneta,galaxiaId) values('" + planeta.getNombre() + "'," + planeta.getEdad() + "," + planeta.getRadio() + "," + galaxiaId + ");";
        Statement st = conn.createStatement();
        st.executeUpdate(query);

        annadirLinkPlaneta(planeta);
        String query2 = "select planetaId from planetas where nombrePlaneta ='" + planeta.getNombre() + "';";
        Statement st2 = conn.createStatement();
        ResultSet rs = st2.executeQuery(query2);
        int planetaId = 0;
        if (rs.next()) {
            planetaId = rs.getInt(1);
        }

        Planeta planeta2 = obtenerPlaneta(galaxiaId, planetaId);
        conn.close();
        return planeta2;
    }

    private static void annadirLinkPlaneta(Planeta planeta) throws NamingException, SQLException {
        InitialContext initialcontext = new InitialContext();
        DataSource datasource;
        datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
        Connection conn = datasource.getConnection();

        String query2 = "select planetaId, galaxiaId from planetas where nombrePlaneta ='" + planeta.getNombre() + "';";
        Statement st2 = conn.createStatement();
        ResultSet rs = st2.executeQuery(query2);
        int planetaId = 0;
        int galaxiaId = 0;
        if (rs.next()) {
            planetaId = rs.getInt(1);
            galaxiaId = rs.getInt(2);
        }

        String query = "update planetas set linkPlaneta = '" + planeta.crearLink(planetaId, galaxiaId) + "' where planetaId = " + planetaId + ";";
        Statement st = conn.createStatement();
        st.executeUpdate(query);
        conn.close();
    }

    private static void annadirLinkGalaxia(Galaxia galaxia) throws NamingException, SQLException {
        InitialContext initialcontext = new InitialContext();
        DataSource datasource;
        datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
        Connection conn = datasource.getConnection();

        String query2 = "select galaxiaId from galaxias where nombreGalaxia ='" + galaxia.getNombre() + "';";
        Statement st2 = conn.createStatement();
        ResultSet rs = st2.executeQuery(query2);
        int galaxiaId = 0;
        if (rs.next()) {
            galaxiaId = rs.getInt(1);
        }

        String query = "update galaxias set linkGalaxia = '" + galaxia.crearLink(galaxiaId) + "' where galaxiaId = " + galaxiaId + ";";
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

    public Galaxia borrarPlaneta(int galaxiaId, int planetaId) {
        Galaxia galaxia = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            Connection conn = datasource.getConnection();
            String query = "delete from planetas where galaxiaId =" + galaxiaId + " and planetaId = " + planetaId + ";";
            Statement st = conn.createStatement();
            st.executeUpdate(query);
            conn.close();
            galaxia = obtenerGalaxia(galaxiaId);
        } catch (Exception e) {
            System.out.println(e);
        }
        return galaxia;
    }

    public Planeta modificarPlaneta(Planeta planeta, int planetaId, int galaxiaId) throws NamingException, SQLException {
        InitialContext initialcontext = new InitialContext();
        DataSource datasource;
        datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
        Connection conn = datasource.getConnection();
        String query = "update planetas set nombrePlaneta ='" + planeta.getNombre() + "', edadPlaneta=" + planeta.getEdad() + ", radioPlaneta = " + planeta.getRadio() + " where planetaId =" + planetaId + " and galaxiaId = " + galaxiaId + ";";
        Statement st = conn.createStatement();
        st.executeUpdate(query);

        annadirLinkPlaneta(planeta);
        Planeta planetaNuevo = obtenerPlaneta(galaxiaId, planetaId);
        return planetaNuevo;
    }

    public String comprobarUsuario(String usuario, String password) {
        String respuesta = "";
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            Connection conn = datasource.getConnection();
            String query = "select * from usuarios where nombre ='" + usuario + "' and password = '" + password + "';";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                respuesta = "Usuario válido";
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return respuesta;
    }

    public String registrarUsuario(String usuario, String password) {
        //comprovar si ya existe nombre en bbdd
        String respuesta = "";
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            Connection conn = datasource.getConnection();
            String query2 = "select * from usuarios where nombre ='" + usuario + "' and password = '" + password + "';";
            Statement st2 = conn.createStatement();
            ResultSet rs = st2.executeQuery(query2);
            if (rs.next()) {
                respuesta = "Ya existe ese usuario";
            } else {
                String query = "insert into usuarios (nombre,password) values('" + usuario + "','" + password + "');";
                Statement st = conn.createStatement();
                st.executeUpdate(query);
                respuesta = "Se ha registrado correctamente el usuario";
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return respuesta;
    }

    public void guardarToken(String usuario, String password, String token) {
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            Connection conn = datasource.getConnection();

            String usuarioId = getUsuarioId(usuario, password);
            String query = "update usuarios set token = '" + token + "' where usuarioId = " + usuarioId + ";";
            Statement st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getUsuarioId(String usuario, String password) {
        String usuarioId = "";
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            Connection conn = datasource.getConnection();
            String query = "select usuarioId from usuarios where nombre ='" + usuario + "' and password = '" + password + "';";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                usuarioId = rs.getString(1);
            }
            st.executeQuery(query);

        } catch (Exception e) {
            System.out.println(e);
        }
        return usuarioId;
    }

    public boolean compobarToken(String token) {
        Boolean resultado = false;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            Connection conn = datasource.getConnection();
            String query = "select usuarioId from usuarios where token ='" + token + "';";
            System.out.println(query);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                resultado = true;
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return resultado;
    }
}
