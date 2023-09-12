package es.prodevelop.pui9.common.configuration;

import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.utils.IPuiObject;

/**
 * The column configuration for each model. For each column it's specified its
 * name (the name is the Java field name); the title for translate the column in
 * the client (concatenate the name of the model, a '.' and the name of the
 * column); the type of the column (numeric, text, date...); if it belongs to
 * the PK of the model; and the visibility (visible, hidden or completelyhidden)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiModelColumn implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private String name;
	private String title;
	private ColumnType type;
	private Boolean isPk = false;
	private ColumnVisibility visibility;

	public PuiModelColumn(String name, String title, ColumnType type, Boolean isPk, ColumnVisibility visibility) {
		this.name = name;
		this.title = title;
		this.type = type;
		this.isPk = isPk;
		this.visibility = visibility;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public ColumnType getType() {
		return type;
	}

	public Boolean isPk() {
		return isPk;
	}

	public ColumnVisibility getVisibility() {
		return visibility;
	}

}
