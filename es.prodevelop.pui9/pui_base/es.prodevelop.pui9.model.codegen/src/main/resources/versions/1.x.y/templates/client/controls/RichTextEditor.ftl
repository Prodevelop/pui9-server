<#-- all used variables are previously created in the context -->
<#-- Start with 5 TAB indent -->
					<v-row dense<#if column.pk> v-if="!isCreatingElement"</#if>>
						<!-- ${column.javaName?upper_case} -->
						<v-col cols="4">
							<pui-rich-text-editor
								:id="isModalDialog ? `${column.javaName}-${config.modelName}-modal` : `${column.javaName}-${config.modelName}`"
								v-model="model.${column.javaName}"
								:label="$t('${config.modelName}.${column.javaName}')"
								<#if richTextEditor.required?? && richTextEditor.required>
								required
								</#if>
								<#if richTextEditor.validationErrors??>
									<#if richTextEditor.validationErrors>
								validationErrors
									</#if>
								<#else>
								:validationErrors="formValidationErrors"
								</#if>
							></pui-rich-text-editor>
						</v-col>
					</v-row>
