package br.com.rodrigues.eliete.milhasinfantis.Model;

/**
 * Created by eliete on 9/19/15.
 */
public class Penalties {

    private int id;
    private String description;
    private int point;

    public Penalties(int id, String desc, int point){
        this.id = id;
        this.description = desc;
        this.point = point;
    }

    public Penalties(String desc, int point){
        this.description = desc;
        this.point = point;
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

    public int getPoint() {
        return point;
    }

    public void setPoint(int redPoint) {
        this.point = redPoint;
    }
}
