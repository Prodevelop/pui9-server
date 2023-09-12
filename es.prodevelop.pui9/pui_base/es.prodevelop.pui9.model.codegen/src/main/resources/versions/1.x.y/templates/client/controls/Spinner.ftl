<#-- all used variables are previously created in the context -->
<#-- Start with 5 TAB indent -->
					<v-row dense<#if column.pk> v-if="!isCreatingElement"</#if>>
						<!-- ${column.javaName?upper_case} -->
						<v-col cols="4">
							<pui-spinner-field
								:id="`${column.javaName}-${config.modelName}`"
								v-model="model.${column.javaName}"
								:label="$t('${config.modelName}.${column.javaName}')"
								<#if spinner.disabled??>
									<#if spinner.disabled>
								disabled
									</#if>
								<#else>
								:disabled="formDisabled"
								</#if>
								<#if spinner.editable?? && !spinner.editable>
								noeditable
								</#if>
								<#if spinner.required?? && spinner.required>
								required
								</#if>
								<#if spinner.topLabel?? && spinner.topLabel>
								toplabel
								</#if>
								<#if spinner.min??>
								min="${spinner.min?c}"
								</#if>
								<#if spinner.max??>
								max="${spinner.max?c}"
								</#if>
							></pui-spinner-field>
						</v-col>
					</v-row>
