package tutorial;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateTest {

	public static void main(String[] args) {

		System.out.println("\nHELLOOOO\n");

		Configuration config = new Configuration();
		config.configure();
		config.addAnnotatedClass(TestObj.class);
		config.addAnnotatedClass(SecondObject.class);

		SessionFactory sess_fact = config.buildSessionFactory();

		Session session = sess_fact.openSession();
		Transaction trans = session.beginTransaction();

		TestObj t_obj = new TestObj();
		t_obj.setName("first_obj");

		TestObj t_obj1 = new TestObj();
		t_obj1.setName("second_obj");

		TestObj t_obj2 = new TestObj();
		t_obj2.setName("third_obj");

		session.save(t_obj);
		session.save(t_obj1);
		session.save(t_obj2);

		SecondObject obj1 = new SecondObject("val1", "val2", 3);
		obj1.getTest_obj().add(t_obj);
		obj1.getTest_obj().add(t_obj2);

		SecondObject obj2 = new SecondObject("val11", "val12", 13);
		obj1.getTest_obj().add(t_obj1);

		t_obj.setSecond_ref(obj1);
		t_obj1.setSecond_ref(obj1);

		System.out.println("\nSAVE\n");

		session.save(obj1);
		session.save(obj2);

		trans.commit();

		sess_fact.close();

	}

}
