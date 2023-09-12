package es.prodevelop.pui9.geo.utils;

import java.text.DecimalFormat;

import org.apache.commons.lang3.ObjectUtils;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.geometry.jts.WKTWriter2;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class GeometryUtils {

	private static final Double TOLERANCE = 10D; // meters
	private static final String EPSG_LITERAL = "EPSG:";

	private static final GeometryFactory geometryFactory = new GeometryFactory();
	private static MathTransform transformToGlobal;
	private static MathTransform transformToMercator;

	public static Geometry wktToGeometry(String wkt) {
		if (ObjectUtils.isEmpty(wkt)) {
			return null;
		}

		String safeWKT = wkt.replace(EpsgEnum.GLOBAL.sridWithAttributeName, "");
		try {
			return new WKTReader2().read(safeWKT);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Coordinate wktToCoordinate(String wkt) {
		if (ObjectUtils.isEmpty(wkt)) {
			return null;
		}

		Geometry geom = wktToGeometry(wkt);
		return geom != null ? geom.getCoordinate() : null;
	}

	public static String toPointWkt(Double x, Double y) {
		return EpsgEnum.GLOBAL.sridWithAttributeName + toPointWktWithoutSrid(x, y);
	}

	public static String toPointWktWithoutSrid(Double x, Double y) {
		return new WKTWriter2().write(geometryFactory.createPoint(new Coordinate(x, y)));
	}

	public static boolean geometryWithinGeometry(Geometry container, Geometry contained) {
		return container != null && contained != null && container.contains(contained);
	}

	public static boolean areEquals(Coordinate first, Coordinate second) {
		if (first == null && second == null) {
			return true;
		}

		if (first == null || second == null) {
			return false;
		}

		GeometryFactory factory = new GeometryFactory();

		Geometry firstGeometry = factory.createPoint(first);
		Geometry secondGeometry = factory.createPoint(second);
		firstGeometry.setSRID(EpsgEnum.GLOBAL.srid);
		secondGeometry.setSRID(EpsgEnum.GLOBAL.srid);

		try {
			firstGeometry = transformGlobalToMercator(firstGeometry);
			secondGeometry = transformGlobalToMercator(secondGeometry);
			return firstGeometry != null && secondGeometry != null
					&& firstGeometry.getCoordinate().equals2D(secondGeometry.getCoordinate(), TOLERANCE);
		} catch (Exception e) {
			DecimalFormat df = new DecimalFormat("#.#########");
			String x1 = df.format(first.x);
			String y1 = df.format(first.y);

			String x2 = df.format(second.x);
			String y2 = df.format(second.y);

			return x1.equals(x2) && y1.equals(y2);
		}
	}

	public static Double getDistanceBetweenTwoPositionsAtCityScale(Coordinate start, Coordinate end) {
		if (start == null || end == null) {
			return 0D;
		}

		float meridianLengthInMeters = 20004000;
		float equatorLengthInMeters = 40075000;

		Double deltaLon = equatorLengthInMeters * Math.abs(start.x - end.x) / 360 * Math.cos(start.y);
		Double deltaLat = meridianLengthInMeters * Math.abs((start.y - end.y)) / 180;

		return Math.sqrt(Math.pow(deltaLon, 2) + Math.pow(deltaLat, 2));
	}

	@SuppressWarnings("unchecked")
	public static <T extends Geometry> T transformGlobalToMercator(T global) {
		try {
			return (T) JTS.transform(global, getTransformToMercator());
		} catch (MismatchedDimensionException | TransformException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends Geometry> T transformMercatorToGlobal(T mercator) {
		try {
			return (T) JTS.transform(mercator, getTransformToGlobal());
		} catch (MismatchedDimensionException | TransformException e) {
			return null;
		}
	}

	private static MathTransform getTransformToGlobal() {
		if (transformToGlobal == null) {
			try {
				CoordinateReferenceSystem sourceCRS = CRS.decode(EPSG_LITERAL + EpsgEnum.MERCATOR.srid, true);
				CoordinateReferenceSystem targetCRS = CRS.decode(EPSG_LITERAL + EpsgEnum.GLOBAL.srid, true);
				transformToGlobal = CRS.findMathTransform(sourceCRS, targetCRS);
			} catch (FactoryException e) {
				e.printStackTrace();
				// do nothing
			}
		}

		return transformToGlobal;
	}

	private static MathTransform getTransformToMercator() {
		if (transformToMercator == null) {
			try {
				CoordinateReferenceSystem sourceCRS = CRS.decode(EPSG_LITERAL + EpsgEnum.GLOBAL.srid, true);
				CoordinateReferenceSystem targetCRS = CRS.decode(EPSG_LITERAL + EpsgEnum.MERCATOR.srid, true);
				transformToMercator = CRS.findMathTransform(sourceCRS, targetCRS);
			} catch (FactoryException e) {
				// do nothing
			}
		}

		return transformToMercator;
	}

	private GeometryUtils() {
		// Can't instantiate
	}

}
