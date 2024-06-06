package Management;

import java.util.ArrayList;
import java.util.List;

public class Password {
    String password;
    private String passwordHash;
    private List<Password> passwords;


    public Password(String password){
        this.password = password;
        this.passwords = new ArrayList<>();
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPasswordHash(){
        return passwordHash;
    }
}
