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

### Backend 


<img width="1043" height="914" alt="Diagramme sans nom drawio (1)" src="https://github.com/user-attachments/assets/0cbed0d4-298c-48a5-a73d-de87630e7428" />

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

### Frontend 

<img width="1047" height="879" alt="image" src="https://github.com/user-attachments/assets/6d3132a0-a26d-40b6-a286-878c1ab47ee8" />

---



## Conception UML : 

### Diagramme de classe : 

![WhatsApp Image 2026-04-09 at 17 40 18](https://github.com/user-attachments/assets/0bb62d37-6b7c-4e3d-8dca-ff1f6e38ad3f)

### Diagramme de séquence  :
<img width="1154" height="782" alt="TPF1hfim44NtynMZjmgBwEsxg5u2I8qgDGXezouCgYK78u-9NZwftz4VwvXOmK4A4Wlcddl7rn8Vx9NpyyM0whnr-DDherbvcXt099BkzAH6Zs-7OQ3EAw_jcDlHEsiCkJTKZFbBi25trHqjLFDCbPV9s1lLD5dMilcsG6NzJ5L_56-PgdwvRqCUQkYLLxyb0uiZG58EzrynnozOd" src="https://github.com/user-attachments/assets/013cb5ce-3687-466f-a5fb-be2f9fecc920" />



## Video Demo 🎥 : 


https://github.com/user-attachments/assets/903456c7-c481-405f-822b-b38d422134fd






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


**Mohamed EL MOUDEN**


**Ilyass OUBABA**




---

## 📜 Licence

Ce projet est destiné à un usage **éducatif et académique**.
