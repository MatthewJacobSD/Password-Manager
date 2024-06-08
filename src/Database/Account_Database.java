package Database;

import Management.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Account_Database {

    private static final String ACCOUNT_FILE = "Account.txt";

    public static boolean saveAccount(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE, true))) {
            writer.write(user.getEmail() + " - " + user.getUsername() + ": " + user.getPassword());
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static List<User> readAccounts() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length == 2) {
                    String[] userInfo = parts[1].split(": ");
                    if (userInfo.length == 2) {
                        users.add(new User(parts[0], userInfo[0], userInfo[1]));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Update password for a given email
    public static boolean updateAccountPassword(String username, String newPassword) {
        List<User> users = readAccounts();
        boolean updated = false;

        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.setPassword(newPassword);
                updated = true;
                break;
            }
        }

        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE))) {
                for (User user : users) {
                    writer.write(user.getEmail() + " - " + user.getUsername() + ": " + user.getPassword());
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }


    // Verify email format
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
