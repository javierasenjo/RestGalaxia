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
import Pojo.Planeta;

/**
 * REST Web Service
 *
 * @author javie
 */
@Singleton
@Path("galaxia")
public class ServiciosGalaxia {

    Galaxia galaxia;
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
    public String postGalaxia(Galaxia galaxia2) {
        String respuesta;
        try {
            //dataBaseHandler.createTable();
            this.galaxia = galaxia2;
            respuesta = "Se ha creado la galaxia correctamente";
        } catch (Exception ex) {
            respuesta = "Ha habido un error";
        }
        return respuesta;
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Galaxia getGalaxia() {
        return this.galaxia;
    }

    @POST
    @Path("planetas")
    @Consumes(MediaType.APPLICATION_XML)
    public String postPlaneta(Planeta planeta) {
        String respuesta;
        try {
            galaxia.annadirPlaneta(planeta);
            respuesta = "Se ha creado el planeta correctamente";
        } catch (Exception ex) {
            respuesta = "Ha habido un error";
        }
        return respuesta;
    }

    @GET
    @Path("planetas/{num}")
    @Produces(MediaType.APPLICATION_XML)
    public Planeta getPlaneta(@PathParam("num") int num) {
        Planeta planeta = null;
        try {
            planeta = galaxia.getPlaneta(num);
        } catch (Exception ex) {
            System.out.println("Ha habido un error");
        }
        return planeta;
    }

    @PUT
    @Path("planetas/{num}")
    @Consumes(MediaType.APPLICATION_XML)
    public String putPlaneta(@PathParam("num") int num, Planeta planeta) {
        String respuesta;
        try {
            List<Planeta> planetas = galaxia.getPlanetas();
            planetas.set(num, planeta);
            respuesta = "Se ha modificado el planeta correctamente";
        } catch (Exception ex) {
            respuesta = "Ha habido un error";
        }
        return respuesta;
    }

    @DELETE
    @Path("planetas/{num}")
    // @Consumes(MediaType.APPLICATION_XML)
    public String deletePlaneta(@PathParam("num") int num) {
        String respuesta;
        try {
            List<Planeta> planetas = galaxia.getPlanetas();
            planetas.remove(num);
            respuesta = "Se ha borrado el planeta correctamente";
        } catch (Exception ex) {
            respuesta = "Ha habido un error";
        }
        return respuesta;
    }

    @GET
    @Path("planetas")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPlanetas() {
        String respuesta;
        try {
            respuesta = galaxia.toString();
        } catch (Exception ex) {
            respuesta = "Ha habido un error";
        }
        return respuesta;
    }
}
