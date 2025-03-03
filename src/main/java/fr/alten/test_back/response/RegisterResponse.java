/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.alten.test_back.response;

/**
 * User register response.
 * @author User
 */
public class RegisterResponse {
    private String username;
    private String firstname;
    private String email;

    public String getUsername() {
        return username;
    }

    public RegisterResponse setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getFirstname() {
        return firstname;
    }

    public RegisterResponse setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public RegisterResponse setEmail(String email) {
        this.email = email;
        return this;
    }
    
        
}
