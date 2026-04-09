# Gestion Société - Backend API

## 📌 Description

**Gestion Société** est une API REST développée avec **Spring Boot** permettant de gérer les activités d’une société :

* gestion des employés
* gestion des projets
* gestion des phases de projet
* gestion des livrables et documents
* gestion des factures
* affectation des employés aux projets
* génération de rapports et tableau de bord

L’architecture du projet suit les bonnes pratiques **Spring Boot professionnelles** avec séparation claire des responsabilités :
Controller → Service → Repository → Entity → DTO.

---

## 🛠 Technologies utilisées

* **Java 17+**
* **Spring Boot**
* **Spring Data JPA**
* **Spring Security**
* **JWT Authentication**
* **Swagger / OpenAPI**
* **Maven**
* **Hibernate**
* **MySQL / PostgreSQL**
* **Lombok**

---

## 📂 Architecture du projet

```
src/main/java/ma/fst/projet2societe

├── config
│   ├── SecurityConfig
│   ├── SwaggerConfig
│   └── DataInitializer
│
├── controllers
│   ├── AuthController
│   ├── EmployeController
│   ├── ProjectController
│   ├── PhaseController
│   ├── AffectationController
│   ├── DocumentController
│   ├── FactureController
│   └── ReportingController
│
├── dto
│   ├── request
│   └── response
│
├── entities
│   ├── Employe
│   ├── Project
│   ├── Phase
│   ├── Affectation
│   ├── Document
│   └── Facture
│
├── repositories
│
├── services
│
└── security
```

---

## 🔐 Authentification et sécurité

Le projet utilise **Spring Security avec JWT** pour sécuriser les endpoints.

Fonctionnalités :

* Authentification utilisateur
* Génération de **JWT Token**
* Protection des routes API
* Gestion des rôles utilisateurs

---

## 📊 Fonctionnalités principales

### 👨‍💼 Gestion des employés

* Création d’un employé
* Modification des informations
* Suppression
* Liste des employés

### 📁 Gestion des projets

* Création d’un projet
* Consultation des projets
* Mise à jour des informations
* Suppression

### 🧩 Gestion des phases

* Ajouter des phases à un projet
* Suivi de l’avancement
* Génération de rapports

### 📄 Gestion des documents

* Upload de documents
* Association aux projets
* Consultation et téléchargement

### 💰 Gestion des factures

* Création de factures
* Suivi des paiements
* Consultation des historiques

### 📈 Reporting

* Tableau de bord
* Rapports sur les projets
* Statistiques globales

---

## 🚀 Installation et lancement

### 1️⃣ Cloner le projet

```bash
git clone https://github.com/your-username/gestion-societe.git
```

---

### 2️⃣ Accéder au projet

```bash
cd gestion-societe
```

---

### 3️⃣ Configurer la base de données

Modifier le fichier :

```
application.properties
```

Exemple :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_soc
spring.datasource.username=root
spring.datasource.password=password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

### 4️⃣ Lancer l'application

```bash
mvn spring-boot:run
```

Ou

```bash
./mvnw spring-boot:run
```

---

## 📖 Documentation API

Swagger est disponible après lancement du projet :

```
http://localhost:8080/swagger-ui.html
```

Cette interface permet de :

* tester les endpoints
* voir les paramètres
* comprendre la structure des réponses

---

## 🧪 Tests API

Les endpoints peuvent être testés avec :

* Postman
* Swagger
* Insomnia

---

## 📌 Bonnes pratiques appliquées

✔ Architecture en couches


✔ Utilisation des **DTO**

✔ Séparation des responsabilités

✔ Gestion des exceptions

✔ Sécurité avec JWT


✔ Documentation API avec Swagger

---

## Contributeur

**Mohamed EDDINARI**


**Mohamed ELMOUDEN**


**Ilyass OUBABA**




---

## 📜 Licence

Ce projet est destiné à un usage **éducatif et académique**.
