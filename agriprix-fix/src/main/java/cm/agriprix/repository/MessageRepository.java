package cm.agriprix.repository;

import cm.agriprix.model.Message;
import cm.agriprix.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Conversation entre deux utilisateurs
    @Query("SELECT m FROM Message m WHERE " +
            "(m.expediteur = :u1 AND m.destinataire = :u2) OR " +
            "(m.expediteur = :u2 AND m.destinataire = :u1) " +
            "ORDER BY m.dateEnvoi ASC")
    List<Message> findConversation(@Param("u1") Utilisateur u1, @Param("u2") Utilisateur u2);

    // Tous les messages reçus par un utilisateur
    List<Message> findByDestinataireOrderByDateEnvoiDesc(Utilisateur destinataire);

    // Messages non lus
    List<Message> findByDestinataireAndLuFalse(Utilisateur destinataire);

    // Tous les interlocuteurs distincts d'un utilisateur
    @Query("SELECT DISTINCT m.expediteur FROM Message m WHERE m.destinataire = :user " +
            "UNION SELECT DISTINCT m.destinataire FROM Message m WHERE m.expediteur = :user")
    List<Utilisateur> findInterlocuteurs(@Param("user") Utilisateur user);
}
