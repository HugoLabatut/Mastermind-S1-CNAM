CREATE TABLE joueur(
                       id_joueur INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                       nom_joueur VARCHAR(50)
);

CREATE TABLE partie(
                       id_partie INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                       etat_partie SMALLINT, -- 0 = partie en cours, 1 = partie terminée gagnée, 2 = partie terminée perdue
                       suite_partie SMALLINT, -- Suite gagnante de la partie (à optimiser)
                       nbcouleur_partie INT,
                       nbcoups_partie INT, -- Nombre de coups max / d'essais de la partie
                       id_joueur INT NOT NULL,
                       FOREIGN KEY(id_joueur) REFERENCES joueur(id_joueur)
);

CREATE TABLE coup(
                     id_coup INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                     contenu_coup INT, -- Contenu du coup sous forme d'un int (2478 pour 2,4,7,8 p.e.)
                     -- Sous cette forme, cela bloquerait une potentielle évolution d'un coup contenant plus de 10
                     -- possibilités pour chaque position (on aurait p.e. 10247 pour 10,2,4,7 ce que l'on ne peut pas découper comme actuellement).
                     -- Possibilité d'amélioration : passer sur un code séparé par des tirets en string (p.e. 10-2-4-7), facile à découper.
                     rang_coup INT, -- Rang du coup dans la partie (1er, 2ème, etc...)
                     id_partie INT NOT NULL,
                     FOREIGN KEY(id_partie) REFERENCES partie(id_partie)
);

ALTER TABLE coup ADD UNIQUE (contenu_coup, rang_coup, id_partie); -- Empêche la création de coups avec un contenu, rang et ID identiques (les 3)