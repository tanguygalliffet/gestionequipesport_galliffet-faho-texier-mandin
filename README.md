
# Site web gestion d'un club sportif (club de foot)

Réalisation de 4 pages (gestion des joueurs, gestion des entraîneurs, gestion des matchs et gestion des entraînements)

Utilisation des packages PSQL



Il y également un header permettant de naviguer entre ces différentes pages. 

1. Gestion des joueurs

Tableau des joueurs avec les paramètres suivants : Numéro, Nom, Prénom, Email, Poste, Téléphone, Actions Modifier ou Supprimer.

Boutons permettant d'afficher tous les joueurs, afficher les joueurs par poste, les joueurs de plus de 20 ans et la possibilité d'ajouter un joueur via un formulaire


2. Gestion des entraîneurs

Tableau des entraîneurs avec les paramètres suivants : Numéro, Nom, Prénom, date de naissance, Email, Téléphone, Numéro d'équipe, Actions Modifier ou Supprimer.

Boutons permettant de rechercher un entraîneur par son nom et possibilité d'ajouter un entraîneur.

3. Gestion des entraînements

Tableau des entraînements avec le numéro de l'entraînement, le numéro de l'équipe, la date et heure de début, la date et heure de fin, les actions (voir les matériels, pour voir les matériels utilisés pendant le séance d'entraînement, la possibilité de modifier un entraînement et le supprimer).

Le message d'erreur suivant s'affiche lorsque on clique sur Modifier. On ne peut pas modifier les matériels, on peut uniquement les voir en cliquant sur "Voir Matériels"


4. Gestion des matchs

Tableau gestion des matchs avec le numéro, l'équipe, l'équipe adverse, la date et l'heure du match, le lieu du match, si le match a été joué à domicile ou à l'extérieur, les buts encaissés, les buts marqués. Ainsi qu'un bouton d'action pour modifier les données saisies et un bouton supprimer pour supprimer le match de la base de donnée. Le bouton "Ajouter un match" permet d'ouvrir un formulaire afin d'ajouter un match. 






## Tech Stack

**Client:** HTML, CSS 

**Server:** Java SpringBoot, Oracle (Base de données)

Connexion à la base de données en utilisant "oracle.jdbc.OracleDriver"



