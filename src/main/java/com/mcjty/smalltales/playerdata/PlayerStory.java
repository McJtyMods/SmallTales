package com.mcjty.smalltales.playerdata;


import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerStory {

    private List<String> discovered = new ArrayList<>();
    private Set<String> read = new HashSet<>();

    public PlayerStory() {
    }

    public void reset() {
        discovered.clear();
        read.clear();
    }

    @Nonnull
    public List<String> getDiscovered() {
        return discovered;
    }

    @Nonnull
    public Set<String> getRead() {
        return read;
    }

    // Return true if we discovered this newly
    public boolean addDiscovered(String id) {
        if (!discovered.contains(id)) {
            discovered.add(id);
            return true;
        }
        return false;
    }

    public void addRead(String id) {
        read.add(id);
    }

    public void copyFrom(PlayerStory source) {
        discovered = new ArrayList<>(source.discovered);
    }

    public void saveNBTData(CompoundNBT compound) {
        ListNBT discoveredList = new ListNBT();
        discovered.forEach(s -> discoveredList.add(StringNBT.valueOf(s)));
        compound.put("discovered", discoveredList);
        ListNBT readList = new ListNBT();
        read.forEach(s -> readList.add(StringNBT.valueOf(s)));
        compound.put("read", readList);
    }

    public void loadNBTData(CompoundNBT compound) {
        ListNBT discoveredList = compound.getList("discovered", Constants.NBT.TAG_STRING);
        this.discovered.clear();
        discoveredList.forEach(nbt -> this.discovered.add(nbt.getAsString()));
        ListNBT readList = compound.getList("read", Constants.NBT.TAG_STRING);
        this.read.clear();
        readList.forEach(nbt -> this.read.add(nbt.getAsString()));
    }
}