package es.prodevelop.pui9.json.adapters;

/**
 * Type adapter for Double type to be used with GSON
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class DoubleTypeAdapter extends AbstractNumberTypeAdapter<Double> {

	@Override
	public Class<Double> getType() {
		return Double.class;
	}

	@Override
	public Class<Double> getUnboxedType() {
		return double.class;
	}

	@Override
	protected Double parse(String result) {
		result = result.replace(',', '.');
		return Double.parseDouble(result);
	}
}
