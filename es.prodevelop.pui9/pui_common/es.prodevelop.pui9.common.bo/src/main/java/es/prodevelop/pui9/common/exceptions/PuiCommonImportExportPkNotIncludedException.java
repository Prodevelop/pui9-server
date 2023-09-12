package es.prodevelop.pui9.common.exceptions;

public class PuiCommonImportExportPkNotIncludedException extends AbstractPuiCommonException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 215;

	public PuiCommonImportExportPkNotIncludedException(String... pks) {
		super(CODE, (Object[]) pks);
	}

}
