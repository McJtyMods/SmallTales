package com.mcjty.smalltales.playerdata;


import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class PlayerStory {

    private List<String> discoveredPages = new ArrayList<>();

    public PlayerStory() {
    }

    public void reset() {
        discoveredPages.clear();
    }

    public List<String> getDiscoveredPages() {
        return discoveredPages;
    }

    public boolean addDiscoveredPage(String id) {
        if (!discoveredPages.contains(id)) {
            discoveredPages.add(id);
            return true;
        }
        return false;
    }

    public void copyFrom(PlayerStory source) {
        discoveredPages = new ArrayList<>(source.discoveredPages);
    }

    public void saveNBTData(CompoundNBT compound) {
        ListNBT list = new ListNBT();
        discoveredPages.forEach(s -> list.add(StringNBT.valueOf(s)));
        compound.put("discovered", list);
    }

    public void loadNBTData(CompoundNBT compound) {
        ListNBT discovered = compound.getList("discovered", Constants.NBT.TAG_STRING);
        discoveredPages.clear();
        discovered.forEach(nbt -> discoveredPages.add(nbt.getAsString()));
    }
}