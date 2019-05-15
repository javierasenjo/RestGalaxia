/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestServices;

import BBDD.DataBaseHandler;
import java.security.Principal;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.naming.NamingException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author javie
 */
@Provider
@NecesidadToken
@Priority(Priorities.AUTHENTICATION)
public class FiltroAutentificacion implements ContainerRequestFilter {

    private static final String REALM = "example";
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    Integer usuaioId = null;
    DataBaseHandler dataBaseHandler = new DataBaseHandler();

    @Override
    public void filter(ContainerRequestContext requestContext) {

        // Get the Authorization header from the request
        String authorizationHeader
                = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        System.out.println("Header: " + authorizationHeader);
        // Validate the Authorization header
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }
        
        
        if (validateToken(authorizationHeader) != null) {
            usuaioId = validateToken(authorizationHeader);
            //System.out.println("Token valido");
        } else {
            System.out.println("no funciona");
            abortWithUnauthorized(requestContext);
        }

        final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {
                return () -> usuaioId.toString();
            }

            @Override
            public boolean isUserInRole(String role) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return currentSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return AUTHENTICATION_SCHEME;
            }
        });
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return authorizationHeader != null || authorizationHeader.length() == 30;
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

        // Abort the filter chain with a 401 status code response
        // The WWW-Authenticate header is sent along with the response
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE,
                                AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
                        .build());
    }

    private Integer validateToken(String token) {

        // Check if the token was issued by the server and if it's not expired
        // Throw an Exception if the token is invalid
        return dataBaseHandler.compobarToken(token);
    }

}
