package cm.agriprix.dto;

import cm.agriprix.model.Utilisateur;
import jakarta.validation.constraints.*;

public class InscriptionRequest {
    @NotBlank
    @Pattern(regexp = "^\\d{9}$", message = "Le téléphone doit contenir 9 chiffres")
    private String telephone;

    @NotBlank
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    @NotNull
    private Utilisateur.Role role;

    @NotBlank
    private String nom;

    // Constructeurs, getters et setters

    public InscriptionRequest() {
    }

    public InscriptionRequest(String telephone, String password, Utilisateur.Role role, String nom) {
        this.telephone = telephone;
        this.password = password;
        this.role = role;
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Utilisateur.Role getRole() {
        return role;
    }

    public void setRole(Utilisateur.Role role) {
        this.role = role;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
