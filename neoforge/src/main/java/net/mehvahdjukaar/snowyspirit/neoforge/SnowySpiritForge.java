package net.mehvahdjukaar.snowyspirit.neoforge;

import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.snowyspirit.SnowySpirit;
import net.mehvahdjukaar.snowyspirit.common.wreath.ServerEvents;
import net.mehvahdjukaar.snowyspirit.reg.ModRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Author: MehVahdJukaar
 */
@Mod(SnowySpirit.MOD_ID)
public class SnowySpiritForge {

    public SnowySpiritForge(IEventBus busEvent) {

        SnowySpirit.commonInit();

        if (PlatHelper.getPhysicalSide().isClient()) {
            ClientHelper.addClientSetup(() -> {
                if (ModList.get().isLoaded("configured")) {
                    //TODO: add back
                    ModConfigSelectScreen.registerConfigScreen(SnowySpirit.MOD_ID, ModConfigSelectScreen::new);
                }
            });
        }

        NeoForge.EVENT_BUS.register(this);
        PlatHelper.addCommonSetup(() -> {
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(Utils.getID(ModRegistry.GINGER.get()), ModRegistry.GINGER_POT);
        });

    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        InteractionResult res = ServerEvents.onRightClickBlock(event.getEntity(), event.getLevel(), event.getItemStack(), event.getPos());
        if (res != InteractionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(res);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void tickEvent(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.level instanceof ServerLevel level) {
            ServerEvents.tickEvent(level);
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerEvents.onPlayerLogin(event.getEntity());
    }

    @SubscribeEvent
    public void onDimensionChanged(PlayerEvent.PlayerChangedDimensionEvent event) {
        ServerEvents.onDimensionChanged(event.getEntity());
    }
}