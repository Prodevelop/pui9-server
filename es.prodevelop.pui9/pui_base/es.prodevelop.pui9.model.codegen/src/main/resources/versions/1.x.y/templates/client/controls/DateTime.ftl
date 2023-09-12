<#-- all used variables are previously created in the context -->
<#-- Start with 5 TAB indent -->
					<v-row dense<#if column.pk> v-if="!isCreatingElement"</#if>>
						<!-- ${column.javaName?upper_case} -->
						<v-col cols="4">
							<pui-date-field
								:id="`${column.javaName}-${config.modelName}`"
								v-model="model.${column.javaName}"
								:label="$t('${config.modelName}.${column.javaName}')"
								<#if dateTime.disabled??>
									<#if dateTime.disabled>
								disabled
									</#if>
								<#else>
								:disabled="formDisabled"
								</#if>
								<#if dateTime.editable?? && !dateTime.editable>
								noeditable
								</#if>
								<#if dateTime.required?? && dateTime.required>
								required
								</#if>
								<#if dateTime.topLabel?? && dateTime.topLabel>
								toplabel
								</#if>
								<#if dateTime.readOnly?? && dateTime.readOnly>
								disableTextInput
								</#if>
								<#if dateTime.today?? && dateTime.today>
								today
								</#if>
								<#if dateTime.withTime?? && dateTime.withTime>
								time
								</#if>
								<#if dateTime.withSeconds?? && dateTime.withSeconds>
								timesecs
								</#if>
								<#if dateTime.dateFormat?? && dateTime.dateFormat?has_content>
								isoDateFormat="${dateTime.dateFormat}"
								</#if>
							></pui-date-field>
						</v-col>
					</v-row>
