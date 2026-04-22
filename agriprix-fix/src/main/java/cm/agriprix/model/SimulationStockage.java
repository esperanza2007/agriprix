package cm.agriprix.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SimulationStockage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private int quantiteSacs;        // Nombre de sacs de 100kg

    private double prixAchatUnitaire; // Prix d'achat par sac en FCFA

    private int dureeMois;

    private double fraisStockageMensuel; // Frais de stockage par sac par mois en FCFA

    private LocalDateTime dateSimulation;

    private double profitNet;

    private double roi; // En pourcentage

    // Constructeurs

    public SimulationStockage() {
    }

    // Getters et setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public int getQuantiteSacs() { return quantiteSacs; }
    public void setQuantiteSacs(int quantiteSacs) { this.quantiteSacs = quantiteSacs; }

    public double getPrixAchatUnitaire() { return prixAchatUnitaire; }
    public void setPrixAchatUnitaire(double prixAchatUnitaire) { this.prixAchatUnitaire = prixAchatUnitaire; }

    public int getDureeMois() { return dureeMois; }
    public void setDureeMois(int dureeMois) { this.dureeMois = dureeMois; }

    public double getFraisStockageMensuel() { return fraisStockageMensuel; }
    public void setFraisStockageMensuel(double fraisStockageMensuel) { this.fraisStockageMensuel = fraisStockageMensuel; }

    public LocalDateTime getDateSimulation() { return dateSimulation; }
    public void setDateSimulation(LocalDateTime dateSimulation) { this.dateSimulation = dateSimulation; }

    public double getProfitNet() { return profitNet; }
    public void setProfitNet(double profitNet) { this.profitNet = profitNet; }

    public double getRoi() { return roi; }
    public void setRoi(double roi) { this.roi = roi; }
}
