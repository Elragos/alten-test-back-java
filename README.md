# Alten Test Back version JAVA

Ceci est la version JAVA du test technique back pour mon entrée chez [Alten](https://www.alten.fr/).
Elle utilise un système d'authentification via JWT.
L'API utilise également un système de traduction, pour l'heure seuls l'anglais et le français fonctionnent.

L'API exploite les technologies suivantes pour fonctionner :

![Static Badge](https://img.shields.io/badge/JDK-23.0.2-green?style=flat)
![Static Badge](https://img.shields.io/badge/Spring-3.4.3-green?style=flat)
![Static Badge](https://img.shields.io/badge/Docker-28.0.1-green?style=flat)
![Static Badge](https://img.shields.io/badge/gradle-8.12.1-green?style=flat)


## Prérequis

* [JDK >= 23.0.2](https://www.oracle.com/fr/java/technologies/downloads/) pour faire tourner le projet.
* [Docker >= 28.0.1](https://www.docker.com/) pour faire tourner la BDD.

## Installation

Récupérer le repo github : 
```
git clone https://github.com/Elragos/alten-test-back-java.git
```
Dupliquer le fichier application_example.yml dans application.yml et mettez 
le à jour selon votre configuration JWT souhaitée :
```
cp application_example.yml application.yml
```
Dupliquer le fichier compose_example.yaml dans compose.yaml et mettez 
le à jour selon votre configuration BDD souhaitée :
```
cp compose_example.yaml compose.yaml
```

## Lancement de l'installation

Lancer gradlew pour démarrer l'application
```
gradlew --configure-on-demand -x check bootRun
```

La BDD MySQL devrait s'initialiser toute seul au premier lancement avec :
* Les rôles USER et ADMIN dans role
* Les utilisateurs et produits définis dans la ressource [initialData.json](https://github.com/Elragos/alten-test-back-java/blob/main/src/main/resources/initialData.json)

## Exploiter l'API

Utiliser le fichier Postman pour avoir toutes les URL disponibles pour communiquer avec l'API
