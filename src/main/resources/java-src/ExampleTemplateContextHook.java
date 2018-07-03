package io.stallion.starter.examples;

import io.stallion.templating.TemplateContextHookHandler;

import java.util.Map;

import static io.stallion.utils.Literals.*;


public class ExampleTemplateContextHook extends TemplateContextHookHandler {
    @Override
    public void handle(Map<String, Object> obj) {
        obj.put("exampleTemplateFoo", "zing zong an example variable in every template.");
    }
}
