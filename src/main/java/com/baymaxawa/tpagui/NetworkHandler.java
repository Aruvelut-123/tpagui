package com.baymaxawa.tpagui;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetworkHandler implements PluginMessageListener {
    private static final List<Object> list = new ArrayList<>();

    @Override
    public void onPluginMessageReceived(@NonNull String channel, @NonNull Player player, byte @NonNull [] message) {
        if (!channel.equals("BungeeCord")) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("PlayerList")) {
            Tpagui.logger.info(Tpagui.INSTANCE.lm.getLangString("velocity.received", in.readUTF()));
            String[] playerList = in.readUTF().split(", ");
            list.addAll(Arrays.asList(playerList));
            Tpagui.logger.info(list.toString());
        }
    }

    public static List<Object> getPlayerList() {
        return list;
    }

    public static void sendGetPlayerListMessage(Player player) {
        for (String serverName : Tpagui.INSTANCE.cm.getStringList("settings.velocity.servers")) {
            sendPluginMessage(player, "PlayerList", serverName);
        }
    }

    public static void sendPluginMessage(Player player, String type, String... params) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(type);
        for (String param : params) out.writeUTF(param);
        player.sendPluginMessage(Tpagui.INSTANCE, "BungeeCord", out.toByteArray());
    }
}
