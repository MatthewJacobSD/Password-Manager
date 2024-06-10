package DataSecurity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Keychain_Database {

    private static final String KEYCHAIN_FILE = "Keychain.txt";

    // Write password details to file
    public static void writePassword(String username, String password, String hashPassword, String generatedPassword) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(KEYCHAIN_FILE, true))) {
            writer.write(username + ": " + password + " [" + hashPassword + "] - (" + generatedPassword + ")");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Read password details from file
    public static List<String[]> readPasswords() {
        List<String[]> passwords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(KEYCHAIN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(": ");
                if (data.length == 2) {
                    String[] userInfo = data[1].split(" \\[");
                    String username = data[0];
                    if (userInfo.length == 2) {
                        String password = userInfo[0];
                        String[] hashGen = userInfo[1].split("] - \\(");
                        if (hashGen.length == 2) {
                            String hashPassword = hashGen[0];
                            String generatedPassword = hashGen[1].substring(0, hashGen[1].length() - 1);
                            String[] passwordInfo = {username, password, hashPassword, generatedPassword};
                            passwords.add(passwordInfo);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return passwords;
    }

    // Update password for a given username
    public static boolean updatePassword(String username, String newPassword) {
        List<String[]> passwords = readPasswords();
        boolean updated = false;
        String hashPassword = SecurityManager.hashPassword(newPassword);

        for (String[] passwordInfo : passwords) {
            if (passwordInfo[0].equals(username)) {
                passwordInfo[1] = newPassword;
                passwordInfo[2] = hashPassword;
                updated = true;
                break;
            }
        }

        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(KEYCHAIN_FILE))) {
                for (String[] passwordInfo : passwords) {
                    writer.write(passwordInfo[0] + ": " + passwordInfo[1] + " [" + passwordInfo[2] + "] - (" + passwordInfo[3] + ")");
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
