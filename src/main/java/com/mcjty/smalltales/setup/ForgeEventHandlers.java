package com.mcjty.smalltales.setup;

import com.mcjty.smalltales.modules.story.items.TheStoryItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
}
