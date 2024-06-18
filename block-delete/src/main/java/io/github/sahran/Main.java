package io.github.sahran;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends JavaPlugin implements org.bukkit.event.Listener {
    @Override
        public void onEnable(){
        Bukkit.getPluginManager().registerEvents(this, this);
    }
    @EventHandler
    public void onBlockPlace (BlockPlaceEvent event) {
        Block block = event.getBlock();

        if (block.getType() == Material.WOOL){
            Bukkit.getScheduler().runTaskLater(this,() -> block.setType(Material.AIR), 1200L);
        }
    }
}