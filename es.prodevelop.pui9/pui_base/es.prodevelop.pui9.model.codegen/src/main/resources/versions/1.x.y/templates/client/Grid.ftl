<template>
	<div>
		<pui-datatable
<#if config.client.detailGrid>
			v-if="!masterDetail"
</#if>
			:modelName="modelName"
<#if config.client.generateActions>
			:actions="actions"
</#if>
			:modelColumnDefs="modelColumnDefs"
<#if config.client.generateForm>
	<#if !config.client.editableForm>
			:showCreateBtn="false"
			:showDeleteBtn="false"
			readOnly
	</#if>
<#else>
			:navigableDetail="false"
</#if>
		></pui-datatable>
<#if config.client.detailGrid>
		<pui-datatable
			v-else
			:modelName="modelName"
	<#if config.client.generateActions>
			:actions="actions"
	</#if>
			:modelColumnDefs="modelColumnDefs"
			:externalFilter="externalFilter"
			:masterDetail="masterDetail"
			:parentModelName="parentModelName"
			:modalDialog="modalDialog"
			:readOnly="readOnly"
			:showCreateBtn="showCreateBtn"
			:showDeleteBtn="showDeleteBtn"
		></pui-datatable>
</#if>
<#if config.client.generateModals>
		<${config.modelName}-modals
			:modelName="modelName"
		></${config.modelName}-modals>
</#if>
	</div>
</template>

<script>
<#if config.client.detailGrid>
import PuiGridDetailMixin from 'pui9-mixins/PuiGridDetailMixin';
</#if>
<#if config.client.generateActions>
import ${config.modelName}Actions from './${config.modelName?cap_first}Actions';
</#if>
<#if config.client.generateModals>
import ${config.modelName}Modals from './${config.modelName?cap_first}Modals.vue';
</#if>

export default {
	name: '${config.modelName}-grid',
	components: {
<#if config.client.generateModals>
		'${config.modelName}-modals': ${config.modelName}Modals
</#if>
	},
<#if config.client.detailGrid>
	mixins: [PuiGridDetailMixin],
</#if>
	data() {
		return {
			modelName: '${config.modelName}',
<#if config.client.generateActions>
			actions: ${config.modelName}Actions.gridactions,
</#if>
			modelColumnDefs: {
			}
		}
	}
}
</script>
