package es.prodevelop.pui9.geo.utils;

public enum EpsgEnum {

	GLOBAL(4326), // lat/lon

	MERCATOR(3857); // x/y

	public final int srid;
	public final String sridWithAttributeName;

	private EpsgEnum(int srid) {
		this.srid = srid;
		this.sridWithAttributeName = "SRID=" + srid + ";";
	}

}