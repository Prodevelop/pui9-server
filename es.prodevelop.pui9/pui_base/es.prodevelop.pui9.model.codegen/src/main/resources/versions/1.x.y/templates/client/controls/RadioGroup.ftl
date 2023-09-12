<#-- all used variables are previously created in the context -->
<#-- Start with 5 TAB indent -->
					<v-row dense<#if column.pk> v-if="!isCreatingElement"</#if>>
						<!-- ${column.javaName?upper_case} -->
						<v-col cols="4">
							<pui-radio-group
								:id="`${column.javaName}-${config.modelName}`"
								v-model="model.${column.javaName}"
								:label="$t('${config.modelName}.${column.javaName}')"
								<#if radioGroup.disabled??>
									<#if radioGroup.disabled>
								disabled
									</#if>
								<#else>
								:disabled="formDisabled"
								</#if>
								<#if radioGroup.editable?? && !radioGroup.editable>
								noeditable
								</#if>
								<#if radioGroup.required?? && radioGroup.required>
								required
								</#if>
								<#if radioGroup.topLabel?? && radioGroup.topLabel>
								toplabel
								</#if>
								<#if radioGroup.asRow?? && radioGroup.asRow>
								row
								<#else>
								column
								</#if>
								:radios="${column.javaName}RadioItems"
							></pui-radio-group>
						</v-col>
					</v-row>
