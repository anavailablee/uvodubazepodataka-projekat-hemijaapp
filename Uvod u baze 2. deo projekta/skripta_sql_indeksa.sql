USE hemija_lab;

-- 1. Indeks na status izvođenja
-- BTREE: Kolona 'status' ima mali kardinalitet (mali broj različitih vrednosti),
--        što bi u teoriji BITMAP učinilo dobrim izborom, ali MySQL ne podržava
--        BITMAP indekse, pa se koristi BTREE koji je jedina opcija u InnoDB-u.
-- Klasterovanost: Nije klasterovan jer InnoDB klasteruje tabele isključivo po primarnom ključu.
-- Optimizacija: Ubrzava pretragu i filtriranje po statusu eksperimenta (npr. WHERE status = 'završen'),
--               što je čest upit u sistemu za praćenje eksperimenata.
CREATE INDEX IX_izvodjenje_status ON Izvodjenje(status);

-- 2. Indeks na datum izvođenja
-- BTREE: Idealan za rad sa datumima i rasponima (BETWEEN, >, <), jer BTREE čuva
--        vrednosti sortirano i efikasno podržava range scan operacije.
-- Klasterovanost: Nije klasterovan jer InnoDB klasteruje tabele isključivo po primarnom ključu.
-- Optimizacija: Ubrzava upite koji filtriraju eksperimente po vremenskom periodu
--               (npr. WHERE datum BETWEEN '2024-01-01' AND '2024-06-01').
CREATE INDEX IX_izvodjenje_datum ON Izvodjenje(datum);

-- 3. Kompozitni indeks (laboratorija + status)
-- BTREE: Pogodan za kompozitne indekse jer podržava pretragu po prefiksu —
--        može se koristiti i kada se filtrira samo po id_laboratorije.
-- Klasterovanost: Nije klasterovan jer InnoDB klasteruje tabele isključivo po primarnom ključu.
-- Optimizacija: Ubrzava upite koji istovremeno filtriraju po laboratoriji i statusu
--               (npr. WHERE id_laboratorije = 3 AND status = 'aktivan'), čime se
--               izbegava full table scan nad velikom tabelom Izvodjenje.
CREATE INDEX IX_izvodjenje_lab_status ON Izvodjenje(id_laboratorije, status);

-- 4. Indeks na Alat -> laboratorija
-- BTREE: Pogodan za JOIN operacije i filtriranje po stranom ključu, jer BTREE
--        omogućava brzo tačkasto i opsežno pretraživanje.
-- Klasterovanost: Nije klasterovan jer InnoDB klasteruje tabele isključivo po primarnom ključu.
-- Optimizacija: Ubrzava JOIN između tabela Alat i Laboratorija, kao i upite koji
--               pribavljaju sve alate koji pripadaju određenoj laboratoriji.
CREATE INDEX IX_alat_laboratorija ON Alat(id_laboratorije);

-- 5. Indeks na Alat -> tip
-- BTREE: Kolona 'id_tipa' ima mali kardinalitet (ograničen broj tipova alata),
--        što bi BITMAP činio pogodnim, ali MySQL/InnoDB ne podržava BITMAP indekse,
--        pa se koristi BTREE.
-- Klasterovanost: Nije klasterovan jer InnoDB klasteruje tabele isključivo po primarnom ključu.
-- Optimizacija: Ubrzava filtriranje alata po tipu (npr. WHERE id_tipa = 2) i
--               JOIN sa tabelom koja opisuje tipove alata.
CREATE INDEX IX_alat_tip ON Alat(id_tipa);

-- 6. Indeks na Sesija -> izvodjenje
-- BTREE: Pogodan za JOIN operacije na stranom ključu; BTREE omogućava efikasno
--        pronalaženje svih sesija vezanih za konkretno izvođenje.
-- Klasterovanost: Nije klasterovan jer InnoDB klasteruje tabele isključivo po primarnom ključu.
-- Optimizacija: Ubrzava JOIN između tabela Sesija i Izvodjenje, smanjujući
--               broj pregledanih redova pri pribavljanju sesija određenog izvođenja.
CREATE INDEX IX_sesija_izvodjenje ON Sesija(id_izvodjenja);

-- 7. Indeks na SesijaResurs -> resurs
-- BTREE: Pogodan za agregacijske upite (SUM, GROUP BY) i JOIN operacije jer
--        sortirano čuva vrednosti stranog ključa, ubrzavajući grupiranje.
-- Klasterovanost: Nije klasterovan jer InnoDB klasteruje tabele isključivo po primarnom ključu.
-- Optimizacija: Ubrzava SUM i GROUP BY operacije nad resursima po sesiji, kao i
--               JOIN sa tabelom Resurs pri analizi potrošnje resursa.
CREATE INDEX IX_sesija_resurs ON SesijaResurs(id_resursa);

-- 8. Kompozitni indeks LaboratorijaResurs
-- BTREE: Efikasan za M:N vezne tabele jer podržava pretragu po oba strana ključa
--        i može pokriti upite koji filtriraju samo po prvom atributu (id_laboratorije).
-- Klasterovanost: Nije klasterovan jer InnoDB klasteruje tabele isključivo po primarnom ključu.
-- Optimizacija: Ubrzava JOIN i statističke upite nad M:N relacijom laboratorija-resurs,
--               kao i proveru da li određena laboratorija koristi određeni resurs.
CREATE INDEX IX_lab_resurs ON LaboratorijaResurs(id_laboratorije, id_resursa);

-- 9. Indeks na Eksperiment -> teorija
-- BTREE: Pogodan za JOIN na stranom ključu i filtriranje po teorijskom okviru;
--        BTREE efikasno podržava tačkasto pretraživanje po id_teorije.
-- Klasterovanost: Nije klasterovan jer InnoDB klasteruje tabele isključivo po primarnom ključu.
-- Optimizacija: Ubrzava JOIN između tabela Eksperiment i TeorijskiOkvir, kao i
--               upite koji grupišu eksperimente po teorijskom okviru (GROUP BY id_teorije).
CREATE INDEX IX_eksperiment_teorija ON Eksperiment(id_teorije);

-- 10. Indeks na Istrazivac -> prezime
-- BTREE: Prezimena su tekstualne vrednosti visokog kardinaliteta (mnogo različitih vrednosti),
--        što BTREE čini idealnim izborom jer efikasno podržava pretragu prefiksom (LIKE 'Pet%')
--        i tačkasto filtriranje. BITMAP bi bio nepogodan zbog visokog kardinaliteta.
-- Klasterovanost: Nije klasterovan jer InnoDB klasteruje tabele isključivo po primarnom ključu.
-- Optimizacija: Ubrzava pretragu istraživača po prezimenu (WHERE prezime = 'Petrović')
--               kao i sortiranje rezultata po prezimenu (ORDER BY prezime).
CREATE INDEX IX_istrazivac_prezime ON Istrazivac(prezime);

-- 11. Indeks na Laboratorija -> naziv
-- BTREE: Naziv laboratorije je tekstualna vrednost visokog kardinaliteta,
--        BTREE je jedini razuman izbor i podržava pretragu prefiksom.
-- Klasterovanost: Nije klasterovan jer InnoDB klasteruje tabele isključivo po primarnom ključu.
-- Optimizacija: Ubrzava pretragu laboratorija po nazivu (WHERE naziv LIKE 'Hemijska%'),
--               što je čest upit u korisničkom interfejsu aplikacije.
CREATE INDEX IX_laboratorija_naziv ON Laboratorija(naziv);

-- 12. Indeks na Resurs -> naziv
-- BTREE: Tekstualna kolona visokog kardinaliteta — BTREE je optimalan izbor.
-- Klasterovanost: Nije klasterovan jer InnoDB klasteruje tabele isključivo po primarnom ključu.
-- Optimizacija: Ubrzava pretragu resursa po nazivu pri dodavanju resursa eksperimentu
--               i pri pregledu kataloga dostupnih resursa.
CREATE INDEX IX_resurs_naziv ON Resurs(naziv);