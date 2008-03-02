package dk.bregnvig.formula1.dao;

public interface CommonDao {
	
	void persist(Object object);
	
	void merge(Object object);
	
	void refresh(Object object);
}
