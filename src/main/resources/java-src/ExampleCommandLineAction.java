package io.stallion.starter.examples;


import io.stallion.boot.AppContextLoader;
import io.stallion.boot.CommandOptionsBase;
import io.stallion.boot.StallionRunAction;

public class ExampleCommandLineAction implements StallionRunAction<CommandOptionsBase>

    {
        @Override
        public String getActionName() {
            return "my-action";
        }

        @Override
        public String getHelp() {
            return "Run my-action";
        }

        @Override
        public void loadApp(CommandOptionsBase options) {
            //options.setLightweightMode(true);
            AppContextLoader.loadCompletely(options);
        }

        @Override
        public void execute(CommandOptionsBase options) throws Exception {
            // Run your command line action here.
        }

}