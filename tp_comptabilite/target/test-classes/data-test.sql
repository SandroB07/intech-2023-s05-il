INSERT INTO compte_comptable (numero, libelle)
    VALUES (15, 'Sandro'),
           (16, 'Matteo'),
           (17, 'Omar');

INSERT INTO journal_comptable (code, libelle)
    VALUES ('AC', 'Achat'),
           ('BQ', 'Banque');

INSERT INTO ecriture_comptable (id, journal_code, date, reference, libelle)
    VALUES (1, 'AC', '2023-06-01' , 'AC-2023/00001', 'Cartouches d’imprimante'),
           (2, 'AC', '2023-06-01' , 'AC-2023/00002', 'Lib'),
           (3, 'BQ', '2023-06-01' , 'BQ-2023/00001', 'TMA');

INSERT INTO sequence_ecriture_comptable (journal_code, annee, derniere_valeur)
    VALUES ('AC', 2023, 2),
           ('BQ', 2023, 1);

INSERT INTO ligne_ecriture_comptable (ecriture_id, ligne_id, compte_comptable_numero, libelle, debit, credit)
    VALUES (1, 1, 15, 'A', 100.55, 0),
           (1, 2, 15, 'B', 0, 50),
           (1, 3, 15, 'C', 0, 50),
           (1, 4, 15, 'D', 0, 0.55),
           (2, 1, 16, 'AC', 125.55, 0),
           (2, 2, 17, 'AD', 0, 125.55),
           (3, 1, 16, 'AX', 55, 1.26),
           (3, 2, 17, 'BC', 54.56, 0),
           (3, 3, 15, 'CD', 126.7, 180),
           (3, 4, 17, 'DF', 150, 205);