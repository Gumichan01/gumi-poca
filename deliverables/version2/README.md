# README Evaluator - Version 2 - Group 8

## Equipe Group 8 et Répartition

- Luxon JEAN-PIERRE
- Duc Anh NGUYEN
- Belaid LAGHA
- Yves VIAUD - Serveur (API + Web App)

## Utilisation

### Compilation

```bash
cd evaluator_server
sbt run
```

### Lancement

Si la compilation réussie, le serveur est lancé.

RDV sur : `http://localhost:9000/` pour afficher le site web.

### Jeu de données fourni

Evaluator est fournit avec un petit jeu de données.

Ce jeu de données contient 4 utilisateurs, **1 professeur** et **3 étudiants** :

  * Gustavo Petri `Petri:gpetri`
  * James Bond `Bond:goldeneye`
  * Jean Valjean `Valjean:hugo`
  * Luke Skywalker `Skywalker:force`

Les logins sont donc : `NomDeFamille:MotDePasse` donnés ci-dessus.

Il contient également un cours : **POOCAv** avec 2 questionnaires pré-fournis et chacuns dispose de **2 étudiants** pré-inscrits (Valjean et Skywalker).
