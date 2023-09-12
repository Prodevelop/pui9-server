const sampleAction = {
	id: 'idSampleAction',
	selectionType: 'single', // [single|multiple|general]
	label: 'action.${config.modelName}.sampleAction',
	functionality: null, // set the functionality if needed
	showInForm: true,
	checkAvailability: function (/*registries*/) {
		// Validation to execute action
		return true;
	},
	runAction: function (action, model, registries) {
		// Sample code to open a model dialog
		const row = registries[0];
		// Get PK for the header
		const objectPk = {};
		for (var index in model.columns) {
			const column = model.columns[index];
			if (column.isPk) {
				objectPk[column.name] = registries[0][column.name];
			}
		}
		row.headerPk = objectPk;
		row.isAction = true;
		this.$puiEvents.$emit('pui-modalDialogForm-${config.modelName}SampleAction-' + model.name + '-show', row);
	}
};

export default {
	gridactions: [sampleAction],
	formactions: [sampleAction]
}