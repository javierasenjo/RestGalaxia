/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestServices;

import BBDD.DataBaseHandler;
import java.util.List;
import javax.inject.Singleton;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import Pojo.Galaxia;
import Pojo.ListaPlanetas;
import Pojo.Planeta;
import java.security.Principal;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.ws.rs.core.SecurityContext;

/**
 * REST Web Service
 *
 * @author javie
 */
//@Singleton
@Path("galaxia")
public class ServiciosGalaxia {

//    Galaxia galaxia;
    @Context
    private UriInfo context;
    DataBaseHandler dataBaseHandler = new DataBaseHandler();
    @Context
    SecurityContext securityContext;

    /**
     * Creates a new instance of GenericResource2
     */
    public ServiciosGalaxia() {
    }

    /**
     * Retrieves representation of an instance of Pojo.ServiciosGalaxia
     *
     * @param galaxia2
     * @param securityContext
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @NecesidadToken
    public Galaxia postGalaxia(Galaxia galaxia2, @Context SecurityContext securityContext) {
        Galaxia galaxiaRes = null;
        Principal principal = securityContext.getUserPrincipal();
        Integer usuarioId = Integer.parseInt(principal.getName());
        try {
            galaxiaRes = dataBaseHandler.crearGalaxia(galaxia2, usuarioId);
        } catch (Exception ex) {
            Logger.getLogger(ServiciosGalaxia.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);
        }
        return galaxiaRes;
    }

    @GET
    @NecesidadToken
    @Produces(MediaType.APPLICATION_XML)
    public Galaxia getGalaxia(@Context SecurityContext securityContext) {
        Integer numGalaxia = getGalaxiaIdPorUsuarioId(securityContext);
        return dataBaseHandler.obtenerGalaxia(numGalaxia);
    }

    @POST
    @Path("/planeta")
    @NecesidadToken
    @Consumes(MediaType.APPLICATION_XML)
    public Planeta postPlaneta(Planeta planeta, @Context SecurityContext securityContext) {
        Planeta planeta2 = null;
        Integer numGalaxia = getGalaxiaIdPorUsuarioId(securityContext);
        try {
            planeta2 = dataBaseHandler.crearPlaneta(planeta, numGalaxia);

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return planeta2;
    }

    @GET
    @Path("/planeta")
    @NecesidadToken
    @Produces(MediaType.APPLICATION_XML)
    public ListaPlanetas getPlanetas(@Context SecurityContext securityContext) {
        List<Planeta> planetas = null;
        Integer numGalaxia = getGalaxiaIdPorUsuarioId(securityContext);
        try {
            planetas = dataBaseHandler.obtenerPlanetas(numGalaxia);

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return new ListaPlanetas(planetas);
    }

    @GET
    @Path("/planeta/{numPlaneta}")
    @NecesidadToken
    @Produces(MediaType.APPLICATION_XML)
    public Planeta getPlaneta(@PathParam("numPlaneta") int numPlaneta, @Context SecurityContext securityContext) {
        Planeta planeta = null;
        Integer numGalaxia = getGalaxiaIdPorUsuarioId(securityContext);
        try {
            planeta = dataBaseHandler.obtenerPlaneta(numGalaxia, numPlaneta);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return planeta;
    }

    @PUT
    @Path("/planeta/{numPlaneta}")
    @NecesidadToken
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Planeta putPlaneta(@PathParam("numPlaneta") int numPlaneta, Planeta planeta, @Context SecurityContext securityContext) {
        Planeta planetaNuevo = null;
        Integer numGalaxia = getGalaxiaIdPorUsuarioId(securityContext);
        try {
            planetaNuevo = dataBaseHandler.modificarPlaneta(planeta, numPlaneta, numGalaxia);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return planetaNuevo;
    }

    @DELETE
    @Path("/planeta/{numPlaneta}")
    @NecesidadToken
    // @Consumes(MediaType.APPLICATION_XML)
    public Galaxia deletePlaneta(@PathParam("numPlaneta") int numPlaneta, @Context SecurityContext securityContext) {
        Galaxia galaxia = null;
        Integer numGalaxia = getGalaxiaIdPorUsuarioId(securityContext);
        try {
            galaxia = dataBaseHandler.borrarPlaneta(numGalaxia, numPlaneta);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return galaxia;
    }

    @GET
    @Path("/planeta/texto")
    @NecesidadToken
    @Produces(MediaType.TEXT_PLAIN)
    public String getPlanetasTexto(@Context SecurityContext securityContext) {
        String respuesta;
        Integer numGalaxia = getGalaxiaIdPorUsuarioId(securityContext);
        try {
            respuesta = dataBaseHandler.obtenerGalaxia(numGalaxia).toString();
        } catch (Exception ex) {
            respuesta = ex.toString();
        }
        return respuesta;
    }

    public Integer getGalaxiaIdPorUsuarioId(SecurityContext securityContext) {
        Principal principal = securityContext.getUserPrincipal();
        Integer usuarioId = Integer.parseInt(principal.getName());
        return dataBaseHandler.getGalaxiaId(usuarioId);
    }
}
