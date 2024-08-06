package com.bobmowzie.mowziesmobs;

import com.chocohead.mm.api.ClassTinkerers;
import com.chocohead.mm.api.EnumAdder;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class ASMHooks implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        EnumAdder adder = ClassTinkerers.enumBuilder("net.minecraft.entity.SpawnRestriction.Location");
        adder.addEnum("MMSPAWN");
        adder.build();
    }
}
