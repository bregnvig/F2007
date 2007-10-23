package dk.bregnvig.formula1.server;

import dk.bregnvig.formula1.client.SeasonService;

public class SeasonServiceImpl extends AbstractService implements SeasonService {

	private static final long serialVersionUID = 3283834885586579712L;

	public String getSeasonName() {
		return getSeason().getName();
	}
}
