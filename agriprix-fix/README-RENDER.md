# 🚀 Déploiement AgriPrix sur Render

## Étapes complètes

### 1. Mettre le code sur GitHub

```bash
git init
git add .
git commit -m "AgriPrix - prêt pour Render"
git branch -M main
git remote add origin https://github.com/TON-PSEUDO/agriprix.git
git push -u origin main
```

### 2. Créer la base de données sur Render

1. Aller sur https://render.com → New + → **PostgreSQL**
2. Remplir :
   - Name : `agriprix-db`
   - Plan : **Free**
3. Cliquer **Create Database**
4. Noter les infos de connexion affichées

### 3. Déployer l'application

1. Render → New + → **Web Service**
2. Connecter le repo GitHub `agriprix`
3. Remplir :
   - **Name** : agriprix
   - **Runtime** : Java
   - **Build Command** : `mvn clean package -DskipTests`
   - **Start Command** : `java -jar target/agriprix-extremenord-1.0.0.jar`
   - **Plan** : Free

4. Dans **Environment Variables** ajouter :

| Clé | Valeur |
|-----|--------|
| `DATABASE_URL` | URL de connexion PostgreSQL (depuis l'étape 2) |
| `DB_USER` | Username PostgreSQL |
| `DB_PASSWORD` | Password PostgreSQL |

5. Cliquer **Create Web Service**

### 4. Comptes de test

| Rôle | Téléphone | Mot de passe |
|------|-----------|--------------|
| Admin | 699000000 | admin123 |
| Producteur | 677123456 | amadou123 |
| Commerçant | 655987654 | fatime123 |

### 5. En local (développement)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
Accès : http://localhost:8080

---
⚠️ Sur le plan gratuit Render, l'app se met en veille après 15 min.
Utiliser UptimeRobot (gratuit) pour la garder active.
