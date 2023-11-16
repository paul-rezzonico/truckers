//TODO :


Exemple message valide : 
	RDV 4 rue Pasteur 18h00
	LIVRAISON prévu le 4 janvier

Exemple message invalide :
	Bonjour, pouvez-vous m’indiquer votre position ?
	Pouvez-vous me répondre maintenant ?

Exemple message de configuration admin : 

CONFIG 
Ajout numéro 011111111111 
Suppression numéro 011111111111 
Ajout mot-clé RENDEZ-VOUS 
Suppression mot-clé RENDEZ-VOUS 
Ajout administrateur 0453627283 
Suppression administrateur 023923492

# Application de Gestion des Messages

Ce service a pour but de traquer les messages de chauffeurs liés à leurs itinéraires, rendez-vous, livraisons, etc.

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

