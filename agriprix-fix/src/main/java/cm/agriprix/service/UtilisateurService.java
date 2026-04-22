package cm.agriprix.service;

import cm.agriprix.dto.InscriptionRequest;
import cm.agriprix.model.Utilisateur;
import cm.agriprix.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String telephone) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByTelephone(telephone)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé: " + telephone));

        return User.builder()
                .username(utilisateur.getTelephone())
                .password(utilisateur.getPassword())
                .roles(utilisateur.getRole().name())
                .build();
    }

    public Utilisateur inscrire(InscriptionRequest request) {
        if (utilisateurRepository.findByTelephone(request.getTelephone()).isPresent()) {
            throw new IllegalArgumentException("Ce numéro de téléphone est déjà utilisé");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setTelephone(request.getTelephone());
        utilisateur.setPassword(passwordEncoder.encode(request.getPassword()));
        utilisateur.setRole(request.getRole());
        utilisateur.setNom(request.getNom());

        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur getCurrentUser(String telephone) {
        return utilisateurRepository.findByTelephone(telephone).orElse(null);
    }
}
