# TP4
## Rétrospective finale

#### Décrivez 2 problématiques que possède votre processus et développer 2 plans distincts afin de les résoudre. Soyez constructifs dans vos critiques et évitez de mettre la faute sur une ou un groupe de personne en particulier.
1. Notre mise en place d'une solution ne tenait pas en compte toutes les demandes faites par le client. En effet, certains tests clients échouaient puisqu'un critère du client n'était pas respecté. Pour résoudre ceci, nous envisageons composer les issues en équipe au lieu de déterminer une personne pour le faire seul. Cette stratégie nous permettrait d'éviter les oublis.
2. Nous éprouvons quelques goulots d'étranglements juste avant la livraison. En effet, nous nous retrouvons avec deux personnes qui travaillent sur les derniers détails tandis que les autres se cherchent du travail. Pour régler ceci, nous envisageons diviser les issues trop larges en plusieurs issues plus petites, surtout celles concernant le ménage du projet avant la livraison.

#### Décrivez la démarche que vous aviez entrepris afin d'intégrer de nouveaux outils technologiques. Quelles étaient les étapes du processus? Comment avez-vous réagis aux différents bogues? Exploriez-vous à l'aide de tests unitaires ou manuels? Qu'avez-vous appris suite à cette démarche?
La démarche utilisée dans la majorité des cas était de se regrouper en équipe d'au moins deux personnes pour faire l'intégration de nouveaux outils et traiter les bogues. Donc, il y avait plus d'une personne dans l'équipe qui connaissait la nouvelle technologie, alors les connaissances étaient bien réparties.

Lorsque nous avions un bogue, nous allions premièrement rechercher la nature du bogue à l'aide du code d'erreur. Celui-ci nous permettait de vérifier dans les ressources fournises dans l'énoncé ou sur Internet afin de trouver la solution appropriée. Pour corriger ceux-ci, on travaillait premièrement dans un environnement local, donc isolé. Cela nous permettait d'avoir un certain contrôle sur l'environnement et de pouvoir essayer plusieurs solutions sans polluer l'environnement de staging et l'environnement de production. En ce qui concerne l'intégration de nouvelles technologies à notre processus, les tests reliés à cette intégration étaient majoritairement manuels.

Nous avons appris que pour régler ce genre de problème, la documentation est essentielle. De plus, il est maintenant évident que la correction de ce type de bogue requiert beaucoup d'essai-erreur. Cependant, les outils technologiques sont super utiles pour assurer la qualité d'un projet, parce qu'ils nous forcent à appliquer des  standards et facilite plusieurs étapes du processus de développement comme l'intégration et l'exécution des tests.

Finalement, le processus employé par notre équipe pourrait être résumé comme suit. La première étape consistait à effectuer la consultation de la documentation concernant les technologies que nous souhaitions implémenter dans le projet. Une fois la technologie relativement comprise par les membres de l'équipe, nous implémentions une version simple de celle-ci dans le projet lui-même (dans une branche dédiée à cet effet). Après avoir assuré le fonctionnement de cette version de base, nous modifions son implémentation afin de l'adapter aux besoins du projet. Cette implémentation modifiée faisait ensuite face à un ensemble de tests afin d'assurer son bon fonctionnement et sa capacité à répondre aux besoins du projet. Les correctifs nécessaires étaient par la suite apportés. Une fois l'accord de tous donné et la confirmation que tous les tests passaient, l'équipe intégrait la nouvelle technologie dans la branche principale de développement du projet afin de poursuivre celui-ci et d'adapter le reste du projet à cette nouvelle technologie.

#### Quels sont les bons coups de votre équipe? De quelles parties êtes-vous fiers? Nommez-en 3.
Nous sommes fiers de l'architecture de notre logiciel. En effet, nous avons beaucoup d'abstraction, ce qui rend la réutilisation du code plus accessible. Également, nous avions une excellente dynamique d'équipe, ce qui a rendu le travail très agréable. Notre code est également très propre et uniforme et nous en sommes fiers. Le style de code est identique d'un fichier à un autre et la façon de nommer les différents éléments est similaire, ce qui permet aux membres de l'équipe de naviguer plus facilement dans le code et de mieux le comprendre.

#### Quel conseil donneriez-vous aux prochains étudiants qui doivent faire ce projet?
Nous conseillons de bien lire les énoncés et les demandes du client. De plus, il est important d'écouter et suivre les laboratoires, car de très bons conseils par rapport à la structure de projet et l'architecture logicielle sont donnés. Également, il est important de ne pas attendre la fin d'une itération avant d'intégrer les nouvelles technologies, car elles sont souvent plus importantes que prévus en termes de temps et difficulté d'intégration.

#### Quels apprentissages, trucs ou techniques appris dans ce projet croyez-vous pouvoir utiliser plus tard? Décrivez-en au moins 2. Cela peut être des apprentissages techniques, pratiques, sur le travail d'équipe ou encore par rapport au processus.
L'utilisation de l'intégration continue est selon nous très importante et nous prévoyons l'utiliser à notre avantage dans les projets futurs. En effet, nous avons énormément apprécié le temps sauvé par l'automatisation de l'exécution des tests, de certaines validations et du déploiement. Les rencontres hebdomadaires et la programmation en paire seront également de très bons outils dans les projets futurs, car ils favorisent la communication et par moment, nous motivent à se mettre au travail. En ce qui concerne le processus, nous avons appris que c'est essentiel à l'organisation d'une équipe. Ça permet de mieux identifier les goulots d'étranglement et le prendre des mesures pour les atténuer.

<br/>

### Open Source

#### Nommez 3 avantages à contribuer à des projets open source en tant qu'entreprise et justifiez en quoi cela peut être bénéfique pour tous.
 - Ça créer un nom pour l'entreprise qui contribue et augmente sa visibilité;
 - La contribution à des projets open source permet d'ajouter des fonctionnalités qui seront éventuellement utiles pour l'entreprise en question, ce qui est avantageux lorsque l'auteur original du projet open source ne prévoit pas implémenter de telles fonctionnalités dans un futur proche, malgré leur utilité. De plus, ceci permet aux autres entreprises et individus d'utiliser ces fonctionnalités, ce qui créer un sentiment d'entre-aide.
 - La contribution à des projets open source a pour effet de générer des librairies et du code beaucoup plus robuste et flexible que s'il n'y avait qu'un seul contributeur.
 - Lorsque les employés contribuent à des projets open source, ils développent des connaissances et compétences en lien avec ces technologies. Cela est avantageux pour l'entreprise, car les employés sont plus à l'aise à utiliser ces technologies dans le contexte des activités de leur organisation.

#### Décrivez 3 défis qu'impose la mise en place d'un projet open source et justifiez.
 - L'un des défis auxquels font face les personnes mettant en place un projet open source est le déploiement du processus de gestion du projet en question. En effet, afin d'assurer le bon déroulement du projet et le contrôle de la qualité de celui-ci, les processus de gestion des changements et des requêtes doivent être clairement définis, ce qui peut se révéler être un problème étant donné la nature de grande taille de certains projets de ce type.
 - Un second défi potentiel pouvant être identifié est le fait qu'il est nécessaire de bien définir la licence exacte, ainsi que le code de conduite du projet. Bien que tout projet possède une licence, plusieurs licences de ce type existe et répondent à des ensembles de besoins différents dépendants de la nature et de l'objectif du projet. Dans un milieu aussi vaste que l'open source, bien cerner les besoins en question, ainsi que sélectionner la bonne licence et la bonne gestion du projet dans son ensemble peut se révéler être un défi de taille dans les phases initiales du projet.
 - Un troisième défi relève de la nature communautaire des projets open source. En effet, lorsque plusieurs groupes ou individus sont impliqués dans le développement et l'avancement d'un projet open source, il peut arriver que ces différents partis possèdent une vue différente de ce vers quoi le projet devrait tendre et des besoins qu'ils devraient adresser. De plus, il peut arriver que la stratégie de développement (le _road map_) ne soit pas adéquate selon tous les partis. Dans ce contexte, il est important de posséder une stratégie afin de faire face à ces conflits, et que celle-ci soit adéquatement définie afin que tout conflit soit adressé de façon similaire et ne cause pas préjudice à qui que ce soit.

#### Quelle information vous a-t-elle le plus surprise à propos de l'open source?
Notre plus grande surprise a été le fait que les organisations de plus grande taille ont souvent tendance à s'impliquer dans ce type de projet et à activement contribuer à leur développement. En effet, la première impression de la personne moyenne serait que ces organisations ne participent pas à ce genre de projet, comme leur nature plus commune et partagée semble entrer directement en conflit avec les intérêts plus monétaires et individuels de ces compagnies. Toutefois, en effectuant nos lectures, nous avons pu observer la mesure dans laquelle ces organisations sont impliquées dans ces projets et profitent tout de même de ceux-ci, que ce soit par les avantages identifiés dans l'une des questions précédentes, ou tout autre potentiel avantage.

<br/>

## Choix de la licence
Nous avons choisi la licence **GNU GPLv3** pour les raisons suivantes:
1. La protection des développeurs et des auteurs originaux du produit est prise en compte. Lorsque le produit est modifié par un autre développeur, il doit explicitement indiquer que sa version est une modification du produit originale. Les problèmes ne seront donc pas attribués à la version originale;
2. La licence oblige les développeurs à conserver la même licence lorsqu'ils modifient ou distribuent le produit modifié. Cela assure la liberté d'accès au code source peu importe la distribution. Alors, n'importe qui à le droit de demander le code source suite à l'acquisition d'une distribution du produit;
3. La licence est compatible avec la licence Apache Software License v2. Par compatibilité, il est entendu qu'un projet utilisant à la fois du code d'un projet utilisant la Apache Software License v2 ainsi que le code de notre projet pourra être créé sans problème au niveau de la licence. Ceci n'étant pas toujours le cas, nous considérons que, dans l'esprit de participer de façon active et plus étendue au milieu du open source, de permettre l'inclusion de notre code dans d'autres projets est un grand atout. Finalement, la compatibilité avec la licence Apache Software License v2 spécifiquement est très intéressante, comme il s'agit d'une licence très utilisée et dont les objectifs généraux s'alignent généralement avec la licence que nous utilisons.

<br/>

## Choix du template du code de conduite
Le template que nous avons choisi pour le code de conduite du projet est celui de [Contributor Covenant](https://www.contributor-covenant.org/). Nous avons déterminé que ce template était le plus adéquat pour plusieurs raisons. Dans un premier temps, celui-ci est directement inspiré du Contributor Covenant, lequel est utilisé dans de nombreux projets open sources et répond bien à nos attentes du code de conduite. Par ceci, nous voulons dire que celui-ci définit clairement nos attentes par rapport à l'objectif du projet en matière de coopération et vise à assurer un environnement de travail sain et accueillant pour tous. Toutefois, le template diverge du Contributor Covenant par le fait qu'il fait utilisation d'une échelle d'impact des événements sur la communauté du projet et permet de clairement définir les actions qui seront potentiellement prises afin d'assurer le maintien du projet et la protection de sa communauté contre les membres ne souhaitant pas se conformer à ce code. Finalement, notons que nous avons ajouté une note afin de donner davantage de capacités aux chefs de la communauté de prendre action contre les contrevenants au code de conduite, afin de maximiser leur capacité à protéger la communauté du projet et de souligner l'intolérance de l'équipe par rapport à toutes formes de discrimination ou d'actions portant atteinte à autrui.

<br/>

## Choix du template des directives de contributions
Le template que nous avons choisi pour les directives de contribution au produit est celui de [briandk](https://gist.github.com/briandk/3d2e8b3ec8daf5a27a62). Nous avons choisi ce template, car il est simple et bien adapté au processus que nous avions déjà défini au sein de notre équipe. De plus, il est indépendant des technologies, alors il était facile d'ajouter les commandes et étapes liées aux technologies que nous utilisons (i.e.: le checkstyle de Maven).

<br/>

## Détails techniques de la _story_ à implémenter
Nous avons choisi la _story_ fournise dans l'énoncé du TP4, soit:

> En tant que vendeur, je peux voir le nombre de visionnements de chacun de mes produits.

Nous avons divisé la _story_ en deux requêtes distinctes:

- `POST /products/{id}/views` pour incrémenter le nombre de vues d'un produit
- `GET /products/{id}/views` pour obtenir le nombre de vues d'un produit

<br/>

### Requête `POST /products/{id}/views`

#### Route
`POST /products/{id}/views`

#### Critères de succès
- Le nombre de vues du produit correspondant à `id` est incrémenté de `1`

#### Requêtes
Le _payload_ de la requête contient aucune propriété. Autrement dit, on envoi un objet JSON vide.

#### Réponses

##### Status
`200 OK` si la requête s'est effectuée avec succès.

##### Exceptions
`ITEM_NOT_FOUND` si `id` ne correspond pas à un produit existant.

<br/>

### Requête `GET /products/{id}/views`

#### Route
`GET /products/{id}/views`

#### Critères de succès
- Le nombre de vues affiché est celui du produit correspondant à `id`

#### Requêtes
Aucun _header_ nécessaire pour l'exécution de la requête.

#### Réponses
##### Format
```
{
    views: int
}
```

##### Exemple
```json
{
    "views": 18
}
```

##### Status
`200 OK` si la requête s'est effectuée avec succès.

##### Exceptions
`ITEM_NOT_FOUND` si `id` ne correspond pas à un produit existant.

<br/>

## Captures d'écran pour le projet

### 1 pour le projet comprenant les colonnes et les *issues* associées
![project](https://user-images.githubusercontent.com/56654138/165976463-119cf1df-90fa-4f7b-b0be-2ccc070e47d3.JPG)

### 1 pour le *milestone* comprenant le titre, la description et les *issues* associées
![milestone_1](https://user-images.githubusercontent.com/56654138/165976520-82070b17-7942-493c-9ee8-d11e4c8892fb.JPG)
![milestone_2](https://user-images.githubusercontent.com/56654138/165976529-66aa326d-ab74-488f-886f-8ef3c1017d44.JPG)
![milestone_3](https://user-images.githubusercontent.com/56654138/165976540-aa9afef7-ed52-40ad-8fca-ba6fa23f8b67.JPG)

### 3 pour les *issues* avec tous les éléments demandés visibles

#### Issue 1
![issues_1](https://user-images.githubusercontent.com/56654138/165976569-d1fd44a9-6451-43c9-af02-1d4b9076ba2c.JPG)

#### Issue 2
![issues_2](https://user-images.githubusercontent.com/56654138/165976590-928eae39-45b1-442a-b1d1-8cb30fc6750f.JPG)

#### Issue 3
![issues_3](https://user-images.githubusercontent.com/56654138/165976613-01e59adf-c253-41af-a44c-e9b3041629a7.JPG)

### 3 pour les *pull requests* avec tous les éléments demandés visibles

#### Pull request 1
![pr_1-1](https://user-images.githubusercontent.com/56654138/165976655-79bdfb8c-00e0-4162-99e5-fed647590faa.JPG)
![pr_1-2](https://user-images.githubusercontent.com/56654138/165976661-3f3b631b-6f8d-4823-807f-51eeee93d508.JPG)

#### Pull request 2
![pr_2-1](https://user-images.githubusercontent.com/56654138/165976705-26c8604e-62b3-418b-8dfd-c8d1f9975250.JPG)
![pr_2-2](https://user-images.githubusercontent.com/56654138/165976710-ec9d73f7-a9f3-485a-b1e5-e39671e60396.JPG)

#### Pull request 3
![pr_3-1](https://user-images.githubusercontent.com/56654138/165976731-648ee7ac-dc7f-4dcb-a5f7-09a1f112f931.JPG)
![pr_3-2](https://user-images.githubusercontent.com/56654138/165976745-97aec950-61a1-4590-94b1-b83e768541b9.JPG)

### 1 pour votre arbre de commits et de branches (au moins 3 branches et/ou 10 commits visibles)
![tree](https://user-images.githubusercontent.com/56654138/165976769-8874ded8-e0ae-4cb3-9a9d-feccb6dd4e86.JPG)


## Captures d'écran pour la couverture des tests (Codecov)
### 1. ![code_coverage_1](https://user-images.githubusercontent.com/77983131/165640492-2939fcb1-74b3-4038-bb1a-e5ad1595009b.JPG)

### 2. ![code_coverage_2](https://user-images.githubusercontent.com/77983131/165640506-411237d0-dca6-4595-85c4-c012851143f2.JPG)

### 3. ![code_coverage_3](https://user-images.githubusercontent.com/77983131/165640517-4207bf3b-4ec3-4357-87e4-8edc128da60d.JPG)

## Captures d'écran pour la qualité du code (QScored)
### 1. ![code_quality_1](https://user-images.githubusercontent.com/77983131/165640540-bf0d4e3e-5d2b-4b85-9b30-6c144aa1a7b3.JPG)

### 2. ![code_quality_2](https://user-images.githubusercontent.com/77983131/165640544-b505d7d9-ce68-42e9-80c5-b162c7319607.JPG)

### 3. ![code_quality_3](https://user-images.githubusercontent.com/77983131/165640554-19d4a216-6fd6-4fc7-abb1-c8d53bcda52d.JPG)


## Captures d'écran pour la sécurité du code (Dependency-Check)
### 1. ![code_security_screenshot_1](https://user-images.githubusercontent.com/56654138/165856087-a2ddc900-cbb1-438e-bd64-e72bcd23a42c.JPG)

### 2. ![code_security_screenshot_2](https://user-images.githubusercontent.com/56654138/165856098-d6113099-6688-42c8-bd18-17f8d177b0a0.JPG)
