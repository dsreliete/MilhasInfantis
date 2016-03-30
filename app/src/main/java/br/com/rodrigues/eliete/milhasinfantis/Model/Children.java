package br.com.rodrigues.eliete.milhasinfantis.Model;

/**
 * Created by eliete on 9/7/15.
 */
public class Children extends User {

    private String name;
    private String birthDate;
    private String gender;


    public Children(String nome, String birthDate, String gender) {
        setName(nome);
        setBirthDate(birthDate);
        setGender(gender);
    }

    public Children(int id, String nome, String data, String gender) {
        super(id);
        setName(nome);
        setBirthDate(data);
        setGender(gender);
    }

    @Override
    public String toString() {
        String message = super.toString();
        message += "Nome = " + name + "\n";
        message += "birthDate = " + birthDate;
        message += "gender = " + gender;

        return message;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
