# 🌾 AgriPrix Extrême-Nord

> Plateforme d'aide à la décision pour le stockage et la vente agricole  
> Région Extrême-Nord, Ca'meroun

---

## 🚀 Démarrage rapide

### Prérequis
- Java 17+
- Maven 3.8+

### Lancer l'application
```bash
mvn spring-boot:run
```
Ouvrir : **http://localhost:8080**

### Comptes de démonstration
| Rôle       | Téléphone   | Mot de passe |
|------------|-------------|--------------|
| Admin      | 699000000   | admin123     |
| Producteur | 677123456   | amadou123    |
| Commerçant | 655987654   | fatime123    |

### Console H2 (base de données dev)
http://localhost:8080/h2-console  
JDBC URL : `jdbc:h2:mem:agriprixdb`

---

## 🏗️ Architecture du projet

```
src/main/java/cm/agriprix/
│
├── AgriPrixApplication.java        ← Point d'entrée Spring Boot
│
├── model/                          ← Entités JPA
│   ├── Produit.java                   (Maïs, Sorgho, Oignon...)
│   ├── PrixHistorique.java            (Relevés quotidiens par marché)
│   ├── Utilisateur.java               (Producteur / Commerçant / Collecteur)
│   └── SimulationStockage.java        (Résultats de simulation sauvegardés)
│
├── repository/                     ← Spring Data JPA (accès base de données)
│   ├── ProduitRepository.java
│   ├── PrixHistoriqueRepository.java  ← Requêtes JPQL pour graphiques
│   ├── UtilisateurRepository.java
│   └── SimulationStockageRepository.java
│
├── service/                        ← Logique métier
│   ├── ProduitService.java            (Prix, graphiques, zones marché)
│   ├── SimulateurService.java         (Calcul profit, ROI, recommandations)
│   └── UtilisateurService.java        (Auth Spring Security, inscription)
│
├── dto/                            ← Objets de transfert de données
│   ├── SimulationRequest.java         (Formulaire simulateur + validation)
│   ├── SimulationResultatDTO.java     (Résultats formatés FCFA)
│   └── InscriptionRequest.java        (Formulaire inscription + validation)
│
├── controller/                     ← Contrôleurs Spring MVC
│   ├── AuthController.java            (GET/POST login, inscription)
│   ├── MarcheController.java          (Tableau de bord, détail produit, API JSON)
│   ├── SimulateurController.java      (Formulaire, calcul, historique)
│   └── AdminController.java           (Dashboard admin, produits, relevés)
│
└── config/
    ├── SecurityConfig.java            (Login par téléphone, BCrypt, routes)
    └── DataInitializer.java           (Données de démo au démarrage)
```

```
src/main/resources/
├── application.properties
├── templates/
│   ├── fragments/layout.html          ← Navbar, bottom-nav, badges réutilisables
│   ├── auth/
│   │   ├── login.html
│   │   └── inscription.html
│   ├── marche/
│   │   ├── tableau-de-bord.html       ← Grille produits + légende zones
│   │   └── detail-produit.html        ← Graphique Chart.js + recommandation
│   ├── simulateur/
│   │   ├── formulaire.html            ← Saisie paramètres de stockage
│   │   ├── resultat.html              ← Profit net, ROI, recommandation
│   │   └── historique.html            ← 5 dernières simulations
│   ├── admin/
│   │   ├── dashboard.html             ← Stats + tableau produits
│   │   ├── produit-form.html          ← Ajout / modification produit
│   │   └── releve-prix.html           ← Saisie prix terrain
│   └── error.html
└── static/
    └── css/
        ├── agriprix.css               ← Styles principaux
        └── agriprix-extra.css         ← Styles admin + historique
```

---

## 🧮 Formule de simulation

```
Investissement total  = Quantité × Prix d'achat
Frais de stockage     = Quantité × Frais mensuels × Durée (mois)
Prix de revient       = Investissement + Frais de stockage
Prix de vente estimé  = Quantité × Prix actuel du marché
Profit net estimé     = Prix de vente - Prix de revient
ROI (%)               = (Profit net / Prix de revient) × 100
```

**Codes couleur :**
| Zone   | ROI          | Conseil                              |
|--------|-------------|--------------------------------------|
| 🟢 VERT | ≥ 20%       | Stocker — Prix bas, bonne opportunité |
| 🟡 JAUNE | 0% à 20%  | Observer — Profit modeste             |
| 🔴 ROUGE | < 0%      | Déficitaire — Revoir les paramètres   |

---

## 🗄️ Pour passer en production (MySQL)

Dans `application.properties`, remplacer la section H2 par :
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/agriprix?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=votre_mot_de_passe
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=false
```

---

## 🧪 Lancer les tests
```bash
mvn test
```

Tests disponibles :
- `SimulateurServiceTest` — 7 tests de calcul profit/ROI
- `ProduitServiceTest` — 6 tests service produit
- `UtilisateurServiceTest` — 4 tests authentification
- `MarcheControllerTest` — 5 tests d'intégration HTTP
