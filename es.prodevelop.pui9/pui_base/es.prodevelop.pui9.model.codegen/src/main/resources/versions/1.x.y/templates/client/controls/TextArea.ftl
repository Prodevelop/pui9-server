<#-- all used variables are previously created in the context -->
<#-- Start with 5 TAB indent -->
					<v-row dense<#if column.pk> v-if="!isCreatingElement"</#if>>
						<!-- ${column.javaName?upper_case} -->
						<v-col cols="4">
							<pui-text-area
								:id="`${column.javaName}-${config.modelName}`"
								v-model="model.${column.javaName}"
								:label="$t('${config.modelName}.${column.javaName}')"
								<#if textArea.disabled??>
									<#if textArea.disabled>
								disabled
									</#if>
								<#else>
								:disabled="formDisabled"
								</#if>
								<#if textArea.editable?? && !textArea.editable>
								noeditable
								</#if>
								<#if textArea.required?? && textArea.required>
								required
								</#if>
								<#if textArea.topLabel?? && textArea.topLabel>
								toplabel
								</#if>
							></pui-text-area>
						</v-col>
					</v-row>
