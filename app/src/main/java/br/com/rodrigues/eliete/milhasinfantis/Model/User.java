package br.com.rodrigues.eliete.milhasinfantis.Model;

/**
 * Created by eliete on 8/16/15.
 */
public class User {

    private int id;

    public User(){    }

    public User(int id){
        this.id = id;
    }

    @Override
    public String toString() {
        String message = "Id = " + id + "\n";
        return message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
