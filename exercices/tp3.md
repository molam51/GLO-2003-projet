# TP3
## Rétrospective sur le processus

### Pipeline CI

#### Combien de temps passiez-vous à vérifier et tester manuellement le code lors des intégrations et des remises avant l'implantation du pipeline de tests automatisés?
Puisqu'on avait déjà des tests implémentés, cele nous prenait une dizaine de minutes pour s'assurer que tout fonctionnait bien avec Postman. Sinon, on roulait nos tests unitaires avant les requêtes de changements pour faire le restes des validations.

#### Combien de temps passiez-vous à faire ces vérifications après l'implantation du CI?
Après l'implantation du CI, on n'a plus à exécuter les tests manuellement, donc ça nous prend environ 2 minutes pour être sûr que tout s'est bien déroulé.

#### Quels sont les points positifs que le CI a apporté à votre processus? Donnez-en au moins 3.
1. Le temps consacré aux tests est grandement diminué par rapport au temps qu'on y passait dessus précédemment.
2. Ça assure une conformité de code parmi les membres de l'équipe, car tous les membres doivent respecter le même formalisme.
3. Ça assure la conformité dans l'organisation du travail parmi les membres de l'équipe. Par exemple, les noms des commits doivent maintenant respecter une certaine mise en forme, ce qui permet au projet et au repository d'être plus propre et organisé.

#### Le pipeline CI amène-t-il un élément qui pourrait devenir négatif ou dangeureux pour le processus, le produit et/ou l'équipe? Justifiez.
On peut avoir tendance à y faire trop confiance et il est limité aux cas de tests qu'on lui donne. Ainsi, il est possible d'avoir des oublis ou de ne pas couvrir certains aspects du code dans les tests, ce qui peut générer des erreurs comme celles de notre TP2, où on avait oublié de tester notre `main`, ce qui a fait planter le programme au démarrage.

### Tests

#### Quel proportion de temps passez-vous à faire l'implémentation du code fonctionnel versus celui des tests? Est-ce que cette proportion évolue au fil du temps? Pourquoi?
En général, on passe 50% du temps consacré à une fonctionnalité à implémenter le code fonctionnel et l'autre 50% de cette issue se passe à faire les tests sur ces fonctionnalités.

#### L'implémentation de tests augmente naturellement la charge de travail. Comment cela a-t-il affecté votre processus? (ex : taille des issues/PRs, temps d'implémentation, planification, etc.)
La taille des issues et la planification des processus n'ont pas changées parce que c'est sous-entendu qu'on doit faire les tests pour chaque issue qu'on implémente. Cependant, le temps qu'on passe sur l'implémentation et sur les revues de code augmente puisqu'il y a plus de code à survoler.

#### Avez-vous plus confiance en votre code maintenant que vous avez des tests? Justifiez.
Oui, on a moins peur de faire des changements dans le code puisque ça sera attrapé facilement par les tests. Par contre, il faut faire attention parce que des erreurs peuvent survenir par des oublis d'implémenter des tests de notre part (comme lors de notre TP2 où on a oublié d'implémenter les tests pour le `main`).

#### Que pouvez-vous faire pour améliorer l'état actuel (début TP2) de vos tests? Donnez au moins 3 solutions.
1. Créer des utilitaires pour certains tests qui réutilisent de la logique commune.
2. Remplacer les valeurs magiques de nos tests par des valeurs aléatoires pour augmenter la robustesse de nos tests.
3. Faire des revues de tests en équipe pour détecter les cas de tests manquants.

## Stories
#### Maintenant que la majorité des features sont complétées, l'équipe de Floppa désire récolter des idées pour le développement futur de la plateforme. Elle vous demande donc de fournir 3 stories originales qui permettront de surpasser la compétition.

### Story 1
**But:** Un acheteur peut se créer un compte et obtenir les détails de son compte

**Discussion:**
- Création d'un compte d'acheteur
- Obtention des détails d'un compte d'acheteur courant

**Tests:**
- Création d'un compte d'acheteur
  - l'adresse courriel doit être présente, non-vide et avoir le format <identifiant>@<domain>.<extension>
  - le nom de la personne doit être présent et non-vide
  - le numéro de téléphone doit être présent, non-vide et avoir le format suivant "111111111111"
  - la date de naissance doit être présente et doit correspondre à une age de 18 ans et plus
  - l'identificateur du compte d'acheteur doit être initialisé et unique aux autres comptes d'acheteur
  - la date de création du compte d'acheteur doit être initialisée
  - le compte d'acheteur est sauvegardé dans l'application
- Obtention des détails d'un compte d'acheteur courant
  - l'identificateur fournit doit être présent et correspondre à un compte d'acheteur existant
  - le compte d'acheteur affiché est celui correspondant à l'identificateur fournit

### Story 2
**But:** Un acheteur peut ajouter et supprimer un produit à ses favoris
  
**Discussion:**
- Ajouter un produit aux favoris de l'acheteur courant
- Supprimer un produit des favoris de l'acheteur courant
  
**Tests:**
- Ajouter un produit aux favoris de l'acheteur courant
  - l'identificateur du produit fournit doit être présent et correspondre à un produit existant
  - l'identificateur du compte d'acheteur fournit doit être présent et correspondre à un compte d'acheteur existant
  - le produit ne doit pas déjà être dans les favoris du compte d'acheteur
  - le produit est ajouté aux favoris du compte d'acheteur
- Supprimer un produit des favoris de l'acheteur courant
  - l'identificateur du produit fournit doit être présent et correspondre à un produit existant
  - l'identificateur du compte d'acheteur fournit doit être présent et correspondre à un compte d'acheteur existant
  - le produit doit déjà être dans les favoris du compte d'acheteur
  - le produit est supprimé des favoris du compte d'acheteur

### Story 3
**But:** Obtenir les statistiques sur un vendeur
  
**Discussion:**
- Nombre de produits en vente
- Nombre de produits vendus
- Délai moyen entre l'affichage d'un produit et la première offre sur celui-ci
- Délai moyen de vente d'un produit

**Tests:**
- l'identificateur du vendeur fournit doit être présent et correspondre à un vendeur existant
- le nombre de produits en vente est correcte (calcul est bon et correspond au bon vendeur)
- le nombre de produits vendus est correcte (calcul est bon et correspond au bon vendeur)
- le délai moyen entre l'affichage d'un produit et la première offre sur celui-ci est correcte (calcul est bon et correspond au bon vendeur)
- le délai moyen de vente d'un produit est correcte (calcul est bon et correspond au bon vendeur)

## Captures d'écran 

### 1. 1 pour le Project comprenant les colonnes et les *issues* associées
![CaptureEcranProjet](https://user-images.githubusercontent.com/77983131/160754646-06ca229c-4aa6-436a-89bd-47b71168b683.PNG)

### 2. 1 pour le milestone comprenant le titre, la description et les *issues* associées
![milestone_1](https://user-images.githubusercontent.com/77983131/160754447-dfddfba7-5d52-4007-af10-45c78b01852f.png)
![milestone_2](https://user-images.githubusercontent.com/77983131/160754468-5bf2ddd2-6731-4cc2-bd99-685b3ef62296.png)
![milestone_3](https://user-images.githubusercontent.com/77983131/160754488-8cbff8ec-0e54-408d-9101-dd80e5973d16.png)
![milestone_4](https://user-images.githubusercontent.com/77983131/160754505-f92c8952-4672-43a4-8d95-9de53d628792.png)

### 3. 3 pour les *issues* avec tous les éléments demandés visibles
![CaptureEcranIssue1](https://user-images.githubusercontent.com/77983131/160754775-4b024a5a-2bce-489f-9239-c64ace7187d7.PNG)
![CaptureEcranIssue2](https://user-images.githubusercontent.com/77983131/160754794-7a1cf7ad-8563-4497-8b6e-1f64acdc9881.PNG)
![CaptureEcranIssue3](https://user-images.githubusercontent.com/77983131/160754809-57f3ef75-54c0-4b8d-8aed-8bf898c5be14.PNG)

### 4. 3 pour les *pull requests* avec tous les éléments demandés visibles
![CapturePR1](https://user-images.githubusercontent.com/77983131/160754671-4ff20a5c-2f3a-4754-b0fc-2a765f60a7e3.PNG)
![CaptureEcranPR2(1)](https://user-images.githubusercontent.com/77983131/160754703-4371fd0f-4788-4945-871d-459996bcf608.PNG)
![CaptureEcranPR2(2)](https://user-images.githubusercontent.com/77983131/160754721-b23181d0-fdd4-4b95-b4dc-2933c0f79bde.PNG)
![CaptureEcranPR3(1)](https://user-images.githubusercontent.com/77983131/160754725-46f53372-7819-472e-94ce-83da02f43346.PNG)
![CaptureEcranPR3(2)](https://user-images.githubusercontent.com/77983131/160754737-ccfffab8-6bd2-48a8-9fd4-02b3ab1d142a.PNG)

### 5. 1 pour votre arbre de commits et de branches (au moins 3 branches et/ou 10 commits visibles)
![arbreDeCommit](https://user-images.githubusercontent.com/77983131/160754585-4ce35f71-812f-48c3-8c7f-6243256d4d5b.png)
