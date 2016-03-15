package br.com.rodrigues.eliete.milhasinfantis.Model;

/**
 * Created by eliete on 9/19/15.
 */
public class Awards {

    private String description;
    private int id;
    private Integer points;

    public Awards(){}

    public Awards(int id, String description, Integer points){
        this.description = description;
        this.id = id;
        this.points = points;
    }

    public Awards(String description, Integer points){
        this.description = description;
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public String toString() {
        String message = super.toString();
        message += "id = " + id;
        message += "description = " + description;
        message += "points = " + points;
        return message;
    }
}
