package net.lucypoulton.pronouns.config;

import net.lucypoulton.squirtgun.format.FormatProvider;

import java.util.List;

public interface ConfigHandler extends FormatProvider {
	List<String> getPredefinedSets();

	List<String> getFilterPatterns();

	boolean filterEnabled();

	SqlInfoContainer getSqlConnectionData();

	boolean checkForUpdates();

	ConnectionType getConnectionType();
}
