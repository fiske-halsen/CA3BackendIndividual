package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PhoneDTO;
import dto.UserDTO;
import entities.User;
import facades.FacadeExample;
import facades.UserFacade;
import fetcher.DadJokeFetcher;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;
import utils.SetupTestUsers;

/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
public class DemoResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final ExecutorService ES = Executors.newCachedThreadPool();
    private static final FacadeExample FACADE = FacadeExample.getFacadeExample(EMF);
    private static final UserFacade USERFACADE = UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static String cachedResponse;
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery ("select u from User u",entities.User.class);
            List<User> users = query.getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }
    
    @Path("parrallel")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getStarWarsParrallel() throws InterruptedException, ExecutionException, TimeoutException {
        String result = fetcher.StarWarsFetcher.responseFromExternalServersParrallel(ES, GSON);
        cachedResponse = result;
        return result;
    }
    
    @Path("joke")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getJoke() throws InterruptedException, ExecutionException, TimeoutException {
        String result = DadJokeFetcher.responseFromExternalDadServerParrallel(ES, GSON);
        cachedResponse = result;
        return result;
    }

    @Path("cached")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getStarWarsCached() throws InterruptedException, ExecutionException, TimeoutException {
        return cachedResponse;
    }
    
     @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("edit/{username}")
    public Response editPerson(@PathParam("username") String userName, String user) {
        UserDTO u = GSON.fromJson(user, UserDTO.class);
        u.setuName(userName);
        UserDTO userDTO = USERFACADE.editUser(u);
        return Response.ok(userDTO).build();
    }
    
    @Path("getuserphones/{username}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getUserPhones(@PathParam("username") String userName) {
        List<PhoneDTO> phoneDTOs = USERFACADE.getAllPhoneNumbersByUser(userName);
        return GSON.toJson(phoneDTOs);
    }
    
    @Path("setUpUsers")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public void setUpUsers() {
        SetupTestUsers.setUpUsers();
    }
    
}