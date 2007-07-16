package dk.bregnvig.formula1.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class SpringStarter {

	private static ApplicationContext context = null;

	private static boolean changed = true;

	private static List<String> configLocations = new ArrayList<String>();

	static {
		configLocations.add("classpath:contexts/data/daoITestContext.xml");
		configLocations.add("classpath:contexts/data/dataLayerContext.xml");
		configLocations.add("classpath:contexts/service/serviceContext.xml");
		configLocations.add("classpath:contexts/service/serviceTestContext.xml");
	};

	public static void addConfigLocation(String location) {
		if (configLocations.contains(location) == false) {
			configLocations.add(location);
		}
	}

	public static ApplicationContext getApplicationContext() {
		if (context == null && changed == true) {
			changed = false;
			context = new FileSystemXmlApplicationContext(configLocations
					.toArray(new String[] {}));
		}
		return context;
	}

	public static String[] getConfigLocations() {
		return configLocations.toArray(new String[] {});
	}

}
