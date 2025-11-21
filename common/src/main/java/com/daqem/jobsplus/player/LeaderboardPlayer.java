package com.daqem.jobsplus.player;

import java.util.UUID;

import com.mojang.serialization.Dynamic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class LeaderboardPlayer {

    private final UUID uuid;
    private final ResourceLocation jobLocation;
    private String playerName;
    private int level;
    private double experience;

    private int rank;

    public LeaderboardPlayer(UUID uuid, ResourceLocation jobLocation) {
        this.uuid = uuid;
        this.jobLocation = jobLocation;
        this.playerName = "Unknown";
        this.level = 0;
        this.experience = 0;
        this.rank = -1;
    }

    public LeaderboardPlayer(UUID uuid, ResourceLocation jobLocation, String playerName, int level, double experience) {
        this.uuid = uuid;
        this.jobLocation = jobLocation;
        this.playerName = playerName;
        this.level = level;
        this.experience = experience;
        this.rank = -1;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ResourceLocation getJobLocation() {
        return jobLocation;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getRank() {
        return rank;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putString("uuid", this.uuid.toString());
        tag.putString("name", this.playerName);
        tag.putString("job", this.jobLocation.toString());
        tag.putInt("level", this.level);
        tag.putDouble("experience", this.experience);
        return tag;
    }

    public static <T> LeaderboardPlayer deserialize(Dynamic<T> dynamic) {
        return new LeaderboardPlayer(
                dynamic.get("uuid").asString().map(UUID::fromString).getOrThrow(),
                dynamic.get("job").asString().map(ResourceLocation::parse).getOrThrow(),
                dynamic.get("name").asString().getOrThrow(),
                dynamic.get("level").asInt(0),
                dynamic.get("experience").asDouble(0)
        );
    }

    public static LeaderboardPlayer fromNetwork(FriendlyByteBuf friendlyByteBuf) {
        LeaderboardPlayer player = new LeaderboardPlayer(
                friendlyByteBuf.readUUID(),
                friendlyByteBuf.readResourceLocation(),
                friendlyByteBuf.readUtf(),
                friendlyByteBuf.readVarInt(),
                friendlyByteBuf.readDouble()
        );
        player.setRank(friendlyByteBuf.readVarInt());
        return player;
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeUUID(this.uuid);
        buf.writeResourceLocation(this.jobLocation);
        buf.writeUtf(this.playerName);
        buf.writeVarInt(this.level);
        buf.writeDouble(this.experience);
        buf.writeVarInt(this.rank);
    }
}