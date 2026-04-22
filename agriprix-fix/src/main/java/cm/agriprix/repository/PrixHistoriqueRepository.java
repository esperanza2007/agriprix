package cm.agriprix.repository;

import cm.agriprix.model.PrixHistorique;
import cm.agriprix.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrixHistoriqueRepository extends JpaRepository<PrixHistorique, Long> {

    List<PrixHistorique> findByProduitOrderByDateDesc(Produit produit);

    @Query("SELECT p FROM PrixHistorique p WHERE p.produit = :produit AND p.date >= :startDate ORDER BY p.date")
    List<PrixHistorique> findRecentByProduit(Produit produit, LocalDate startDate);

    // Pour graphiques : derniers prix par produit
    @Query("SELECT p FROM PrixHistorique p WHERE p.produit.id = :produitId ORDER BY p.date DESC")
    List<PrixHistorique> findByProduitIdOrderByDateDesc(Long produitId);
}
