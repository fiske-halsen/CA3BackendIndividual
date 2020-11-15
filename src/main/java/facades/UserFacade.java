package facades;

import dto.UserDTO;
import dto.PhoneDTO;
import entities.Phone;
import entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import security.errorhandling.AuthenticationException;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }
    
      //edit a person
   
    public UserDTO editUser(UserDTO userDTO) {
        EntityManager em = emf.createEntityManager();
        User user = em.find(User.class, userDTO.getuName());
        Phone phone = new Phone(userDTO.getpNumber());
        phone.addUser(user);

        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();

            return new UserDTO(user);
        } finally {
            em.close();
        }
    }
    
    
      public List<PhoneDTO> getAllPhoneNumbersByUser(String userName) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT p FROM Phone p JOIN p.user users WHERE users.userName = :userName", Phone.class);
            query.setParameter("userName", userName);
            List<Phone> phoneList = query.getResultList();
            
            List<PhoneDTO> phoneDTOs = new ArrayList();
            for (Phone phone : phoneList) {
                phoneDTOs.add(new PhoneDTO(phone));
            }
            
            return phoneDTOs;
        } finally {
            em.close();
        }

    }
    
    
    
    
    
    
    
    
    
    
    
    

}
