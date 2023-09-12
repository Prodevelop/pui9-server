<#-- all used variables are previously created in the context -->
<#-- Start with 5 TAB indent -->
					<v-row dense<#if column.pk> v-if="!isCreatingElement"</#if>>
						<!-- ${column.javaName?upper_case} -->
						<v-col cols="12">
							<pui-field-set title="${codeEditor.mode}">
								<pui-code-editor
									v-model="model.${column.javaName}"
									mode="${codeEditor.mode.value}"
									<#if codeEditor.required?? && codeEditor.required>
									required
									</#if>
									<#if codeEditor.validationErrors??>
										<#if codeEditor.validationErrors>
									validationErrors
										</#if>
									<#else>
									:validationErrors="formValidationErrors"
									</#if>
								></pui-code-editor>
							</pui-field-set>
						</v-col>
					</v-row>
