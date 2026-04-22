package cm.agriprix.controller;

import cm.agriprix.model.Produit;
import cm.agriprix.model.PrixHistorique;
import cm.agriprix.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MarcheController {

    @Autowired
    private ProduitService produitService;

    @GetMapping("/")
    public String tableauDeBord(Model model) {
        Map<String, List<Produit>> produitsByCategorie = produitService.getProduitsByCategorie();
        model.addAttribute("produitsByCategorie", produitsByCategorie);

        // Préparer les prix actuels pour chaque produit (affichage dans les cartes)
        Map<Long, String> prixActuels = produitService.getAllProduits().stream()
                .collect(Collectors.toMap(
                        Produit::getId,
                        p -> {
                            PrixHistorique latest = produitService.getLatestPrix(p);
                            return latest != null ? ProduitService.formaterPrix(latest.getPrix()) : "N/A";
                        }
                ));
        model.addAttribute("prixActuels", prixActuels);
        return "marche/tableau-de-bord";
    }

    @GetMapping("/produit/{id}")
    public String detailProduit(@PathVariable Long id, Model model) {
        Produit produit = produitService.getProduitById(id);
        if (produit == null) {
            return "error";
        }
        List<PrixHistorique> prixHistory = produitService.getPrixHistory(produit);
        PrixHistorique latestPrix = produitService.getLatestPrix(produit);

        model.addAttribute("produit", produit);
        model.addAttribute("prixHistory", prixHistory);
        model.addAttribute("latestPrix", latestPrix);

        // Déterminer la zone couleur selon la position dans le cycle saisonnier
        String zone = determinerZone(prixHistory, latestPrix);
        model.addAttribute("zone", zone);

        return "marche/detail-produit";
    }

    /** Endpoint JSON pour les graphiques Chart.js — @ResponseBody obligatoire */
    @GetMapping("/api/produit/{id}/prix")
    @ResponseBody
    public List<Map<String, Object>> getPrixHistoryJson(@PathVariable Long id) {
        Produit produit = produitService.getProduitById(id);
        if (produit == null) return List.of();

        return produitService.getPrixHistory(produit).stream()
                .map(p -> Map.<String, Object>of(
                        "date", p.getDate().toString(),
                        "prix", p.getPrix(),
                        "marche", p.getMarche()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Détermine la zone de couleur basée sur la tendance des prix.
     * Compare le dernier prix aux 3 mois précédents.
     */
    private String determinerZone(List<PrixHistorique> history, PrixHistorique latest) {
        if (latest == null || history.size() < 2) return "jaune";

        int taille = history.size();
        double prixActuel = latest.getPrix();
        double prixIlY3Mois = history.get(Math.max(0, taille - 4)).getPrix();
        double prixMin = history.stream().mapToDouble(PrixHistorique::getPrix).min().orElse(prixActuel);
        double prixMax = history.stream().mapToDouble(PrixHistorique::getPrix).max().orElse(prixActuel);
        double amplitude = prixMax - prixMin;

        if (amplitude == 0) return "jaune";

        double position = (prixActuel - prixMin) / amplitude; // 0 = bas, 1 = haut

        if (position >= 0.75) return "rouge";      // Zone de vente — prix au sommet
        if (position >= 0.40) return "jaune";      // Zone d'observation — prix en hausse
        return "vert";                              // Zone de stockage — prix bas
    }
}
