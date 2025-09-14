package com.daqem.jobsplus.player;

import com.daqem.jobsplus.player.job.Job;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record ServerPlayerData(
        List<Job> jobs,
        int coins
) {
    public static final Codec<ServerPlayerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Job.CODEC.listOf().fieldOf("jobs").forGetter(ServerPlayerData::jobs),
            Codec.INT.fieldOf("coins").forGetter(ServerPlayerData::coins)
    ).apply(instance, ServerPlayerData::new));
}
