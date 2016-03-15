package br.com.rodrigues.eliete.milhasinfantis.Model;

/**
 * Created by eliete on 9/7/15.
 */
public class Children extends User {

    private String name;
    private String dataNasc;
    private String gender;


    public Children(String nome, String dataNasc, String gender) {
        setName(nome);
        setDataNasc(dataNasc);
        setGender(gender);
    }

    public Children(int id, String nome, String data, String gender) {
        super(id);
        setName(nome);
        setDataNasc(data);
        setGender(gender);
    }

    @Override
    public String toString() {
        String message = super.toString();
        message += "Nome = " + name + "\n";
        message += "dataNasc = " + dataNasc;
        message += "gender = " + gender;

        return message;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
