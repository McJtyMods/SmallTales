package com.mcjty.smalltales.setup;

import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.modules.story.items.TheStoryItem;
import com.mcjty.smalltales.playerdata.PlayerProperties;
import com.mcjty.smalltales.playerdata.PlayerStory;
import com.mcjty.smalltales.playerdata.PropertiesDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeEventHandlers {

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getItemStack().getItem() instanceof TheStoryItem) {
            BlockPos pos = event.getHitVec().getBlockPos();
            World world = event.getWorld();
            TileEntity blockEntity = world.getBlockEntity(pos);
//            if (blockEntity instanceof AbstractSignTileEntity) {
//                event.setUseBlock(Event.Result.DENY);
//                event.setUseItem(Event.Result.ALLOW);
//            }
        }
    }

    @SubscribeEvent
    public void onEntityConstructing(AttachCapabilitiesEvent<Entity> event){
        if (event.getObject() instanceof PlayerEntity) {
            if (!event.getObject().getCapability(PlayerProperties.PLAYER_STORY).isPresent()) {
                event.addCapability(new ResourceLocation(SmallTales.MODID, "story"), new PropertiesDispatcher());
            }
        }
    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            // We need to copyFrom the capabilities
            LazyOptional<PlayerStory> capability = event.getOriginal().getCapability(PlayerProperties.PLAYER_STORY);
            capability.ifPresent(oldStore -> {
                event.getPlayer().getCapability(PlayerProperties.PLAYER_STORY).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }
}

