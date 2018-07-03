package io.stallion.starter.examples.testing;

import io.stallion.plugins.PluginRegistry;
import io.stallion.starter.examples.boot.MyZyxAppStallionApp;
import io.stallion.testing.AppIntegrationCaseBase;
import org.apache.commons.io.FilenameUtils;
import org.junit.BeforeClass;


public class BaseIntegrationTest extends AppIntegrationCaseBase {


    @BeforeClass
    public static void setUpClass() throws Exception {
        String path = FilenameUtils.concat(System.getProperty("user.dir"), "../site");
        startApp(path);
        MyZyxAppStallionApp booter = new MyZyxAppStallionApp();
        PluginRegistry.instance().loadPluginFromBooter(booter);
        booter.boot();

    }




}