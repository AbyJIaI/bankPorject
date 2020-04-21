package sample.BankClasses;

import java.io.Serializable;

public abstract class Operation implements Serializable {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public Operation() {
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Operation(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Operation(String name) {
        this.name = name;
    }
}
