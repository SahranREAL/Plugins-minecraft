package ovh.foxping.sahran;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

    // Map pour stocker les dernières coordonnées des joueurs
    private final Map<Player, Location> lastLocations = new HashMap<>();
    // Variable pour stocker l'ID de la tâche programmée
    private int freezeTaskId = -1;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("freeze").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("freeze")) {
            if (args.length != 1) {
                sender.sendMessage("Usage: /freeze <player>");
                return false;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("Player not found.");
                return false;
            }

            if (freezeTaskId == -1) {
                startFreezeTask(target);
                sender.sendMessage("Player " + target.getName() + " is now frozen!");
            } else {
                stopFreezeTask();
                sender.sendMessage("Player " + target.getName() + " is no longer frozen!");
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        // Arrêter la tâche si le plugin est désactivé
        stopFreezeTask();
    }

    @org.bukkit.event.EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        // Stocker les dernières coordonnées du joueur à chaque mouvement
        lastLocations.put(player, player.getLocation());
    }

    private void startFreezeTask(Player target) {
        // Démarrer une tâche répétitive toutes les secondes (20 ticks)
        freezeTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Location lastLocation = lastLocations.get(target);
                if (lastLocation != null) {
                    target.teleport(lastLocation);
                }
            }
        }, 0L, 1L);
    }

    private void stopFreezeTask() {
        // Annuler la tâche si elle est en cours d'exécution
        if (freezeTaskId != -1) {
            Bukkit.getScheduler().cancelTask(freezeTaskId);
            freezeTaskId = -1;
        }
    }
}

