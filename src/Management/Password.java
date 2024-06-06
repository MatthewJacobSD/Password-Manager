package Management;

public class Password {
    private String password;
    private final String newPassword;
    private final String confirmPassword;

    public Password(String newPassword, String confirmPassword) {
        this.password = password;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String newPassword){
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    // Method to validate password
    public boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasSymbol = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        return hasUppercase && hasSymbol;
    }

    // Hash the password
    public static String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }
}
