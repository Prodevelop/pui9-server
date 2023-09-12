<template>
	<div>
		<!-- If modal have a "pui-select" or "pui-date-field", set property :overflow="false" -->
		<!-- If fixed width is needed, set property :widthDialog="XXX" -->
		<pui-modal-dialog-form
			:titleText="$t('modal.${config.modelName}.sample.title')"
			:modelName="modelName"
			:dialogName="${config.modelName}SampleModal"
<#if config.client.generateHeader>
			:componentHeaderName="'${config.modelName}-form-header'"
</#if>
			:onOk="onOkSampleModal"
			:onShow="onShowSampleModal"
		>
			<template slot="message" slot-scope="data">
				<#assign column = config.selectedTable.columns[0]>
				<#assign text = column.controlConfiguration>
				<#assign fromModal = true>
				<#include "/client/controls/Text.ftl">
			</template>
		</pui-modal-dialog-form>
	</div>
</template>

<script>
export default {
	name: '${config.modelName}-modals',
	data() {
		return {
			${config.modelName}SampleModal: '${config.modelName}SampleAction'
		};
	},
	props: {
		modelName: {
			required: true,
			type: String
		}
	},
	methods: {
		onOkSampleModal(modalData) {
			const params = {
				attr1: modalData.field1
			};

			const url = this.$store.getters.getModelByName(this.modelName).url.sampleUrl;
			var title = this.$puiI18n.t('puiaction.notifyTitle') + ' > ' + this.$puiI18n.t('modal.${config.modelName}.sampleaction.title');
			return new Promise((resolve) => {
				this.$puiRequests.postRequest(
					url,
					null,
					(response) => {
						// Valid response, do code and close modal
						console.log(response);
						resolve(true);
					},
					(e) => {
						// Error response, do code and not close modal
						let message = this.$puiI18n.t('puiaction.notifyError');
						if (e.response && e.response.data) {
							message = e.response.data.message;
						}
						this.$puiEvents.$emit('onPui-action-running-ended-' + this.modelName);
						this.$puiNotify.error(message, title);
						resolve(false);
					},
					null,
					params
				);
			});
		},	
		onShowSampleModal(modalData) {
			// Put here code to execute before open modal.
			// Examples:
			// - Watch pui-select/pui-datatable with events
			// - change values or set new values "this.$set(modalData,'field3', null)"
			// ...
			console.log(modalData);
		}
	}
};
</script>
