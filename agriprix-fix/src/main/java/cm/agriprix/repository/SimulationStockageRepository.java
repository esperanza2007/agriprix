package cm.agriprix.repository;

import cm.agriprix.model.SimulationStockage;
import cm.agriprix.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimulationStockageRepository extends JpaRepository<SimulationStockage, Long> {

    List<SimulationStockage> findByUtilisateurOrderByDateSimulationDesc(Utilisateur utilisateur);
}
