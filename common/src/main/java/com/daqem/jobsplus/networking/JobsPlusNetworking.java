package com.daqem.jobsplus.networking;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.networking.c2s.*;
import com.daqem.jobsplus.networking.s2c.ClientboundLevelUpJobPacket;
import com.daqem.jobsplus.networking.s2c.ClientboundOpenJobsScreenPacket;
import com.daqem.jobsplus.networking.s2c.ClientboundUnlockItemRestrictionPacket;
import com.daqem.jobsplus.networking.sync.coin.ClientBoundUpdateCoinsPacket;
import com.daqem.jobsplus.networking.sync.job.ClientboundRemoveJobPacket;
import com.daqem.jobsplus.networking.sync.job.ClientboundUpdateJobPacket;
import com.daqem.jobsplus.networking.sync.job.ClientboundUpdateJobsPacket;
import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface JobsPlusNetworking {

    CustomPacketPayload.Type<ServerboundTogglePowerUpPacket> SERVERBOUND_TOGGLE_POWERUP =
            new CustomPacketPayload.Type<>(JobsPlus.getId("serverbound_toggle_powerup"));
    CustomPacketPayload.Type<ServerboundStartJobPacket> SERVERBOUND_START_JOB =
            new CustomPacketPayload.Type<>(JobsPlus.getId("serverbound_start_job"));
    CustomPacketPayload.Type<ServerboundStartPowerupPacket> SERVERBOUND_START_POWERUP =
            new CustomPacketPayload.Type<>(JobsPlus.getId("serverbound_start_powerup"));
    CustomPacketPayload.Type<ServerboundRequestJobsPacket> SERVERBOUND_REQUEST_JOBS =
            new CustomPacketPayload.Type<>(JobsPlus.getId("serverbound_request_jobs"));
    CustomPacketPayload.Type<ServerboundOpenJobsScreenPacket> SERVERBOUND_OPEN_JOBS_SCREEN =
            new CustomPacketPayload.Type<>(JobsPlus.getId("serverbound_open_jobs_screen"));
    CustomPacketPayload.Type<ClientboundUnlockItemRestrictionPacket> CLIENTBOUND_UNLOCK_ITEM_RESTRICTION =
            new CustomPacketPayload.Type<>(JobsPlus.getId("clientbound_unlock_item_restriction"));

    CustomPacketPayload.Type<ClientboundRemoveJobPacket> CLIENTBOUND_REMOVE_JOB =
            new CustomPacketPayload.Type<>(JobsPlus.getId("clientbound_remove_job"));
    CustomPacketPayload.Type<ClientboundUpdateJobPacket> CLIENTBOUND_UPDATE_JOB =
            new CustomPacketPayload.Type<>(JobsPlus.getId("clientbound_update_job"));
    CustomPacketPayload.Type<ClientboundUpdateJobsPacket> CLIENTBOUND_UPDATE_JOBS =
            new CustomPacketPayload.Type<>(JobsPlus.getId("clientbound_update_jobs"));
    CustomPacketPayload.Type<ClientBoundUpdateCoinsPacket> CLIENTBOUND_UPDATE_COINS =
            new CustomPacketPayload.Type<>(JobsPlus.getId("clientbound_update_coins"));
    CustomPacketPayload.Type<ClientboundOpenJobsScreenPacket> CLIENTBOUND_OPEN_JOBS_SCREEN =
            new CustomPacketPayload.Type<>(JobsPlus.getId("clientbound_open_jobs_screen"));
    CustomPacketPayload.Type<ClientboundLevelUpJobPacket> CLIENTBOUND_LEVEL_UP_JOB =
            new CustomPacketPayload.Type<>(JobsPlus.getId("clientbound_level_up_job"));

    static void initClient() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENTBOUND_REMOVE_JOB, ClientboundRemoveJobPacket.STREAM_CODEC, ClientboundRemoveJobPacket::handleClientSide);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENTBOUND_UPDATE_JOB, ClientboundUpdateJobPacket.STREAM_CODEC, ClientboundUpdateJobPacket::handleClientSide);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENTBOUND_UPDATE_JOBS, ClientboundUpdateJobsPacket.STREAM_CODEC, ClientboundUpdateJobsPacket::handleClientSide);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENTBOUND_UPDATE_COINS, ClientBoundUpdateCoinsPacket.STREAM_CODEC, ClientBoundUpdateCoinsPacket::handleClientSide);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENTBOUND_OPEN_JOBS_SCREEN, ClientboundOpenJobsScreenPacket.STREAM_CODEC, ClientboundOpenJobsScreenPacket::handleClientSide);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENTBOUND_LEVEL_UP_JOB, ClientboundLevelUpJobPacket.STREAM_CODEC, ClientboundLevelUpJobPacket::handleClientSide);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENTBOUND_UNLOCK_ITEM_RESTRICTION, ClientboundUnlockItemRestrictionPacket.STREAM_CODEC, ClientboundUnlockItemRestrictionPacket::handleClientSide);
    }

    static void initCommon() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_TOGGLE_POWERUP, ServerboundTogglePowerUpPacket.STREAM_CODEC, ServerboundTogglePowerUpPacket::handleServerSide);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_START_JOB, ServerboundStartJobPacket.STREAM_CODEC, ServerboundStartJobPacket::handleServerSide);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_START_POWERUP, ServerboundStartPowerupPacket.STREAM_CODEC, ServerboundStartPowerupPacket::handleServerSide);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_REQUEST_JOBS, ServerboundRequestJobsPacket.STREAM_CODEC, ServerboundRequestJobsPacket::handleServerSide);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_OPEN_JOBS_SCREEN, ServerboundOpenJobsScreenPacket.STREAM_CODEC, ServerboundOpenJobsScreenPacket::handleServerSide);
    }

    static void initServer() {
        NetworkManager.registerS2CPayloadType(CLIENTBOUND_REMOVE_JOB, ClientboundRemoveJobPacket.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(CLIENTBOUND_UPDATE_JOB, ClientboundUpdateJobPacket.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(CLIENTBOUND_UPDATE_JOBS, ClientboundUpdateJobsPacket.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(CLIENTBOUND_UPDATE_COINS, ClientBoundUpdateCoinsPacket.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(CLIENTBOUND_OPEN_JOBS_SCREEN, ClientboundOpenJobsScreenPacket.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(CLIENTBOUND_LEVEL_UP_JOB, ClientboundLevelUpJobPacket.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(CLIENTBOUND_UNLOCK_ITEM_RESTRICTION, ClientboundUnlockItemRestrictionPacket.STREAM_CODEC);
    }

    static void init() {
        EnvExecutor.runInEnv(Env.CLIENT, () -> JobsPlusNetworking::initClient);
        EnvExecutor.runInEnv(Env.SERVER, () -> JobsPlusNetworking::initServer);
        initCommon();
    }
}
