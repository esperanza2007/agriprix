package cm.agriprix.dto;

import jakarta.validation.constraints.*;

public class SimulationRequest {

    @NotNull
    private Long produitId;

    @NotNull
    @Min(1)
    private Integer quantiteSacs; // Nombre de sacs (1 sac = 100 kg)

    @NotNull
    @Min(0)
    private Double prixAchatUnitaire; // FCFA par sac

    @NotNull
    @Min(0)
    private Double fraisStockageMensuel; // FCFA par sac par mois

    @NotNull
    @Min(1)
    @Max(24)
    private Integer dureeMois;

    @NotNull
    @Min(0)
    private Double prixVenteEstime; // FCFA par sac (prix actuel du marché)

    // Constructeurs

    public SimulationRequest() {
    }

    // Getters et Setters

    public Long getProduitId() {
        return produitId;
    }

    public void setProduitId(Long produitId) {
        this.produitId = produitId;
    }

    public Integer getQuantiteSacs() {
        return quantiteSacs;
    }

    public void setQuantiteSacs(Integer quantiteSacs) {
        this.quantiteSacs = quantiteSacs;
    }

    public Double getPrixAchatUnitaire() {
        return prixAchatUnitaire;
    }

    public void setPrixAchatUnitaire(Double prixAchatUnitaire) {
        this.prixAchatUnitaire = prixAchatUnitaire;
    }

    public Double getFraisStockageMensuel() {
        return fraisStockageMensuel;
    }

    public void setFraisStockageMensuel(Double fraisStockageMensuel) {
        this.fraisStockageMensuel = fraisStockageMensuel;
    }

    public Integer getDureeMois() {
        return dureeMois;
    }

    public void setDureeMois(Integer dureeMois) {
        this.dureeMois = dureeMois;
    }

    public Double getPrixVenteEstime() {
        return prixVenteEstime;
    }

    public void setPrixVenteEstime(Double prixVenteEstime) {
        this.prixVenteEstime = prixVenteEstime;
    }
}
