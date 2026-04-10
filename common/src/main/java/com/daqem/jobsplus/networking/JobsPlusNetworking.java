package com.daqem.jobsplus.networking;

import com.daqem.jobsplus.client.networking.*;
import com.daqem.jobsplus.networking.c2s.*;
import com.daqem.jobsplus.networking.s2c.*;
import com.daqem.knot.Knot;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface JobsPlusNetworking {

    static void init() {
        Knot.NETWORKING.registerServerbound(ServerboundOpenJobsScreenPacket.TYPE, ServerboundOpenJobsScreenPacket.STREAM_CODEC, () -> ServerboundOpenJobsScreenPacket::handle);
        Knot.NETWORKING.registerServerbound(ServerboundOpenPowerupsScreenPacket.TYPE, ServerboundOpenPowerupsScreenPacket.STREAM_CODEC, () -> ServerboundOpenPowerupsScreenPacket::handle);
        Knot.NETWORKING.registerServerbound(ServerboundRequestLeaderboardPacket.TYPE, ServerboundRequestLeaderboardPacket.STREAM_CODEC, () -> ServerboundRequestLeaderboardPacket::handle);
        Knot.NETWORKING.registerServerbound(ServerboundRequestPlayerJobsPacket.TYPE, ServerboundRequestPlayerJobsPacket.STREAM_CODEC, () -> ServerboundRequestPlayerJobsPacket::handle);
        Knot.NETWORKING.registerServerbound(ServerboundStartJobPacket.TYPE, ServerboundStartJobPacket.STREAM_CODEC, () -> ServerboundStartJobPacket::handle);
        Knot.NETWORKING.registerServerbound(ServerboundStartPowerupPacket.TYPE, ServerboundStartPowerupPacket.STREAM_CODEC, () -> ServerboundStartPowerupPacket::handle);
        Knot.NETWORKING.registerServerbound(ServerboundSyncPlayerJobsPacket.TYPE, ServerboundSyncPlayerJobsPacket.STREAM_CODEC, () -> ServerboundSyncPlayerJobsPacket::handle);
        Knot.NETWORKING.registerServerbound(ServerboundTogglePowerUpPacket.TYPE, ServerboundTogglePowerUpPacket.STREAM_CODEC, () -> ServerboundTogglePowerUpPacket::handle);

        Knot.NETWORKING.registerClientbound(ClientboundDeleteJobPacket.TYPE, ClientboundDeleteJobPacket.STREAM_CODEC, () -> ClientboundDeleteJobPacketHandler::handle);
        Knot.NETWORKING.registerClientbound(ClientboundLeaderboardPacket.TYPE, ClientboundLeaderboardPacket.STREAM_CODEC, () -> ClientboundLeaderboardPacketHandler::handle);
        Knot.NETWORKING.registerClientbound(ClientboundLevelUpJobPacket.TYPE, ClientboundLevelUpJobPacket.STREAM_CODEC, () -> ClientboundLevelUpJobPacketHandler::handle);
        Knot.NETWORKING.registerClientbound(ClientboundOpenHudEditorPacket.TYPE, ClientboundOpenHudEditorPacket.STREAM_CODEC, () -> ClientboundOpenHudEditorPacketHandler::handle);
        Knot.NETWORKING.registerClientbound(ClientboundOpenJobsScreenPacket.TYPE, ClientboundOpenJobsScreenPacket.STREAM_CODEC, () -> ClientboundOpenJobsScreenPacketHandler::handle);
        Knot.NETWORKING.registerClientbound(ClientboundOpenPowerupsScreenPacket.TYPE, ClientboundOpenPowerupsScreenPacket.STREAM_CODEC, () -> ClientboundOpenPowerupsScreenPacketHandler::handle);
        Knot.NETWORKING.registerClientbound(ClientboundPlayerJobsPacket.TYPE, ClientboundPlayerJobsPacket.STREAM_CODEC, () -> ClientboundPlayerJobsPacketHandler::handle);
        Knot.NETWORKING.registerClientbound(ClientboundSyncJobLevelPacket.TYPE, ClientboundSyncJobLevelPacket.STREAM_CODEC, () -> ClientboundSyncJobLevelPacketHandler::handle);
        Knot.NETWORKING.registerClientbound(ClientboundSyncJobPacket.TYPE, ClientboundSyncJobPacket.STREAM_CODEC, () -> ClientboundSyncJobPacketHandler::handle);
        Knot.NETWORKING.registerClientbound(ClientboundUnlockItemRestrictionPacket.TYPE, ClientboundUnlockItemRestrictionPacket.STREAM_CODEC, () -> ClientboundUnlockItemRestrictionPacketHandler::handle);
        Knot.NETWORKING.registerClientbound(ClientboundUnlockPowerupPacket.TYPE, ClientboundUnlockPowerupPacket.STREAM_CODEC, () -> ClientboundUnlockPowerupPacketHandler::handle);
    }
}
