
package dto;

import entities.Phone;
import entities.Role;
import entities.User;
import java.util.ArrayList;
import java.util.List;


public class UserDTO {
    
    private String uName;
    private String pNumber;
   
    
    
    public UserDTO(User user ){
      this.uName = user.getUserName();
      this.pNumber = user.getPhones().get(0).getNumber();
         
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getpNumber() {
        return pNumber;
    }

    public void setpNumber(String pNumber) {
        this.pNumber = pNumber;
    }

    
}
