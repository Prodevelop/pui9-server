package es.prodevelop.codegen.pui9.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum DatabaseType {

	ORACLE("Oracle", "oracle", "Oracle", Arrays.asList("sdo_geometry", "st_geometry"), 1521),

	SQL_SERVER("SQL Server", "sqlserver", "Microsoft SQL Server", Collections.singletonList("geometry"), 1433),

	POSTGRE_SQL("Postgre SQL", "postgresql", "PostgreSQL", Collections.singletonList("geometry"), 5432);

	public String name;
	public String innerName;
	public String officialName;
	public List<String> geometryColumnNames;
	public int defaultPort;

	private DatabaseType(String name, String innerName, String officialName, List<String> geometryColumnNames,
			int defaultPort) {
		this.name = name;
		this.innerName = innerName;
		this.officialName = officialName;
		this.geometryColumnNames = geometryColumnNames;
		this.defaultPort = defaultPort;
	}

	public static DatabaseType getByName(String name) {
		for (DatabaseType dte : values()) {
			if (dte.name.equals(name)) {
				return dte;
			}
		}
		return null;
	}

	public static DatabaseType getByOfficialName(String officialName) {
		for (DatabaseType dte : values()) {
			if (dte.officialName.equals(officialName)) {
				return dte;
			}
		}
		return null;
	}

	public static String[] getAllValues() {
		List<String> names = new ArrayList<>();
		for (DatabaseType dte : values()) {
			names.add(dte.name);
		}

		return names.toArray(new String[0]);
	}

}
