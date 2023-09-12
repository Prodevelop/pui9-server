<#-- all used variables are previously created in the context -->
<#-- Start with 5 TAB indent -->
					<v-row dense<#if column.pk> v-if="!isCreatingElement"</#if>>
						<!-- ${column.javaName?upper_case} -->
						<v-col cols="4">
							<pui-select
								:id="`${column.javaName}-${config.modelName}`"
								:attach="`${column.javaName}-${config.modelName}`"
								:label="$t('${config.modelName}.${column.javaName}')"
								<#if select.disabled??>
									<#if select.disabled>
								disabled
									</#if>
								<#else>
								:disabled="formDisabled"
								</#if>
								<#if select.editable?? && !select.editable>
								noeditable
								</#if>
								<#if select.required?? && select.required>
								required
								</#if>
								<#if select.topLabel?? && select.topLabel>
								toplabel
								</#if>
								<#if select.clearable?? && select.clearable>
								clearable
								</#if>
								<#if select.readOnly?? && select.readOnly>
								readonly
								</#if>
								<#if select.resultsFromRequest?? && select.resultsFromRequest>
								modelName="${select.modelName}"
								<#else>
								:items="select${column.javaName?cap_first}Items"
								</#if>
								<#if select.multiple?? && select.multiple>
								v-model="model.attribute_list"
								multiple
								:itemsToSelect="model.attribute_list"
								<#else>
								v-model="model"
								reactive
								:itemsToSelect="${column.javaName}ItemsToSelect"
								</#if>
								:modelFormMapping="{ ${select.referencedAttribute}: '${column.javaName}' }"
								:itemValue="['${select.referencedAttribute}']"
								:itemText="(item) => `${r"${"}item.${select.referencedAttribute}${r"}"}`"
								<#if select.detailModelName?? && select.detailModelName?has_content>
								detailModelName="${select.detailModelName}"
								</#if>
								<#if select.detailComponentName?? && select.detailComponentName?has_content>
								detailComponentName="${select.detailComponentName}"
								</#if>
							></pui-select>
						</v-col>
					</v-row>
