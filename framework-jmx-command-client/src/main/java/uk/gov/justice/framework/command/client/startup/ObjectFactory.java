package uk.gov.justice.framework.command.client.startup;

import uk.gov.justice.framework.command.client.CommandExecutor;
import uk.gov.justice.framework.command.client.CommandRunModeSelector;
import uk.gov.justice.framework.command.client.JmxParametersFactory;
import uk.gov.justice.framework.command.client.MainApplication;
import uk.gov.justice.framework.command.client.ReturnCodeFactory;
import uk.gov.justice.framework.command.client.cdi.producers.OptionsFactory;
import uk.gov.justice.framework.command.client.io.CommandPrinter;
import uk.gov.justice.framework.command.client.io.ToConsolePrinter;
import uk.gov.justice.framework.command.client.jmx.CommandChecker;
import uk.gov.justice.framework.command.client.jmx.CommandPoller;
import uk.gov.justice.framework.command.client.jmx.ListCommandsInvoker;
import uk.gov.justice.framework.command.client.jmx.SystemCommandInvoker;
import uk.gov.justice.framework.command.client.util.Sleeper;
import uk.gov.justice.framework.command.client.util.UtcClock;
import uk.gov.justice.services.jmx.api.name.CommandMBeanNameProvider;
import uk.gov.justice.services.jmx.api.name.ObjectNameFactory;
import uk.gov.justice.services.jmx.system.command.client.SystemCommanderClientFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.ConnectorWrapper;
import uk.gov.justice.services.jmx.system.command.client.connection.EnvironmentFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.JMXConnectorFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.JmxUrlFactory;
import uk.gov.justice.services.jmx.system.command.client.connection.MBeanConnector;
import uk.gov.justice.services.jmx.system.command.client.connection.RemoteMBeanFactory;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.HelpFormatter;

public class ObjectFactory {

    public MainApplication mainApplication() {

        return new MainApplication(
                commandLineArgumentParser(),
                jmxParametersFactory(),
                listCommandsInvoker(),
                optionsFactory(),
                helpFormatter(),
                commandExecutor(),
                returnCodeFactory()
        );
    }

    public CommandLineArgumentParser commandLineArgumentParser() {

        return new CommandLineArgumentParser(toConsolePrinter(), optionsFactory(), basicParser());
    }

    public JmxParametersFactory jmxParametersFactory() {
        return new JmxParametersFactory();
    }

    public ListCommandsInvoker listCommandsInvoker() {
        return new ListCommandsInvoker(systemCommanderClientFactory(), toConsolePrinter());
    }

    public OptionsFactory optionsFactory() {
        return new OptionsFactory();
    }

    public CommandExecutor commandExecutor() {
        return new CommandExecutor(systemCommandInvoker(), commandPrinter(), commandRunModeSelector());
    }

    public HelpFormatter helpFormatter() {
        return new HelpFormatter();
    }

    public ReturnCodeFactory returnCodeFactory() {
        return new ReturnCodeFactory(toConsolePrinter());
    }

    public ToConsolePrinter toConsolePrinter() {
        return new ToConsolePrinter();
    }

    public SystemCommanderClientFactory systemCommanderClientFactory() {
        return new SystemCommanderClientFactory(
                mBeanConnector(),
                jmxConnectorFactory()
        );
    }

    public BasicParser basicParser() {
        return new BasicParser();
    }

    public SystemCommandInvoker systemCommandInvoker() {
        return new SystemCommandInvoker(
                systemCommanderClientFactory(),
                commandPoller(),
                toConsolePrinter()
        );
    }

    public CommandPoller commandPoller() {
        return new CommandPoller(commandChecker(), utcClock(), sleeper(), toConsolePrinter());
    }

    public CommandChecker commandChecker() {
        return new CommandChecker(toConsolePrinter(), utcClock());
    }

    public UtcClock utcClock() {
        return new UtcClock();
    }

    public Sleeper sleeper() {
        return new Sleeper();
    }

    public CommandPrinter commandPrinter() {
        return new CommandPrinter(toConsolePrinter());
    }

    public CommandRunModeSelector commandRunModeSelector() {
        return new CommandRunModeSelector();
    }

    public MBeanConnector mBeanConnector() {
        return new MBeanConnector(
                commandMBeanNameProvider(),
                remoteMBeanFactory());
    }
    public JMXConnectorFactory jmxConnectorFactory() {
        return new JMXConnectorFactory(
                jmxUrlFactory(),
                connectorWrapper(),
                environmentFactory()
        );
    }

    public JmxUrlFactory jmxUrlFactory() {
        return new JmxUrlFactory();
    }

    public ConnectorWrapper connectorWrapper() {
        return new ConnectorWrapper();
    }

    public EnvironmentFactory environmentFactory() {
        return new EnvironmentFactory();
    }

    public CommandMBeanNameProvider commandMBeanNameProvider() {
        return new CommandMBeanNameProvider(objectNameFactory());
    }

    public ObjectNameFactory objectNameFactory() {
        return new ObjectNameFactory();
    }

    public RemoteMBeanFactory remoteMBeanFactory() {
        return new RemoteMBeanFactory();
    }
}
