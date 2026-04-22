package cm.agriprix.service;

import cm.agriprix.dto.SimulationRequest;
import cm.agriprix.dto.SimulationResultatDTO;
import cm.agriprix.model.*;
import cm.agriprix.repository.ProduitRepository;
import cm.agriprix.repository.SimulationStockageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class SimulateurService {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private SimulationStockageRepository simulationRepository;

    public SimulationResultatDTO calculer(SimulationRequest request, Utilisateur utilisateur) {

        // Récupérer le produit
        Produit produit = produitRepository.findById(request.getProduitId())
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé"));

        int nbSacs = request.getQuantiteSacs();
        double prixAchat = request.getPrixAchatUnitaire();
        double fraisMensuels = request.getFraisStockageMensuel();
        int dureeMois = request.getDureeMois();
        double prixVente = request.getPrixVenteEstime();

        // Calculs financiers
        double investissement = (double) nbSacs * prixAchat;
        double fraisTotal = (double) nbSacs * fraisMensuels * dureeMois;
        double prixRevient = investissement + fraisTotal;
        double prixRevente = (double) nbSacs * prixVente;
        double profitNet = prixRevente - prixRevient;
        double roi = prixRevient > 0 ? (profitNet / prixRevient) * 100.0 : 0.0;

        // Formatage FCFA propre (ex: "90 000 FCFA")
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRANCE);
        symbols.setGroupingSeparator(' ');
        DecimalFormat df = new DecimalFormat("#,##0", symbols);

        SimulationResultatDTO resultat = new SimulationResultatDTO();
        resultat.setDureeMois(dureeMois + " mois");
        resultat.setInvestissementTotal(df.format(investissement) + " FCFA");
        resultat.setFraisStockage(df.format(fraisTotal) + " FCFA");
        resultat.setProfitNet(df.format(Math.abs(profitNet)) + " FCFA");
        resultat.setRoi(String.format("%.0f%%", Math.abs(roi)));
        resultat.setProfitNetBrut(profitNet);
        resultat.setRoiBrut(roi);

        // Logique décisionnelle basée sur le ROI
        // vert = stocker (prix encore bas, bon moment d'achat)
        // jaune = observer (transition)
        // rouge = vendre (pic atteint, profit maximal)
        if (roi >= 25) {
            // Profit très intéressant → le prix de vente estimé est bien au-dessus du prix d'achat → VENDRE
            resultat.setConseil("Le prix a atteint son sommet. Vendez maintenant pour un profit maximal.");
            resultat.setCouleur("rouge");
            resultat.setRecommandation("VENDRE MAINTENANT");
        } else if (roi >= 10) {
            // Profit modéré → le prix monte encore → ATTENDRE
            resultat.setConseil("Le prix progresse. Attendez encore pour maximiser votre profit.");
            resultat.setCouleur("jaune");
            resultat.setRecommandation("ATTENDRE");
        } else if (roi >= 0) {
            // Profit faible mais positif → prix encore bas → STOCKER
            resultat.setConseil("Prix encore bas. C'est le bon moment pour stocker.");
            resultat.setCouleur("vert");
            resultat.setRecommandation("STOCKER / ACHETER");
        } else {
            // Déficitaire → revoir les paramètres
            resultat.setConseil("Cette simulation est déficitaire. Révisez vos coûts ou votre prix de vente.");
            resultat.setCouleur("rouge");
            resultat.setRecommandation("ATTENTION — DÉFICIT");
        }

        // Sauvegarder la simulation
        SimulationStockage simulation = new SimulationStockage();
        simulation.setUtilisateur(utilisateur);
        simulation.setProduit(produit);
        simulation.setQuantiteSacs(nbSacs);
        simulation.setPrixAchatUnitaire(prixAchat);
        simulation.setDureeMois(dureeMois);
        simulation.setFraisStockageMensuel(fraisMensuels);
        simulation.setDateSimulation(LocalDateTime.now());
        simulation.setProfitNet(profitNet);
        simulation.setRoi(roi);
        simulationRepository.save(simulation);

        return resultat;
    }
}
