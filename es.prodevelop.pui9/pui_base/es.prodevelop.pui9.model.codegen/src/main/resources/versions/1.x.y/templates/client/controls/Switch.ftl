<#-- all used variables are previously created in the context -->
<#-- Start with 5 TAB indent -->
					<v-row dense<#if column.pk> v-if="!isCreatingElement"</#if>>
						<!-- ${column.javaName?upper_case} -->
						<v-col cols="4">
							<pui-switch
								:id="`${column.javaName}-${config.modelName}`"
								v-model="model.${column.javaName}"
								:label="$t('${config.modelName}.${column.javaName}')"
								<#if switch.disabled??>
									<#if switch.disabled>
								disabled
									</#if>
								<#else>
								:disabled="formDisabled"
								</#if>
								<#if switch.editable?? && !switch.editable>
								noeditable
								</#if>
								<#if switch.required?? && switch.required>
								required
								</#if>
								<#if switch.topLabel?? && switch.topLabel>
								toplabel
								</#if>
								true-value="${switch.trueValue}"
								false-value="${switch.falseValue}"
							></pui-switch>
						</v-col>
					</v-row>
