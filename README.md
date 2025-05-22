# Application de Gestion des Actions de Charité

Une application web Spring Boot pour la gestion des actions de charité et des dons.

## Fonctionnalités

- Gestion des utilisateurs et authentification
- Gestion des dons et des actions de charité
- Tableau de bord pour le suivi des activités
- Interface utilisateur moderne et responsive
- Sécurité renforcée avec Spring Security

## Technologies utilisées

- Java 17
- Spring Boot 3.2.3
- Spring Security
- Spring Data JPA
- Thymeleaf
- MongoDB
- Bootstrap 5
- Font Awesome

## Prérequis

- JDK 17 ou supérieur
- Maven 3.6 ou supérieur
- MongoDB

## Installation

1. Cloner le dépôt :
```bash
git clone https://github.com/Holy-C-afk/OrganisationCharite.git
cd OrganisationCharite
```

2. Configurer la base de données dans `application.properties`

3. Compiler et exécuter l'application :
```bash
mvn clean install
mvn spring-boot:run
```

4. Accéder à l'application : http://localhost:8080

## Structure du projet

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── charite/
│   │           ├── config/
│   │           ├── controller/
│   │           ├── model/
│   │           ├── repository/
│   │           ├── service/
│   │           └── ChariteApplication.java
│   └── resources/
│       ├── static/
│       ├── templates/
│       └── application.properties
└── test/
```

## Contribution

Les contributions sont les bienvenues ! N'hésitez pas à ouvrir une issue ou à soumettre une pull request.

## Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails. 