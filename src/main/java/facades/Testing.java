/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

/**
 *
 * @author phill
 */
public class Testing {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static UserFacade USERFACADE = UserFacade.getUserFacade(EMF);
    
    public static void main(String[] args) {
        
        System.out.println(USERFACADE.getAllPhoneNumbersByUser("user").get(3).getNumber());
        
        
        
        
        
    }
}
