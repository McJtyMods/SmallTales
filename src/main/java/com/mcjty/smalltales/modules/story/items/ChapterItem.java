package com.mcjty.smalltales.modules.story.items;

import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.playerdata.StoryTools;
import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.tooltips.ITooltipSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;

import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.header;
import static mcjty.lib.builder.TooltipBuilder.parameter;

public class ChapterItem extends Item implements ITooltipSettings {

    private final Lazy<TooltipBuilder> tooltipBuilder = () -> new TooltipBuilder()
            .info(header(),
                    parameter("chapter", this::getChapterString),
                    parameter("message", this::getMessageString));

    public ChapterItem() {
        super(new Properties()
                .stacksTo(1)
                .tab(SmallTales.setup.getTab()));
    }

    public String getMessageString(ItemStack stack) {
        String chapter = getChapter(stack);
        return chapter == null ? "<unknown>" : chapter;
    }
    public String getChapterString(ItemStack stack) {
        String message = getMessage(stack);
        return message == null ? "<unknown>" : message;
    }

    public String getChapter(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null) {
            return tag.getString("chapter");
        } else {
            return null;
        }
    }

    public String getMessage(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null) {
            return tag.getString("message");
        } else {
            return null;
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
            StoryTools.acquireKnowledge(player, getChapter(stack), getMessage(stack),true);
        }
        return super.use(level, player, hand);
    }
}
