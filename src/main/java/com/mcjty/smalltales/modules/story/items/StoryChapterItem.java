package com.mcjty.smalltales.modules.story.items;

import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.modules.story.network.PacketSyncStory;
import com.mcjty.smalltales.playerdata.PlayerProperties;
import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.tooltips.ITooltipSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;

import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.*;

public class StoryChapterItem extends Item implements ITooltipSettings {

    private final Lazy<TooltipBuilder> tooltipBuilder = () -> new TooltipBuilder()
            .info(key("message.smalltales.shiftmessage"))
            .infoShift(header(), parameter("name", this::getChapter));

    public StoryChapterItem() {
        super(new Properties()
                .stacksTo(1)
                .tab(SmallTales.setup.getTab()));
    }

    public String getChapter(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null) {
            return tag.getString("chapter");
        } else {
            return "<unknown>";
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flags) {
        super.appendHoverText(itemStack, world, list, flags);
        tooltipBuilder.get().makeTooltip(getRegistryName(), itemStack, list, flags);
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        if (!level.isClientSide()) {
            ItemStack stack = player.getItemInHand(hand);
            CompoundNBT tag = stack.getTag();
            if (tag != null) {
                String chapter = tag.getString("chapter");
                player.getCapability(PlayerProperties.PLAYER_STORY).ifPresent(story -> {
                    if (story.addDiscoveredPage(chapter)) {
                        player.sendMessage(new StringTextComponent("You discover " + chapter + "!"), Util.NIL_UUID);
                    } else {
                        player.sendMessage(new StringTextComponent("You already known " + chapter + "!"), Util.NIL_UUID);
                    }
                    PacketSyncStory.syncStory(story, player);
                });
            }
        }
        return super.use(level, player, hand);
    }
}
