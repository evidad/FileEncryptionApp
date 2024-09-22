/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fileencryptionapp;

/**
 *
 * @author errol
 */

public class FileTemplate {
    private String encryptedContent;
    private String salt;

    // Default constructor (required by Jackson)
    public FileTemplate() {}

    // Constructor with parameters
    public FileTemplate(String encryptedContent, String salt) {
        this.encryptedContent = encryptedContent;
        this.salt = salt;
    }

    // Getters and setters
    public String getEncryptedContent() {
        return encryptedContent;
    }

    public void setEncryptedContent(String encryptedContent) {
        this.encryptedContent = encryptedContent;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "FileTemplate{" +
                "encryptedContent='" + encryptedContent + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
}


