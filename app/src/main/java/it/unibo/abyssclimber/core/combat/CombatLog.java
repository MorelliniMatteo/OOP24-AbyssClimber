package it.unibo.abyssclimber.core.combat;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class CombatLog {
    @FXML private TextFlow textFlow;

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

    public void renderLog() {
        textFlow.getChildren().clear();

        for (var line : events) {
            for ( BattleText bt : line) {
                Text t = new Text(bt.text());

                switch (bt.type()) {
                    case NORMAL -> t.setFill(Color.WHITE);
                    case DAMAGE -> t.setFill(Color.RED);
                    case CRITICAL -> t.setFill(Color.GOLD);
                }
                textFlow.getChildren().add(t);

            }
        }
    }
}
