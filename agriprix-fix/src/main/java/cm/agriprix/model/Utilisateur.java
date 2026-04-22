package cm.agriprix.model;

import jakarta.persistence.*;

@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String telephone;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String nom;

    // Constructeurs, getters et setters

    public Utilisateur() {
    }

    public Utilisateur(String telephone, String password, Role role, String nom) {
        this.telephone = telephone;
        this.password = password;
        this.role = role;
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public enum Role {
        PRODUCTEUR, COMMERCANT, ADMIN
    }
}
