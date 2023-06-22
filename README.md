![CI](https://github.com/GLO2003-H22-eq02/projet/actions/workflows/maven.yml/badge.svg)
![CI: Static Code Analysis](https://github.com/GLO2003-H22-eq02/projet/actions/workflows/static_code_analysis.yml/badge.svg)
![CI: Automatic Staging Deployment](https://github.com/GLO2003-H22-eq02/projet/actions/workflows/automatic_staging_deployment.yml/badge.svg)
![CI: Manuel Production Deployment](https://github.com/GLO2003-H22-eq02/projet/actions/workflows/manual_production_deployment.yml/badge.svg)

[![Code Coverage](https://codecov.io/gh/GLO2003-H22-eq02/projet/branch/develop/graph/badge.svg?token=I2QNPNK1BB)](https://codecov.io/gh/GLO2003-H22-eq02/projet)
![Quality Score](https://qscored.com/badge/21c39a2493700c827f6232a3e2c38625ec41c4ad5341e107b29b9818649260b4/score/)
![Quality Rank](https://qscored.com/badge/21c39a2493700c827f6232a3e2c38625ec41c4ad5341e107b29b9818649260b4/rank/)
[![License](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

# Interprétation du _Quality Score_ et _Quality Rank_
Selon la documentation de QScored:
> Periodically (currently once every week), QScored ranks all the projects in the corpus based on their quality score. Lower the quality score, better the software in  terms of code quality. Hence, a project with the lowest quality score is assigned the first rank, and so on.

# Floppa

Le meilleur site de vente anonyme au Québec!

## Description

Floppa permet de mettre en place un système de vente de produits par différents vendeurs. Il s'agit d'un API REST. Il comprend des vendeurs, des produits et incorpore également un système d'offres qui peuvent être faits sur les différents produits. L'API nous permet:
- d'aller chercher un produit en fonction de son ID,
- d'aller chercher un vendeur en fonction de son ID,
- d'ajouter un offre avec un acheteur à un produit,
- de filtrer les produits en fonction de plusieurs critères,
- d'ajouter des vendeurs, des produits à la base de données,
- et plus encore!

Il s'agit d'un API efficace, fabriqué avec des techniques de code et un processus moderne, optimal, respectant les principes du Clean Code.
Pour une personne cherchant un API de vente de qualité, Floppa est fait pour vous!

## Documentation
- [LICENSE](https://github.com/GLO2003-H22-eq02/projet/blob/develop/LICENSE.md)
- [CODE_OF_CONDUCT](https://github.com/GLO2003-H22-eq02/projet/blob/develop/CODE_OF_CONDUCT.md)
- [CONTRIBUTION](https://github.com/GLO2003-H22-eq02/projet/blob/develop/CONTRIBUTION.md)

## Requis

- Java 11
- Maven

## Setup

### Compilation

```
mvn clean install
```

### Exécution

```
mvn exec:java
```
