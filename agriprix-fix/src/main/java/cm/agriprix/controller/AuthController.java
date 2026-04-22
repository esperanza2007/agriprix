package cm.agriprix.controller;

import cm.agriprix.dto.InscriptionRequest;
import cm.agriprix.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/inscription")
    public String inscriptionForm(Model model) {
        model.addAttribute("inscriptionRequest", new InscriptionRequest());
        return "auth/inscription";
    }

    @PostMapping("/inscription")
    public String inscrire(@ModelAttribute InscriptionRequest request, RedirectAttributes redirectAttributes) {
        try {
            utilisateurService.inscrire(request);
            redirectAttributes.addFlashAttribute("success", "Inscription réussie ! Connectez-vous.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/inscription";
        }
    }
}
