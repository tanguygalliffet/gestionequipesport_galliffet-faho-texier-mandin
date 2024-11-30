/*
Nom, Prénom Participant 1:	GALLIFFET Tanguy		        , Insertions des lignes
Nom, Prénom Participant 2:	MANDIN Brieuc    		        , Activités paricipant2
Nom, Prénom Participant 3:	MBATCHOU FAHO Arnold perez		, Activités paricipant3
Nom, Prénom Participant 4:	TEXIER Léo		                , Schéma physique
*/

/*

5.	Requêtes libres de Mise à jour et consultation des données
Les requêtes de mise à jour (modification, suppression) et de consulatation doivent être rendues dans un fichier ayant le nom suivant :
Maj_Consultation_De_Donnees_NomProjet_Nom1_Nom2_Nom3_Nom4.sql
Nom1 à NomN correspondent aux membres du groupe.

Les requêtes de mise à jour (modification, suppression) et de consulatation à écrire sont celles définies dans la section 2.
Connectez vous avec le compte Oracle choisi dans la section 3 (local ou distant).  
Exécutez ensuite le script suivant :
Maj_Consultation_De_Donnees_NomProjet_Nom1_Nom2_Nom3_Nom4.sql pour créer tous les objets qui s’y trouvent. . Les actions peuvent être mises en œuvre pas à pas.

*/
//1
-- Selectionner tout les joueurs de la base 
select * from JOUEURS;

//2
-- Selection de tout les joueurs ayant le poste de gardien
select * from JOUEURS where JOPOSTE='GARDIEN';

//3
-- Selection de tout les joueurs ayant le poste de defenseur
select * from JOUEURS where JOPOSTE='DEFENSEUR';

//4
-- Selection de tout les joueurs ayant le poste de milieu
select * from JOUEURS where JOPOSTE='MILIEU';

//5
-- Selection de tout les joueurs ayant le poste d'attaquant
select * from JOUEURS where JOPOSTE='ATTAQUANT';

//6
-- Selection de tout les joueurs ayant un age superieur a 20 ans
SELECT *FROM JOUEURS WHERE (TRUNC(MONTHS_BETWEEN(SYSDATE, JODATE_NAI) / 12)) > 20;
//7
-- Affichage de tous les entraineurs de la base
select * from ENTRAINEURS;

//8
-- Affichage de toutes les equipes de la base
select * from EQUIPES;

//9
-- Affichage de tous les matchs de la base entre 2 dates
select * from MATCHS where MATCHDATE > '01/10/2024' and MATCHDATE < '01/11/2024';

//10
-- Affichage de tous les matchs ou aucun but n'a été encaissé
select * from MATCHS where MATCHBUT_ENC = 0 AND MATCHBUT_MIS = 0;

//11
-- Affichage de tous les matchs ou plus de 3 buts ont été marqués par notre equipe
select * from MATCHS where MATCHBUT_MIS >= 3;

//12
-- Affichage de tous les paiements de la base
select * from PAIEMENTS;

//13
-- Affichage de tous les paiements fais par le nom du joueur ROUSSEAU JULIEN
select j.JONOM, j.JOPRENOM, p.PALIBELLE as "Libelle du paiement" , p.PAMONTANT as "Montant en €" 
from PAIEMENTS p, JOUEURS j 
where p.JONUMERO = j.JONUMERO and j.JONOM = 'MARTIN' and j.JOPRENOM = 'ARTHUR';

//select * from JOUEURS where JONUMERO = 4

//14
-- Affichage du matériel repertorié dans la base
select * from materiels;

//15
-- Affichage du material utilisé pour l'entrainement de la date de début est à 2024-10-05 18:00

SELECT 
    M.MATERIELNOM,
    EUD.NOMBREUTILISE
FROM 
    TRAININGS T
JOIN 
    EST_UTILISE_DANS EUD ON T.TRNUMERO = EUD.TRNUMERO
JOIN 
    MATERIELS M ON EUD.MATERIELNUMERO = M.MATERIELNUMERO
WHERE 
    T.TRDATE_DEB = TO_DATE('2024-10-05 18:00', 'YYYY-MM-DD HH24:MI');

//16
-- Affichage de tous les entrainements de la base
select * from TRAININGS;

//16
-- Affichage de tous les entrainements de prevu pour l'équipe BIARRITZ U17 1
select * 
from EQUIPES e, TRAININGS t  
where e.EQNUMERO = t.TRNUMERO and e.EQNOM = 'BIARRITZ U17 1';

//17
-- Affichage de tous les joueurs à partir d'un nom d'equipe donnée
SELECT 
    J.JONOM, 
    J.JOPRENOM
FROM 
    JOUEURS J
JOIN 
    CONSTITUE C ON J.JONUMERO = C.JONUMERO
JOIN 
    EQUIPES E ON C.EQNUMERO = E.EQNUMERO
WHERE 
    E.EQNOM = UPPER('BIARRITZ U17 1');


-- Affichage de tous les joueurs etant defenseur dans une equipe


SELECT * FROM USER_OBJECTS WHERE OBJECT_TYPE = 'PACKAGE' AND OBJECT_NAME = 'GESTIONFOOTBALL';
