package it.unibo.abyssclimber.core;

/**
 * Temporary combat bridge implementation.
 * Replace with real combat system.
 */
public class DefaultCombatBridge implements CombatBridge {

    @Override
    public CombatResult startFight(RoomOption option) {
        System.out.println("DefaultCombatBridge: combat placeholder for " + option.title());
        return CombatResult.WIN;
    }
}
