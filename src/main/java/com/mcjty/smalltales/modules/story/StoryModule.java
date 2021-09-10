package com.mcjty.smalltales.modules.story;

import com.mcjty.smalltales.modules.story.blocks.StoryAnchorBlock;
import com.mcjty.smalltales.modules.story.blocks.StoryAnchorTile;
import com.mcjty.smalltales.modules.story.items.ChapterItem;
import com.mcjty.smalltales.modules.story.items.ConfiguratorItem;
import com.mcjty.smalltales.modules.story.items.TheStoryItem;
import mcjty.lib.modules.IModule;
import net.minecraft.item.BlockItem;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.mcjty.smalltales.setup.Registration.*;

public class StoryModule implements IModule {

    public static final RegistryObject<TheStoryItem> THE_STORY = ITEMS.register("the_story", TheStoryItem::new);
    public static final RegistryObject<ChapterItem> STORY_CHAPTER = ITEMS.register("story_chapter", ChapterItem::new);
    public static final RegistryObject<ConfiguratorItem> CONFIGURATOR = ITEMS.register("configurator", ConfiguratorItem::new);

    public static final RegistryObject<StoryAnchorBlock> STORY_ANCHOR_STONE = BLOCKS.register("story_anchor_stone", StoryAnchorBlock::new);
    public static final RegistryObject<BlockItem> STORY_ANCHOR_STONE_ITEM = ITEMS.register("story_anchor_stone", () -> new BlockItem(STORY_ANCHOR_STONE.get(), createStandardProperties()));
    public static final RegistryObject<StoryAnchorBlock> STORY_ANCHOR_INVISIBLE = BLOCKS.register("story_anchor_invisible", StoryAnchorBlock::new);
    public static final RegistryObject<BlockItem> STORY_ANCHOR_INVISIBLE_ITEM = ITEMS.register("story_anchor_invisible", () -> new BlockItem(STORY_ANCHOR_INVISIBLE.get(), createStandardProperties()));
    public static final RegistryObject<TileEntityType<StoryAnchorTile>> STORY_ANCHOR_TILE = TILES.register("story_anchor", () ->
            TileEntityType.Builder.of(StoryAnchorTile::new, STORY_ANCHOR_STONE.get(), STORY_ANCHOR_INVISIBLE.get()).build(null));

    @Override
    public void init(FMLCommonSetupEvent event) {

    }

    @Override
    public void initClient(FMLClientSetupEvent event) {
//        event.enqueueWork(() -> {
//            TheStoryGui.register();
//        });
    }

    @Override
    public void initConfig() {

    }
}
