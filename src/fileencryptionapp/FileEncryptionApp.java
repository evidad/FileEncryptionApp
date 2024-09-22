/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package fileencryptionapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author errol
 */
public class FileEncryptionApp {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            System.out.println("Menu:");
            System.out.println("1. Read a file");
            System.out.println("2. Encrypt a file");
            System.out.println("3. Decrypt a file");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    readFile();
                    break;
                case 2:
                    encryptFile();
                    break;
                case 3:
                    decryptFile();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
        System.out.println("Exiting the program...");
    }

    private static void readFile() {
        System.out.print("Enter file name to read: ");
        String fileName = scanner.nextLine();
        File file = new File(fileName);
        try {
            ObjectMapper mapper = new ObjectMapper(); // Jackson ObjectMapper
            FileTemplate obj = mapper.readValue(file, FileTemplate.class);
            System.out.println("File content: " + obj.toString());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private static void encryptFile() {
        System.out.print("Enter the text you want to encrypt: ");
        String content = scanner.nextLine();
        System.out.print("Enter your secret key: ");
        String secretKey = scanner.nextLine();
        System.out.print("Enter your salt: ");
        String salt = scanner.nextLine();
        System.out.print("Enter the output file name (e.g., encryptedData.json): ");
        String outputFileName = scanner.nextLine();

        try {
            AES256.encryptToFile(content, secretKey, salt, outputFileName);
        } catch (Exception e) {
            System.out.println("Error encrypting file: " + e.getMessage());
        }
    }

    private static void decryptFile() {
        System.out.print("Enter your secret key: ");
        String secretKey = scanner.nextLine();
        System.out.print("Enter the input file name (e.g., encryptedData.json): ");
        String inputFileName = scanner.nextLine();

        try {
            String decryptedContent = AES256.decryptFromFile(secretKey, inputFileName);
            if (decryptedContent != null) {
                System.out.println("Decrypted content: " + decryptedContent);
            } else {
                System.out.println("Decryption failed.");
            }
        } catch (Exception e) {
            System.out.println("Error decrypting file: " + e.getMessage());
        }
    }

}
