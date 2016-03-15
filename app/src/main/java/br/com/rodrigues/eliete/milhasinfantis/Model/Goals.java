package br.com.rodrigues.eliete.milhasinfantis.Model;

/**
 * Created by eliete on 9/19/15.
 */
public class Goals {

    private int id;
    private String description;
    private int redPoint;
    private int yellowPoint;
    private int greenPoint;
    private int catId;

    public Goals(){ }

    public Goals(int id, String desc, int redPoint, int yellowPoint, int greenPoint, int catId){
        this.id = id;
        this.description = desc;
        this.catId = catId;
        this.redPoint = redPoint;
        this.yellowPoint = yellowPoint;
        this.greenPoint = greenPoint;

    }

    public Goals(String desc, int redPoint, int yellowPoint, int greenPoint, int catId){
        this.description = desc;
        this.catId = catId;
        this.redPoint = redPoint;
        this.yellowPoint = yellowPoint;
        this.greenPoint = greenPoint;

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

    public int getRedPoint() {
        return redPoint;
    }

    public void setRedPoint(int redPoint) {
        this.redPoint = redPoint;
    }

    public int getYellowPoint() {
        return yellowPoint;
    }

    public void setYellowPoint(int yellowPoint) {
        this.yellowPoint = yellowPoint;
    }

    public int getGreenPoint() {
        return greenPoint;
    }

    public void setGreenPoint(int greenPoint) {
        this.greenPoint = greenPoint;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    @Override
    public String toString() {
        return description;
    }

    public String toStrings() {
        String message = super.toString();
        message += "\n id = " + id + "\n";
        message += "description = " + description + "\n";
        message += "red points = " + redPoint + "\n";
        message += "yellow points = " + yellowPoint + "\n";
        message += "green points = " + greenPoint + "\n";
        message += "catId = " + catId + "\n";
        return message;
    }

//    public boolean equalsId(Goals g) {
//        if(g instanceof Goals){
//            Goals goal = (Goals) g;
//            if(this.id == goal.getId())
//                return true;
//        }
//        return false;
//    }


}
