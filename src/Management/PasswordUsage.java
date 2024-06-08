package Management;

import java.util.Objects;

public class PasswordUsage {
    private Password password;

    public PasswordUsage(Password password){
        setPassword(password);
    }

    public Password getPassword(){
        return password;
    }

    public void setPassword(Password password){
        this.password = password;
    }

    @Override
    public int hashCode(){
        return Objects.hash(password);
    }

    @Override
    public String toString(){
        return "Password Usage{ password: " + password.getPassword() + "}";
    }
}
