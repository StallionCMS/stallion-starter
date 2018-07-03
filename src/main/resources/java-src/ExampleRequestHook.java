package io.stallion.starter.examples;

import io.stallion.requests.PreRequestHookHandler;
import io.stallion.requests.StRequest;
import io.stallion.requests.StResponse;

import static io.stallion.utils.Literals.*;


public class ExampleRequestHook extends PreRequestHookHandler {
    @Override
    public void handleRequest(StRequest request, StResponse response) {
        request.getItems().put("exampleFoo", "bar");
    }
}
