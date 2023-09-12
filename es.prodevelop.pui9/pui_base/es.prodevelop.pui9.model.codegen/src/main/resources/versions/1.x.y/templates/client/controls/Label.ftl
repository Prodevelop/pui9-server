<#-- all used variables are previously created in the context -->
<#-- Start with 5 TAB indent -->
					<v-row dense<#if column.pk> v-if="!isCreatingElement"</#if>>
						<!-- ${column.javaName?upper_case} -->
						<v-col cols="4">
							<pui-field
								:label="$t('${config.modelName}.${column.javaName}')"
								:value="model.${column.javaName}"
							></pui-field>
						</v-col>
					</v-row>
