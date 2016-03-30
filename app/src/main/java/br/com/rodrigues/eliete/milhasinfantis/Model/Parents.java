package br.com.rodrigues.eliete.milhasinfantis.Model;

/**
 * Created by eliete on 8/16/15.
 */
public class Parents extends User {

    private String name;
    private String nameFamily;

    public Parents(){

    }

    public Parents(String nome, String nameFamily) {
        setName(nome);
        setNameFamily(nameFamily);
    }

    public Parents(int id, String nome, String nameFamily) {
        super(id);
        setName(nome);
        setNameFamily(nameFamily);
    }

    @Override
    public String toString() {
        String message = super.toString();
        message += "Nome = " + name + "\n";
        message += "Nome Familia = " + nameFamily;

        return message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameFamily() {
        return nameFamily;
    }

    public void setNameFamily(String nameFamily) {
        this.nameFamily = nameFamily;
    }


}
