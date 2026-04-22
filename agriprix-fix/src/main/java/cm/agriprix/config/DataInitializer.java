package cm.agriprix.config;

import cm.agriprix.model.*;
import cm.agriprix.repository.PrixHistoriqueRepository;
import cm.agriprix.repository.ProduitRepository;
import cm.agriprix.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private PrixHistoriqueRepository prixHistoriqueRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // --- Utilisateurs ---
        if (utilisateurRepository.count() == 0) {
            Utilisateur admin = new Utilisateur();
            admin.setTelephone("699000000");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Utilisateur.Role.ADMIN);
            admin.setNom("Administrateur");
            utilisateurRepository.save(admin);

            Utilisateur producteur = new Utilisateur();
            producteur.setTelephone("677123456");
            producteur.setPassword(passwordEncoder.encode("amadou123"));
            producteur.setRole(Utilisateur.Role.PRODUCTEUR);
            producteur.setNom("Amadou Diallo");
            utilisateurRepository.save(producteur);

            Utilisateur commercant = new Utilisateur();
            commercant.setTelephone("655987654");
            commercant.setPassword(passwordEncoder.encode("fatime123"));
            commercant.setRole(Utilisateur.Role.COMMERCANT);
            commercant.setNom("Fatime Hassan");
            utilisateurRepository.save(commercant);
        }

        // --- Produits ---
        if (produitRepository.count() == 0) {
            // Céréales
            Produit mais = produitRepository.save(new Produit("Maïs", "Céréales", "🌽"));
            Produit sorgho = produitRepository.save(new Produit("Sorgho", "Céréales", "🌾"));
            Produit mil = produitRepository.save(new Produit("Mil", "Céréales", "🌿"));
            Produit riz = produitRepository.save(new Produit("Riz", "Céréales", "🍚"));

            // Légumineuses
            Produit niebe = produitRepository.save(new Produit("Niébé", "Légumineuses", "🫘"));
            Produit arachide = produitRepository.save(new Produit("Arachide", "Légumineuses", "🥜"));

            // Rente
            Produit oignon = produitRepository.save(new Produit("Oignon", "Rente", "🧅"));
            Produit coton = produitRepository.save(new Produit("Coton", "Rente", "🌸"));

            // --- Prix historiques réalistes sur 12 mois (sac de 100kg en FCFA) ---
            // Cycle saisonnier : bas en Oct-Jan (récolte), monte en Fév-Avr, pic Mai-Juil (soudure)
            ajouterPrixMensuel(mais, new double[]{13000, 12500, 12000, 11500, 14000, 17000, 22000, 25000, 24000, 20000, 17000, 14500}, "Maroua");
            ajouterPrixMensuel(sorgho, new double[]{11000, 10500, 10200, 10000, 12500, 15000, 19000, 21000, 20000, 17000, 14500, 12000}, "Maroua");
            ajouterPrixMensuel(mil, new double[]{12000, 11500, 11000, 10500, 13000, 16000, 20000, 23000, 22000, 18500, 16000, 13500}, "Maroua");
            ajouterPrixMensuel(riz, new double[]{25000, 25500, 26000, 26500, 27000, 28000, 30000, 31000, 30500, 29000, 27500, 26000}, "Maroua");
            ajouterPrixMensuel(niebe, new double[]{16000, 15500, 15000, 14800, 17000, 21000, 26000, 28000, 27000, 23000, 20000, 17500}, "Maroua");
            ajouterPrixMensuel(arachide, new double[]{22000, 21000, 20500, 20000, 23000, 27000, 33000, 35000, 34000, 30000, 27000, 23500}, "Maroua");
            ajouterPrixMensuel(oignon, new double[]{20000, 19000, 18500, 18000, 22000, 28000, 36000, 38000, 35000, 30000, 26000, 22000}, "Maroua");
            ajouterPrixMensuel(coton, new double[]{30000, 30000, 29500, 29000, 31000, 34000, 38000, 40000, 39000, 36000, 33000, 31000}, "Maroua");
        }
    }

    /**
     * Ajoute 12 entrées mensuelles pour un produit.
     * Les prix correspondent aux 12 derniers mois (du plus ancien au plus récent).
     */
    private void ajouterPrixMensuel(Produit produit, double[] prixMensuels, String marche) {
        LocalDate debut = LocalDate.now().minusMonths(11).withDayOfMonth(1);
        for (int i = 0; i < prixMensuels.length; i++) {
            PrixHistorique p = new PrixHistorique();
            p.setProduit(produit);
            p.setPrix(prixMensuels[i]);
            p.setDate(debut.plusMonths(i));
            p.setMarche(marche);
            prixHistoriqueRepository.save(p);
        }
    }
}
