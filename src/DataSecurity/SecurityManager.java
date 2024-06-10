package DataSecurity;

import Management.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SecurityManager {

    private static final String SALT = "My$3cUr3S@lt"; // A more complex salt

    // Check if password is strong
    public static boolean isnotPasswordStrong(String password) {
        return password.length() < 8 ||
                !password.matches(".*[A-Z].*") ||
                !password.matches(".*[!@#$%^&*()-_=+|{};:'\",<.>/?].*");
    }

    // Check if password was generated for a given username
    public static boolean isPasswordGenerated(String username, String password) {
        return verifyPassword(username, password);
    }

    // Validate password for a given username
    public static boolean verifyPassword(String username, String password) {
        List<User> users = Account_Database.readAccounts();
        String hashedPassword = hashPassword(password);
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(hashedPassword)) {
                return true;
            }
        }
        return false;
    }

    // Generate random password
    public static String generateRandomPassword(int length) {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*()-_=+|{};:'\",<.>/?";

        String allChars = upperCase + lowerCase + numbers + specialChars;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(numbers.charAt(random.nextInt(numbers.length())));
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        for (int i = 4; i < length; i++) {
            int randomIndex = random.nextInt(allChars.length());
            password.append(allChars.charAt(randomIndex));
        }

        return password.toString();
    }

    // Hash password
    public static String hashPassword(String password) {
        String saltedPassword = SALT + password;
        StringBuilder hashedPassword = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(saltedPassword.getBytes());
            for (byte b : hashBytes) {
                hashedPassword.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword.toString();
    }

    // Verify email format
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
