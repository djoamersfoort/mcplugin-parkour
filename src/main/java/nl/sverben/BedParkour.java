package nl.sverben;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

public class BedParkour extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Logger logger = getLogger();
        logger.info("BedParkour enabled");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onMovement(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        if (player.getWorld().getName().equalsIgnoreCase("bedwars") == false) {
            return;
        }

        if ((location.getX() <= -206 && location.getX() >= -262 && location.getZ() <= - 164 && location.getZ() >= -234) == false) {
            return;
        }

        if (location.getY() <= 61) {
            player.teleport(new Location(player.getWorld(), -230,69, -220));
            String parkour;
            if (player.hasMetadata("parkour")) {
                parkour = player.getMetadata("parkour").get(0).asString() + " ";
                player.removeMetadata("parkour", this);
            } else {
                parkour = "";
            }
            player.sendMessage(ChatColor.RED + "You failed the " + parkour + "parkour, try again!");
        }

        if (location.getBlockX() == -229 && location.getBlockY() == 69 && location.getBlockZ() == -224) {
            if (!player.hasMetadata("parkour")) {
                player.sendMessage(ChatColor.YELLOW + "You entered a really hard parkour, good luck!");
                player.setMetadata("parkour", new FixedMetadataValue(this, "hard"));
                player.setMetadata("parkour-starttime", new FixedMetadataValue(this, LocalTime.now().toSecondOfDay()));
                player.playNote(player.getLocation(), Instrument.PLING, Note.sharp(2, Note.Tone.F));
                return;
            }
        }
        if (location.getBlockX() == -231 && location.getBlockY() == 69 && location.getBlockZ() == -224) {
            if (!player.hasMetadata("parkour")) {
                player.sendMessage(ChatColor.YELLOW + "You entered the easy parkour, it is still pretty hard...");
                player.setMetadata("parkour", new FixedMetadataValue(this, "easy"));
                player.setMetadata("parkour-starttime", new FixedMetadataValue(this, LocalTime.now().toSecondOfDay()));
                player.playNote(player.getLocation(), Instrument.PLING, Note.sharp(2, Note.Tone.F));
                return;
            }
        }
        if (location.getBlockX() <= -229 && location.getBlockX() >= -231 && location.getBlockY() == 69 && location.getBlockZ() == -223) {
            if (player.hasMetadata("parkour")) {
                String parkour = player.getMetadata("parkour").get(0).asString();
                player.removeMetadata("parkour", this);
                player.sendMessage(ChatColor.BLUE + "You left the " + parkour + " parkour");
            }
        }
        if (location.getBlockX() >= -231 && location.getBlockX() <= -229 && location.getBlockY() == 72 && location.getBlockZ() == -195) {
            if (player.hasMetadata("parkour")) {
                String parkour = player.getMetadata("parkour").get(0).asString();
                Integer time = player.getMetadata("parkour-starttime").get(0).asInt();
                Integer now = LocalTime.now().toSecondOfDay();
                Integer seconds = now - time;
                player.removeMetadata("parkour", this);
                for (Player onlineplayer: getServer().getOnlinePlayers()) {
                    onlineplayer.sendTitle(ChatColor.YELLOW + player.getDisplayName(), "completed the " + parkour + " parkour in " + seconds.toString() + " seconds!", 10, 70, 20);
                    onlineplayer.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 500.0f, 1.0f);
                }
                player.sendMessage(ChatColor.GREEN + "You completed the parkour in " + seconds.toString() + " seconds");
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player == false) {
            return;
        }

        Entity player = event.getEntity();
        Location location = player.getLocation();

        if (player.getWorld().getName().equalsIgnoreCase("bedwars") == false) {
            return;
        }

        if ((location.getX() <= -206 && location.getX() >= -262 && location.getZ() <= -164 && location.getZ() >= -234) == false) {
            return;
        }

        event.setCancelled(true);
    }
}
