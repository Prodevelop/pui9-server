<template>
	<div class="pui-form">
<#if config.client.generateModals>
		<${config.modelName}-modals
			:modelName="modelName"
		></${config.modelName}-modals>
</#if>
		<pui-form-header
			v-if="modelLoaded"
			:showHeader="!isCreatingElement">
			<${config.modelName}-form-header
				:modelPk="modelPk"
			></${config.modelName}-form-header>
		</pui-form-header>
		<v-form class="mb-4 pb-4" ref="form" v-model="valid" lazy-validation @submit.prevent v-if="modelLoaded">
<#if config.client.generateTabs>
			<v-tabs v-model="tabmodel" class="ml-3 mb-3" slider-color="primary">
				<v-tab :key="0" :href="'#maintab'">{{ $t('${config.modelName}.tabs.maintab') }}</v-tab>
		<#assign tabNum = 0>
		<#if config.client.generateDetailsTab>
			<#list 1..config.client.numberOfDetailsTab as tab>
				<#assign tabNum = tabNum + 1>
				<v-tab :key="${tabNum}" :href="'#detailmodelname_${tabNum}'" v-if="!isCreatingElement && !isModalDialog">{{ $t('${config.modelName}.tabs.detail_${tabNum}') }}</v-tab>  <!-- change detailmodelname_${tabNum} -->
			</#list>
		</#if>
		<#if config.client.generateEmptyTabs>
			<#list 1..config.client.numberOfEmptyTabs as tab>
				<#assign tabNum = tabNum + 1>
				<v-tab :key="${tabNum}" :href="'#tab_empty_${tabNum}'" v-if="!isCreatingElement && !isModalDialog">{{ $t('${config.modelName}.tabs.empty_${tabNum}') }}</v-tab>
			</#list>
		</#if>
		<#if config.client.generateDocumentsTab>
				<#assign tabNum = tabNum + 1>
				<v-tab :key="${tabNum}" :href="'#tab_documents'" v-if="!isCreatingElement && !isModalDialog">{{ $t('pui9.documents.tab') }}</v-tab>
		</#if>
			</v-tabs>
			<v-tabs-items v-model="tabmodel">
				<v-tab-item :key="0" :value="'maintab'">
<#else>
			<v-row class="pui-form-layout">
				<v-col cols="12">
</#if>
<#list config.selectedTable.columns as column>
	<#if !column.clientInForm>
		<#continue>
	</#if>
	<#switch column.clientControlType>
		<#case "CHECKBOX">
			<#assign checkbox = column.controlConfiguration>
			<#include "/client/controls/Checkbox.ftl">
			<#break>
		<#case "CODE_EDITOR">
			<#assign codeEditor = column.controlConfiguration>
			<#include "/client/controls/CodeEditor.ftl">
			<#break>
		<#case "DATE_TIME">
			<#assign dateTime = column.controlConfiguration>
			<#include "/client/controls/DateTime.ftl">
			<#break>
		<#case "LABEL">
			<#assign label = column.controlConfiguration>
			<#include "/client/controls/Label.ftl">
			<#break>
		<#case "MULTI_SELECT">
			<#assign multiSelect = column.controlConfiguration>
			<#include "/client/controls/MultiSelect.ftl">
			<#break>
		<#case "NUMBER">
			<#assign number = column.controlConfiguration>
			<#include "/client/controls/Number.ftl">
			<#break>
		<#case "PASSWORD">
			<#assign password = column.controlConfiguration>
			<#include "/client/controls/Password.ftl">
			<#break>
		<#case "RADIO_GROUP">
			<#assign radioGroup = column.controlConfiguration>
			<#include "/client/controls/RadioGroup.ftl">
			<#break>
		<#case "RICH_TEXT_EDITOR">
			<#assign richTextEditor = column.controlConfiguration>
			<#include "/client/controls/RichTextEditor.ftl">
			<#break>
		<#case "SELECT">
			<#assign select = column.controlConfiguration>
			<#include "/client/controls/Select.ftl">
			<#break>
		<#case "SPINNER">
			<#assign spinner = column.controlConfiguration>
			<#include "/client/controls/Spinner.ftl">
			<#break>
		<#case "SWITCH">
			<#assign switch = column.controlConfiguration>
			<#include "/client/controls/Switch.ftl">
			<#break>
		<#case "TEXT">
			<#assign text = column.controlConfiguration>
			<#assign fromModal = false>
			<#include "/client/controls/Text.ftl">
			<#break>
		<#case "TEXT_AREA">
			<#assign textArea = column.controlConfiguration>
			<#include "/client/controls/TextArea.ftl">
			<#break>
	</#switch>
</#list>
<#if config.client.generateMiniAudit>
				<!-- MINI AUDIT -->
				<pui-form-mini-audit class="pl-2" :model="model" v-if="!isCreatingElement"></pui-form-mini-audit>
</#if>
<#if config.client.generateTabs>
				</v-tab-item>
	<#assign tabNum = 0>
	<#if config.client.generateDetailsTab>
		<#list 1..config.client.numberOfDetailsTab as tab>
			<#assign tabNum = tabNum + 1>
				<v-tab-item :key="${tabNum}" :value="'detailmodelname_${tabNum}'" v-if="!isCreatingElement && !isModalDialog"> <!-- change detailmodelname_${tabNum} -->
					<pui-master-detail
						componentName="detailgridcomponentname_grid"
						parentModelName="modelName"
						:parentPk="pk"
						:parentPkChildFk="{
							<#list config.selectedTable.primaryKeys as column>
								${column.javaName}: 'referencedColumn'<#if column?has_next>,</#if>
							</#list>
						}"
						:formDisabled="formDisabled"
					></pui-master-detail>
				</v-tab-item>
		</#list>
	</#if>
	<#if config.client.generateEmptyTabs>
		<#list 1..config.client.numberOfEmptyTabs as tab>
			<#assign tabNum = tabNum + 1>
				<v-tab-item :key="${tabNum}" :value="'tab_empty_${tabNum}'" v-if="!isCreatingElement && !isModalDialog">
				</v-tab-item>
		</#list>
	</#if>
	<#if config.client.generateDocumentsTab>
			<#assign tabNum = tabNum + 1>
				<v-tab-item :key="${tabNum}" :value="'tab_documents'" v-if="showDocuments && !isCreatingElement">
					<pui-master-detail componentName="PuiDocumentGrid" :parentModelName="modelName" :parentPk="pk"></pui-master-detail>
				</v-tab-item>
	</#if>
			</v-tabs-items>
<#else>
				</v-col>
			</v-row>
</#if>

			<!-- Page Footer -->
			<pui-form-footer v-if="!isModalDialog">
				<pui-form-footer-btns
					:formDisabled="formDisabled"
					:saveDisabled="saving"
					:saveAndNew="saveAndNew"
					:saveAndUpdate="saveAndUpdate"
					:save="save"
					:back="back"
				></pui-form-footer-btns>
			</pui-form-footer>
		</v-form>
		<pui-form-loading
			v-else
		></pui-form-loading>
	</div>
</template>

<script>
import PuiFormMethodsMixin from 'pui9-mixins/PuiFormMethodsMixin';
<#if config.client.generateActions>
import ${config.modelName}Actions from './${config.modelName?cap_first}Actions';
</#if>
<#if config.client.generateModals>
import ${config.modelName}Modals from './${config.modelName?cap_first}Modals.vue';
</#if>

export default {
	name: '${config.modelName}-form',
	mixins: [PuiFormMethodsMixin],
	components: {
<#if config.client.generateModals>
		'${config.modelName}-modals': ${config.modelName}Modals
</#if>
	},
	data() {
		return {
			modelName: '${config.modelName}'
			<#if config.client.detailGrid>
			, parentModelName: '[PARENT_MODEL_NAME]';
			</#if>
			<#if config.client.generateTabs>
			, tabmodel: 'maintab'
			</#if>
			<#if config.client.generateActions>
			, actions: ${config.modelName}Actions.formActions
			</#if>
			<#list config.selectedTable.getColumnsOfType("RADIO_GROUP") as column>
				<#assign radioGroup = column.controlConfiguration>
			, ${column.javaName}RadioItems: [
				<#list radioGroup.items as radioItem>
				{
					id: "${radioItem.id}",
					label: this.$t('${config.modelName}.${column.javaName}.radioItem.${radioItem.label}')
				}<#if radioItem?has_next>,</#if>
				</#list>
			]
			</#list>
			<#list config.selectedTable.getColumnsOfType("SELECT") as column>
				<#assign select = column.controlConfiguration>
				<#if select.resultsFromRequest?? && !select.resultsFromRequest>
			, select${column.javaName?cap_first}Items: []
				</#if>
			</#list>
			<#if config.selectedTable.getColumnsOfType("TEXT")?filter(column -> column.controlConfiguration.isPassword)?has_content>
			, hidePass: false
			</#if>
		};
	},
	methods: {
		afterGetData() {
			// Do something after get data from server
			<#if config.client.detailGrid>
			this.setParentPk();
			</#if>
		}
		<#if config.client.detailGrid>
		, setParentPk() {
			const parentModel = this.$puiUtils.getPathModelMethodPk(this.$router, this.parentModelName);
			if (parentModel) {
				this.model.[LOCAL_FK_ATTRIBUTE] = parentModel.pk.[REMOTE_FK_ATTRIBUTE];
			}
		}
		</#if>
	},
	computed: {
		<#assign methodsCreated = false>
		<#list config.selectedTable.getColumnsOfType("MULTI_SELECT") as column>
			<#assign multiSelect = column.controlConfiguration>
		<#if methodsCreated>, </#if>get${column.javaName?cap_first}AllItems() {
			return this.$store.state.${config.modelName} && this.$store.state.${config.modelName}.${multiSelect.itemsModel} ? this.$store.state.${config.modelName}.${multiSelect.itemsModel} : [];
		}
			<#assign methodsCreated = true>
		</#list>
		<#if config.selectedTable.getColumnsOfType("TEXT")?filter(column -> column.controlConfiguration.isPassword)?has_content>
		<#if methodsCreated>, </#if>isLdapActive() {
			return this.$store.getters.isLdapActive;
		}
			<#assign methodsCreated = true>
		</#if>
		<#list config.selectedTable.getColumnsOfType("SELECT") as column>
			<#assign select = column.controlConfiguration>
			<#if select.multiple?? && !select.multiple>
		<#if methodsCreated>, </#if>${column.javaName}ItemsToSelect() {
			return [{ ${select.referencedAttribute}: this.model.${column.javaName} }];
		}
				<#assign methodsCreated = true>
			</#if>
		</#list>
	},
	created() {
		<#list config.selectedTable.getColumnsOfType("MULTI_SELECT") as column>
			<#assign multiSelect = column.controlConfiguration>
		this.$store.dispatch('puiMultiSelectGetAllItems', { model: '${config.modelName}', requestModel: '${multiSelect.itemsModel}', vue: this });
		</#list>
	}
}
</script>
