package cm.agriprix.controller;

import cm.agriprix.dto.SimulationRequest;
import cm.agriprix.dto.SimulationResultatDTO;
import cm.agriprix.model.SimulationStockage;
import cm.agriprix.model.Utilisateur;
import cm.agriprix.service.ProduitService;
import cm.agriprix.service.SimulateurService;
import cm.agriprix.service.UtilisateurService;
import cm.agriprix.repository.SimulationStockageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class SimulateurController {

    @Autowired
    private SimulateurService simulateurService;

    @Autowired
    private ProduitService produitService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private SimulationStockageRepository simulationRepository;

    @GetMapping("/simulateur")
    public String formulaire(Model model) {
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("simulationRequest", new SimulationRequest());
        return "simulateur/formulaire";
    }

    @PostMapping("/simulateur")
    public String calculer(@ModelAttribute SimulationRequest request, Model model, Principal principal) {
        Utilisateur utilisateur = utilisateurService.getCurrentUser(principal.getName());
        SimulationResultatDTO resultat = simulateurService.calculer(request, utilisateur);
        model.addAttribute("resultat", resultat);
        model.addAttribute("request", request);
        // Récupérer le nom du produit pour l'affichage
        model.addAttribute("produitNom", produitService.getProduitById(request.getProduitId()).getNom());
        return "simulateur/resultat";
    }

    @GetMapping("/simulateur/historique")
    public String historique(Model model, Principal principal) {
        Utilisateur utilisateur = utilisateurService.getCurrentUser(principal.getName());
        List<SimulationStockage> simulations = simulationRepository.findByUtilisateurOrderByDateSimulationDesc(utilisateur);
        model.addAttribute("simulations", simulations);
        return "simulateur/historique";
    }
}
