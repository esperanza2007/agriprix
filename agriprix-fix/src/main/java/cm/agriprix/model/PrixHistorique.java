package cm.agriprix.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class PrixHistorique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private double prix; // Prix en FCFA

    private LocalDate date;

    private String marche; // e.g., Maroua, Mokolo

    // Constructeurs, getters et setters

    public PrixHistorique() {
    }

    public PrixHistorique(Produit produit, double prix, LocalDate date, String marche) {
        this.produit = produit;
        this.prix = prix;
        this.date = date;
        this.marche = marche;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMarche() {
        return marche;
    }

    public void setMarche(String marche) {
        this.marche = marche;
    }
}
