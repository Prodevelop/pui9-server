<#-- all used variables are previously created in the context -->
<#-- Start with 5 TAB indent -->
					<v-row dense<#if column.pk> v-if="!isCreatingElement"</#if>>
						<!-- ${column.javaName?upper_case} -->
						<v-col cols="4">
							<pui-field-set :title="get${column.javaName?cap_first}Title">
								<pui-multi-select
									v-model="model.${column.javaName}"
									<#if multiSelect.disabled??>
										<#if multiSelect.disabled>
									disabled
										</#if>
									<#else>
									:disabled="formDisabled"
									</#if>
									:items="get${column.javaName?cap_first}AllItems"
									:itemValue="['<#if multiSelect.itemValue??>${multiSelect.itemValue}</#if>']"
									itemText="<#if multiSelect.itemText??>${multiSelect.itemText}</#if>"
									itemDescription="<#if multiSelect.itemDescription??>${multiSelect.itemDescription}</#if>"
								></pui-multi-select>
							</pui-field-set>
						</v-col>
					</v-row>
