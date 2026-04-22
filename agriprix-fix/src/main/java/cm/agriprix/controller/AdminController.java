package cm.agriprix.controller;

import cm.agriprix.model.Produit;
import cm.agriprix.model.Utilisateur;
import cm.agriprix.repository.ProduitRepository;
import cm.agriprix.repository.PrixHistoriqueRepository;
import cm.agriprix.repository.UtilisateurRepository;
import cm.agriprix.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired private ProduitService produitService;
    @Autowired private ProduitRepository produitRepository;
    @Autowired private PrixHistoriqueRepository prixHistoriqueRepository;
    @Autowired private UtilisateurRepository utilisateurRepository;

    // ── Dashboard ──────────────────────────────────────────────
    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("produits", produitService.getAllProduits());
        model.addAttribute("totalProduits", produitRepository.count());
        model.addAttribute("totalUtilisateurs", utilisateurRepository.count());
        return "admin/dashboard";
    }

    // ── CRUD Produits ──────────────────────────────────────────

    /** Formulaire d'ajout */
    @GetMapping("/admin/produits/ajouter")
    public String formulaireAjout(Model model) {
        model.addAttribute("categories", List.of("Céréales", "Légumineuses", "Rente", "Légumes", "Fruits"));
        model.addAttribute("produit", new Produit());
        model.addAttribute("mode", "ajout");
        return "admin/produit-form";
    }

    /** Enregistrer un nouveau produit */
    @PostMapping("/admin/produits/ajouter")
    public String ajouterProduit(@RequestParam String nom,
                                 @RequestParam String categorie,
                                 @RequestParam String emoji,
                                 RedirectAttributes redirectAttributes) {
        produitRepository.save(new Produit(nom.trim(), categorie, emoji.trim()));
        redirectAttributes.addFlashAttribute("succes", "Produit \"" + nom + "\" ajouté avec succès !");
        return "redirect:/admin";
    }

    /** Formulaire de modification */
    @GetMapping("/admin/produits/modifier/{id}")
    public String formulaireModifier(@PathVariable Long id, Model model) {
        Produit produit = produitService.getProduitById(id);
        if (produit == null) return "error";
        model.addAttribute("produit", produit);
        model.addAttribute("categories", List.of("Céréales", "Légumineuses", "Rente", "Légumes", "Fruits"));
        model.addAttribute("mode", "modification");
        return "admin/produit-form";
    }

    /** Enregistrer la modification */
    @PostMapping("/admin/produits/modifier/{id}")
    public String modifierProduit(@PathVariable Long id,
                                  @RequestParam String nom,
                                  @RequestParam String categorie,
                                  @RequestParam String emoji,
                                  RedirectAttributes redirectAttributes) {
        Produit produit = produitService.getProduitById(id);
        if (produit == null) return "error";
        produit.setNom(nom.trim());
        produit.setCategorie(categorie);
        produit.setEmoji(emoji.trim());
        produitRepository.save(produit);
        redirectAttributes.addFlashAttribute("succes", "Produit \"" + nom + "\" modifié avec succès !");
        return "redirect:/admin";
    }

    /** Supprimer un produit */
    @PostMapping("/admin/produits/supprimer/{id}")
    public String supprimerProduit(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Produit produit = produitService.getProduitById(id);
        if (produit == null) return "error";
        // Supprimer d'abord l'historique des prix
        prixHistoriqueRepository.deleteAll(prixHistoriqueRepository.findByProduitOrderByDateDesc(produit));
        produitRepository.delete(produit);
        redirectAttributes.addFlashAttribute("succes", "Produit supprimé avec succès !");
        return "redirect:/admin";
    }

    // ── Gestion utilisateurs ───────────────────────────────────

    @GetMapping("/admin/utilisateurs")
    public String utilisateurs(Model model) {
        model.addAttribute("utilisateurs", utilisateurRepository.findAll());
        return "admin/utilisateurs";
    }
}
