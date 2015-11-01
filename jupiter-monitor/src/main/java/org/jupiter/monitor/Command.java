package org.jupiter.monitor;

import org.jupiter.common.util.Maps;
import org.jupiter.monitor.handler.*;

import java.util.Map;

import static org.jupiter.monitor.Command.ChildCommand.*;

/**
 * Monitor command.
 *
 * jupiter
 * org.jupiter.monitor
 *
 * @author jiachun.fjc
 */
public enum Command {
    AUTH("Login with password", new AuthHandler()),
    HELP("Help information", new HelpHandler()),
    METRICS("Performance metrics", new MetricsHandler(), REPORT),
    REGISTRY("Registry info(P/S command must follow behind ADDRESS)", new RegistryHandler(), ADDRESS, P, S, BY_SERVICE, BY_ADDRESS, GREP),
    QUIT("Quit monitor", new QuitHandler());

    private final String description;
    private final CommandHandler handler;
    private final ChildCommand[] children;

    Command(String description, CommandHandler handler, ChildCommand... children) {
        this.description = description;
        this.handler = handler;
        this.children = children;
    }

    public String description() {
        return description;
    }

    public CommandHandler handler() {
        return handler;
    }

    public ChildCommand[] children() {
        return children;
    }

    public ChildCommand parseChild(String childName) {
        if (childName.indexOf('-') == 0) {
            childName = childName.substring(1);
        }
        for (ChildCommand c : children()) {
            if (c.name().equalsIgnoreCase(childName)) {
                return c;
            }
        }
        return null;
    }

    public static Command parse(String name) {
        return commands.get(name.toLowerCase());
    }

    private static final Map<String, Command> commands = Maps.newHashMap();
    static {
        for (Command c : Command.values()) {
            commands.put(c.name().toLowerCase(), c);
        }
    }

    public enum ChildCommand {
        REPORT("Report the current values of all metrics in the registry", null),
        ADDRESS("List all publisher/subscriber's addresses", new AddressHandler()),
        BY_SERVICE("List all providers by service name", new ByServiceHandler()),
        BY_ADDRESS("List all services by addresses", new ByAddressHandler()),
        P("Publisher", null),
        S("Subscriber", null),
        GREP("Search for pattern in each line", null);

        private final String description;
        private final CommandHandler handler;

        ChildCommand(String description, CommandHandler handler) {
            this.description = description;
            this.handler = handler;
        }

        public String description() {
            return description;
        }

        public CommandHandler handler() {
            return handler;
        }
    }
}
