CREATE VIEW v_pui_docgen_template AS
SELECT
	dt.id,
	dt.name,
	dt.description, 
	dt.main_model,
	dt.models,
	dt.column_filename,
	dm.label,
	dt.filename
FROM pui_docgen_template dt
LEFT JOIN pui_docgen_model dm ON
	dm.model = dt.main_model;
