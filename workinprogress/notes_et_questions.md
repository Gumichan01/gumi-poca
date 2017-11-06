# Notes et questions sur travail en cours

*Garder l'historique de ce qu'il y a ici pour pouvoir synthétiser nos difficultés*

## Diagramme de classes

- Où stocke t-on les notes ? Dans la fiche réponse pour un questionnaire en particulier mais la note générale par rapport à un cours en particulier ?
- Lien entre étudiants/professeurs et cours : est-ce que c'est bon ? Est ce qu'on devrait pas plutôt le mettre au niveau de utilisateur ?
- Lien entre Serveur et Cours : raison du lien ? Si on se mets dans la situation du Serveur, on peut faire /GET quoi ? /GET users (et encore...) et /GET cours non ? Pas de raison d'aller voir les professeurs pour voir quels sont les cours existants...
- Question utile : comment un professeur peut il récupérer la liste des fiches réponses à corriger ? une possibilité avec graphe existant (dans commit courant) : consulter un cours, voir les étudiants inscrits, voir leur fiches réponses, corriger ce qu'il reste à corriger. => C'est dans l'impléméntation qu'on gère ça.
