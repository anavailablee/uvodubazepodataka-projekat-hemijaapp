USE hemija_lab;

-- 1. Indeks na status izvođenja
-- Koristi se BTREE indeks jer dobro radi sa filtriranjem po statusu
-- Nije klasterovan jer InnoDB klasteruje samo primarni ključ
-- Ubrzava pretragu po statusu, npr. zavrseni eksperimenti
CREATE INDEX IX_izvodjenje_status ON Izvodjenje(status);

-- 2. Indeks na datum izvođenja
-- BTREE je dobar za rad sa datumima i rasponima
-- Nije klasterovan jer ne menja fizički raspored tabele
-- Koristi se za brže pretrage po vremenskom periodu
CREATE INDEX IX_izvodjenje_datum ON Izvodjenje(datum);

-- 3. Kompozitni indeks (laboratorija + status)
-- Ovaj indeks pomaže kada se zajedno koriste laboratorija i status
-- Nije klasterovan jer je primarni ključ već glavni indeks
-- Ubrzava upite gde filtriramo po više uslova
CREATE INDEX IX_izvodjenje_lab_status ON Izvodjenje(id_laboratorije, status);

-- 4. Indeks na Alat -> laboratorija
-- Pomaže kod spajanja tabela (JOIN)
-- Nije klasterovan jer se tabela organizuje po primarnom ključu
-- Brže prikazuje alate po laboratoriji
CREATE INDEX IX_alat_laboratorija ON Alat(id_laboratorije);

-- 5. Indeks na Alat -> tip
-- BTREE: pogodan za filtriranje po kategoriji
-- Klasterovan: NE
-- Optimizacija: ubrzava upite po tipu alata
CREATE INDEX IX_alat_tip ON Alat(id_tipa);

-- 6. Indeks na Sesija -> izvodjenje
-- BTREE: optimizuje relacije između tabela
-- Klasterovan: NE
-- Optimizacija: ubrzava JOIN sesija i izvođenja
CREATE INDEX IX_sesija_izvodjenje ON Sesija(id_izvodjenja);

-- 7. Indeks na SesijaResurs -> resurs
-- BTREE: pogodan za agregacije
-- Klasterovan: NE
-- Optimizacija: ubrzava SUM i GROUP BY operacije
CREATE INDEX IX_sesija_resurs ON SesijaResurs(id_resursa);

-- 8. Kompozitni indeks LaboratorijaResurs
-- BTREE: efikasan za M:N relacije
-- Klasterovan: NE
-- Optimizacija: ubrzava JOIN i statističke upite
CREATE INDEX IX_lab_resurs ON LaboratorijaResurs(id_laboratorije, id_resursa);

-- 9. Indeks na Eksperiment -> teorija
-- BTREE: optimizuje povezane entitete
-- Klasterovan: NE
-- Optimizacija: ubrzava JOIN eksperimenata i teorije
CREATE INDEX IX_eksperiment_teorija ON Eksperiment(id_teorije);

-- 10. Indeks na Istrazivac prezime
-- Koristi se za bržu pretragu po prezimenu
-- Nije klasterovan jer je prezime često ponovljeno
-- Pomaže kod lakšeg pronalaženja istraživača
CREATE INDEX IX_istrazivac_prezime ON Istrazivac(prezime);