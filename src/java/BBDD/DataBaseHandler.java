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

    public Galaxia crearGalaxia(Galaxia galaxia, Integer usuarioId) {
        Galaxia galaxiaRes = null;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            conn = datasource.getConnection();

            st = conn.createStatement();

            String query3 = "select * from galaxias where nombreGalaxia ='" + galaxia.getNombre() + "';";
            rs = st.executeQuery(query3);
            if (rs.next()) {
                return null;
            }

            String query = "insert into galaxias (nombreGalaxia,usuarioId) values('" + galaxia.getNombre() + "'," + usuarioId + ");";

            st.executeUpdate(query);

            annadirLinkGalaxia(galaxia);

            String query2 = "select galaxiaId from galaxias where nombreGalaxia ='" + galaxia.getNombre() + "';";
            rs = st.executeQuery(query2);
            int galaxiaId = 0;
            if (rs.next()) {
                galaxiaId = rs.getInt(1);
            }
            Planeta planeta = null;
            for (int i = 0; i < galaxia.contarPlanetas(); i++) {
                planeta = galaxia.getPlaneta(i);
                crearPlaneta(planeta, galaxiaId);
            }
            galaxiaRes = obtenerGalaxia(galaxiaId);
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            liberarRecursos(rs, st, conn);
        }
        return galaxiaRes;
    }

    public Galaxia obtenerGalaxia(int galaxiaId) {
        Galaxia galaxia = null;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            conn = datasource.getConnection();
            String query = "select * from galaxias where galaxiaId =" + galaxiaId + ";";
            st = conn.createStatement();
            rs = st.executeQuery(query);
            String nombreGalaxia = "", linkGalaxia = "";
            int idGalaxia = 0;
            if (rs.next()) {
                idGalaxia = rs.getInt(1);
                nombreGalaxia = rs.getString(2);
                linkGalaxia = rs.getNString(3);
            }
            galaxia = new Galaxia(idGalaxia, nombreGalaxia, obtenerPlanetas(galaxiaId), linkGalaxia);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            liberarRecursos(rs, st, conn);
        }
        return galaxia;
    }

    public List<Planeta> obtenerPlanetas(int galaxiaId) {
        List<Planeta> listaPlanetas = new ArrayList();
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            conn = datasource.getConnection();
            String query = "select * from planetas where galaxiaId ='" + galaxiaId + "';";
            st = conn.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {

                Planeta planeta = new Planeta(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4), rs.getString(6));
                listaPlanetas.add(planeta);
                System.out.println(planeta.toString());
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            liberarRecursos(rs, st, conn);
        }
        return listaPlanetas;
    }

    public Planeta crearPlaneta(Planeta planeta, int galaxiaId) {
        Planeta planeta2 = null;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            conn = datasource.getConnection();
            String query = "insert into planetas (nombrePlaneta,edadPlaneta,radioPlaneta,galaxiaId) values('" + planeta.getNombre() + "'," + planeta.getEdad() + "," + planeta.getRadio() + "," + galaxiaId + ");";
            st = conn.createStatement();
            st.executeUpdate(query);

            annadirLinkPlaneta(planeta);
            String query2 = "select planetaId from planetas where nombrePlaneta ='" + planeta.getNombre() + "';";
            rs = st.executeQuery(query2);
            int planetaId = 0;
            if (rs.next()) {
                planetaId = rs.getInt(1);
            }

            planeta2 = obtenerPlaneta(galaxiaId, planetaId);
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            liberarRecursos(rs, st, conn);
        }
        return planeta2;
    }

    private void annadirLinkPlaneta(Planeta planeta) {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            conn = datasource.getConnection();

            String query2 = "select planetaId, galaxiaId from planetas where nombrePlaneta ='" + planeta.getNombre() + "';";
            st = conn.createStatement();
            rs = st.executeQuery(query2);
            int planetaId = 0;
            int galaxiaId = 0;
            if (rs.next()) {
                planetaId = rs.getInt(1);
                galaxiaId = rs.getInt(2);
            }

            String query = "update planetas set linkPlaneta = '" + planeta.crearLink(planetaId, galaxiaId) + "' where planetaId = " + planetaId + ";";
            st.executeUpdate(query);
        } catch (Exception ex) {

        } finally {
            liberarRecursos(rs, st, conn);
        }
    }

    private void annadirLinkGalaxia(Galaxia galaxia) {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            conn = datasource.getConnection();

            String query2 = "select galaxiaId from galaxias where nombreGalaxia ='" + galaxia.getNombre() + "';";
            Statement st2 = conn.createStatement();
            rs = st2.executeQuery(query2);
            int galaxiaId = 0;
            if (rs.next()) {
                galaxiaId = rs.getInt(1);
            }

            String query = "update galaxias set linkGalaxia = '" + galaxia.crearLink(galaxiaId) + "' where galaxiaId = " + galaxiaId + ";";
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception ex) {

        } finally {
            liberarRecursos(rs, st, conn);
        }
    }

    public Planeta obtenerPlaneta(int galaxiaId, int planetaId) {
        Planeta planeta = null;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            conn = datasource.getConnection();
            String query = "select * from planetas where galaxiaId =" + galaxiaId + " and planetaId = " + planetaId + ";";
            st = conn.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                planeta = new Planeta(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4), rs.getString(6));
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            liberarRecursos(rs, st, conn);
        }
        return planeta;
    }

    public Galaxia borrarPlaneta(int galaxiaId, int planetaId) {
        Galaxia galaxia = null;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            conn = datasource.getConnection();
            String query = "delete from planetas where galaxiaId =" + galaxiaId + " and planetaId = " + planetaId + ";";
            st = conn.createStatement();
            st.executeUpdate(query);
            galaxia = obtenerGalaxia(galaxiaId);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            liberarRecursos(rs, st, conn);
        }
        return galaxia;
    }

    public Planeta modificarPlaneta(Planeta planeta, int planetaId, int galaxiaId) {
        Planeta planetaNuevo = null;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            conn = datasource.getConnection();
            String query = "update planetas set nombrePlaneta ='" + planeta.getNombre() + "', edadPlaneta=" + planeta.getEdad() + ", radioPlaneta = " + planeta.getRadio() + " where planetaId =" + planetaId + " and galaxiaId = " + galaxiaId + ";";
            st = conn.createStatement();
            st.executeUpdate(query);

            annadirLinkPlaneta(planeta);
            planetaNuevo = obtenerPlaneta(galaxiaId, planetaId);
        } catch (Exception ex) {

        } finally {
            liberarRecursos(rs, st, conn);
        }
        return planetaNuevo;
    }

    public String comprobarUsuario(String usuario, String password) {
        String respuesta = "";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            conn = datasource.getConnection();
            String query = "select * from usuarios where nombre ='" + usuario + "' and password = '" + password + "';";
            st = conn.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                respuesta = "Usuario válido";
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            liberarRecursos(rs, st, conn);
        }
        return respuesta;
    }

    public String registrarUsuario(String usuario, String password) {
        //comprobar si ya existe nombre en bbdd
        String respuesta = "";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            conn = datasource.getConnection();
            String query2 = "select * from usuarios where nombre ='" + usuario + "' and password = '" + password + "';";
            st = conn.createStatement();
            rs = st.executeQuery(query2);
            if (rs.next()) {
                respuesta = "Ya existe ese usuario";
            } else {
                String query = "insert into usuarios (nombre,password) values('" + usuario + "','" + password + "');";
                st.executeUpdate(query);
                respuesta = "Se ha registrado correctamente el usuario";
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            liberarRecursos(rs, st, conn);
        }
        return respuesta;
    }

    public void guardarToken(String usuario, String password, String token) {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            conn = datasource.getConnection();

            String usuarioId = getUsuarioId(usuario, password);
            String query = "update usuarios set token = '" + token + "' where usuarioId = " + usuarioId + ";";
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            liberarRecursos(rs, st, conn);
        }
    }

    public String getUsuarioId(String usuario, String password) {
        String usuarioId = "";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            conn = datasource.getConnection();
            String query = "select usuarioId from usuarios where nombre ='" + usuario + "' and password = '" + password + "';";
            st = conn.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
                usuarioId = rs.getString(1);
            }
            st.executeQuery(query);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            liberarRecursos(rs, st, conn);
        }
        return usuarioId;
    }

    public Integer compobarToken(String token) {
        Integer usuarioId = null;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            conn = datasource.getConnection();
            String query = "select usuarioId from usuarios where token ='" + token + "';";
            //System.out.println(query);
            st = conn.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                usuarioId = rs.getInt(1);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            liberarRecursos(rs, st, conn);
        }
        return usuarioId;
    }

    public Integer getGalaxiaId(Integer usuarioId) {
        Integer galaxiaId = 0;
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            InitialContext initialcontext = new InitialContext();
            DataSource datasource;
            datasource = (DataSource) initialcontext.lookup("jdbc/galaxiaDatabase");
            conn = datasource.getConnection();
            String query = "select galaxiaId from galaxias where usuarioId =" + usuarioId + ";";
            st = conn.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                galaxiaId = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            liberarRecursos(rs, st, conn);
        }
        return galaxiaId;
    }

    public static void liberarRecursos(ResultSet rs, Statement st, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
}
