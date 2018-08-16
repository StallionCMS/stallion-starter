package io.stallion.starter;

import com.hubspot.jinjava.Jinjava;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        CommandOptions options = new CommandOptions();
        CmdLineParser parser = new CmdLineParser(options);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            throw new RuntimeException(e);
        }
        String targetDir = System.getProperty("user.dir");
        if (options.getTarget() != null && !"".equals(options.getTarget())) {
            targetDir = options.getTarget();
        }


        new App().run(targetDir);

    }

    private String groupId;
    private String artifactId;
    private String packageName;
    private String appName;
    private String appNameCamelCase;
    private String appNameCamelTitleCase;
    private String targetDir = "";

    public void run(String targetDir) {
        this.targetDir = targetDir;

        groupId = new Prompter("Choose a Java group id (eg, com.yourdomain): ")
                .setValidPattern("[\\w\\.]+")
                .setMinLength(5)
                .prompt();
        packageName = new Prompter("Choose a Java base package name (default if blank is " + groupId + "): ")
                .setValidPattern("[\\w\\.]*")
                .prompt();
        if ("".equals(packageName)) {
            packageName = groupId;
        }
        packageName = StringUtils.strip(packageName, ".");
        groupId = StringUtils.strip(groupId, ".");


        artifactId = new Prompter("Choose a Java artifact id (my-app): ")
                .setValidPattern("[\\w\\-]+")
                .setMinLength(5)
                .prompt();

        appName = new Prompter("Choose an internal name for your application that will be used for the base name of various files and locators. No spaces, hyphens and periods are allowed. ")
                .setValidPattern("[\\w\\-\\.]+")
                .setMinLength(5)
                .prompt();
        appNameCamelCase = toCamelCase(appName);
        appNameCamelTitleCase = toCamelCaseUpper(appName);


        copy("/other/pom.xml", "java-app/pom.xml");

        copy("/conf/myzyxappname.toml", "site/conf/$APP_NAME$.toml");
        copy("/conf/stallion.toml", "site/conf/stallion.toml");
        copy("/conf/stallion.qa.toml", "site/conf/stallion.qa.toml");
        copy("/conf/stallion.prod.toml", "site/conf/stallion.prod.toml");

        String packageMainPath = "java-app/src/main/java/" + packageName.replace(".", "/");
        String packageTestPath = "java-app/src/test/java/" + packageName.replace(".", "/");

        copy("/java-src/testing/BaseIntegrationTest.java", packageTestPath + "/BaseIntegrationTest.java");
        copy("/java-src/testing/TestMyApp.java", packageTestPath + "/TestMyApp.java");

        copy("/java-src/boot/MyZyxAppSettings.java", packageMainPath + "/boot/MyZyxAppSettings.java");
        copy("/java-src/boot/Main.java", packageMainPath + "/boot/Main.java");
        copy("/java-src/boot/MyZyxAppStallionApp.java", packageMainPath + "/boot/MyZyxAppStallionApp.java");



        copy("/java-src/TodoApiEndpoints.java", packageMainPath + "/TodoApiEndpoints.java");
        copy("/java-src/BaseEndpoints.java", packageMainPath + "/BaseEndpoints.java");
        copy("/java-src/ExampleCommandLineAction.java", packageMainPath + "/ExampleCommandLineAction.java");
        copy("/java-src/ExampleRequestHook.java", packageMainPath + "/ExampleRequestHook.java");
        copy("/java-src/ExampleTemplateContextHook.java", packageMainPath + "/ExampleTemplateContextHook.java");
        copy("/java-src/RootEndpoints.java", packageMainPath + "/RootEndpoints.java");
        copy("/java-src/ReminderEmailer.java", packageMainPath + "/ReminderEmailer.java");
        copy("/java-src/ReminderEmailScheduledTask.java", packageMainPath + "/ReminderEmailScheduledTask.java");
        copy("/java-src/Todo.java", packageMainPath + "/Todo.java");
        copy("/java-src/TodoController.java", packageMainPath + "/TodoController.java");
        copy("/java-src/WeeklyTodosEmailer.java", packageMainPath + "/WeeklyTodosEmailer.java");
        copy("/java-src/WeeklyTodosJob.java", packageMainPath + "/WeeklyTodosJob.java");






        copy("/assets/app.bundle", "java-app/src/main/resources/assets/app.bundle");
        copy("/assets/styles.scss", "java-app/src/main/resources/assets/styles.scss");
        copy("/assets/js/example1.js", "java-app/src/main/resources/assets/js/example1.js");
        copy("/assets/js/vue-app-booter.js", "java-app/src/main/resources/assets/js/vue-app-booter.js");
        copy("/assets/js/todo-app.vue", "java-app/src/main/resources/assets/js/todo-app.vue");

        copy("/templates/app.jinja", "java-app/src/main/resources/templates/app.jinja");
        copy("/templates/email-base.jinja", "java-app/src/main/resources/templates/email-base.jinja");
        copy("/templates/reminder-email.jinja", "java-app/src/main/resources/templates/reminder-emailer.jinja");
        copy("/templates/weekly-todos-email.jinja", "java-app/src/main/resources/templates/weekly-todos-email.jinja");




        // Your configuration is in conf/stallion.toml
        // - Set up an email server
        // - Set up database server
        // - Set up siteUrl/cdnUrl in stallion.prod.toml and stallion.qa.toml

        // Once you set up a database, use:

        // ./run.sh serve

        // ./run.sh


    }

    private void copy(String source, String dest) {
        dest = replaceAll(dest);
        String text = resourceToString(source);
        while (dest.startsWith("/")) {
            dest = dest.substring(1);
        }
        String destFull = targetDir + "/" + dest;
        try {
            FileUtils.forceMkdir(new File(destFull).getParentFile());
            FileUtils.writeStringToFile(new File(destFull), text, Charset.forName("UTF8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private String resourceToString(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                throw new IOException("Could not find resource for: " + path);
            }
            String s = IOUtils.toString(url, Charset.forName("UTF8"));
            s = replaceAll(s);
            return s;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String replaceAll(String s) {
        s = s.replace("MyApp", appNameCamelTitleCase);
        s = s.replace("MyZyxApp", appNameCamelTitleCase);
        s = s.replace("myzyxappname", appName);
        s = s.replace("myzyxAppName", appNameCamelCase);
        s = s.replace("io.stallion.starter.examples.testing", packageName);
        s = s.replace("io.stallion.starter.examples", packageName);
        s = s.replace("$GROUP_ID$", groupId);
        s = s.replace("$ARTIFACT_ID$", artifactId);
        s = s.replace("$PACKAGE_NAME$", packageName);
        s = s.replace("$APP_NAME$", appName);
        s = s.replace("$APP_TITLE_CAMEL$", appNameCamelTitleCase);
        s = s.replace("$APP_CAMEL$", appNameCamelTitleCase);
        return s;
    }


    public static String toCamelCase(final String init) {
        if (init==null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length()==init.length()))
                ret.append(" ");
        }

        return ret.toString();
    }


    public static String toCamelCaseUpper(final String init) {
        if (init==null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(word.substring(0, 1).toUpperCase());
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length()==init.length()))
                ret.append(" ");
        }

        String s = ret.toString();
        s = ret.substring(0, 1).toUpperCase() + ret.substring(1);

        return s;
    }



}
