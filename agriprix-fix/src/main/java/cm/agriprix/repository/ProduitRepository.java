package cm.agriprix.repository;

import cm.agriprix.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    // Méthodes de requête personnalisées si nécessaire
}
