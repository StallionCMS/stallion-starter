package io.stallion.starter;

import org.kohsuke.args4j.Option;


public class CommandOptions {
    @Option(name="-target", usage="The path to your project directory with the settings.toml file in it")
    private String target = "";


    public String getTarget() {
        return target;
    }

    public CommandOptions setTarget(String target) {
        this.target = target;
        return this;
    }
}
