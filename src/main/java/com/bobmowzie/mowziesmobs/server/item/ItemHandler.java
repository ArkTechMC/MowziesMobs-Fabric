package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityDart;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemHandler {
    public static final List<Item> ITEMS = new ArrayList<>();
    public static final ItemFoliaathSeed FOLIAATH_SEED = register("foliaath_seed", new ItemFoliaathSeed(new Item.Settings()));
    public static final ItemMobRemover MOB_REMOVER = register("mob_remover", new ItemMobRemover(new Item.Settings()));
    public static final ItemWroughtAxe WROUGHT_AXE = register("wrought_axe", new ItemWroughtAxe(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final ItemWroughtHelm WROUGHT_HELMET = register("wrought_helmet", new ItemWroughtHelm(new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_FURY = register("umvuthana_mask_fury", new ItemUmvuthanaMask(MaskType.FURY, new Item.Settings()));
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_FEAR = register("umvuthana_mask_fear", new ItemUmvuthanaMask(MaskType.FEAR, new Item.Settings()));
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_RAGE = register("umvuthana_mask_rage", new ItemUmvuthanaMask(MaskType.RAGE, new Item.Settings()));
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_BLISS = register("umvuthana_mask_bliss", new ItemUmvuthanaMask(MaskType.BLISS, new Item.Settings()));
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_MISERY = register("umvuthana_mask_misery", new ItemUmvuthanaMask(MaskType.MISERY, new Item.Settings()));
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_FAITH = register("umvuthana_mask_faith", new ItemUmvuthanaMask(MaskType.FAITH, new Item.Settings()));
    public static final ItemSolVisage SOL_VISAGE = register("sol_visage", new ItemSolVisage(new Item.Settings().rarity(Rarity.RARE)));
    public static final ItemDart DART = register("dart", new ItemDart(new Item.Settings()));
    public static final ItemSpear SPEAR = register("spear", new ItemSpear(new Item.Settings().maxCount(1)));
    public static final ItemBlowgun BLOWGUN = register("blowgun", new ItemBlowgun(new Item.Settings().maxCount(1).maxDamage(300)));
    public static final ItemGrantSunsBlessing GRANT_SUNS_BLESSING = register("grant_suns_blessing", new ItemGrantSunsBlessing(new Item.Settings().maxCount(1).rarity(Rarity.EPIC)));
    public static final ItemIceCrystal ICE_CRYSTAL = register("ice_crystal", new ItemIceCrystal(new Item.Settings().maxDamageIfAbsent(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durabilityValue).rarity(Rarity.RARE)));
    public static final ItemCapturedGrottol CAPTURED_GROTTOL = register("captured_grottol", new ItemCapturedGrottol(new Item.Settings().maxCount(1)));
    public static final ItemGlowingJelly GLOWING_JELLY = register("glowing_jelly", new ItemGlowingJelly(new Item.Settings().food(ItemGlowingJelly.GLOWING_JELLY_FOOD)));
    public static final ItemNagaFang NAGA_FANG = register("naga_fang", new ItemNagaFang(new Item.Settings()));
    public static final ItemNagaFangDagger NAGA_FANG_DAGGER = register("naga_fang_dagger", new ItemNagaFangDagger(new Item.Settings()));
    public static final ItemEarthrendGauntlet EARTHREND_GAUNTLET = register("earthrend_gauntlet", new ItemEarthrendGauntlet(new Item.Settings().maxDamageIfAbsent(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.durabilityValue).rarity(Rarity.RARE)));
    public static final ItemSculptorStaff SCULPTOR_STAFF = register("sculptor_staff", new ItemSculptorStaff(new Item.Settings().maxDamageIfAbsent(1000).rarity(Rarity.RARE)));
//    public static final ItemSandRake SAND_RAKE = register("sand_rake", new ItemSandRake(new Item.Settings().maxDamageIfAbsent(64)));
    public static final Item LOGO = register("logo", new Item(new Item.Settings()));
    public static final MusicDiscItem PETIOLE_MUSIC_DISC = register("music_disc_petiole", new MusicDiscItem(14, MMSounds.MUSIC_PETIOLE, new Item.Settings().maxCount(1).rarity(Rarity.RARE), 2800));
    public static final SpawnEggItem FOLIAATH_SPAWN_EGG = register("foliaath_spawn_egg", new SpawnEggItem(EntityHandler.FOLIAATH, 0x47CC3B, 0xC03BCC, new Item.Settings()));
    public static final SpawnEggItem WROUGHTNAUT_SPAWN_EGG = register("wroughtnaut_spawn_egg", new SpawnEggItem(EntityHandler.WROUGHTNAUT, 0x8C8C8C, 0xFFFFFF, new Item.Settings()));
    public static final SpawnEggItem UMVUTHANA_SPAWN_EGG = register("umvuthana_spawn_egg", new SpawnEggItem(EntityHandler.UMVUTHANA_MINION, 0xba5f1e, 0x3a2f2f, new Item.Settings()));
    public static final SpawnEggItem UMVUTHANA_RAPTOR_SPAWN_EGG = register("umvuthana_raptor_spawn_egg", new SpawnEggItem(EntityHandler.UMVUTHANA_RAPTOR, 0xba5f1e, 0xf6f2f1, new Item.Settings()));
    public static final SpawnEggItem UMVUTHANA_CRANE_SPAWN_EGG = register("umvuthana_crane_spawn_egg", new SpawnEggItem(EntityHandler.UMVUTHANA_CRANE, 0xba5f1e, 0xfddc76, new Item.Settings()));
    public static final SpawnEggItem UMVUTHI_SPAWN_EGG = register("umvuthi_spawn_egg", new SpawnEggItem(EntityHandler.UMVUTHI, 0xf6f2f1, 0xba5f1e, new Item.Settings()));
    public static final SpawnEggItem FROSTMAW_SPAWN_EGG = register("frostmaw_spawn_egg", new SpawnEggItem(EntityHandler.FROSTMAW, 0xf7faff, 0xafcdff, new Item.Settings()));
    public static final SpawnEggItem GROTTOL_SPAWN_EGG = register("grottol_spawn_egg", new SpawnEggItem(EntityHandler.GROTTOL, 0x777777, 0xbce0ff, new Item.Settings()));
    public static final SpawnEggItem LANTERN_SPAWN_EGG = register("lantern_spawn_egg", new SpawnEggItem(EntityHandler.LANTERN, 0x6dea00, 0x235a10, new Item.Settings()));
    public static final SpawnEggItem NAGA_SPAWN_EGG = register("naga_spawn_egg", new SpawnEggItem(EntityHandler.NAGA, 0x154850, 0x8dd759, new Item.Settings()));
    public static final SpawnEggItem SCULPTOR_SPAWN_EGG = register("sculptor_spawn_egg", new SpawnEggItem(EntityHandler.SCULPTOR, 0xc4a137, 0xfff5e7, new Item.Settings()));
    public static Style TOOLTIP_STYLE = Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY));

    private static <T extends Item> T register(String name, T item) {
        ITEMS.add(item);
        return Registry.register(Registries.ITEM, new Identifier(MowziesMobs.MODID, name), item);
    }

    public static void init() {
        initializeAttributes();
        initializeDispenserBehaviors();
    }

    public static void initializeAttributes() {
        WROUGHT_AXE.getAttributesFromConfig();
        WROUGHT_HELMET.getAttributesFromConfig();
        UMVUTHANA_MASK_FURY.getAttributesFromConfig();
        UMVUTHANA_MASK_FEAR.getAttributesFromConfig();
        UMVUTHANA_MASK_RAGE.getAttributesFromConfig();
        UMVUTHANA_MASK_BLISS.getAttributesFromConfig();
        UMVUTHANA_MASK_MISERY.getAttributesFromConfig();
        UMVUTHANA_MASK_FAITH.getAttributesFromConfig();
        SOL_VISAGE.getAttributesFromConfig();
        SPEAR.getAttributesFromConfig();
        NAGA_FANG_DAGGER.getAttributesFromConfig();
        EARTHREND_GAUNTLET.getAttributesFromConfig();
    }

    public static void initializeDispenserBehaviors() {
        DispenserBlock.registerBehavior(DART, new ProjectileDispenserBehavior() {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected ProjectileEntity createProjectile(World worldIn, Position position, ItemStack stackIn) {
                EntityDart dartentity = new EntityDart(EntityHandler.DART, worldIn, position.getX(), position.getY(), position.getZ());
                dartentity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return dartentity;
            }
        });
    }
}