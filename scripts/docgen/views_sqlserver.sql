CREATE VIEW v_pui_docgen_model AS
SELECT
	dm.model,
	m.entity,
	dm.label + ' (' + m.entity + ')' AS label,
	dm.identity_fields
FROM pui_docgen_model dm
LEFT JOIN pui_model m ON
	m.model = dm.model;
