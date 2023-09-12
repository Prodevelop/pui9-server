package es.prodevelop.pui9.geo.utils;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geotools.geometry.jts.WKTWriter2;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKBReader;
import org.springframework.util.ObjectUtils;

import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

public class DtoGeoUtils {

	private static final Logger logger = LogManager.getLogger(DtoGeoUtils.class);

	public static <T extends IDto> void fillGeometryValue(T dto) {
		List<String> geomColumns = DtoRegistry.getGeomColumns(dto.getClass());
		geomColumns.forEach(geomColumn -> {
			Field field = DtoRegistry.getJavaFieldFromColumnName(dto.getClass(), geomColumn);
			Object wkb = null;

			try {
				wkb = FieldUtils.readField(field, dto, true);
			} catch (Exception e) {
				return;
			}

			if (ObjectUtils.isEmpty(wkb)) {
				return;
			}

			try {
				Geometry geometry = new WKBReader().read(WKBReader.hexToBytes(wkb.toString()));
				String wkt = new WKTWriter2().write(geometry);
				FieldUtils.writeField(field, dto, wkt, true);
			} catch (Exception e) {
				String dtoData = dto.toString() + " (wkb: " + wkb + ")";
				logger.error(e.getMessage() + ":: " + dtoData, e);
			}
		});
	}

	private DtoGeoUtils() {
	}

}
