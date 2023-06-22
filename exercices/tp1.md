# TP1
## Rôles

**Responsable des *pull requests* :** Clément Abergel (au moins pour l'itération 1)

- Prendre les décisions finales au niveau des *pull requests*
- Consultant pour la gestion des conflits majeurs lors des *pull requests*

**Responsable du formalisme :** William Pedneault

- Participer aux révisions de code (lors des *pull requests*)
- Vérifier le respect des normes de programmation (*Clean Code*)
- Vérifier que les conventions d'équipe sont respectées (surtout au niveau de la nomenclature des *commits* et des branches)

**Responsable du GitHub :** David Ferland, Anthony Blanchette-Potvin (tutorat)

- Vérifier que les conventions d'équipe sont respectées
- Gérer les GitHub Actions
- Gestion du repository GitHub (issues, configurations, etc.)

**Gestionnaire de projet :** Monique Lambert, William Pedneault

- Organiser les réunions
  - Préparer les ordres du jour
  - Prendre des notes pendant les réunions
- Animer les réunions
- Assurer que l'équipe est en voie de livrer à la date limite du *milestone* courant

**Responsable de l'architecture logicielle :** Anthony Blanchette-Potvin, Clément Abergel (assistant)

- Prendre les décisions finales au niveau de l'architecture logicielle

*Chacun des rôles détermine les responsabilités prioritaires de chacun des membres, mais l'opinion et la contribution de chacun des membres n'est en aucun cas interdit.*

*L'auteur d'une pull request se charge de fermer celle-ci lorsqu'elle a été approuvée par tous les reviewers.*

*L'auteur d'une pull request est responsable de corriger les conflits de celle-ci si nécessaire. Il est préférable que celui-ci consulte le gestionnaire des pull requests en cas de conflits majeurs.*

## Conventions
### 1. Nomenclature des commits

Le texte des commits doit respecter la nomenclature suivante : `<type>: <description>` ([source](https://www.conventionalcommits.org/en/v1.0.0/)).

`<type>`: `bug`, `feat`, `test`, `doc`, `refactor`

`<description>`: une description concise, complète et claire du travail effectué (en anglais)

Ex.: `feat: created class Error`

### 2. Quand faire un commit et quoi inclure dans celui-ci ?

- Il ne faut pas faire de commit si le code n'est pas fonctionnel à 100% ;
- Avant de faire un commit, il faut faire un minimum de nettoyage pour s'assurer que le code est clair et peut facilement être repris par un autre développeur si besoin sans que celui-ci soit confus ou perdu dans le code ;
- Le ménage du code dans d'autres fichiers sur d'autres *features* est permis dans une branche si c'est un changement mineur (pas plus de 30 minutes à consacrer au changement). Si le ménage est majeur, il faut reporter le changement en créant une nouvelle *issue* sur GitHub. On applique ici le principe du « boy scout » ([source](https://drive.google.com/file/d/0B1lMPPfEr07IV1VqVFFJaE5ZbTA/preview?resourcekey=0-kpoviTbHIbX5A4_A46X79g)) ;
- Tous les commits doivent se faire en anglais.

### 3. Quelles sont les branches de base (qui sont communes et qui existeront toujours) et quels sont leurs rôles (chacune) ?

- `develop`: branche de développement dans laquelle les branches correspondantes aux différentes *issues* seront fusionnées ;
- `main`: branche stable dans laquelle la branche `develop` sera fusionnée à la fin de chaque milestone.

Méthodologie inspirée de celles présentées dans cet [article](https://www.toptal.com/software/trunk-based-development-git-flow).

### 4. Quelle branche est la branche principale (contenant le code officiellement intégré et pouvant être remis) ?

La branche officielle contenant le code officiellement intégré et pouvant être remis est `main` ([source](https://www.toptal.com/software/trunk-based-development-git-flow)).

La *pull request* correspondante au merge de `develop` vers `main` doit être approuvée par atteinte d'un consensus (toute l'équipe).

### 5. Quand créer une nouvelle branche ?

On doit créer une branche pour chaque *issue*.

Nomenclature d'une branche :
`<assignee>/<iteration_name>/<issue_number>_<issue_name>`
  
`<assignee>` : le nom de la personne assignée (ex. : cabergel, mlambert, dferland...)
  
`<iteration_name>` : it1, it2, it3, it4
  
`<issue_number>` : le numéro de l'*issue* sur GitHub (ex. : #10, #2, #99)
  
`<issue_name>` : le nom approximatif de l'*issue* sur GitHub en *camel back*, soit la première lettre en minuscule et la première lettre de chaque mot consécutif en majuscule (ex. : « createClassError » pour l'*issue* « Créer la classe Error »)

Ex.: `cabergel/it1/#10_createClassError`

### 6. Quand faire une demande de changement / d'intégration (pull request / merge request) et sur quelle branche la faire ?

- Lorsque qu'une *issue* est complétée, une *pull request* de la branche appropriée vers `develop` peut être demandée pour intégrer la *feature* à la branche `develop` ;
- Une *issue* est considéré comme *complétée* si elle répond aux critères suivants :
  - tous les *requirements* sont respectés ;
  - tous les tests du projet passent ;
  - le code est exempt de lignes de code/commentaires superflus ;
  - le code respecte les normes de programmation.
- À la fin d'un milestone, une *pull request* de la branche `develop` vers `main` doit être effectuée pour la remise éventuelle ;
- Une *pull request* doit être approuvée par un minimum de 2 personnes avant de pouvoir être acceptée ;
- Les *reviewers* de chaque *pull request* doivent contenir au minimum le responsable des *pull requests* (révision obligatoire) et le responsable du *formalisme* (révision obligatoire).

## Captures d'écran 

### 1. pour le Project comprenant les colonnes et les *issues* associées
![1 1](https://user-images.githubusercontent.com/77983131/151466256-a17bec1d-f925-46ff-99ba-4178a7ac50f1.png)

### 2. pour le milestone comprenant le titre, la description et les *issues* associées
![2 1](https://user-images.githubusercontent.com/77983131/151466274-7866b9d7-c635-45ec-87de-09791680ad77.png)

### 3. pour les *issues* avec tous les éléments demandés visibles
![3 1](https://user-images.githubusercontent.com/77983131/151466483-2796bb76-e54e-438a-92c1-931531215b5c.png)
![3 2](https://user-images.githubusercontent.com/77983131/151466491-9876b5b1-fb3c-47fb-a0a7-e0a7fcfa4c35.png)
![3 3](https://user-images.githubusercontent.com/77983131/151466500-af31a74f-76f1-48ef-9b1e-bc568c1f61f4.png)

### 4. pour les *pull requests* avec tous les éléments demandés visibles
![4 1](https://user-images.githubusercontent.com/77983131/151466518-8f429e43-c808-481e-86a7-78741119b299.png)
![4 2](https://user-images.githubusercontent.com/77983131/151466524-bb27656d-02e7-4b1c-8628-9c86c21fec71.png)
![4 3](https://user-images.githubusercontent.com/77983131/151466538-b1e42736-7476-4205-ad6d-269ab8436172.png)
