# Yoga App

## Cloner le dépôt du projet
> git clone https://github.com/fredg6/yoga

## Back-end

### Installation de Java

* Télécharger le JDK 11 par exemple [ici](https://adoptium.net/fr/temurin/releases?version=11&os=any&arch=any), puis l'installer
* Créer si besoin la variable d'environnement système suivante :
  
|    Nom    |                   Valeur                   |
|:---------:|:------------------------------------------:|
| JAVA_HOME | <chemin_vers_le_répertoire_d_installation> |

* Ajouter à la variable d'environnement ```Path``` la valeur ```<chemin_vers_le_répertoire_d_installation>\bin``` (exemple sous Windows)

### Installation de Maven

* Télécharger Maven [ici](https://maven.apache.org/download.cgi), puis décompresser l'archive dans le répertoire de votre choix
* Créer si besoin la variable d'environnement système suivante :

|    Nom     |                   Valeur                   |
|:----------:|:------------------------------------------:|
| MAVEN_HOME | <chemin_vers_le_répertoire_d_installation> |

* Ajouter à la variable d'environnement ```Path``` la valeur ```<chemin_vers_le_répertoire_d_installation>\bin``` (exemple sous Windows)

### Installation de la base de données

* Pour installer le SGBD MySql en version 8.0.43, suivre ce [guide OpenClassrooms très complet :)](https://openclassrooms.com/fr/courses/6971126-implementez-vos-bases-de-donnees-relationnelles-avec-sql/7152681-installez-le-sgbd-mysql)
* Créer la base de données en exécutant dans le client en ligne de commande MySql (exemple sous Windows) :
> mysql> CREATE DATABASE test;\
> mysql> USE DATABASE test;\
> mysql> source <chemin_vers_le_projet>\ressources\sql\script.sql

**En cas d'erreur sur la commande ```source```, placer le fichier à la racine du lecteur par exemple**
* Créer les variables d'environnement système suivantes :
  
 |        Nom        |                            Valeur                             |
 |:-----------------:|:-------------------------------------------------------------:|
 |   DATABASE_URL    | jdbc:mysql://localhost:3306/test?allowPublicKeyRetrieval=true |
 | DATABASE_USERNAME |                      root _(par défaut)_                      |
 | DATABASE_PASSWORD |                        <mot_de_passe>                         |

Les commandes suivantes sont à exécuter dans le répertoire _back_ du projet :

### Installation des dépendances

> mvn clean install

### Démarrage de l'application

> mvn spring-boot:run

### Exécution des tests

> mvn clean test

Le rapport de couverture est généré sous _back/target/site/jacoco/index.html_

## Front-end

### Installation de Node.js

* Télécharger Node.js 16 [ici](https://nodejs.org/en/download), puis l'installer
* Ajouter à la variable d'environnement ```Path``` la valeur ```<chemin_vers_le_répertoire_d_installation>``` (exemple sous Windows)

Les commandes suivantes sont à exécuter dans le répertoire _front_ du projet :

### Installation des dépendances

> npm install

### Démarrage de l'application

> npm run start

### Exécution des tests unitaires et d'intégration

> ng test --coverage

### Exécution des tests E2E

> npm run e2e:ci && npm run e2e:coverage

Le rapport de couverture est généré sous _front/coverage/lcov-report/index.html_