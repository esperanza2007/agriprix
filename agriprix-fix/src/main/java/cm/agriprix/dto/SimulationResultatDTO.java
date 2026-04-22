package cm.agriprix.dto;

public class SimulationResultatDTO {

    private String dureeMois;
    private String investissementTotal;  // quantite * prixAchat
    private String fraisStockage;        // quantite * fraisMensuels * dureeMois
    private String profitNet;            // prixRevente - prixRevient
    private String roi;                  // en pourcentage
    private String conseil;              // texte de recommandation
    private String couleur;              // "vert", "jaune", "rouge"
    private String recommandation;       // "STOCKER / ACHETER", "ATTENDRE", "VENDRE MAINTENANT"

    // Valeurs brutes pour l'affichage formaté
    private double profitNetBrut;
    private double roiBrut;

    public SimulationResultatDTO() {
    }

    public String getDureeMois() { return dureeMois; }
    public void setDureeMois(String dureeMois) { this.dureeMois = dureeMois; }

    public String getInvestissementTotal() { return investissementTotal; }
    public void setInvestissementTotal(String investissementTotal) { this.investissementTotal = investissementTotal; }

    public String getFraisStockage() { return fraisStockage; }
    public void setFraisStockage(String fraisStockage) { this.fraisStockage = fraisStockage; }

    public String getProfitNet() { return profitNet; }
    public void setProfitNet(String profitNet) { this.profitNet = profitNet; }

    public String getRoi() { return roi; }
    public void setRoi(String roi) { this.roi = roi; }

    public String getConseil() { return conseil; }
    public void setConseil(String conseil) { this.conseil = conseil; }

    public String getCouleur() { return couleur; }
    public void setCouleur(String couleur) { this.couleur = couleur; }

    public String getRecommandation() { return recommandation; }
    public void setRecommandation(String recommandation) { this.recommandation = recommandation; }

    public double getProfitNetBrut() { return profitNetBrut; }
    public void setProfitNetBrut(double profitNetBrut) { this.profitNetBrut = profitNetBrut; }

    public double getRoiBrut() { return roiBrut; }
    public void setRoiBrut(double roiBrut) { this.roiBrut = roiBrut; }
}
