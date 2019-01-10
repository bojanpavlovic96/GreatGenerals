package database.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class Repository<EntityType> {

	private Session session;
	private Transaction transaction;

	public Session getSession() {
		return session;
	}

	// repository interface

	public void add(EntityType entity) {
		this.session.save(entity);
	}

	public void remove(EntityType entity) {
		this.session.delete(entity);
	}

	public EntityType getById(long id) {
		return (EntityType) this.session.get(this.getClass(), id);
	}

	public List<EntityType> find() {

		return null;
	}

	//

	public void setSession(Session session) {
		this.session = session;
	}

	public Repository() {
		// TODO Auto-generated constructor stub
	}

	public Repository(Session session) {
		super();
		this.session = session;
	}

}
