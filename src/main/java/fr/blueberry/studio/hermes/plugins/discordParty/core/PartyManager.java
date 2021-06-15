package fr.blueberry.studio.hermes.plugins.discordParty.core;

import java.util.HashSet;
import java.util.Set;

public class PartyManager {
    private final Set<Party> parties;

    public PartyManager() {
        this.parties = new HashSet<Party>();
    }

    public void addParty(Party party) {
        this.parties.add(party);
    }

    public void removeParty(Party party) {
        this.parties.remove(party);
    }

    public boolean hasParty(Party party) {
        return this.parties.contains(party);
    }

    public Party getPartyById(long messageId) {
        return this.parties.stream().parallel().filter(p -> p.getMessageId() == messageId).findFirst().get();
    }

    public Set<Party> getParties() {
        return parties;
    }
}
