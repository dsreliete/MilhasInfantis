package br.com.rodrigues.eliete.milhasinfantis.Model;

/**
 * Created by eliete on 10/4/15.
 */
public class Associates {

    private int idChildren;
    private int idActivity;
    private int catId;

    public Associates(int idChildren, int idActivity, int idCategory){
        this.idChildren = idChildren;
        this.idActivity = idActivity;
        this.catId = idCategory;
    }

    public int getIdChildren() {
        return idChildren;
    }

    public void setIdChildren(int idChildren) {
        this.idChildren = idChildren;
    }

    public int getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(int idActivity) {
        this.idActivity = idActivity;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    @Override
    public String toString() {
        String message = super.toString();
        message += "idChildren = " + idChildren + "\n";
        message += "idActivity = " + idActivity + "\n";
        message += "idCategory = " + catId + "\n";
        return message;
    }

}
