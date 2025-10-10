package com.daqem.jobsplus.networking;

import com.daqem.jobsplus.JobsPlus;
import com.daqem.jobsplus.client.networking.ClientboundLevelUpJobPacketHandler;
import com.daqem.jobsplus.client.networking.ClientboundOpenJobsScreenPacketHandler;
import com.daqem.jobsplus.client.networking.ClientboundOpenPowerupsScreenPacketHandler;
import com.daqem.jobsplus.client.networking.ClientboundUnlockItemRestrictionPacketHandler;
import com.daqem.jobsplus.networking.c2s.*;
import com.daqem.jobsplus.networking.s2c.*;
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
    CustomPacketPayload.Type<ServerboundOpenJobsScreenPacket> SERVERBOUND_OPEN_JOBS_SCREEN =
            new CustomPacketPayload.Type<>(JobsPlus.getId("serverbound_open_jobs_screen"));
    CustomPacketPayload.Type<ServerboundOpenPowerupsScreenPacket> SERVERBOUND_OPEN_POWERUPS_SCREEN =
            new CustomPacketPayload.Type<>(JobsPlus.getId("serverbound_open_powerups_screen"));

    CustomPacketPayload.Type<ClientboundUnlockItemRestrictionPacket> CLIENTBOUND_UNLOCK_ITEM_RESTRICTION =
            new CustomPacketPayload.Type<>(JobsPlus.getId("clientbound_unlock_item_restriction"));
    CustomPacketPayload.Type<ClientboundOpenJobsScreenPacket> CLIENTBOUND_OPEN_JOBS_SCREEN =
            new CustomPacketPayload.Type<>(JobsPlus.getId("clientbound_open_jobs_screen"));
    CustomPacketPayload.Type<ClientboundLevelUpJobPacket> CLIENTBOUND_LEVEL_UP_JOB =
            new CustomPacketPayload.Type<>(JobsPlus.getId("clientbound_level_up_job"));
    CustomPacketPayload.Type<ClientboundOpenPowerupsScreenPacket> CLIENTBOUND_OPEN_POWERUPS_SCREEN =
            new CustomPacketPayload.Type<>(JobsPlus.getId("clientbound_open_powerups_screen"));

    static void initClient() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENTBOUND_OPEN_JOBS_SCREEN, ClientboundOpenJobsScreenPacket.STREAM_CODEC, ClientboundOpenJobsScreenPacketHandler::handleClientSide);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENTBOUND_LEVEL_UP_JOB, ClientboundLevelUpJobPacket.STREAM_CODEC, ClientboundLevelUpJobPacketHandler::handleClientSide);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENTBOUND_UNLOCK_ITEM_RESTRICTION, ClientboundUnlockItemRestrictionPacket.STREAM_CODEC, ClientboundUnlockItemRestrictionPacketHandler::handleClientSide);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, CLIENTBOUND_OPEN_POWERUPS_SCREEN, ClientboundOpenPowerupsScreenPacket.STREAM_CODEC, ClientboundOpenPowerupsScreenPacketHandler::handleClientSide);
    }

    static void initCommon() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_TOGGLE_POWERUP, ServerboundTogglePowerUpPacket.STREAM_CODEC, ServerboundTogglePowerUpPacket::handleServerSide);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_START_JOB, ServerboundStartJobPacket.STREAM_CODEC, ServerboundStartJobPacket::handleServerSide);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_START_POWERUP, ServerboundStartPowerupPacket.STREAM_CODEC, ServerboundStartPowerupPacket::handleServerSide);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_OPEN_JOBS_SCREEN, ServerboundOpenJobsScreenPacket.STREAM_CODEC, ServerboundOpenJobsScreenPacket::handleServerSide);
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SERVERBOUND_OPEN_POWERUPS_SCREEN, ServerboundOpenPowerupsScreenPacket.STREAM_CODEC, ServerboundOpenPowerupsScreenPacket::handleServerSide);
    }

    static void initServer() {
        NetworkManager.registerS2CPayloadType(CLIENTBOUND_OPEN_JOBS_SCREEN, ClientboundOpenJobsScreenPacket.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(CLIENTBOUND_LEVEL_UP_JOB, ClientboundLevelUpJobPacket.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(CLIENTBOUND_UNLOCK_ITEM_RESTRICTION, ClientboundUnlockItemRestrictionPacket.STREAM_CODEC);
        NetworkManager.registerS2CPayloadType(CLIENTBOUND_OPEN_POWERUPS_SCREEN, ClientboundOpenPowerupsScreenPacket.STREAM_CODEC);
    }

    static void init() {
        EnvExecutor.runInEnv(Env.CLIENT, () -> JobsPlusNetworking::initClient);
        EnvExecutor.runInEnv(Env.SERVER, () -> JobsPlusNetworking::initServer);
        initCommon();
    }
}
