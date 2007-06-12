package dk.bregnvig.formula1.dao;

import java.util.Set;

import dk.bregnvig.formula1.Driver;

/**
 * This interface describes the methods that a driver dao
 * must implement
 * @author 4u603ctpsz9
 *
 */
public interface DriverDao {

	void createDriver(Driver driver);
	
	/**
	 * Deletes a driver. Might fail, if the user is assosiated with a race
	 * season etc.
	 * @param driver
	 */
	void deleteDriver(Driver driver);
	
	/**
	 * Returns the driver based on the primary key
	 * @param id
	 * @return
	 */
	Driver findById(Long id);
	
	/**
	 * Returns all drivers
	 * @return
	 */
	Set<Driver> findAll();
}
