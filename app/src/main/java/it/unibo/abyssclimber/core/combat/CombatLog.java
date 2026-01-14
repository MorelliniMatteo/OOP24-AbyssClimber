package it.unibo.abyssclimber.core.combat;

import java.util.ArrayList;
import java.util.List;

public class CombatLog {

    private final List<List<BattleText>> events = new ArrayList<>();

    public void logCombat(List<BattleText> text) {
        events.add(text);
    }

    public void logCombat(String text, LogType type) {
        events.add(List.of(new BattleText(text, type)));
    }
    
    public List<List<BattleText>> getEvents() {
        return List.copyOf(events);
    }

    public void clearEvents() {
        events.clear();
    }

}
