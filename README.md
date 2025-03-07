If it doesn't work after docker-compose up, restart the app and/or the ocr-worker container.
Although they depend on the other containers through the compose.yaml, it can sometimes happen, that the other containers are not ready for the connection yet, causing this issue.


The routes:

**Base URL:** `(http://localhost:8081)`

**API Routes:**

- **/api/PDFentries**
  - **GET:** holt alle
  - **POST:** neuer Eintrag, braucht json im body

- **/api/PDFentries/{Id von Eintrag}**
  - **GET:** holt den Eintrag
  - **PUT:** updated den Eintrag, braucht json im body
  - **DELETE:** löscht den Eintrag

- **/api/PDFentries/search**
  - **GET:** leitet den query parameter an Elastic Search für die Suche weiter und returned die entsprechenden jsons.



## Frontend
Accessible over localhost:80

```npm run dev``` um das frontent zu entwickeln

```npm run build``` before docker compose

---
## Backend

```./mvnw clean package -Papi -DskipTests```

```./mvnw clean package -Pocr -DskipTests```

um das backend zu baun

## Tests

```./mvnw clean test``` im backend folder

Will man nach den tests "docker-compose build" machen, muss man die commands, die das backend zu bauen, erneut durchführen.