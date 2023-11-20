# Application de Gestion des Messages

Ce service a pour but de traquer les messages de chauffeurs liés à leurs itinéraires, rendez-vous, livraisons, etc.

## Ajout du Fichier Défaut

Pour que le service puisse se lancer, il faut donner à l'application un fichier défaut.json (contenant minimum un numéro admin par défaut).

1. Ouvrez Android Studio et connectez votre appareil Android à l'ordinateur.

2. Accédez à l'onglet "Device Explorer" dans l'IDE Android Studio.

3. Naviguez vers le répertoire interne de l'application sur votre téléphone en suivant le chemin : `data/data/com.unilim.iut.truckers/files`.

4. Copiez le fichier "defaut.json" situé dans le répertoire "média" du projet dans le répertoire mentionné ci-dessus sur votre téléphone.

5. Assurez-vous que le fichier "defaut.json" est correctement placé dans le répertoire spécifié.

Le fichier "defaut.json" contient les configurations par défaut nécessaires pour le bon fonctionnement de l'application. Veillez à ne pas modifier ce fichier de manière inappropriée pour éviter tout problème de configuration.
Vous pouvez relancer l'application, le service va se lancer et rester actif.


## Utilisation de l'Application

1. **Recevoir des Messages :**
   - Le service récupère automatiquement les messages reçus par les chauffeurs.
   - Les messages valides sont stockés, tandis que les messages invalides sont ignorés.

2. **Gérer les Messages :**
   - Les messages stockés seront synchronisés dans une API REST afin d'avoir un historique des messages reçus côté Admin.

## Exemples de Messages Validés

- **Rendez-vous :** RDV 4 rue Pasteur 18h00
- **Livraison :** LIVRAISON prévue le 4 janvier

## Exemple de Message Invalide

- Bonjour, pouvez-vous m’indiquer votre position ? Pouvez-vous me répondre maintenant ?

## Configuration Administrative

Les administrateurs peuvent configurer l'application en utilisant des messages de configuration spécifiques.

- **Ajout de Numéro :** CONFIG Ajout numéro 011111111111
- **Suppression de Numéro :** CONFIG Suppression numéro 011111111111
- **Ajout de Mot-clé :** CONFIG Ajout mot-clé RENDEZ-VOUS
- **Suppression de Mot-clé :** CONFIG Suppression mot-clé RENDEZ-VOUS
- **Ajout d'Administrateur :** CONFIG Ajout administrateur 0453627283
- **Suppression d'Administrateur :** CONFIG Suppression administrateur 023923492

## Stockage des Messages

Les messages sont stockés dans le répertoire `/data/data/com.unilim.iut.truckers/files`. Ils sont répartis dans deux fichiers distincts en fonction de leur validité. Chaque jour, de nouveaux fichiers sont créés pour minimiser les risques de perte d'informations.

1. Ouvrez le device explorer sur Android Studio.

2. Accédez au répertoire `/data/data/com.unilim.iut.truckers/files`.

3. Vous y trouverez les fichiers `messagesValides-date` et `messageInvalides-date`.

4. Pour consulter les messages ouvrez les fichiers avec un éditeur de texte.



## Consulter les Logs

Pour vérifier le bon fonctionnement du service, vous pouvez consulter les logs enregistrés dans le fichier "logcat.txt" sur votre téléphone, accessible dans votre répertoire de téléchargements.

1. Ouvrez un gestionnaire de fichiers sur votre téléphone.

2. Accédez au répertoire de téléchargements.

3. Recherchez le fichier "logcat.txt" et ouvrez-le avec un éditeur de texte.

4. Vous verrez les détails des actions du service en réponse à la réception des messages.


