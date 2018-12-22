package tutorial;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateTest {

	public static void main(String[] args) {

		Configuration config = new Configuration();
		config.configure();
		config.addAnnotatedClass(TestObj.class);

		SessionFactory sess_fact = config.buildSessionFactory();

		TestObj t_obj = new TestObj();
		t_obj.setId(3);
		t_obj.setName("third_name");

		Session session = sess_fact.openSession();
		Transaction trans = session.beginTransaction();

		session.save(t_obj);
		trans.commit();

	}

}
