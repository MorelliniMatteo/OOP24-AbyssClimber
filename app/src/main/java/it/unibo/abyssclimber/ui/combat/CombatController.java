package it.unibo.abyssclimber.ui.combat;

import java.util.List;

import it.unibo.abyssclimber.core.combat.Combat;
import it.unibo.abyssclimber.core.combat.MoveLoader.Move;
import it.unibo.abyssclimber.model.Player;
import it.unibo.abyssclimber.model.Tipo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.TextFlow;

public class CombatController {
    @FXML private Button move1Button;
    @FXML private Button move2Button;
    @FXML private Button move3Button;
    @FXML private Button move4Button;
    @FXML private Button move5Button;
    @FXML private Button move6Button;
    @FXML private TextFlow logFlow;

    private List<Button> buttonList = List.of(move1Button, move2Button, move3Button, move4Button, move5Button, move6Button);
    private Player player;
    private Combat combat;
    //TODO: fix
    @SuppressWarnings("unused")
    private void initialize() {
        enableMoveButtons();
        /*this.player = ;
        setMoveButton(player);
        combat = new Combat(, );*/
    }

    private void applyTipoStyle(Button b, Tipo tipo){
        b.getStyleClass().removeIf(s -> s.startsWith("tipo-"));
        b.getStyleClass().add("tipo-" + tipo.name().toLowerCase());
    }

    private void setMoveButton (Player player){
        for ( int i = 0; i < player.getSelectedMoves().size(); i++ ) {
            Button b = buttonList.get(i);
            Move mv = player.getSelectedMoves().get(i);
            b.setText(mv.getName());
            b.setUserData(mv);
            applyTipoStyle(b, mv.getElement());
        }
    }

    @FXML
    private void onMovePressed(ActionEvent e){
        Button clicked = (Button) e.getSource();
        Move move = (Move) clicked.getUserData();
        disableMoveButtons();
        combat.fight(move);
        enableMoveButtons();
    }

    private void disableMoveButtons() {
        buttonList.forEach(b -> b.setDisable(true));
    }

    private void enableMoveButtons() {
        buttonList.forEach(b -> b.setDisable(false));
    }
}
