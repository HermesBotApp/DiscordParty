package fr.blueberry.studio.hermes.plugins.discordParty.commands;

import java.util.Collections;
import java.util.HashSet;

import fr.blueberry.studio.hermes.api.app.Sender;
import fr.blueberry.studio.hermes.api.bots.Bot;
import fr.blueberry.studio.hermes.api.bots.BotManager;
import fr.blueberry.studio.hermes.api.commands.Command;
import fr.blueberry.studio.hermes.api.utils.CommandHelper;
import fr.blueberry.studio.hermes.api.utils.StringHelper;
import fr.blueberry.studio.hermes.plugins.discordParty.DiscordParty;
import fr.blueberry.studio.hermes.plugins.discordParty.core.Party;
import fr.blueberry.studio.hermes.plugins.discordParty.core.PartyManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandParty extends Command {

    @Override
    public String getLabel() {
        return "party";
    }

    @Override
    public void execute(Sender sender, String[] args) {
        if(args.length > 2) {
            final BotManager botManager = getHermes().getBotManager();
            final Bot bot = botManager.getTriggerer();
            final Guild guild = bot.getGuild();
            final Member member = guild.getMember(sender.getUser());
            final int partyMaxCount = Math.abs(Integer.parseInt(args[0]));
            final String schedule = args[1]; //14:02
            final String content = StringHelper.stringify(2, args);
            final Party party = new Party(partyMaxCount, schedule, content, member, new HashSet<Member>(Collections.singleton(member)));
            final PartyManager partyManager = DiscordParty.INSTANCE.getPartyManager();

            party.sendPartyMessage((TextChannel)sender.getMessageChannel());
            partyManager.addParty(party);
        } else {
            CommandHelper.sendCommandWrongArgs(sender);
        }
    }

    @Override
    public boolean isOpRestricted() {
        return false;
    }

    @Override
    public String getUsage() {
        return """
                    !party <max> <heure:minute> <message> - Créer une party
               """;
    }

    @Override
    public String getDescription() {
        return "Permet de créer des parties de joueurs";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"prty"};
    }
}
