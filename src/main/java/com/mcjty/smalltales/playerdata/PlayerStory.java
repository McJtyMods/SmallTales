package com.mcjty.smalltales.playerdata;


import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class PlayerStory {

    private List<String> discovered = new ArrayList<>();

    public PlayerStory() {
    }

    public void reset() {
        discovered.clear();
    }

    public List<String> getDiscovered() {
        return discovered;
    }

    // Return true if we discovered this newly
    public boolean addDiscovered(String id) {
        if (!discovered.contains(id)) {
            discovered.add(id);
            return true;
        }
        return false;
    }

    public void copyFrom(PlayerStory source) {
        discovered = new ArrayList<>(source.discovered);
    }

    public void saveNBTData(CompoundNBT compound) {
        ListNBT list = new ListNBT();
        discovered.forEach(s -> list.add(StringNBT.valueOf(s)));
        compound.put("discovered", list);
    }

    public void loadNBTData(CompoundNBT compound) {
        ListNBT discovered = compound.getList("discovered", Constants.NBT.TAG_STRING);
        this.discovered.clear();
        discovered.forEach(nbt -> this.discovered.add(nbt.getAsString()));
    }
}