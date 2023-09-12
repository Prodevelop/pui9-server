package es.prodevelop.codegen.pui9.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import es.prodevelop.codegen.pui9.model.client.CheckboxControlConfiguration;
import es.prodevelop.codegen.pui9.model.client.CodeEditorControlConfiguration;
import es.prodevelop.codegen.pui9.model.client.DateTimeControlConfiguration;
import es.prodevelop.codegen.pui9.model.client.IControlConfiguration;
import es.prodevelop.codegen.pui9.model.client.LabelControlConfiguration;
import es.prodevelop.codegen.pui9.model.client.MultiSelectControlConfiguration;
import es.prodevelop.codegen.pui9.model.client.NumberControlConfiguration;
import es.prodevelop.codegen.pui9.model.client.RadioGroupControlConfiguration;
import es.prodevelop.codegen.pui9.model.client.RichTextEditorControlConfiguration;
import es.prodevelop.codegen.pui9.model.client.SelectControlConfiguration;
import es.prodevelop.codegen.pui9.model.client.SpinnerControlConfiguration;
import es.prodevelop.codegen.pui9.model.client.SwitchControlConfiguration;
import es.prodevelop.codegen.pui9.model.client.TextAreaControlConfiguration;
import es.prodevelop.codegen.pui9.model.client.TextControlConfiguration;

public enum ClientControlType {

	CHECKBOX(CheckboxControlConfiguration.class),

	CODE_EDITOR(CodeEditorControlConfiguration.class),

	DATE_TIME(DateTimeControlConfiguration.class),

	LABEL(LabelControlConfiguration.class),

	MULTI_SELECT(MultiSelectControlConfiguration.class),

	NUMBER(NumberControlConfiguration.class),

	RADIO_GROUP(RadioGroupControlConfiguration.class),

	RICH_TEXT_EDITOR(RichTextEditorControlConfiguration.class),

	SELECT(SelectControlConfiguration.class),

	SPINNER(SpinnerControlConfiguration.class),

	SWITCH(SwitchControlConfiguration.class),

	TEXT(TextControlConfiguration.class),

	TEXT_AREA(TextAreaControlConfiguration.class);

	public final Class<? extends IControlConfiguration> configClass;

	private ClientControlType(Class<? extends IControlConfiguration> configClass) {
		this.configClass = configClass;
	}

	public static String[] asArrayString() {
		List<String> list = Arrays.asList(values()).stream().map(Enum::name).collect(Collectors.toList());
		return list.toArray(new String[0]);
	}

}
