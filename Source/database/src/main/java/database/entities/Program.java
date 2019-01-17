package database.entities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Program {
	public static void main(String[] args) {

		try {

			Configuration config = new Configuration();

			config.configure();
			config.addAnnotatedClass(database.entities.Board.class);
			config.addAnnotatedClass(database.entities.Chat.class);
			config.addAnnotatedClass(database.entities.Command.class);
			config.addAnnotatedClass(database.entities.Game.class);
			config.addAnnotatedClass(database.entities.ChatMessage.class);
			config.addAnnotatedClass(database.entities.Move.class);
			config.addAnnotatedClass(database.entities.Player.class);
			config.addAnnotatedClass(database.entities.Territory.class);

			SessionFactory session_fact = config.buildSessionFactory();

			Session session = session_fact.openSession();

			Transaction transaction = session.beginTransaction();

			// transaction.commit();

			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
