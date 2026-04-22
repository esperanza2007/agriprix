package cm.agriprix.service;

import cm.agriprix.model.PrixHistorique;
import cm.agriprix.model.Produit;
import cm.agriprix.repository.PrixHistoriqueRepository;
import cm.agriprix.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private PrixHistoriqueRepository prixRepository;

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Produit getProduitById(Long id) {
        return produitRepository.findById(id).orElse(null);
    }

    /** Dernier prix enregistré pour un produit */
    public PrixHistorique getLatestPrix(Produit produit) {
        List<PrixHistorique> history = prixRepository.findByProduitOrderByDateDesc(produit);
        return history.isEmpty() ? null : history.get(0);
    }

    /** Historique chronologique (du plus ancien au plus récent) pour les graphiques */
    public List<PrixHistorique> getPrixHistory(Produit produit) {
        List<PrixHistorique> history = prixRepository.findByProduitOrderByDateDesc(produit);
        // Inverser pour avoir l'ordre chronologique (ancien → récent)
        java.util.Collections.reverse(history);
        return history;
    }

    /** Produits groupés par catégorie pour le tableau de bord */
    public Map<String, List<Produit>> getProduitsByCategorie() {
        return produitRepository.findAll().stream()
                .collect(Collectors.groupingBy(Produit::getCategorie));
    }

    /** Formate un prix en FCFA lisible (ex: 16 500 FCFA) */
    public static String formaterPrix(double prix) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRANCE);
        symbols.setGroupingSeparator(' ');
        DecimalFormat df = new DecimalFormat("#,##0", symbols);
        return df.format(prix) + " FCFA";
    }
}
