package Database;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Keychain_Database {

    private static final String KEYCHAIN_FILE = "keychain.txt";

    public static void writePassword(String username, String password, String hashPassword) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(KEYCHAIN_FILE, true))) {
            writer.write(username + ": " + password + " - " + hashPassword);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> readPasswords() {
        List<String[]> passwords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(KEYCHAIN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(": ");
                if (data.length == 2) {
                    String[] userInfo = data[1].split(" - ");
                    if (userInfo.length == 2) {
                        String[] passwordInfo = {data[0], userInfo[0], userInfo[1]};
                        passwords.add(passwordInfo);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return passwords;
    }

    public static boolean validatePassword(String username, String password) {
        List<String[]> passwords = readPasswords();
        for (String[] passwordInfo : passwords) {
            if (passwordInfo[0].equals(username) && passwordInfo[1].equals(password)) {
                return true;
            }
        }
        return false;
    }
}
