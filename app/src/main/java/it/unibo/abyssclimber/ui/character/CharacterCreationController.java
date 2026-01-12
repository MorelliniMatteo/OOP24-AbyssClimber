package it.unibo.abyssclimber.ui.character;

import it.unibo.abyssclimber.core.GameState;
import it.unibo.abyssclimber.core.SceneId;
import it.unibo.abyssclimber.core.SceneRouter;
import it.unibo.abyssclimber.model.Classe;
import it.unibo.abyssclimber.model.Tipo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

/**
 * Controller for the character creation screen.
 * Handles element and class selection and confirms player creation.
 */
public class CharacterCreationController {

    @FXML private Label summaryLabel;

    // Element selection buttons
    @FXML private ToggleButton hydroBtn;
    @FXML private ToggleButton natureBtn;
    @FXML private ToggleButton thunderBtn;
    @FXML private ToggleButton fireBtn;

    // Class selection buttons
    @FXML private ToggleButton mageBtn;
    @FXML private ToggleButton soldierBtn;
    @FXML private ToggleButton knightBtn;

    // Toggle groups to enforce single selection
    private final ToggleGroup elementGroup = new ToggleGroup();
    private final ToggleGroup classGroup = new ToggleGroup();

    @FXML
    private void initialize() {
        // Configure element toggles
        configureElementToggle(hydroBtn, Tipo.HYDRO);
        configureElementToggle(natureBtn, Tipo.NATURE);
        configureElementToggle(thunderBtn, Tipo.LIGHTNING);
        configureElementToggle(fireBtn, Tipo.FIRE);

        // Configure class toggles
        configureClassToggle(mageBtn, Classe.MAGO);
        configureClassToggle(soldierBtn, Classe.SOLDATO);
        configureClassToggle(knightBtn, Classe.CAVALIERE);

        // Update summary whenever a selection changes
        elementGroup.selectedToggleProperty().addListener((obs, o, n) -> updateSummary());
        classGroup.selectedToggleProperty().addListener((obs, o, n) -> updateSummary());

        updateSummary();
    }
    
    // Assigns an element type to a toggle button
    private void configureElementToggle(ToggleButton button, Tipo tipo) {
        button.setToggleGroup(elementGroup);
        button.setUserData(tipo);
    }

    // Assigns a class to a toggle button
    private void configureClassToggle(ToggleButton button, Classe classe) {
        button.setToggleGroup(classGroup);
        button.setUserData(classe);
    }

    // Updates the summary label based on current selections
    private void updateSummary() {
        Tipo el = elementGroup.getSelectedToggle() != null
            ? (Tipo) elementGroup.getSelectedToggle().getUserData()
            : null;
        Classe cl = classGroup.getSelectedToggle() != null
            ? (Classe) classGroup.getSelectedToggle().getUserData()
            : null;

        String elLabel = el != null ? el.displayName() : "—";
        String clLabel = cl != null ? cl.getName() : "—";
        summaryLabel.setText("Tipo: " + elLabel + " | Classe: " + clLabel);
    }

    // Returns to the main menu
    @FXML
    private void onBack() {
        SceneRouter.goTo(SceneId.MAIN_MENU);
    }

    // Confirms the selection and initializes the player
    @FXML
    private void onConfirm() {
        Tipo chosenTipo = elementGroup.getSelectedToggle() != null
            ? (Tipo) elementGroup.getSelectedToggle().getUserData()
            : null;

        Classe chosenClasse = classGroup.getSelectedToggle() != null
            ? (Classe) classGroup.getSelectedToggle().getUserData()
            : null;

        // Prevent confirmation if selections are incomplete
        if (chosenTipo == null || chosenClasse == null) {
            System.err.println("Select both Element and Class before confirming.");
            return;
        }

        // Initialize player and move to the next scene
        GameState.get().initializePlayer("Hero", chosenTipo, chosenClasse);
        SceneRouter.goTo(SceneId.MOVE_SELECTION);
    }
}
