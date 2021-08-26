package com.mcjty.smalltales.modules.story.items;

import com.mcjty.smalltales.SmallTales;
import mcjty.lib.builder.TooltipBuilder;
import mcjty.lib.tooltips.ITooltipSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ReadBookScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.play.server.SOpenBookWindowPacket;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;

import java.util.List;

import static mcjty.lib.builder.TooltipBuilder.header;
import static mcjty.lib.builder.TooltipBuilder.key;

public class TheStoryItem extends WrittenBookItem implements ITooltipSettings {

    private final Lazy<TooltipBuilder> tooltipBuilder = () -> new TooltipBuilder()
            .info(key("message.smalltales.shiftmessage"))
            .infoShift(header());

    public TheStoryItem() {
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
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        CompoundNBT tag = itemstack.getOrCreateTag();
        if (!tag.contains("pages")) {
            ListNBT pages = new ListNBT();
            ListNBT page = new ListNBT();
            page.add(StringNBT.valueOf("{\"text\":\"This is a test\"}"));
            page.add(StringNBT.valueOf("{\"text\":\"Line 2\"}"));
            pages.add(page);
            tag.put("pages", pages);
            tag.putString("title", "Title");
            tag.putString("author", "Me");
            tag.putBoolean("resolved", true);
        }
        Item item = itemstack.getItem();
        if (world.isClientSide()) {
            // @TODO FIX FIX FIX
            // Everything must be client side here
//            Minecraft.getInstance().setScreen(new EditBookScreen(player, itemstack, hand));
            ReadBookScreen.WrittenBookInfo info = new ReadBookScreen.WrittenBookInfo(itemstack);
            Minecraft.getInstance().setScreen(new ReadBookScreen(info));
        } else {
            if (WrittenBookItem.resolveBookComponents(itemstack, player.createCommandSourceStack(), player)) {
                player.containerMenu.broadcastChanges();
            }
            ((ServerPlayerEntity)player).connection.send(new SOpenBookWindowPacket(hand));
        }
//        player.openItemGui(itemstack, hand);
        player.awardStat(Stats.ITEM_USED.get(this));
        return ActionResult.sidedSuccess(itemstack, world.isClientSide());
    }
}
