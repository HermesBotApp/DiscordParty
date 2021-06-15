package fr.blueberry.studio.hermes.plugins.discordParty.listeners.discord;

import emoji4j.EmojiUtils;
import fr.blueberry.studio.hermes.api.app.Hermes;
import fr.blueberry.studio.hermes.plugins.discordParty.DiscordParty;
import fr.blueberry.studio.hermes.plugins.discordParty.core.Party;
import fr.blueberry.studio.hermes.plugins.discordParty.core.PartyManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildReactionAddListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if(event.getUser().isBot()) {
            return;
        }

        final Member member = event.getMember();
        final long messageId = event.getMessageIdLong();
        final MessageReaction.ReactionEmote reactionEmote = event.getReactionEmote();
        final PartyManager partyManager = DiscordParty.INSTANCE.getPartyManager();
        
        if(reactionEmote.isEmoji()) {
            if(reactionEmote.getEmoji().equals(EmojiUtils.getEmoji(":white_check_mark:").getEmoji())) {
                final Party party = partyManager.getPartyById(messageId);

                if(party != null) {
                    party.addMember(member);
                } else {
                    Hermes.getHermes().getLogger().debug("Party for message id " + messageId + " was null. Aborting...");
                }
            }
        }
    }
}
