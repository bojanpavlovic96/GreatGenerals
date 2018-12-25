package entities;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Program {
	public static void main(String[] args) {

		try {

			Configuration config = new Configuration();
			
			config.configure();
			config.addAnnotatedClass(entities.Board.class);
			config.addAnnotatedClass(entities.Chat.class);
			config.addAnnotatedClass(entities.Command.class);
			config.addAnnotatedClass(entities.Game.class);
			config.addAnnotatedClass(entities.Message.class);
			config.addAnnotatedClass(entities.Move.class);
			config.addAnnotatedClass(entities.Player.class);
			config.addAnnotatedClass(entities.Territory.class);

			SessionFactory session_fact = config.buildSessionFactory();

			Session session = session_fact.openSession();
			Transaction transaction = session.beginTransaction();

//			transaction.commit();

			session.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
