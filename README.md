## Faza 3: do 11.1.2019.
```
dodao:
client-model
client-view
	napravio prozor za prikaz igre (JFrame)
	launcher klasa pokrece GameFrame i dodeljuje joj debugFrame u kom se prikazuju poruke
	slovo 'd' prikazuje i sakriva debugFrame
	trenutno ne radi zbog dodeljivanja handlera za debuFrame, dva konstruktora, nullException-i ...
	
	'napravio' interfejs za view (eventDrivenComponent, FieldPainter)
	drawable ce mozda da implementira Field, ali nisam bas siguran da je potrebo, ne deluje, ipak je potrebno da view zna sta iscrtava, ne moze sve preko interfejsa da ide, a i nema razloga
	
client-controller
	krenuo, stao ...
```
