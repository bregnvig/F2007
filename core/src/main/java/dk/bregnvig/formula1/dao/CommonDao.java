package dk.bregnvig.formula1.dao;

public interface CommonDao {
	
	void persist(Object object);
	
	<T> T merge(T object);
	
	void flush();
}
