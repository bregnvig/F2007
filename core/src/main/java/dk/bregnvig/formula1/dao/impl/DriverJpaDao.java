package dk.bregnvig.formula1.dao.impl;

import java.util.Set;

import org.springframework.orm.jpa.support.JpaDaoSupport;

import dk.bregnvig.formula1.Driver;
import dk.bregnvig.formula1.dao.DriverDao;

public class DriverJpaDao extends JpaDaoSupport implements DriverDao {

	public void createDriver(Driver driver) {
		getJpaTemplate().persist(driver);
	}

	public void deleteDriver(Driver driver) {
		getJpaTemplate().remove(driver);
	}

	public Set<Driver> findAll() {
		return null;
	}

	public Driver findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
