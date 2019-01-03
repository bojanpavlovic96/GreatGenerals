## Faza 3: do 11.1.2019.
```
dodao:
client-model
client-view
	class Launcher - main za pokretanje javafx app; kreiranje DrawingStage-a, Kontrolera i modela i njihovo povezivanje
	class view.DrawingStage - prozor za prikaz implements EventDrivenComponent, CommandDrivenComponent, ShouldBeShutdown
	package view.command - sadrzi komande koje ce biti proslednjivane commandQueue-u iz DrawingStage-a
	package view.components - sadrzi komponente koje ce biti iscrtavane, Hexagon, Terrain, Unit
	
	view.CommandGenerator - koriscnjen za testiranje CommandDrivenComponent
	view.ModelHexaong - predstavlja model podataka iz koga ce biti kreiran heksagon pogodan za iscrtavanje
	
	view.CommandProcessor - implements QueueEventHandler, pri svakom dodavanju u queue dok queue nije prazan uzima komande iz queue-a, dodeljuje im odgovarajuci canvas na kome ce se izvrsiti crtanje i poziva njihovu execute metodu.
	
	package field.region - zanemariti, sadrzi implementaciju hexagona kao zasebne komponete (javafx region)
	
client-controller
	
```
