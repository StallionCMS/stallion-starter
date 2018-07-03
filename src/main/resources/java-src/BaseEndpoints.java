package io.stallion.starter.examples;


import io.stallion.requests.IRequest;
import io.stallion.requests.StResponse;
import io.stallion.restfulEndpoints.EndpointResource;
import io.stallion.restfulEndpoints.JavaRestEndpoint;

public class BaseEndpoints implements EndpointResource {
    @Override
    public void preRequest(JavaRestEndpoint endpoint, IRequest request, StResponse response) {
        // Do some processing on every request
    }
}