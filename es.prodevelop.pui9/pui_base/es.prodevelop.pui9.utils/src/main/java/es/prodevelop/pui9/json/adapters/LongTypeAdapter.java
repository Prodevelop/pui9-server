package es.prodevelop.pui9.json.adapters;

/**
 * Type adapter for Long type to be used with GSON
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class LongTypeAdapter extends AbstractNumberTypeAdapter<Long> {

	@Override
	public Class<Long> getType() {
		return Long.class;
	}

	@Override
	public Class<Long> getUnboxedType() {
		return long.class;
	}

	@Override
	protected Long parse(String result) {
		return Long.parseLong(result);
	}
}
