<#-- all used variables are previously created in the context -->
<#-- Start with 5 TAB indent -->
					<v-row dense<#if text.isPassword?? && text.isPassword> v-if="isCreatingElement && !isLdapActive"</#if>>
						<!-- ${column.javaName?upper_case} -->
						<v-col cols="4">
						<#if fromModal?? && fromModal><v-col cols="12"><#else><v-col cols="4"></#if>
							<#if text.isPassword?? && text.isPassword>
							<input id="fakeusername" style="opacity: 0; position: absolute;" type="text" name="fakeusernameremembered" />
							</#if>
							<pui-text-field
								:id="`${column.javaName}-${config.modelName}`"
								v-model="<#if fromModal?? && fromModal>data.modalData.${column.javaName}<#else>model.${column.javaName}</#if>"
								:label="$t('${config.modelName}.${column.javaName}')"
								<#if text.disabled??>
									<#if text.disabled>
								disabled
									</#if>
								<#else>
								:disabled="formDisabled"
								</#if>
								<#if text.editable?? && !text.editable>
								noeditable
								</#if>
								<#if text.required?? && text.required>
								required
								</#if>
								<#if text.topLabel?? && text.topLabel>
								toplabel
								</#if>
								<#if text.readOnly?? && text.readOnly>
								readonly
								</#if>
								<#if text.maxlength??>
								maxlength="${text.maxlength?c}"
								</#if>
								<#if text.isPassword?? && text.isPassword>
								:append-icon="hidePass ? 'visibility_off' : 'visibility'"
								:append-icon-cb="() => (hidePass = !hidePass)"
								:type="hidePass ? 'text' : 'password'"
								:autocomplete="'new-password'"
								</#if>
							></pui-text-field>
						</v-col>
					</v-row>
