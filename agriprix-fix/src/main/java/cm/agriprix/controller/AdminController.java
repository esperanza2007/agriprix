package cm.agriprix.controller;

import cm.agriprix.model.Produit;
import cm.agriprix.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private ProduitService produitService;

    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("produits", produitService.getAllProduits());
        // Ajouter des stats si nécessaire
        return "admin/dashboard";
    }

    // Autres méthodes pour gérer produits, relevés, etc.
}
