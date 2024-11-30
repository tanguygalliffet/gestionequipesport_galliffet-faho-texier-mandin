/*
Nom, Prénom Participant 1:	GALLIFFET Tanguy		        , Insertions des lignes
Nom, Prénom Participant 2:	MANDIN Brieuc    		        , Activités paricipant2
Nom, Prénom Participant 3:	MBATCHOU FAHO Arnold perez		, Activités paricipant3
Nom, Prénom Participant 4:	TEXIER Léo		                , Schéma physique

*/

/*

3.	Génération du schéma physique de données
Le schéma doit  être généré dans un fichier de script ayant le nom suivant :
Schema_physique_NomProjet_Nom1_Nom2_Nom3_Nom4.sql
Nom1 à NomN correspondent aux membres du groupe.
Définir le schéma physique consiste à produire les ordres SQL de création des tables, indexes etc.. Vous pouvez générer ces tables en convertissant le schéma logique vers des ordres SQL  avec POWERAMC ou manuellement. Toutefois, il faut privilégier POWERAMC.
Connectez vous avec le compte Oracle choisi (local ou distant).  
Exécutez ensuite le script suivant :
Schema_physique_NomProjet_Nom1_Nom2_Nom3_Nom4.sql pour créer tous les objets qui s’y trouvent. . Les actions peuvent être mises en œuvre pas à pas.

*/


/*==============================================================*/
/* Nom de SGBD :  ORACLE Version 10gR2                          */
/* Date de création :  15/10/2024 16:49:04                      */
/*==============================================================*/


alter table CONSTITUE
   drop constraint FK_CONSTITU_CONSTITUE_JOUEURS;

alter table CONSTITUE
   drop constraint FK_CONSTITU_CONSTITUE_EQUIPES;

alter table ENTRAINEURS
   drop constraint FK_ENTRAINE_GERE_EQUIPES;

alter table EST_UTILISE_DANS
   drop constraint FK_EST_UTIL_EST_UTILI_TRAINING;

alter table EST_UTILISE_DANS
   drop constraint FK_EST_UTIL_EST_UTILI_MATERIEL;

alter table MATCHS
   drop constraint FK_MATCHS_DISPUTE_EQUIPES;

alter table PAIEMENTS
   drop constraint FK_PAIEMENT_PAYE_JOUEURS;

alter table TRAININGS
   drop constraint FK_TRAINING_ASSISTE_EQUIPES;

drop index CONSTITUE2_FK;

drop index CONSTITUE_FK;

drop table CONSTITUE cascade constraints;

drop index GERE_FK;

drop table ENTRAINEURS cascade constraints;

drop table EQUIPES cascade constraints;

drop index EST_UTILISE_DANS2_FK;

drop index EST_UTILISE_DANS_FK;

drop table EST_UTILISE_DANS cascade constraints;

drop table JOUEURS cascade constraints;

drop index DISPUTE_FK;

drop table MATCHS cascade constraints;

drop table MATERIELS cascade constraints;

drop index PAYE_FK;

drop table PAIEMENTS cascade constraints;

drop index ASSISTE_FK;

drop table TRAININGS cascade constraints;

/*==============================================================*/
/* Table : CONSTITUE                                            */
/*==============================================================*/
create table CONSTITUE  (
   JONUMERO             NUMBER(9)                       not null
      constraint CKC_JONUMERO_CONSTITU check (JONUMERO >= 0),
   EQNUMERO             NUMBER(9)                       not null,
   constraint PK_CONSTITUE primary key (JONUMERO, EQNUMERO)
);

/*==============================================================*/
/* Index : CONSTITUE_FK                                         */
/*==============================================================*/
create index CONSTITUE_FK on CONSTITUE (
   JONUMERO ASC
);

/*==============================================================*/
/* Index : CONSTITUE2_FK                                        */
/*==============================================================*/
create index CONSTITUE2_FK on CONSTITUE (
   EQNUMERO ASC
);

/*==============================================================*/
/* Table : ENTRAINEURS                                          */
/*==============================================================*/
create table ENTRAINEURS  (
   ENNUMERO             NUMBER(9)                       not null,
   EQNUMERO             NUMBER(9)                       not null,
   ENNOM                VARCHAR2(30)                    not null
      constraint CKC_ENNOM_ENTRAINE check (ENNOM = upper(ENNOM)),
   ENPRENOM             VARCHAR2(30)                    not null
      constraint CKC_ENPRENOM_ENTRAINE check (ENPRENOM = upper(ENPRENOM)),
   ENDATE_NAI           DATE                            not null,
   ENEMAIL              VARCHAR2(100)                   not null
      constraint CKC_ENEMAIL_ENTRAINE check (ENEMAIL = upper(ENEMAIL)),
   ENTELEPHONE          VARCHAR2(30),
   constraint PK_ENTRAINEURS primary key (ENNUMERO)
);

/*==============================================================*/
/* Index : GERE_FK                                              */
/*==============================================================*/
create index GERE_FK on ENTRAINEURS (
   EQNUMERO ASC
);

/*==============================================================*/
/* Table : EQUIPES                                              */
/*==============================================================*/
create table EQUIPES  (
   EQNUMERO             NUMBER(9)                       not null,
   EQNOM                VARCHAR2(100)                   not null
      constraint CKC_EQNOM_EQUIPES check (EQNOM = upper(EQNOM)),
   EQNIVEAU             VARCHAR2(50)                    not null
      constraint CKC_EQNIVEAU_EQUIPES check (EQNIVEAU = upper(EQNIVEAU)),
   constraint PK_EQUIPES primary key (EQNUMERO)
);

/*==============================================================*/
/* Table : EST_UTILISE_DANS                                     */
/*==============================================================*/
create table EST_UTILISE_DANS  (
   TRNUMERO             NUMBER(9)                       not null,
   MATERIELNUMERO       NUMBER(9)                       not null,
   NOMBREUTILISE        NUMBER(9)                       not null,
   constraint PK_EST_UTILISE_DANS primary key (TRNUMERO, MATERIELNUMERO)
);

/*==============================================================*/
/* Index : EST_UTILISE_DANS_FK                                  */
/*==============================================================*/
create index EST_UTILISE_DANS_FK on EST_UTILISE_DANS (
   TRNUMERO ASC
);

/*==============================================================*/
/* Index : EST_UTILISE_DANS2_FK                                 */
/*==============================================================*/
create index EST_UTILISE_DANS2_FK on EST_UTILISE_DANS (
   MATERIELNUMERO ASC
);

/*==============================================================*/
/* Table : JOUEURS                                              */
/*==============================================================*/
create table JOUEURS  (
   JONUMERO             NUMBER(9)                       not null
      constraint CKC_JONUMERO_JOUEURS check (JONUMERO >= 0),
   JONOM                VARCHAR2(30)                    not null
      constraint CKC_JONOM_JOUEURS check (JONOM = upper(JONOM)),
   JOPRENOM             VARCHAR2(30)                    not null
      constraint CKC_JOPRENOM_JOUEURS check (JOPRENOM = upper(JOPRENOM)),
   JODATE_NAI           DATE                            not null,
   JOEMAIL              VARCHAR2(100)                   not null,
   JOPOSTE              VARCHAR2(30),
   JOTELEPHONE          VARCHAR2(30),
   constraint PK_JOUEURS primary key (JONUMERO)
);

/*==============================================================*/
/* Table : MATCHS                                               */
/*==============================================================*/
create table MATCHS  (
   MATCHNUMERO          NUMBER(9)                       not null,
   EQNUMERO             NUMBER(9)                       not null,
   MATCH_EQADV          VARCHAR2(100)                   not null
      constraint CKC_MATCH_EQADV_MATCHS check (MATCH_EQADV = upper(MATCH_EQADV)),
   MATCHDATE            DATE                            not null,
   MATCHLIEU            VARCHAR2(100)                   not null
      constraint CKC_MATCHLIEU_MATCHS check (MATCHLIEU = upper(MATCHLIEU)),
   MATCHEXTERIEUR       SMALLINT                       default 0 not null
      constraint CKC_MATCHEXTERIEUR_MATCHS check (MATCHEXTERIEUR between 0 and 1),
   MATCHBUT_ENC         NUMBER(2),
   MATCHBUT_MIS         NUMBER(2),
   constraint PK_MATCHS primary key (MATCHNUMERO)
);

/*==============================================================*/
/* Index : DISPUTE_FK                                           */
/*==============================================================*/
create index DISPUTE_FK on MATCHS (
   EQNUMERO ASC
);

/*==============================================================*/
/* Table : MATERIELS                                            */
/*==============================================================*/
create table MATERIELS  (
   MATERIELNUMERO       NUMBER(9)                       not null,
   MATERIELNOM          VARCHAR2(50)                    not null,
   MATERIELNOMBRE       NUMBER(9)                       not null,
   constraint PK_MATERIELS primary key (MATERIELNUMERO)
);

/*==============================================================*/
/* Table : PAIEMENTS                                            */
/*==============================================================*/
create table PAIEMENTS  (
   PANUMERO             NUMBER(9)                       not null,
   JONUMERO             NUMBER(9)                      
      constraint CKC_JONUMERO_PAIEMENT check (JONUMERO is null or (JONUMERO >= 0)),
   PALIBELLE            VARCHAR2(100)                   not null
      constraint CKC_PALIBELLE_PAIEMENT check (PALIBELLE = upper(PALIBELLE)),
   PADATE               DATE                            not null,
   PAMONTANT            NUMBER(9)                       not null
      constraint CKC_PAMONTANT_PAIEMENT check (PAMONTANT >= 1),
   PATYPE               SMALLINT                        not null,
   constraint PK_PAIEMENTS primary key (PANUMERO)
);

/*==============================================================*/
/* Index : PAYE_FK                                              */
/*==============================================================*/
create index PAYE_FK on PAIEMENTS (
   JONUMERO ASC
);

/*==============================================================*/
/* Table : TRAININGS                                            */
/*==============================================================*/
create table TRAININGS  (
   TRNUMERO             NUMBER(9)                       not null,
   EQNUMERO             NUMBER(9),
   TRDATE_DEB           DATE                            not null,
   TRDATE_FIN           DATE                            not null,
   constraint PK_TRAININGS primary key (TRNUMERO)
);

/*==============================================================*/
/* Index : ASSISTE_FK                                           */
/*==============================================================*/
create index ASSISTE_FK on TRAININGS (
   EQNUMERO ASC
);

alter table CONSTITUE
   add constraint FK_CONSTITU_CONSTITUE_JOUEURS foreign key (JONUMERO)
      references JOUEURS (JONUMERO);

alter table CONSTITUE
   add constraint FK_CONSTITU_CONSTITUE_EQUIPES foreign key (EQNUMERO)
      references EQUIPES (EQNUMERO);

alter table ENTRAINEURS
   add constraint FK_ENTRAINE_GERE_EQUIPES foreign key (EQNUMERO)
      references EQUIPES (EQNUMERO);
      
ALTER TABLE ENTRAINEURS DROP CONSTRAINT CKC_ENEMAIL_ENTRAINE;
ALTER TABLE ENTRAINEURS
    ADD CONSTRAINT CKC_ENEMAIL_FORMAT CHECK (REGEXP_LIKE(ENEMAIL, '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'));


alter table EST_UTILISE_DANS
   add constraint FK_EST_UTIL_EST_UTILI_TRAINING foreign key (TRNUMERO)
      references TRAININGS (TRNUMERO);

alter table EST_UTILISE_DANS
   add constraint FK_EST_UTIL_EST_UTILI_MATERIEL foreign key (MATERIELNUMERO)
      references MATERIELS (MATERIELNUMERO);

alter table MATCHS
   add constraint FK_MATCHS_DISPUTE_EQUIPES foreign key (EQNUMERO)
      references EQUIPES (EQNUMERO);

alter table PAIEMENTS
   add constraint FK_PAIEMENT_PAYE_JOUEURS foreign key (JONUMERO)
      references JOUEURS (JONUMERO);

alter table TRAININGS
   add constraint FK_TRAINING_ASSISTE_EQUIPES foreign key (EQNUMERO)
      references EQUIPES (EQNUMERO);

