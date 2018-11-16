# Faze implementacije projekta

Projekat se realizuje kroz 3 faze koje treba predati u predviđenim rokovima. Predaja svake faze projekta u predviđenom roku vrednuje se sa maksimalno 10 poena. Zakasnela predaja faze u terminu odbrane projekta se vrednuje sa maksimalno 5 poena. 

## Faza 1: do 7.12.2018.
Arhitekturni dizajn softverskog sistema (u Architecture folderu na GIT repozitorijumu):

- Definisanje funkcionalnosti softverskog sistema, arhitekturnih zahteva i ne-funkcionalnih zahteva koje treba podržati i zadovoljiti (atribute kvaliteta)

- Arhitekturni pattern-i koji će biti primenjeni

- Arhitekturni dizajn svog projekta sa prikazom funkcionalnih elemenata (modula, komponenti) i njihovih veza korišćenjem strukturnog, bihevioralnog i alokacionog pogleda (ili 4+1 modela pogleda – logičkog/procesnog/implementacionog/deployment) i korišćenjem odgovarajućih UML dijagrama

- Specifikacija biblioteka, komponenti i aplikacionih okvira (framework-a) koji će biti korišćeni za implementaciju klijentske aplikacija, serverske aplikacije, perzistencije i komunikacije.



## Faza 2: do 21.12.2018.

Potrebno je implementirati i dokumentovati (u Documents folderu na GIT repozitorijumu) model podataka (business model) i model perzistencije (entity model). Osim toga potrebno je implementirati i biblioteku sa funkcijama za snimanje i ucitavanje prethodno implementiranog (business) modela podataka. Ova biblioteka treba i da implementira mapiranje business data modela u entity model koji se perzistira u bazi po nekom od Data Layer pattern-a.



## Faza 3: do 11.1.2019.

Potrebno je implementirati i dokumentovati (u Documents folderu na GIT repozitorijumu) model komunikacije i odgovarajuću softversku komponentu. Ova komponenta treba da koristi neku od poznatih biblioteka koje rešavaju problem komunikacije.

