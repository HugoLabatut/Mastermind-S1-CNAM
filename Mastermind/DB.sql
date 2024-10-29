CREATE TABLE joueur(
                       id_joueur INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                       nom_joueur VARCHAR(50)
);

CREATE TABLE partie(
                       id_partie INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                       etat_partie SMALLINT, -- 0 = partie en cours, 1 = partie terminée gagnée, 2 = partie terminée perdue
                       suite_partie SMALLINT, -- Suite gagnante de la partie (à optimiser)
                       nbcoups_partie INT,
                       nbcouleur_partie INT,
                       id_joueur INT NOT NULL,
                       FOREIGN KEY(id_joueur) REFERENCES joueur(id_joueur)
);

CREATE TABLE coup(
                     id_coup INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                     contenu_coup INT,
                     rang_coup INT,
                     id_partie INT NOT NULL,
                     FOREIGN KEY(id_partie) REFERENCES partie(id_partie)
);