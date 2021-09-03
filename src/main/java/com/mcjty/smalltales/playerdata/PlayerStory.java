package com.mcjty.smalltales.playerdata;


import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.Constants;

import java.util.HashSet;
import java.util.Set;

public class PlayerStory {

    private Set<String> discoveredPages = new HashSet<>();

    public PlayerStory() {
    }

    public Set<String> getDiscoveredPages() {
        return discoveredPages;
    }

    public void addDiscoveredPage(String id) {
        discoveredPages.add(id);
    }

    public void copyFrom(PlayerStory source) {
        discoveredPages = new HashSet<>(source.discoveredPages);
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