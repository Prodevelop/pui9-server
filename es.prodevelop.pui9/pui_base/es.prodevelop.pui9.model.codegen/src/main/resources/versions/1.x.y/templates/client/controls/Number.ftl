<#-- all used variables are previously created in the context -->
<#-- Start with 5 TAB indent -->
					<v-row dense<#if column.pk> v-if="!isCreatingElement"</#if>>
						<!-- ${column.javaName?upper_case} -->
						<v-col cols="4">
							<pui-number-field
								:id="`${column.javaName}-${config.modelName}`"
								v-model="model.${column.javaName}"
								:label="$t('${config.modelName}.${column.javaName}')"
								<#if number.disabled??>
									<#if number.disabled>
								disabled
									</#if>
								<#else>
								:disabled="formDisabled"
								</#if>
								<#if number.editable?? && !number.editable>
								noeditable
								</#if>
								<#if number.required?? && number.required>
								required
								</#if>
								<#if number.topLabel?? && number.topLabel>
								toplabel
								</#if>
								<#if number.integer?? && number.integer>
								integer
								</#if>
								<#if number.decimals??>
								decimals="${number.decimals?c}"
								</#if>
								<#if number.min??>
								min="${number.min?c}"
								</#if>
								<#if number.max??>
								max="${number.max?c}"
								</#if>
							></pui-number-field>
						</v-col>
					</v-row>
