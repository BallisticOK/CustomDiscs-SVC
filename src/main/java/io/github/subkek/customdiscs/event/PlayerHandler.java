package io.github.subkek.customdiscs.event;

import io.github.subkek.customdiscs.CustomDiscs;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerHandler implements Listener {
  private final CustomDiscs plugin = CustomDiscs.getPlugin();
  @Getter
  private final HashMap<UUID, Integer> playersSelecting = new HashMap<>();

  private static PlayerHandler instance;

  public static PlayerHandler getInstance() {
    if (instance == null) return instance = new PlayerHandler();
    return instance;
  }

  @EventHandler
  public void onClickJukebox(PlayerInteractEvent event) {
    UUID playerUUID = event.getPlayer().getUniqueId();
    if (!playersSelecting.containsKey(playerUUID)) return;
    if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
    Block block = event.getClickedBlock();
    if (block == null) return;
    if (!block.getType().equals(Material.JUKEBOX)) {
      CustomDiscs.sendMessage(event.getPlayer(),
          plugin.getLanguage().PComponent("command.distance.messages.error.not-jukebox"));
      playersSelecting.remove(playerUUID);
      return;
    }

    event.setCancelled(true);

    UUID blockUUID = UUID.nameUUIDFromBytes(block.getLocation().toString().getBytes());
    int distance = playersSelecting.remove(playerUUID);
    plugin.getCDData().getJukeboxDistanceMap().put(blockUUID, distance);

    CustomDiscs.sendMessage(event.getPlayer(),
        plugin.getLanguage().PComponent("command.distance.messages.success", distance));
  }
}
