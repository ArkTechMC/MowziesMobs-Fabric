package com.bobmowzie.mowziesmobs.server.advancement;

import net.minecraft.advancement.criterion.Criteria;

public class AdvancementHandler {
    public static final StealIceCrystalTrigger STEAL_ICE_CRYSTAL_TRIGGER = Criteria.register(new StealIceCrystalTrigger());
    public static final GrottolKillFortuneTrigger GROTTOL_KILL_FORTUNE_TRIGGER = Criteria.register(new GrottolKillFortuneTrigger());
    public static final GrottolKillSilkTouchTrigger GROTTOL_KILL_SILK_TOUCH_TRIGGER = Criteria.register(new GrottolKillSilkTouchTrigger());
    public static final SneakGroveTrigger SNEAK_VILLAGE_TRIGGER = Criteria.register(new SneakGroveTrigger());

    public static void preInit() {
    }
}