/*
Nom, Prénom Participant 1:	GALLIFFET Tanguy		        , Insertions des lignes
Nom, Prénom Participant 2:	MANDIN Brieuc    		        , Activités paricipant2
Nom, Prénom Participant 3:	MBATCHOU FAHO Arnold perez		, Activités paricipant3
Nom, Prénom Participant 4:	TEXIER Léo		                , Schéma physique

*/

/*

6.	Définition et implémentation des packages PLSQL 

Les programmes et les tests de chaque méthodes doivent être rendues dans un fichier ayant le nom suivant :
Package_plsql_NomProjet_Nom1_Nom2_Nom3_Nom4.sql
Nom1 à NomN correspondent aux membres du groupe.
Il s’agit définir les packages spécifications et d’implémenter le code des packages bodies introduits au chapitre 2. 

Vous devez aussi proposer le code de test des méthodes de chacun de ces packages.

Vous devez dans ce même fichier programmer les spécifications des deux triggers décrits dans le chapitre 2.

Connectez vous avec le compte Oracle choisi dans la section 3 (local ou distant).  
Exécutez ensuite le script suivant :
Package_plsql_NomProjet_Nom1_Nom2_Nom3_Nom4.sql pour créer tous les objets qui s’y trouvent. Les actions peuvent être mises en œuvre pas à pas.

*/





-- Création de séquences
CREATE SEQUENCE JOUEURS_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
/


CREATE SEQUENCE ENTRAINEURS_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
/

CREATE SEQUENCE TRAININGS_SEQ 
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
/



CREATE SEQUENCE EQUIPES_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
    
/


CREATE SEQUENCE MATCHS_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;
    
/



CREATE OR REPLACE PACKAGE GestionFootball AS

    -- 1. Sélectionner tous les joueurs
    PROCEDURE GetTousLesJoueurs(
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 2. Sélection des joueurs selon le poste
    PROCEDURE GetJoueursParPoste(
        JOPOSTE_IN IN VARCHAR2, 
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 3. Sélection des joueurs ayant un âge supérieur à 20 ans
    PROCEDURE GetJoueursPlusDe20Ans(
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 4. Affichage de tous les entraîneurs
    PROCEDURE GetTousLesEntraineurs(
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 5. Sélection des entraîneurs par nom
    PROCEDURE GetEntraineursParNom(
        EN_NOM_IN IN VARCHAR2, 
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 6. Affichage de toutes les équipes
    PROCEDURE GetToutesLesEquipes(
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 7. Affichage des matchs entre deux dates
    PROCEDURE GetMatchsEntreDates(
        P_DATE_DEB IN DATE, 
        P_DATE_FIN IN DATE, 
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 8. Affichage des matchs sans buts
    PROCEDURE GetMatchsSansButs(
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 9. Affichage des matchs avec plus de 3 buts
    PROCEDURE GetMatchsPlusDe3Buts(
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 10. Affichage de tous les paiements
    PROCEDURE GetTousLesPaiements(
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 11. Paiements par nom de joueur
    PROCEDURE GetPaiementsParJoueur(
        P_NOM IN VARCHAR2, 
        P_PRENOM IN VARCHAR2, 
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 12. Affichage du matériel
    PROCEDURE GetMateriel(
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 13. Affichage du matériel utilisé pour un entraînement spécifique
    PROCEDURE GetMaterielUtilise(
        P_TRDATE_DEB IN DATE, 
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 14. Affichage de tous les entraînements
    PROCEDURE GetTousLesTrainings(
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 15. Affichage des entraînements pour une équipe spécifique
    PROCEDURE GetTrainingsParEquipe(
        P_EQNOM IN VARCHAR2, 
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 16. Affichage des joueurs d'une équipe spécifique
    PROCEDURE GetJoueursParEquipe(
        P_EQNOM IN VARCHAR2, 
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 17. Modification d'un joueur
    PROCEDURE UpdateJoueur(
        P_JO_NUMERO     IN NUMBER,
        P_JO_NOM        IN VARCHAR2,
        P_JO_PRENOM     IN VARCHAR2,
        P_JO_DATE_NAI   IN DATE,
        P_JO_EMAIL      IN VARCHAR2,
        P_JO_POSTE      IN VARCHAR2,
        P_JO_TELEPHONE  IN VARCHAR2,
        OUT_CURSOR      OUT SYS_REFCURSOR
    );

    -- 18. Suppression d'un joueur
    PROCEDURE DeleteJoueur(
        P_JO_NUMERO IN NUMBER,
        OUT_CURSOR  OUT SYS_REFCURSOR
    );

    -- 19. Ajout d'un joueur
    PROCEDURE AddJoueur(
        P_JO_NOM       IN VARCHAR2,
        P_JO_PRENOM    IN VARCHAR2,
        P_JO_DATE_NAI  IN DATE,
        P_JO_EMAIL     IN VARCHAR2,
        P_JO_POSTE     IN VARCHAR2,
        P_JO_TELEPHONE IN VARCHAR2,
        OUT_CURSOR     OUT SYS_REFCURSOR
    );

    -- 20. Ajout d'un entraîneur
    PROCEDURE AddEntraineur(
        EN_NOM        IN VARCHAR2,
        EN_PRENOM     IN VARCHAR2,
        EN_DATE_NAI   IN DATE,
        EN_EMAIL      IN VARCHAR2,
        EN_TELEPHONE  IN VARCHAR2,
        EQNUMERO      IN NUMBER, -- Ajout du paramètre EQNUMERO
        OUT_CURSOR    OUT SYS_REFCURSOR
    );

    -- 21. Modification d'un entraîneur
    PROCEDURE UpdateEntraineur(
        EN_NUMERO     IN NUMBER,
        EN_NOM        IN VARCHAR2,
        EN_PRENOM     IN VARCHAR2,
        EN_DATE_NAI   IN DATE,
        EN_EMAIL      IN VARCHAR2,
        EN_TELEPHONE  IN VARCHAR2,
        EQNUMERO      IN NUMBER, -- Ajout du paramètre EQNUMERO
        OUT_CURSOR    OUT SYS_REFCURSOR
    );

    -- 22. Suppression d'un entraîneur
    PROCEDURE DeleteEntraineur(
        EN_NUMERO IN NUMBER,
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 23. Ajout d'un entraînement
    PROCEDURE AddTraining(
        EQNUMERO    IN NUMBER,
        TRDATE_DEB  IN DATE,
        TRDATE_FIN  IN DATE,
        OUT_CURSOR  OUT SYS_REFCURSOR
    );

    -- 24. Modification d'un entraînement
    PROCEDURE UpdateTraining(
        p_TRNUMERO    IN NUMBER,
        p_EQNUMERO    IN NUMBER,
        p_TRDATE_DEB  IN DATE,
        p_TRDATE_FIN  IN DATE,
        OUT_CURSOR  OUT SYS_REFCURSOR
    );

    -- 25. Suppression d'un entraînement
    PROCEDURE DeleteTraining(
        p_TRNUMERO IN NUMBER
    );

    -- 26. Affichage des matériels utilisés pour un entraînement spécifique
    PROCEDURE GetMaterielsParTraining(
        p_TRNUMERO IN NUMBER,
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- 27. Ajout d'un matériel dans un entraînement
    PROCEDURE AddMaterielDansTraining(
        p_TRNUMERO       IN NUMBER,
        p_MATERIELNUMERO IN NUMBER,
        p_NOMBREUTILISE  IN NUMBER,
        OUT_CURSOR     OUT SYS_REFCURSOR
    );

    -- 28. Mise à jour d'un matériel dans un entraînement
    PROCEDURE UpdateMaterielDansTraining(
        p_TRNUMERO       IN NUMBER,
        p_MATERIELNUMERO IN NUMBER,
        p_NOMBREUTILISE  IN NUMBER,
        OUT_CURSOR     OUT SYS_REFCURSOR
    );

    -- 29. Suppression d'un matériel dans un entraînement
    PROCEDURE DeleteMaterielDansTraining(
        p_TRNUMERO       IN NUMBER,
        p_MATERIELNUMERO IN NUMBER
    );
    
    PROCEDURE GetTrainingById(
        P_TRNUMERO IN NUMBER,
        OUT_CURSOR OUT SYS_REFCURSOR
    );
    
    
    -- Procédure pour récupérer tous les matchs
    PROCEDURE GetTousLesMatchs(
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- Procédure pour récupérer un match par son numéro
    PROCEDURE GetMatchById(
        P_MATCHNUMERO IN NUMBER,
        OUT_CURSOR OUT SYS_REFCURSOR
    );

    -- Procédure pour ajouter un nouveau match
    PROCEDURE AddMatch(
        P_EQNUMERO IN NUMBER,
        P_MATCH_EQADV IN VARCHAR2,
        P_MATCHDATE IN DATE,
        P_MATCHLIEU IN VARCHAR2,
        P_MATCHEXTERIEUR IN NUMBER,
        P_MATCHBUT_ENC IN NUMBER,
        P_MATCHBUT_MIS IN NUMBER,
        OUT_MATCHNUMERO OUT NUMBER
    );

    -- Procédure pour mettre à jour un match existant
    PROCEDURE UpdateMatch(
        P_MATCHNUMERO IN NUMBER,
        P_EQNUMERO IN NUMBER,
        P_MATCH_EQADV IN VARCHAR2,
        P_MATCHDATE IN DATE,
        P_MATCHLIEU IN VARCHAR2,
        P_MATCHEXTERIEUR IN NUMBER,
        P_MATCHBUT_ENC IN NUMBER,
        P_MATCHBUT_MIS IN NUMBER
    );

    -- Procédure pour supprimer un match
    PROCEDURE DeleteMatch(
        P_MATCHNUMERO IN NUMBER
    );

    

END GestionFootball;
/


CREATE OR REPLACE PACKAGE BODY GestionFootball AS

    -- 1. Sélectionner tous les joueurs
    PROCEDURE GetTousLesJoueurs(
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR 
            SELECT * FROM JOUEURS;
    END GetTousLesJoueurs;

    -- 2. Sélection des joueurs selon le poste
    PROCEDURE GetJoueursParPoste(
        JOPOSTE_IN IN VARCHAR2, 
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR 
            SELECT * FROM JOUEURS 
            WHERE JOPOSTE = JOPOSTE_IN;
    END GetJoueursParPoste;

    -- 3. Sélection des joueurs ayant un âge supérieur à 20 ans
    PROCEDURE GetJoueursPlusDe20Ans(
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR 
            SELECT * FROM JOUEURS 
            WHERE TRUNC(MONTHS_BETWEEN(SYSDATE, JODATE_NAI) / 12) > 20;
    END GetJoueursPlusDe20Ans;

    -- 4. Affichage de tous les entraîneurs
    PROCEDURE GetTousLesEntraineurs(
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR 
            SELECT * FROM ENTRAINEURS;
    END GetTousLesEntraineurs;

    -- 5. Sélection des entraîneurs par nom
    PROCEDURE GetEntraineursParNom(
        EN_NOM_IN IN VARCHAR2, 
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR 
            SELECT * FROM ENTRAINEURS 
            WHERE ENNOM LIKE '%' || EN_NOM_IN || '%';
    END GetEntraineursParNom;

    -- 6. Affichage de toutes les équipes
    PROCEDURE GetToutesLesEquipes(
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR 
            SELECT * FROM EQUIPES;
    END GetToutesLesEquipes;

    -- 7. Affichage des matchs entre deux dates
    PROCEDURE GetMatchsEntreDates(
        P_DATE_DEB IN DATE, 
        P_DATE_FIN IN DATE, 
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR 
            SELECT * FROM MATCHS 
            WHERE MATCHDATE BETWEEN P_DATE_DEB AND P_DATE_FIN;
    END GetMatchsEntreDates;

    -- 8. Affichage des matchs sans buts
    PROCEDURE GetMatchsSansButs(
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR 
            SELECT * FROM MATCHS 
            WHERE MATCHBUT_ENC = 0 AND MATCHBUT_MIS = 0;
    END GetMatchsSansButs;

    -- 9. Affichage des matchs avec plus de 3 buts
    PROCEDURE GetMatchsPlusDe3Buts(
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR 
            SELECT * FROM MATCHS 
            WHERE MATCHBUT_MIS > 3;
    END GetMatchsPlusDe3Buts;

    -- 10. Affichage de tous les paiements
    PROCEDURE GetTousLesPaiements(
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR 
            SELECT * FROM PAIEMENTS;
    END GetTousLesPaiements;

    -- 11. Paiements par nom de joueur
    PROCEDURE GetPaiementsParJoueur(
        P_NOM IN VARCHAR2, 
        P_PRENOM IN VARCHAR2, 
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR 
            SELECT 
                j.JONOM AS joueur_nom, 
                j.JOPRENOM AS joueur_prenom, 
                p.PALIBELLE AS paiement_libelle, 
                p.PAMONTANT AS paiement_montant
            FROM 
                PAIEMENTS p
            JOIN 
                JOUEURS j ON p.JONUMERO = j.JONUMERO
            WHERE 
                j.JONOM = P_NOM 
                AND j.JOPRENOM = P_PRENOM;
    END GetPaiementsParJoueur;

    -- 12. Affichage du matériel
    PROCEDURE GetMateriel(
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR 
            SELECT * FROM MATERIELS;
    END GetMateriel;

    -- 13. Affichage du matériel utilisé pour un entraînement spécifique
    PROCEDURE GetMaterielUtilise(
        P_TRDATE_DEB IN DATE, 
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR 
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
                T.TRDATE_DEB = P_TRDATE_DEB;
    END GetMaterielUtilise;

    -- 14. Affichage de tous les entraînements
    PROCEDURE GetTousLesTrainings(
    OUT_CURSOR OUT SYS_REFCURSOR
) IS
BEGIN
    OPEN OUT_CURSOR FOR
        SELECT
            T.TRNUMERO,
            T.EQNUMERO,
            T.TRDATE_DEB,
            T.TRDATE_FIN,
            M.MATERIELNUMERO AS MATERIELNUMERO,
            M.MATERIELNOM AS MATERIELNOM,
            EUD.NOMBREUTILISE AS NOMBREUTILISE
        FROM
            TRAININGS T
        LEFT JOIN
            EST_UTILISE_DANS EUD ON T.TRNUMERO = EUD.TRNUMERO
        LEFT JOIN
            MATERIELS M ON EUD.MATERIELNUMERO = M.MATERIELNUMERO
        ORDER BY T.TRNUMERO;
END GetTousLesTrainings;

    -- 15. Affichage des entraînements pour une équipe spécifique
    PROCEDURE GetTrainingsParEquipe(
        P_EQNOM IN VARCHAR2, 
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR 
            SELECT 
                t.TRNUMERO AS training_numero, 
                e.EQNOM AS equipe_nom,
                t.TRDATE_DEB,
                t.TRDATE_FIN
            FROM 
                EQUIPES e
            JOIN 
                TRAININGS t ON e.EQNUMERO = t.EQNUMERO
            WHERE 
                e.EQNOM = P_EQNOM;
    END GetTrainingsParEquipe;

    -- 16. Affichage des joueurs d'une équipe spécifique
    PROCEDURE GetJoueursParEquipe(
        P_EQNOM IN VARCHAR2, 
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR 
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
                E.EQNOM = P_EQNOM;
    END GetJoueursParEquipe;

    -- 17. Modification d'un joueur
    PROCEDURE UpdateJoueur(
        P_JO_NUMERO     IN NUMBER,
        P_JO_NOM        IN VARCHAR2,
        P_JO_PRENOM     IN VARCHAR2,
        P_JO_DATE_NAI   IN DATE,
        P_JO_EMAIL      IN VARCHAR2,
        P_JO_POSTE      IN VARCHAR2,
        P_JO_TELEPHONE  IN VARCHAR2,
        OUT_CURSOR      OUT SYS_REFCURSOR
    ) IS
    BEGIN
        UPDATE JOUEURS
        SET 
            JONOM        = P_JO_NOM,
            JOPRENOM     = P_JO_PRENOM,
            JODATE_NAI   = P_JO_DATE_NAI,
            JOEMAIL      = P_JO_EMAIL,
            JOPOSTE      = P_JO_POSTE,
            JOTELEPHONE  = P_JO_TELEPHONE
        WHERE 
            JONUMERO = P_JO_NUMERO;

        OPEN OUT_CURSOR FOR 
            SELECT * FROM JOUEURS WHERE JONUMERO = P_JO_NUMERO;
    END UpdateJoueur;

    -- 18. Suppression d'un joueur
    PROCEDURE DeleteJoueur(
        P_JO_NUMERO IN NUMBER,
        OUT_CURSOR  OUT SYS_REFCURSOR
    ) IS
    BEGIN
        DELETE FROM JOUEURS 
        WHERE JONUMERO = P_JO_NUMERO;

        -- Optionnel, retourner les joueurs restants ou une confirmation
        OPEN OUT_CURSOR FOR 
            SELECT * FROM JOUEURS;
    END DeleteJoueur;
    
    -- 19. Ajout d'un joueur
    PROCEDURE AddJoueur(
        P_JO_NOM       IN VARCHAR2,
        P_JO_PRENOM    IN VARCHAR2,
        P_JO_DATE_NAI  IN DATE,
        P_JO_EMAIL     IN VARCHAR2,
        P_JO_POSTE     IN VARCHAR2,
        P_JO_TELEPHONE IN VARCHAR2,
        OUT_CURSOR     OUT SYS_REFCURSOR
    ) IS
        v_jonumero JOUEURS.JONUMERO%TYPE;
    BEGIN
        INSERT INTO JOUEURS (
            JONUMERO, 
            JONOM, 
            JOPRENOM, 
            JODATE_NAI, 
            JOEMAIL, 
            JOPOSTE, 
            JOTELEPHONE
        )
        VALUES (
            JOUEURS_SEQ.NEXTVAL, 
            P_JO_NOM, 
            P_JO_PRENOM, 
            P_JO_DATE_NAI, 
            P_JO_EMAIL, 
            P_JO_POSTE, 
            P_JO_TELEPHONE
        )
        RETURNING JONUMERO INTO v_jonumero;
        
        OPEN OUT_CURSOR FOR 
            SELECT * FROM JOUEURS WHERE JONUMERO = v_jonumero;
    END AddJoueur;

    -- 20. Ajout d'un entraîneur
    PROCEDURE AddEntraineur(
        EN_NOM        IN VARCHAR2,
        EN_PRENOM     IN VARCHAR2,
        EN_DATE_NAI   IN DATE,
        EN_EMAIL      IN VARCHAR2,
        EN_TELEPHONE  IN VARCHAR2,
        EQNUMERO      IN NUMBER, -- Ajout du paramètre EQNUMERO
        OUT_CURSOR    OUT SYS_REFCURSOR
    ) IS
        v_en_numero ENTRAINEURS.ENNUMERO%TYPE;
    BEGIN
        INSERT INTO ENTRAINEURS (
            ENNUMERO, 
            EQNUMERO, 
            ENNOM, 
            ENPRENOM, 
            ENDATE_NAI, 
            ENEMAIL, 
            ENTELEPHONE
        )
        VALUES (
            ENTRAINEURS_SEQ.NEXTVAL,  -- Génère un nouvel ENNUMERO
            EQNUMERO,                  -- Utilise le EQNUMERO fourni
            EN_NOM, 
            EN_PRENOM, 
            EN_DATE_NAI, 
            EN_EMAIL, 
            EN_TELEPHONE
        )
        RETURNING ENNUMERO INTO v_en_numero;
        
        OPEN OUT_CURSOR FOR 
            SELECT * FROM ENTRAINEURS WHERE ENNUMERO = v_en_numero;
    END AddEntraineur;

    -- 21. Modification d'un entraîneur
    PROCEDURE UpdateEntraineur(
        EN_NUMERO     IN NUMBER,
        EN_NOM        IN VARCHAR2,
        EN_PRENOM     IN VARCHAR2,
        EN_DATE_NAI   IN DATE,
        EN_EMAIL      IN VARCHAR2,
        EN_TELEPHONE  IN VARCHAR2,
        EQNUMERO      IN NUMBER, -- Ajout du paramètre EQNUMERO
        OUT_CURSOR    OUT SYS_REFCURSOR
    ) IS
    BEGIN
        UPDATE ENTRAINEURS
        SET 
            ENNOM        = EN_NOM,
            ENPRENOM     = EN_PRENOM,
            ENDATE_NAI   = EN_DATE_NAI,
            ENEMAIL      = EN_EMAIL,
            ENTELEPHONE  = EN_TELEPHONE,
            EQNUMERO     = EQNUMERO -- Mise à jour de EQNUMERO
        WHERE 
            ENNUMERO = EN_NUMERO;

        OPEN OUT_CURSOR FOR 
            SELECT * FROM ENTRAINEURS WHERE ENNUMERO = EN_NUMERO;
    END UpdateEntraineur;

    -- 22. Suppression d'un entraîneur
    PROCEDURE DeleteEntraineur(
        EN_NUMERO IN NUMBER,
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        DELETE FROM ENTRAINEURS 
        WHERE ENNUMERO = EN_NUMERO;

        -- Optionnel, retourner les entraîneurs restants ou une confirmation
        OPEN OUT_CURSOR FOR 
            SELECT * FROM ENTRAINEURS;
    END DeleteEntraineur;

    -- 23. Ajout d'un entraînement
    PROCEDURE AddTraining(
        EQNUMERO    IN NUMBER,
        TRDATE_DEB  IN DATE,
        TRDATE_FIN  IN DATE,
        OUT_CURSOR  OUT SYS_REFCURSOR
    ) IS
        v_trnumero TRAININGS.TRNUMERO%TYPE;
    BEGIN
        INSERT INTO TRAININGS (
            TRNUMERO,
            EQNUMERO,
            TRDATE_DEB,
            TRDATE_FIN
        ) VALUES (
            TRAININGS_SEQ.NEXTVAL,
            EQNUMERO,
            TRDATE_DEB,
            TRDATE_FIN
        )
        RETURNING TRNUMERO INTO v_trnumero;

        OPEN OUT_CURSOR FOR
            SELECT * FROM TRAININGS WHERE TRNUMERO = v_trnumero;
    END AddTraining;

    -- 24. Modification d'un entraînement
    PROCEDURE UpdateTraining(
        p_TRNUMERO    IN NUMBER,
        p_EQNUMERO    IN NUMBER,
        p_TRDATE_DEB  IN DATE,
        p_TRDATE_FIN  IN DATE,
        OUT_CURSOR  OUT SYS_REFCURSOR
    ) IS
    BEGIN
        UPDATE TRAININGS
        SET 
            EQNUMERO    = p_EQNUMERO,
            TRDATE_DEB  = p_TRDATE_DEB,
            TRDATE_FIN  = p_TRDATE_FIN
        WHERE 
            TRNUMERO = p_TRNUMERO;

        OPEN OUT_CURSOR FOR
            SELECT * FROM TRAININGS WHERE TRNUMERO = p_TRNUMERO;
    END UpdateTraining;

    -- 25. Suppression d'un entraînement
    PROCEDURE DeleteTraining(
        p_TRNUMERO IN NUMBER
    ) IS
    BEGIN
        DELETE FROM TRAININGS
        WHERE TRNUMERO = p_TRNUMERO;
    END DeleteTraining;

    -- 26. Affichage des matériels utilisés pour un entraînement spécifique
    PROCEDURE GetMaterielsParTraining(
    p_TRNUMERO IN NUMBER,
    OUT_CURSOR OUT SYS_REFCURSOR
) IS
BEGIN
    OPEN OUT_CURSOR FOR
        SELECT 
            EUD.TRNUMERO, -- Ajoutez ceci si vous voulez inclure TRNUMERO dans les résultats
            M.MATERIELNUMERO,
            M.MATERIELNOM,
            EUD.NOMBREUTILISE
        FROM 
            EST_UTILISE_DANS EUD
        JOIN 
            MATERIELS M ON EUD.MATERIELNUMERO = M.MATERIELNUMERO
        WHERE 
            EUD.TRNUMERO = p_TRNUMERO;
END GetMaterielsParTraining;


    -- 27. Ajout d'un matériel dans un entraînement
    PROCEDURE AddMaterielDansTraining(
        p_TRNUMERO       IN NUMBER,
        p_MATERIELNUMERO IN NUMBER,
        p_NOMBREUTILISE  IN NUMBER,
        OUT_CURSOR     OUT SYS_REFCURSOR
    ) IS
    BEGIN
        INSERT INTO EST_UTILISE_DANS (
            TRNUMERO,
            MATERIELNUMERO,
            NOMBREUTILISE
        ) VALUES (
            p_TRNUMERO,
            p_MATERIELNUMERO,
            p_NOMBREUTILISE
        );

        OPEN OUT_CURSOR FOR
            SELECT 
                M.MATERIELNUMERO,
                M.MATERIELNOM,
                EUD.NOMBREUTILISE
            FROM 
                EST_UTILISE_DANS EUD
            JOIN 
                MATERIELS M ON EUD.MATERIELNUMERO = M.MATERIELNUMERO
            WHERE 
                EUD.TRNUMERO = p_TRNUMERO AND EUD.MATERIELNUMERO = p_MATERIELNUMERO;
    END AddMaterielDansTraining;

    -- 28. Mise à jour d'un matériel dans un entraînement
    PROCEDURE UpdateMaterielDansTraining(
        p_TRNUMERO       IN NUMBER,
        p_MATERIELNUMERO IN NUMBER,
        p_NOMBREUTILISE  IN NUMBER,
        OUT_CURSOR     OUT SYS_REFCURSOR
    ) IS
    BEGIN
        UPDATE EST_UTILISE_DANS
        SET 
            NOMBREUTILISE = p_NOMBREUTILISE
        WHERE 
            TRNUMERO = p_TRNUMERO AND MATERIELNUMERO = p_MATERIELNUMERO;

        OPEN OUT_CURSOR FOR
            SELECT 
                M.MATERIELNUMERO,
                M.MATERIELNOM,
                EUD.NOMBREUTILISE
            FROM 
                EST_UTILISE_DANS EUD
            JOIN 
                MATERIELS M ON EUD.MATERIELNUMERO = M.MATERIELNUMERO
            WHERE 
                EUD.TRNUMERO = p_TRNUMERO AND EUD.MATERIELNUMERO = p_MATERIELNUMERO;
    END UpdateMaterielDansTraining;

    -- 29. Suppression d'un matériel dans un entraînement
    PROCEDURE DeleteMaterielDansTraining(
        p_TRNUMERO       IN NUMBER,
        p_MATERIELNUMERO IN NUMBER
    ) IS
    BEGIN
        DELETE FROM EST_UTILISE_DANS
        WHERE TRNUMERO = p_TRNUMERO AND MATERIELNUMERO = p_MATERIELNUMERO;
    END DeleteMaterielDansTraining;
    
    --30
    PROCEDURE GetTrainingById(
    P_TRNUMERO IN NUMBER,
    OUT_CURSOR OUT SYS_REFCURSOR
) IS
BEGIN
    OPEN OUT_CURSOR FOR
        SELECT
            T.TRNUMERO,
            T.EQNUMERO,
            T.TRDATE_DEB,
            T.TRDATE_FIN,
            M.MATERIELNUMERO,
            M.MATERIELNOM,
            EUD.NOMBREUTILISE
        FROM
            TRAININGS T
        LEFT JOIN
            EST_UTILISE_DANS EUD ON T.TRNUMERO = EUD.TRNUMERO
        LEFT JOIN
            MATERIELS M ON EUD.MATERIELNUMERO = M.MATERIELNUMERO
        WHERE
            T.TRNUMERO = P_TRNUMERO;
END GetTrainingById;

-- Procédure pour récupérer tous les matchs
    PROCEDURE GetTousLesMatchs(
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR
            SELECT
                M.MATCHNUMERO,
                M.EQNUMERO,
                M.MATCH_EQADV,
                M.MATCHDATE,
                M.MATCHLIEU,
                M.MATCHEXTERIEUR,
                M.MATCHBUT_ENC,
                M.MATCHBUT_MIS,
                E.EQNOM
            FROM MATCHS M
            LEFT JOIN EQUIPES E ON M.EQNUMERO = E.EQNUMERO
            ORDER BY M.MATCHNUMERO;
    END GetTousLesMatchs;

    -- Procédure pour récupérer un match par son numéro
    PROCEDURE GetMatchById(
        P_MATCHNUMERO IN NUMBER,
        OUT_CURSOR OUT SYS_REFCURSOR
    ) IS
    BEGIN
        OPEN OUT_CURSOR FOR
            SELECT
                M.MATCHNUMERO,
                M.EQNUMERO,
                M.MATCH_EQADV,
                M.MATCHDATE,
                M.MATCHLIEU,
                M.MATCHEXTERIEUR,
                M.MATCHBUT_ENC,
                M.MATCHBUT_MIS,
                E.EQNOM
            FROM MATCHS M
            LEFT JOIN EQUIPES E ON M.EQNUMERO = E.EQNUMERO
            WHERE M.MATCHNUMERO = P_MATCHNUMERO;
    END GetMatchById;

    -- Procédure pour ajouter un nouveau match
    PROCEDURE AddMatch(
        P_EQNUMERO IN NUMBER,
        P_MATCH_EQADV IN VARCHAR2,
        P_MATCHDATE IN DATE,
        P_MATCHLIEU IN VARCHAR2,
        P_MATCHEXTERIEUR IN NUMBER,
        P_MATCHBUT_ENC IN NUMBER,
        P_MATCHBUT_MIS IN NUMBER,
        OUT_MATCHNUMERO OUT NUMBER
    ) IS
    BEGIN
        SELECT MATCHS_SEQ.NEXTVAL INTO OUT_MATCHNUMERO FROM DUAL;
        INSERT INTO MATCHS (
            MATCHNUMERO,
            EQNUMERO,
            MATCH_EQADV,
            MATCHDATE,
            MATCHLIEU,
            MATCHEXTERIEUR,
            MATCHBUT_ENC,
            MATCHBUT_MIS
        ) VALUES (
            OUT_MATCHNUMERO,
            P_EQNUMERO,
            P_MATCH_EQADV,
            P_MATCHDATE,
            P_MATCHLIEU,
            P_MATCHEXTERIEUR,
            P_MATCHBUT_ENC,
            P_MATCHBUT_MIS
        );
    END AddMatch;

    -- Procédure pour mettre à jour un match existant
    PROCEDURE UpdateMatch(
        P_MATCHNUMERO IN NUMBER,
        P_EQNUMERO IN NUMBER,
        P_MATCH_EQADV IN VARCHAR2,
        P_MATCHDATE IN DATE,
        P_MATCHLIEU IN VARCHAR2,
        P_MATCHEXTERIEUR IN NUMBER,
        P_MATCHBUT_ENC IN NUMBER,
        P_MATCHBUT_MIS IN NUMBER
    ) IS
    BEGIN
        UPDATE MATCHS SET
            EQNUMERO = P_EQNUMERO,
            MATCH_EQADV = P_MATCH_EQADV,
            MATCHDATE = P_MATCHDATE,
            MATCHLIEU = P_MATCHLIEU,
            MATCHEXTERIEUR = P_MATCHEXTERIEUR,
            MATCHBUT_ENC = P_MATCHBUT_ENC,
            MATCHBUT_MIS = P_MATCHBUT_MIS
        WHERE MATCHNUMERO = P_MATCHNUMERO;
    END UpdateMatch;

    -- Procédure pour supprimer un match
    PROCEDURE DeleteMatch(
        P_MATCHNUMERO IN NUMBER
    ) IS
    BEGIN
        DELETE FROM MATCHS WHERE MATCHNUMERO = P_MATCHNUMERO;
    END DeleteMatch;

    

END GestionFootball;
/


-- Création des triggers


ALTER TABLE JOUEURS ADD (last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP);

CREATE OR REPLACE TRIGGER trg_before_update_joueur
BEFORE UPDATE ON JOUEURS
FOR EACH ROW
BEGIN
    :NEW.last_updated := CURRENT_TIMESTAMP;
END;
/



CREATE OR REPLACE TRIGGER trg_before_insert_entraineur
BEFORE INSERT ON ENTRAINEURS
FOR EACH ROW
DECLARE
    v_exists NUMBER;
BEGIN

    SELECT COUNT(*)
    INTO v_exists
    FROM EQUIPES
    WHERE EQNUMERO = :NEW.EQNUMERO;

    IF v_exists = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'L''équipe spécifiée n''existe pas.');
    END IF;
END;
/


