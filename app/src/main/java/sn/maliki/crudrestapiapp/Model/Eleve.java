package sn.maliki.crudrestapiapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Eleve implements Serializable {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("clef")
    @Expose
    private String clef;
    @SerializedName("prenom")
    @Expose
    private String prenom;
    @SerializedName("nom")
    @Expose
    private String nom;
    @SerializedName("dateNaissance")
    @Expose
    private String dateNaissance;
    @SerializedName("dateEnregistrement")
    @Expose
    private String dateEnregistrement;
    @SerializedName("dateModification")
    @Expose
    private String dateModification;
    private final static long serialVersionUID = -3106147750084783271L;

    /**
     * No args constructor for use in serialization
     */
    public Eleve() {
    }

    /**
     * @param clef
     * @param dateNaissance
     * @param dateModification
     * @param prenom
     * @param nom
     * @param email
     * @param dateEnregistrement
     */
    public Eleve(String email, String clef, String prenom, String nom, String dateNaissance, String dateEnregistrement, String dateModification) {
        super();
        this.email = email;
        this.clef = clef;
        this.prenom = prenom;
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.dateEnregistrement = dateEnregistrement;
        this.dateModification = dateModification;
    }

    public Eleve(String email, String clef, String prenom, String nom) {
        super();
        this.email = email;
        this.clef = clef;
        this.prenom = prenom;
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClef() {
        return clef;
    }

    public void setClef(String clef) {
        this.clef = clef;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        this.dateNaissance = formatter.format(dateNaissance);
    }

    public String getDateEnregistrement() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return dateEnregistrement;
    }

    public void setDateEnregistrement(Date dateEnregistrement) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        this.dateEnregistrement = formatter.format(dateEnregistrement);
    }

    public String getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        this.dateModification = formatter.format(dateModification);;
    }

}