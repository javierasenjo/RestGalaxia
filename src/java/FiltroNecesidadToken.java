//
//import RestServices.NecesidadToken;
//import javax.annotation.Priority;
//import javax.inject.Inject;
//import javax.ws.rs.Priorities;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.ext.Provider;
//
//@Provider
//@NecesidadToken
//@Priority(Priorities.AUTHENTICATION)
//public class FiltroNecesidadToken implements ContainerRequestFilter {
// 
//    @Inject
//    private KeyGenerator keyGenerator;
// 
//    public void filter(ContainerRequestContext requestContext) throws IOException {
// 
//        // Get the HTTP Authorization header from the request
//        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
// 
//        // Extract the token from the HTTP Authorization header
//        String token = authorizationHeader.substring("Bearer".length()).trim();
// 
//        try {
// 
//            // Validate the token
//            Key key = keyGenerator.generateKey();
//            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
//            logger.info("#### valid token : " + token);
// 
//        } catch (Exception e) {
//            logger.severe("#### invalid token : " + token);
//            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
//        }
//    }
//}