<#-- all used variables are previously created in the context -->
<#-- Start with 5 TAB indent -->
					<v-row dense<#if column.pk> v-if="!isCreatingElement"</#if>>
						<!-- ${column.javaName?upper_case} -->
						<v-col cols="4">
							<pui-checkbox
								:id="`${column.javaName}-${config.modelName}`"
								v-model="model.${column.javaName}"
								:label="$t('${config.modelName}.${column.javaName}')"
								<#if checkbox.disabled??>
									<#if checkbox.disabled>
								disabled
									</#if>
								<#else>
								:disabled="formDisabled"
								</#if>
								<#if checkbox.editable?? && !checkbox.editable>
								noeditable
								</#if>
								<#if checkbox.required?? && checkbox.required>
								required
								</#if>
								<#if checkbox.topLabel?? && checkbox.topLabel>
								toplabel
								</#if>
								true-value="${checkbox.trueValue}"
								false-value="${checkbox.falseValue}"
							></pui-checkbox>
						</v-col>
					</v-row>
