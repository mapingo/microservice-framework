package uk.gov.justice.framework.command.client.startup;

import uk.gov.justice.framework.command.client.ReturnCode;

public class Bootstrapper {

    private final ObjectFactory objectFactory;

    public Bootstrapper() {
        this(new ObjectFactory());
    }

    public Bootstrapper(final ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public ReturnCode startContainerAndRun(final String[] args) {
        return objectFactory.mainApplication().run(args);

    }

    public static void main(String[] args) {

        final String command = "PING";
        final String commandRuntimeId = "";
        final String contextName = "people";

        final String userName = "admin";
        final String password = "admin";


        final String[] arguments = {
                "-c", command,
                "-rcid", commandRuntimeId,
                "-u", userName,
                "-pw", password,
                "-cn", contextName
        };

        new Bootstrapper().startContainerAndRun(arguments);
    }
}
