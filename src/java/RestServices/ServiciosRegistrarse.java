/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestServices;

import BBDD.DataBaseHandler;
import Pojo.Usuario;
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
@Path("signup")
public class ServiciosRegistrarse {

    @Context
    private UriInfo context;
    DataBaseHandler dataBaseHandler = new DataBaseHandler();

    /**
     * Creates a new instance of ServiciosRegistrarse
     */
    public ServiciosRegistrarse() {
    }

    /**
     * Retrieves representation of an instance of
     * RestServices.ServiciosRegistrarse
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
     * PUT method for updating or creating an instance of ServiciosRegistrarse
     *
     * @param usuario
     * @param password
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public String signup(Usuario usuario) {
        String respuesta = "";
        String nombre = usuario.getNombre();
        String password = usuario.getPassword();
        
        try {
            respuesta = dataBaseHandler.registrarUsuario(nombre, password);
            //crearToken
        } catch (Exception ex) {
            Logger.getLogger(ServiciosGalaxia.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }

        return respuesta;
    }
}
