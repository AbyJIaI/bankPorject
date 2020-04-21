package sample.Controllers;

import java.io.Serializable;

public class UsersTable implements Serializable {
    public String name;
    public String surname;
    public String username;
    public String role;

    public UsersTable(String name, String surname, String username, String role) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
