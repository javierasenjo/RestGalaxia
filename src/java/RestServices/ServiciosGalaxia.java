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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 * REST Web Service
 *
 * @author javie
 */
@Singleton
@Path("galaxia")
public class ServiciosGalaxia {

//    Galaxia galaxia;
    @Context
    private UriInfo context;
    DataBaseHandler dataBaseHandler = new DataBaseHandler();

    /**
     * Creates a new instance of GenericResource2
     */
    public ServiciosGalaxia() {
    }

    /**
     * Retrieves representation of an instance of Pojo.ServiciosGalaxia
     *
     * @param galaxia2
     * @return an instance of java.lang.String
     */
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Galaxia postGalaxia(Galaxia galaxia2) {
        Galaxia galaxiaRes = null;
        try {
            galaxiaRes = dataBaseHandler.crearGalaxia(galaxia2);
            //this.galaxia = galaxia2;

        } catch (Exception ex) {
            Logger.getLogger(ServiciosGalaxia.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex);

        }
        return galaxiaRes;
    }

    @GET
    @Path("{numGalaxia}")
    @Produces(MediaType.APPLICATION_XML)
    public Galaxia getGalaxia(@PathParam("numGalaxia") int numGalaxia) {
        return dataBaseHandler.obtenerGalaxia(numGalaxia);
    }

    @POST
    @Path("{numGalaxia}/planeta")
    @Consumes(MediaType.APPLICATION_XML)
    public Galaxia postPlaneta(Planeta planeta, @PathParam("numGalaxia") int numGalaxia) {
        Galaxia galaxia = null;
        try {
            dataBaseHandler.crearPlaneta(planeta, numGalaxia);
            galaxia = dataBaseHandler.obtenerGalaxia(numGalaxia);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return galaxia;
    }

    @GET
    @Path("{numGalaxia}/planeta")
    @Produces(MediaType.APPLICATION_XML)
    public ListaPlanetas getPlanetas(@PathParam("numGalaxia") int numGalaxia) {
        List<Planeta> planetas = null;
        try {
            planetas = dataBaseHandler.obtenerPlanetas(numGalaxia);

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return new ListaPlanetas(planetas);
    }

    @GET
    @Path("{numGalaxia}/planeta/{numPlaneta}")
    @Produces(MediaType.APPLICATION_XML)
    public Planeta getPlaneta(@PathParam("numPlaneta") int numPlaneta, @PathParam("numGalaxia") int numGalaxia) {
        Planeta planeta = null;
        try {
            planeta = dataBaseHandler.obtenerPlaneta(numGalaxia, numPlaneta);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return planeta;
    }

//    @PUT
//    @Path("{numGalaxia}/planeta/{numPlaneta}")
//    @Consumes(MediaType.APPLICATION_XML)
//    public String putPlaneta(@PathParam("numPlaneta") int numPlaneta, Planeta planeta,@PathParam("numGalaxia") int numGalaxia) {
//        String respuesta;
//        try {
//            List<Planeta> planetas = galaxia.getPlanetas();
//            planetas.set(numPlaneta, planeta);
//            respuesta = "Se ha modificado el planeta correctamente";
//        } catch (Exception ex) {
//            respuesta = ex.toString();
//        }
//        return respuesta;
//    }
//
//    @DELETE
//    @Path("{numGalaxia}/planeta/{numPlaneta}")
//    // @Consumes(MediaType.APPLICATION_XML)
//    public String deletePlaneta(@PathParam("numPlaneta") int numPlaneta,@PathParam("numGalaxia") int numGalaxia) {
//        String respuesta;
//        try {
//            List<Planeta> planetas = galaxia.getPlanetas();
//            planetas.remove(num);
//            respuesta = "Se ha borrado el planeta correctamente";
//        } catch (Exception ex) {
//            respuesta = ex.toString();
//        }
//        return respuesta;
//    }
    @GET
    @Path("{numGalaxia}/planeta/texto")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPlanetasTexto(@PathParam("numGalaxia") int numGalaxia) {
        String respuesta;
        try {
            respuesta = dataBaseHandler.obtenerGalaxia(numGalaxia).toString();
        } catch (Exception ex) {
            respuesta = ex.toString();
        }
        return respuesta;
    }
}
