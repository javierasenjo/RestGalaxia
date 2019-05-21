/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestServices;

import BBDD.DataBaseHandler;
import Pojo.Usuario;
import static RestServices.ServiciosLogin.generarToken;
import java.security.Principal;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 * REST Web Service
 *
 * @author javie
 */
@Path("logout")
public class ServiciosLogout {

    DataBaseHandler dataBaseHandler = new DataBaseHandler();
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ServiciosLogout
     */
    public ServiciosLogout() {
    }

    /**
     * Retrieves representation of an instance of RestServices.ServiciosLogout
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
     * PUT method for updating or creating an instance of ServiciosLogout
     *
     * @param securityContext
     * @param content representation for the resource
     * @return 
     */
    @POST
    @NecesidadToken
    //@Produces(MediaType.TEXT_PLAIN)
    public String cerrarSesion(@Context SecurityContext securityContext) {
        String respuesta = "";
        Principal principal = securityContext.getUserPrincipal();
        Integer usuarioId = Integer.parseInt(principal.getName());
        try {
            dataBaseHandler.borrarToken(usuarioId);
            respuesta = "Token borrado";
            return respuesta;
        } catch (Exception ex) {
            Logger.getLogger(ServiciosGalaxia.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
        return null;
    }

    public Integer getGalaxiaIdPorUsuarioId(SecurityContext securityContext) {
        Principal principal = securityContext.getUserPrincipal();
        Integer usuarioId = Integer.parseInt(principal.getName());
        return dataBaseHandler.getGalaxiaId(usuarioId);
    }
}
