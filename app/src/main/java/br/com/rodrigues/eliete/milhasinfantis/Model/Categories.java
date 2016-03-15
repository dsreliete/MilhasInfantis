package br.com.rodrigues.eliete.milhasinfantis.Model;

/**
 * Created by eliete on 9/19/15.
 */
public class Categories {

    private int id;
    private String description;

    public Categories(String description) {
        this.description = description;
    }

    public Categories(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

    public String toStrings() {
        String message = "\n id = " + id + "\n";
        message += "description = " + description + "\n";
        return message;
    }
}
