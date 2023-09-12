<template>
	<div class="${config.modelName}FormHeader puiformheader">
<#list config.selectedView.columns as column>
	<#if column.clientInHeader>
		<v-row>
			<v-col cols="12">
				<pui-field :label="$t('header.${config.modelName}.${column.javaName}')" :value="get${column.javaName?cap_first}Value"></pui-field>
			</v-col>
		</v-row>
	</#if>
</#list>
	</div>
</template>

<script>
import PuiFormHeaderMixin from 'pui9-mixins/PuiFormHeaderMixin';

export default {
	name: '${config.modelName}-form-header',
	mixins: [PuiFormHeaderMixin],
	data() {
		return {
			modelName: '${config.modelName}'
		};
	},
	computed: {
<#list config.selectedView.columns as column>
	<#if column.clientInHeader>
		get${column.javaName?cap_first}Value() {
			return this.model && this.model.${column.javaName} ? this.model.${column.javaName} : '-';
		}<#if column?has_next>,</#if>
	</#if>
</#list>
	}
}
</script>
