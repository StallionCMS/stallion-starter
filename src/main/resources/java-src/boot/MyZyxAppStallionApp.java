package io.stallion.starter.examples.boot;

import io.stallion.hooks.HookRegistry;
import io.stallion.jobs.JobCoordinator;
import io.stallion.plugins.StallionJavaPlugin;
import io.stallion.restfulEndpoints.EndpointResource;
import io.stallion.restfulEndpoints.EndpointsRegistry;
import io.stallion.starter.examples.*;

import java.util.List;

import static io.stallion.utils.Literals.list;

public class MyZyxAppStallionApp extends StallionJavaPlugin {
    @Override
    public String getPluginName() {
        return "myzyxappname";
    }

    @Override
    public void boot() throws Exception {

        // Register models and controllers
        TodoController.register();


        // Register endpoints
        List<EndpointResource> resources = list(
                new TodoApiEndpoints(),
                new RootEndpoints()
        );
        for(EndpointResource r:resources) {
            EndpointsRegistry.instance().addResource("", r);
        }


        // Register Hooks
        HookRegistry.instance().register(new ExampleRequestHook());
        HookRegistry.instance().register(new ExampleTemplateContextHook());


        // Register Jobs
        JobCoordinator.instance().registerJob(new WeeklyTodosJob());


    }
}