/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestServices;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 * REST Web Service
 *
 * @author javie
 */
@Path("validador")
public class ServiciosValidador {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ServiciosValidador
     */
    public ServiciosValidador() {
    }



    /**
     * PUT method for updating or creating an instance of ServiciosValidador
     * @param contenidoXml
     * @return 
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String postValidarXSD(String contenidoXml) {
        //getresourceasstring
        String respuesta = "";
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File xsdFile = new File(classLoader.getResource("galaxia.xsd").getFile());

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            Source ficheroXml = new StreamSource(new StringReader(contenidoXml));
            validator.validate(ficheroXml);
            respuesta = ("El fichero es válido");
        } catch (SAXException ex) {
            respuesta = (" NO es válido");
            System.out.println(ex);
        } catch (IOException ex) {
            respuesta = (" NO es válido");
            System.out.println(ex);
        }
        return respuesta;
    }
}
