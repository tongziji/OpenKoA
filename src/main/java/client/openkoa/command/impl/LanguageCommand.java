package client.openkoa.command.impl;

import client.openkoa.OpenKoAClient;
import client.openkoa.command.Command;
import client.openkoa.command.impl.LanguageCommand.EventHandler;

public class LanguageCommand
extends Command {
    public static final class EventHandler {
        private final LanguageCommand parent;

        public EventHandler(LanguageCommand parent) {
            this.parent = parent;
        }
    }

    public LanguageCommand() {
        super("language", new String[]{"lang"});
    }

    @Override
    public void onCommand(String[] stringArray) {
        OpenKoAClient.getInstance().getEventBus().register(new LanguageCommand.EventHandler(this));
    }

    @Override
    public String[] onTab(String[] stringArray) {
        return new String[0];
    }
}