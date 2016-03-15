package br.com.rodrigues.eliete.milhasinfantis.Model;

/**
 * Created by eliete on 10/28/15.
 */
public class Realizates {
    private int idRealization;
    private int idChild;
    private String nameChild;
    private int pointRealization;
    private String pointTypeRealization;
    private int actionId;
    private int actionCatId;
    private String actionType;
    private String date;
    private String hour;

    public Realizates(int id, int idChild, String name, int point, String pointType, int actionId, int actionCatId, String actionType, String date, String hour) {
        this.idRealization = id;
        this.idChild = idChild;
        this.hour = hour;
        this.date = date;
        this.actionCatId = actionCatId;
        this.actionId = actionId;
        this.pointTypeRealization = pointType;
        this.pointRealization = point;
        this.nameChild = name;
        this.actionType = actionType;
    }

    public Realizates(int idChild, String name, int point, String pointType, int actionId, int actionCatId, String actionType, String date, String hour) {
        this.hour = hour;
        this.idChild = idChild;
        this.date = date;
        this.actionCatId = actionCatId;
        this.actionId = actionId;
        this.pointTypeRealization = pointType;
        this.pointRealization = point;
        this.nameChild = name;
        this.actionType = actionType;
    }

    public int getId() {
        return idRealization;
    }

    public void setId(int id) {
        this.idRealization = id;
    }

    public String getName() {
        return nameChild;
    }

    public void setName(String name) {
        this.nameChild = name;
    }

    public int getPoint() {
        return pointRealization;
    }

    public void setPoint(int point) {
        this.pointRealization = point;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public String getPointType() {
        return pointTypeRealization;
    }

    public void setPointType(String pointType) {
        this.pointTypeRealization = pointType;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public int getActionCatId() {
        return actionCatId;
    }

    public void setActionCatId(int actionCatId) {
        this.actionCatId = actionCatId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getIdChild() {
        return idChild;
    }

    public void setIdChild(int idChild) {
        this.idChild = idChild;
    }

    @Override
    public String toString() {
        String message = super.toString();
        message += "\n idRealization = " + idRealization + "\n";
        message += "\n idChild = " + idChild + "\n";
        message += "\n nameChild = " + nameChild + "\n";
        message += "\n pointRealization = " + pointRealization + "\n";
        message += "\n pointTypeRealization = " + pointTypeRealization + "\n";
        message += "\n actionId = " + actionId + "\n";
        message += "\n actionCatId = " + actionCatId + "\n";
        message += "\n actionType = " + actionType + "\n";
        message += "\n date = " + date + "\n";
        return message;
    }
}
