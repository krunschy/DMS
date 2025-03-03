https://www.youtube.com/watch?v=KuM6OtuaYRs&list=PLGRDMO4rOGcNLnW1L2vgsExTBg-VPoZHr&ab_channel=JavaGuides

Dem bin ich vor allem gefolgt für sprint 3. Also die routes sind

(http://localhost:8081)
/api/PDFentries
  GET: holt alle
  POST: neuer Eintrag, braucht json im body
/api/PDFentries/{Id von Eintrag}
  GET: holt den Eintrag
  PUT: updated den Eintrag, braucht json im body
  DELETE: löscht den Eintrag


check die datenbank mit
docker exec -it dms-db psql -U user -d dmsdb
select * from pdfs;


"npm run dev" um das frontent zu entwickeln
"npm run build" before docker compose

./mvnw clean package -Papi -DskipTests
./mvnw clean package -Pocr -DskipTests

   um das backend zu baun, skiptests is wichtig, weils sonst keine db hat, und das baut den ocr worker und api service separat


