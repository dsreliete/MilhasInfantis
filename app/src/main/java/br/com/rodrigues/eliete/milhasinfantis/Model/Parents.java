package br.com.rodrigues.eliete.milhasinfantis.Model;

/**
 * Created by eliete on 8/16/15.
 */
public class Parents extends User {

    private String name;
    private String nomeFamilia;

    public Parents(){

    }

    public Parents(String nome, String nomeFamilia) {
        setName(nome);
        setNomeFamilia(nomeFamilia);
    }

    public Parents(int id, String nome, String nomeFamilia) {
        super(id);
        setName(nome);
        setNomeFamilia(nomeFamilia);
    }

    @Override
    public String toString() {
        String message = super.toString();
        message += "Nome = " + name + "\n";
        message += "Nome Familia = " + nomeFamilia;

        return message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNomeFamilia() {
        return nomeFamilia;
    }

    public void setNomeFamilia(String nomeFamilia) {
        this.nomeFamilia = nomeFamilia;
    }


}
