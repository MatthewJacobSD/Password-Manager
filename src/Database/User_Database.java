package Database;

import Management.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User_Database {

    private static final String USER_FILE = "users.txt";

    // Save a new user to the file
    public static boolean saveUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            writer.write(user.getUsername() + " - " + user.getEmail());
            writer.newLine();
            return true; // Return true upon successful write
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Return false if an exception occurs
    }

    // Read all users from the file
    public static List<User> readUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String email = parts[1];
                    users.add(new User(username, email));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Verify email format
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}