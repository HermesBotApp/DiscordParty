package fr.blueberry.studio.hermes.plugins.discordParty.core;

import java.awt.Color;
import java.util.Set;

import emoji4j.EmojiUtils;
import fr.blueberry.studio.hermes.api.app.Hermes;
import fr.blueberry.studio.hermes.api.bots.Bot;
import fr.blueberry.studio.hermes.api.utils.MessageEmbedHelper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class Party {
    private final String schedule;
    private final Member owner;
    private int maxCount;
    private String description;
    private Set<Member> members;
    private long textChannelId = 0;
    private long messageId = 0;

    public Party(int maxCount, String schedule, String description, Member owner, Set<Member> members) {
        this.schedule = schedule;
        this.maxCount = maxCount;
        this.description = description;
        this.owner = owner;
        this.members = members;
    }

    public void sendPartyMessage(TextChannel textChannel) {
        if(this.messageId == 0) {
            final MessageEmbed embed = craftPartyMessage();

            textChannel.sendMessage(embed).queue(m -> {
                this.messageId = m.getIdLong();
                this.textChannelId = textChannel.getIdLong();
                
                m.addReaction(EmojiUtils.getEmoji(":white_check_mark:").getEmoji()).queue();
            });
        }
        Hermes.getHermes().getLogger().debug("The party is already created.");
    }

    public void addMember(Member member) {
        if(!members.contains(member)) {
            this.members.add(member);
            this.updateEmbed();
        }
    }

    public void removeMember(Member member) {
        if(members.contains(member)) {
            this.members.remove(member);
            this.updateEmbed();
        }
    }

    public void updateEmbed() {
        final Bot bot = Hermes.getHermes().getBotManager().getTriggerer();
        final Guild guild = bot.getGuild();
        final TextChannel textChannel = guild.getTextChannelById(textChannelId);

        if(textChannel != null) {
            textChannel.retrieveMessageById(messageId).queue(m -> {
                final MessageEmbed updatedEmbed = craftPartyMessage();
                
                m.editMessage(updatedEmbed).queue();
            });
        }
    }

    private MessageEmbed craftPartyMessage() {
        final MessageEmbed embed = MessageEmbedHelper.getBuilder()
            .setTitle(owner.getEffectiveName() + " a crée une party ! ("+ this.members.size() + "/" + maxCount +")")
            .setDescription(craftEmbedDescription(description))
            .setColor(new Color(204, 102, 255))
            .setFooter("Par " + owner.getEffectiveName() + "・RDV à " + this.schedule)
            .build();

        return embed;
    }

    private String craftEmbedDescription(String content) {
        final StringBuilder sb = new StringBuilder();

        sb.append(content);
        sb.append("\n \n");

        for(Member member : members) {
            sb.append(":white_small_square: ")
              .append(member.getAsMention())
              .append("\n");
        }

        return sb.toString();
    }
    
    public String getDescription() {
        return description;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public Set<Member> getMembers() {
        return members;
    }

    public String getSchedule() {
        return schedule;
    }

    public Member getOwner() {
        return owner;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getTextChannelId() {
        return textChannelId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public void setMembers(Set<Member> members) {
        this.members = members;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public void setTextChannelId(long textChannelId) {
        this.textChannelId = textChannelId;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj instanceof Party) {
            if(this.messageId == ((Party)obj).messageId)
            {
                return true;
            }
        }
        return super.equals(obj);
    }
}
