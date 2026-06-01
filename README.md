# Hemija Lab – Aplikacija za praćenje laboratorijskih eksperimenata

## O projektu

Ovaj projekat izrađen je u okviru predmeta **Uvod u baze podataka** na Računarskom fakultetu (RAF), Univerzitet Union, Beograd.

Projekat predstavlja aplikaciju za praćenje laboratorijskih eksperimenata u hemijskim laboratorijama, sa bazom podataka koja čuva podatke o eksperimentima, laboratorijama, resursima, alatima i istraživačima.

**Autori:** Ana Nikolić (135/24si), Danilo Ristović  
**Akademska godina:** 2024/2025  
**Podtema:** Hemija – eksterni korisnik

---

## Tehnologije

- **Java 22** – programski jezik
- **Java Swing** – grafički korisnički interfejs
- **JDBC** – konekcija na bazu podataka
- **MySQL** – relaciona baza podataka
- **Maven** – upravljanje zavisnostima

---

## Struktura projekta
hemija-app-AnaDanilo/
├── src/
│   └── main/
│       └── java/
│           └── hemija/
│               ├── MainFrame.java          # Početni ekran aplikacije
│               ├── LoginForm.java          # Forma za prijavu
│               ├── RegisterForm.java       # Forma za registraciju
│               ├── MainMenu.java           # Glavni meni
│               ├── LaboratorijeForm.java   # Pregled laboratorija i istraživača
│               ├── AzuriranjeNalogaForm.java # Ažuriranje korisničkog naloga
│               ├── BrisanjeNalogaForm.java # Brisanje korisničkog naloga
│               └── DataBaseConnection.java # Konekcija na bazu podataka
├── pom.xml
└── korisnici.txt

---

## Baza podataka

Baza podataka `hemija_lab` sadrži sledeće tabele:

| Tabela | Opis |
|--------|------|
| Laboratorija | Hemijske laboratorije sa bezbednosnom klasom (BSL-1 do BSL-4) |
| Resurs | Hemijske supstance sa formulom, CAS brojem, agregatnim stanjem i GHS klasifikacijom |
| TipAlata | Apstraktne klasifikacije alata (spektrometar, centrifuga...) |
| Alat | Konkretni instrumenti u laboratorijama |
| LaboratorijaResurs | Inventar resursa po laboratorijama |
| Teorija | Hemijske teorije i metode (Le Chatelier, Bragg...) |
| Eksperiment | Hemijski eksperimenti sa tipom (sinteza, titracija, spektroskopija...) |
| EksperimentResurs | Resursi potrebni za eksperiment |
| EksperimentAlat | Alati potrebni za eksperiment |
| Istrazivac | Hemičari sa stepenom obrazovanja i specijalizacijom |
| Dizajner | Veza između istraživača i eksperimenata koje su dizajnirali |
| Izvodjenje | Izvođenja eksperimenata u laboratorijama sa statusom |
| TimIzvodjenja | Tim istraživača koji učestvuju u izvođenju |
| Sesija | Zakazane sesije za izvođenje eksperimenata |
| SesijaResurs | Utrošeni resursi po sesiji |
| SesijaAlat | Korišćeni alati po sesiji |
| korisnici | Korisnički nalozi aplikacije |

### Dodatni objekti u bazi

- **Pogled** `StatistikaLaboratorija` – statistika laboratorija sa brojem uspešnih izvođenja i ukupno utrošenim resursima
- **Procedura** `DodajIzvodjenjeISesiju` – dodaje novo izvođenje i prvu sesiju u jednoj transakciji
- **Funkcija** `UkupnaKolicinaResursa` – vraća ukupnu količinu resursa u datoj laboratoriji
- **Test funkcija** `TestUkupnaKolicinaResursa` – testira prethodnu funkciju za 5 različitih laboratorija

---

## Forme aplikacije

Aplikacija je izrađena iz ugla **eksternog korisnika** i sadrži sledeće forme:

| Forma | Opis |
|-------|------|
| Početni ekran | Opcije za prijavu i registraciju |
| Prijava (Log in) | Autentifikacija korisnika iz baze |
| Registracija (Sign up) | Kreiranje novog korisničkog naloga |
| Pregled laboratorija i istraživača | Tabela sa svim laboratorijama i istraživačima koji u njima rade |
| Ažuriranje naloga | Promena korisničkog imena i lozinke |
| Brisanje naloga | Brisanje naloga uz obaveznu potvrdu lozinke |

---

## Samostalni upiti

### Ana Nikolić – Top 10 najkorišćenijih hemikalija
Prikazuje top 10 najkorišćenijih hemikalija u sesijama, sa ukupno utrošenom količinom i GHS klasifikacijom opasnosti.

```sql
SELECT 
    r.naziv AS hemikalija,
    r.hemijska_formula,
    r.ghs_klasifikacija,
    r.agregatno_stanje,
    SUM(sr.kolicina_utrosena) AS ukupno_utroseno,
    COUNT(sr.id_sesije) AS broj_sesija
FROM Resurs r
JOIN SesijaResurs sr ON r.id = sr.id_resursa
GROUP BY r.id, r.naziv, r.hemijska_formula, r.ghs_klasifikacija, r.agregatno_stanje
ORDER BY ukupno_utroseno DESC
LIMIT 10;
```

### Danilo Ristović – Istraživači sa doktoratom
Prikazuje istraživače sa doktoratom, broj eksperimenata koje su dizajnirali i njihovu oblast specijalizacije.

```sql
SELECT 
    i.ime,
    i.prezime,
    i.oblast_specijalizacije,
    COUNT(d.id_eksperimenta) AS broj_dizajniranih_eksperimenata
FROM Istrazivac i
JOIN Dizajner d ON i.id = d.id_istrazivaca
WHERE i.stepen_obrazovanja = 'doktorat'
GROUP BY i.id, i.ime, i.prezime, i.oblast_specijalizacije
HAVING COUNT(d.id_eksperimenta) > 1
ORDER BY broj_dizajniranih_eksperimenata DESC;
```

---

## Pokretanje projekta

### Preduslovi
- Java 22
- MySQL Server (lokalno na portu 3306)
- IntelliJ IDEA
- Maven

### Koraci

1. Klonirati repozitorijum:
```bash
git clone https://github.com/anavailablee/uvodubazepodataka-projekat-hemijaapp.git
```

2. Pokrenuti MySQL server i izvršiti SQL skripte redom:
   kreiranje_baze.sql
   punjenje_baze_podacima.sql
   dodatni_objekti.sql
   korisnici.sql

3. U fajlu `DataBaseConnection.java` promeniti lozinku:
```java
private static final String PASSWORD = "vasa_lozinka";
```

4. Otvoriti projekat u IntelliJ IDEA i pokrenuti `MainFrame.java`

### Podrazumevani korisnici
| Username | Password |
|----------|----------|
| korisnik1 | lozinka1 |
| korisnik2 | lozinka2 |

---

## Napomena

Lozinke u bazi su čuvane kao plain text u skladu sa zahtevima projektnog zadatka. U produkcijskom okruženju lozinke bi bile heširane.