/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestServices;

import BBDD.DataBaseHandler;
import com.sun.codemodel.JSwitch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author javie
 */
@Path("login")
public class ServiciosLogin {

    @Context
    private UriInfo context;
    DataBaseHandler dataBaseHandler = new DataBaseHandler();
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Creates a new instance of ServiciosLogin
     */
    public ServiciosLogin() {
    }

    /**
     * Retrieves representation of an instance of RestServices.ServiciosLogin
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ServiciosLogin
     *
     * @param usuario
     * @param password
     * @param content representation for the resource
     * @return
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public String loginear(@QueryParam("usuario") String usuario, @QueryParam("password") String password) {
        String respuesta = "";
        try {
            respuesta = dataBaseHandler.comprobarUsuario(usuario, password);
            //Algorithm algorithm = Algorithm.HMAC256(usuario);
            //Creamos token si es el login es correcto
            if (respuesta.equals("Usuario válido")) {
                String token = generarToken(30);
                dataBaseHandler.guardarToken(usuario, password, token);
                return token;
            }
        } catch (Exception ex) {
            Logger.getLogger(ServiciosGalaxia.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
        return null;
    }

    public static String generarToken(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
