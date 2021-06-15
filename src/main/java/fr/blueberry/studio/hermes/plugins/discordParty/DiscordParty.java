package fr.blueberry.studio.hermes.plugins.discordParty;

import fr.blueberry.studio.hermes.api.plugins.Plugin;
import fr.blueberry.studio.hermes.plugins.discordParty.commands.CommandParty;
import fr.blueberry.studio.hermes.plugins.discordParty.core.PartyManager;
import fr.blueberry.studio.hermes.plugins.discordParty.listeners.discord.GuildReactionAddListener;
import fr.blueberry.studio.hermes.plugins.discordParty.listeners.discord.GuildReactionRemoveListener;

public class DiscordParty extends Plugin {
    public static DiscordParty INSTANCE;

    private PartyManager partyManager;

    @Override
    public void onLoad() {
        INSTANCE = this;

        this.partyManager = new PartyManager();
    }

    @Override
    public void onEnable() {
        getCommandRegistry().registerCommand(new CommandParty(), this);

        getHermes().getBotManager().getJDAListenerManager().registerJDAListener(new GuildReactionAddListener());
        getHermes().getBotManager().getJDAListenerManager().registerJDAListener(new GuildReactionRemoveListener());
    }

    @Override
    public void onDisable() {
    
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

    //TODO

    // Sauvegarde en YML
    // Worker qui ping
    // Owner peut éditer la description
    // Owner peut kick un membre 
    // User leave le discord 
}
