package it.unibo.abyssclimber.core.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

import it.unibo.abyssclimber.core.combat.MoveLoader.Move;
import it.unibo.abyssclimber.model.Creature;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class Combat {
    private int turn = 0;
    private boolean playerTurn = true;
    private Creature player;
    private Creature monster;
    private Random random = new Random();
    private final CombatLog combatLog = new CombatLog();
    private ArrayList<Move> enemyMoves;

    public Combat(Creature creature1, Creature creature2) {
        this.player = creature1;
        this.monster = creature2;
        this.loadEnemyMove();
    }

    private void loadEnemyMove () {
        TreeSet<MoveLoader.Move> moveSet = new TreeSet<>(Comparator.comparingInt(Move::getCost).thenComparingInt(Move::getId));
        moveSet.add(MoveLoader.moves.get(random.nextInt(8)));
        while ( moveSet.size() < 4) {
            moveSet.add(MoveLoader.moves.get(random.nextInt(32)));
        }
        if ("BOSS".equalsIgnoreCase(monster.getStage())) {
            moveSet.add(MoveLoader.moves.get(33));
        }

        enemyMoves = new ArrayList<>(moveSet);
    }


    private int dmgCalc(MoveLoader.Move attack, Creature attacker, Creature target){
        int miss = 0;
        int dmg = 0;
        double weak = 0;
        weak = ElementUtils.getEffect(attack, target);
        combatLog.logCombat("" + attacker.getName() + " attacks with " + attack.getName() + ".\n", LogType.NORMAL);
        attacker.setSTAM(Math.min(0,attacker.getSTAM() - attack.getCost()));
        miss = random.nextInt(101);
        if (miss > attack.getAcc()) {
            combatLog.logCombat(attacker.getName() + " missed.\n", LogType.NORMAL);
        } else {
            if (attack.getType() == 1){
                dmg = (int) Math.floor(Math.max(0,(attacker.getATK() - target.getDEF()) * weak * (1 + (attack.getPower()/100))));
            } else {
                dmg = (int) Math.floor(Math.max(0,(attacker.getMATK() - target.getMDEF())* weak * (1 + (attack.getPower()/100))));
            }
            if (random.nextInt(101) <= attacker.getCrit()) {
                combatLog.logCombat(attacker.getName() + " scored a critical hit!\n", LogType.CRITICAL);
                dmg = (int) Math.floor(dmg*attacker.getCritDMG());
            }
            target.setHP(target.getHP() - dmg );
            ElementUtils.weakPhrase(weak, combatLog);
            combatLog.logCombat(List.of(
                new BattleText("" + attacker.getName() + " dealt ", LogType.NORMAL),
                new BattleText(String.valueOf(dmg), LogType.DAMAGE),
                new BattleText(" damage.\n", LogType.NORMAL)
            ));
        }
        return dmg;
    }
    
    public void playerTurn(Move move) {
        if ( !playerTurn || move.getCost() > player.getSTAM() || player.getHP() <= 0 || monster.getHP() <= 0) return;

        playerTurn = false; 
        dmgCalc(move, player, monster);
        if (monster.getHP() <= 0) {
            combatLog.logCombat("" + monster.getName() + " died. You win.\n", LogType.NORMAL);
            // TODO: HANDLE WIN CONDITION
            return;
        }
        
        turn++;
        player.setSTAM(Math.min(player.getMaxSTAM(), player.getSTAM() + player.regSTAM())); 
        combatLog.renderLog();
        monsterTurn();
    }

    public void monsterTurn() {
        int choice = 0;
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        playerTurn = true;
        if (monster.getSTAM() >= monster.getMaxSTAM()) {
            dmgCalc(enemyMoves.getLast(), monster, player);
        } else {
            choice = random.nextInt(enemyMoves.size());
            dmgCalc(enemyMoves.get(choice), monster, player);
        }
        
        if (player.getHP() <= 0) {
            combatLog.logCombat("" + player.getName() + " died. You lose.\n", LogType.NORMAL);
            // TODO: handle lose condition
            return;
        }

        monster.setSTAM(Math.min(monster.getMaxSTAM(), monster.getSTAM() + monster.regSTAM()));
    }
    

    public void fight(Move move) {
        combatLog.clearEvents();
        combatLog.logCombat("Turn " + turn + "\n", LogType.NORMAL);
        if ( playerTurn ) {
            playerTurn(move);
        } else {
            return;
        }
        turn++;
    }
}

