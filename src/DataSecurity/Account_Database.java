package DataSecurity;

import Management.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Account_Database {

    private static final String ACCOUNT_FILE = "Account.txt";

    // Save user account details to file
    public static boolean saveAccount(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE, true))) {
            writer.write(user.getEmail() + " - " + user.getUsername() + ": " + user.getPassword());
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Read user accounts from file
    public static List<User> readAccounts() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 2) {
                    String[] userInfo = parts[1].split(": ");
                    if (userInfo.length == 2) {
                        users.add(new User(parts[0], userInfo[0], userInfo[1]));//changerInfo into a caller for password receiver of password usage
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Update password for a given username
    public static boolean updateAccountPassword(String username, String newPassword) {
        List<User> users = readAccounts();
        boolean updated = false;

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.setPassword(newPassword); // Convert newPassword to string and update the password
                updated = true;
                break;
            }
        }

        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE))) {
                for (User user : users) {
                    // Write the updated user information to the file
                    writer.write(user.getEmail() + " - " + user.getUsername() + ": " + user.getPassword());
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }
}