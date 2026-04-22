package cm.agriprix.controller;

import cm.agriprix.model.Message;
import cm.agriprix.model.Utilisateur;
import cm.agriprix.repository.MessageRepository;
import cm.agriprix.repository.UtilisateurRepository;
import cm.agriprix.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessageController {

    @Autowired private MessageRepository messageRepository;
    @Autowired private UtilisateurRepository utilisateurRepository;
    @Autowired private UtilisateurService utilisateurService;

    /** Liste des conversations */
    @GetMapping
    public String index(Model model, Principal principal) {
        Utilisateur moi = utilisateurService.getCurrentUser(principal.getName());

        // Récupérer tous les utilisateurs avec qui j'ai échangé
        List<Utilisateur> interlocuteurs = messageRepository.findInterlocuteurs(moi);

        // Nombre de messages non lus
        long nonLus = messageRepository.findByDestinataireAndLuFalse(moi).size();

        model.addAttribute("interlocuteurs", interlocuteurs);
        model.addAttribute("nonLus", nonLus);
        model.addAttribute("moi", moi);

        // Si admin : montrer aussi tous les utilisateurs pour démarrer une conv
        if (moi.getRole() == Utilisateur.Role.ADMIN) {
            List<Utilisateur> tousUtilisateurs = utilisateurRepository.findAll()
                    .stream().filter(u -> !u.getId().equals(moi.getId())).toList();
            model.addAttribute("tousUtilisateurs", tousUtilisateurs);
        }

        return "messages/index";
    }

    /** Conversation avec un utilisateur spécifique */
    @GetMapping("/{userId}")
    public String conversation(@PathVariable Long userId, Model model, Principal principal) {
        Utilisateur moi = utilisateurService.getCurrentUser(principal.getName());
        Utilisateur interlocuteur = utilisateurRepository.findById(userId).orElse(null);
        if (interlocuteur == null) return "error";

        // Charger la conversation
        List<Message> messages = messageRepository.findConversation(moi, interlocuteur);

        // Marquer comme lus les messages reçus
        messages.stream()
                .filter(m -> m.getDestinataire().getId().equals(moi.getId()) && !m.isLu())
                .forEach(m -> { m.setLu(true); messageRepository.save(m); });

        model.addAttribute("messages", messages);
        model.addAttribute("interlocuteur", interlocuteur);
        model.addAttribute("moi", moi);

        // Pour admin : liste tous les utilisateurs
        if (moi.getRole() == Utilisateur.Role.ADMIN) {
            model.addAttribute("tousUtilisateurs", utilisateurRepository.findAll()
                    .stream().filter(u -> !u.getId().equals(moi.getId())).toList());
        }

        return "messages/conversation";
    }

    /** Envoyer un message */
    @PostMapping("/envoyer")
    public String envoyer(@RequestParam Long destinataireId,
                          @RequestParam String contenu,
                          Principal principal,
                          RedirectAttributes redirectAttributes) {
        Utilisateur moi = utilisateurService.getCurrentUser(principal.getName());
        Utilisateur destinataire = utilisateurRepository.findById(destinataireId).orElse(null);

        if (destinataire == null || contenu.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("erreur", "Message invalide.");
            return "redirect:/messages";
        }

        messageRepository.save(new Message(moi, destinataire, contenu.trim()));
        return "redirect:/messages/" + destinataireId;
    }

    /** Nouveau message (démarrer une conversation) */
    @GetMapping("/nouveau/{userId}")
    public String nouveau(@PathVariable Long userId, Model model, Principal principal) {
        Utilisateur moi = utilisateurService.getCurrentUser (principal.getName());
        Utilisateur destinataire = utilisateurRepository.findById(userId).orElse(null);
        if (destinataire == null) return "error";
        return "redirect:/messages/" + userId;
    }
}
