package com.daqem.jobsplus.player;

import java.util.List;

import com.daqem.jobsplus.player.job.Job;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record ServerPlayerData(
        List<Job> jobs,
        double coins
) {
    public static final Codec<ServerPlayerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Job.CODEC.listOf().fieldOf("jobs").forGetter(ServerPlayerData::jobs),
            Codec.DOUBLE.fieldOf("coins").forGetter(ServerPlayerData::coins)
    ).apply(instance, ServerPlayerData::new));
}
