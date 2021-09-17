# Projet 12: HandleYourLoans (back-end)

Dépôt contenant le _code_ _source_ des différents modules liés à la partie _back-end_ du projet _HYL_ ainsi que sa _base_ _de_ _données_ .


## Hébergement du code source

1. Pour la partie back-end -> https://github.com/Aasdam/hyl_back-end
2. Pour la partie front-end -> https://github.com/Aasdam/hyl_web-client


## Hébergement des configurations

https://github.com/Aasdam/hyl_config-repo  
_Si_ _le_ _dépôt_ _est_ _en_ _privé_ _,_ _me_ _contacter_ _(ayrton.as.dam@gmail.com)_


## Description

* _db_ : Ressources concernant la base de données
* _dev_ : Code souce de l'application (partie back-end)
* _integration_ : Test d'intégration (Postman)
* _attach_ : Annexes du projet (configurations, dataset-image, diagrams)

## Information

Une fois la partie _back-end_ déployée, elle peut être testée grâce au fichier _integration/P12_HYL.postman_collection.json_ compatible avec l'outil de test d'API _Postman_ .
Pour avoir un point de vue plus concret, il faudra déployer la partie _front-end_ dont l'adresse d'hébergement est disponible ci-dessus.

## Procédure de déploiement

### Mise en place de la base de donnée

Vérifiez sur votre machine la présences des fichiers suivants dans le répertoire _db/scripts-sql_ :
* _create_users_db.sql_ (Créer les utilisateurs autorisés à se connecter à la base de donnée)
* _create_user_schema.sql_ (Créer le schéma, les tables, les séquences, etc, du microservice utilisateur)
* _create_loan_schema.sql_ (Créer le schéma, les tables, les séquences, etc, du microservice prêt)
* _create_item_schema.sql_ (Créer le schéma, les tables, les séquences, etc, du microservice objet)
* _create_memo_schema.sql_ (Créer le schéma, les tables, les séquences, etc, du microservice memo)
* _insert_dataset.sql_ (Insert un jeu de données afin de vérifier le bon fonctionnement du système)

Vérifiez également que votre outil _Docker_ soit configuré sur l'adresse IP : 192.168.99.100

#### Lancement

	Exécutez le script shell db/deploy-db.sh


#### Remise à zéro

    Exécutez le fichier db/reset-data.sql depuis votre outil d'administration de base de données préféré.


### Mise en place de l'application (La base de données doit être fonctionnelle)

#### Générer les .jar
1. Ouvrez votre terminal
2. Placez-vous sur un des modules présent dans le répertoire _dev_
3. Exécutez la commande _mvn_ _clean_ _package_
4. Répétez la commande pour _chaque_ _module_
5. Vérifiez la présence du .jar dans le fichier _target_ de chacun des différents modules


#### Orchestration du déploiement
Déployez avec la commande _java_ _-jar_ _chemin_du_fichier/target/nom_du_module.jar_ en respectant l'ordre suivant: 
* le serveur des configurations externalisées _config-serveur_
* le serveur d'enregistrement _discovery-server_
* la passerelle _gateway-server_
* les différentes instances des _microservices_ en configurant leur port avec _-Dserver.port=XXXX_ à la suite de la commande ( _utilisateur(conseillé:9000)_ , _prêt(conseillé:9100)_ , _objet(conseillé:9200)_ , _mémo(conseillé:9300)_  )
* le serveur courriel _mail-server_ en configurant son port avec _-Dserver.port=XXXX_ à la suite de la commande _(conseillé:9400)_
* le serveur de gestion de l'application _admin-server_

#### Vérification du déploiement
Ouvrez votre navigateur web et saisissez :
* http://localhost:9091/ pour avoir accès à toutes les intances enregistrées _discovery-server_
* http://localhost:9092/ pour avoir accès à l'ihm de gestion de l'application _admin-server_										


## Lancement du batch journalier (La base de données doit être fonctionnelle)

### Générer le .jar

1. Ouvrez votre terminal à _dev/batch_ puis exécutez la commande _mvn_ _clean_ _package_

### Batch: Envoi d'un email aux utilisateurs ayant un prêt avec demande de rappel (en ce jour)

1. Vérifiez la présence du fichier _batch.jar_
2. Exécutez le batch avec la commande _java_ _-jar_ _chemin_du_fichier/target/batch.jar_

