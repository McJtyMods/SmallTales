package com.mcjty.smalltales.modules.story.items;

import com.mcjty.smalltales.SmallTales;
import com.mcjty.smalltales.modules.story.blocks.StoryAnchorTile;
import com.mcjty.smalltales.modules.story.client.GuiConfigurator;
import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.tooltips.ITooltipSettings;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;

import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.header;

public class ConfiguratorItem extends Item implements ITooltipSettings {

    private final Lazy<TooltipBuilder> tooltipBuilder = () -> new TooltipBuilder()
            .info(header());

    public ConfiguratorItem() {
        super(new Properties()
                .stacksTo(1)
                .tab(SmallTales.setup.getTab()));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flags) {
        super.appendHoverText(itemStack, world, list, flags);
        tooltipBuilder.get().makeTooltip(getRegistryName(), itemStack, list, flags);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        TileEntity be = context.getLevel().getBlockEntity(context.getClickedPos());
        if (be instanceof StoryAnchorTile) {
            if (context.getLevel().isClientSide()) {
                GuiConfigurator.open(context.getClickedPos());
            }
            return ActionResultType.SUCCESS;
        }
        return super.useOn(context);
    }

    @Override
    public void onUseTick(World pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pCount) {
        pLivingEntity.sendMessage(new StringTextComponent("Use this on a story anchor!").withStyle(TextFormatting.RED), Util.NIL_UUID);
        super.onUseTick(pLevel, pLivingEntity, pStack, pCount);
    }
}
