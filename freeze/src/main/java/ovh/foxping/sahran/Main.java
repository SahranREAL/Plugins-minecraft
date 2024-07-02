package ovh.foxping.sahran;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

    // Map pour stocker les dernières coordonnées des joueurs
    private final Map<Player, Location> lastLocations = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("freeze").setExecutor(this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        // Stocker les dernières coordonnées du joueur
        lastLocations.put(player, player.getLocation());
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
                sender.sendMessage("Le joueur n'a pas été trouvé.");
                return false;
            }

            // Récupérer la dernière position enregistrée du joueur
            Location lastLocation = lastLocations.get(target);
            if (lastLocation == null) {
                sender.sendMessage("Impossible de trouver la dernière position du joueur.");
                return false;
            }

            target.teleport(lastLocation);
            sender.sendMessage("Player " + target.getName() + " has been frozen (teleported) to their last location!");
            return true;
        }
        return false;
    }
}
