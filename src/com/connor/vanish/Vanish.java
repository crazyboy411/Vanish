package com.connor.vanish;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Vanish extends JavaPlugin {

    public Logger log = Logger.getLogger("Minecraft");
    private ArrayList<String> hiddenUsernames = new ArrayList<String>();
    
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new VanishLoginHandler(this), this);
        getServer().getPluginManager().registerEvents(new VanishPlayerHandler(this), this);
        getCommand("vanish").setExecutor(this);
    }

    public void onDisable() {
        for (Player p : getServer().getOnlinePlayers()) {
            if (isVanished(p)) {
                showPlayer(p);
            }
        }
        hiddenUsernames.clear();
        log.info("All players are visible again");
    }
    

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) return false;
        
        Player player = (Player) sender;
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("list")) {
                if (!player.hasPermission("vanish.list")) {
                    player.sendMessage(ChatColor.GRAY + "You don't have permission");
                    return true;
                }
                if (hiddenUsernames.size() > 0) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < hiddenUsernames.size(); i++) {
                        builder.append(hiddenUsernames.get(i));
                        if (i < hiddenUsernames.size() - 1) {
                            builder.append(", ");
                        }
                    }
                
                    player.sendMessage(ChatColor.GRAY + "Hidden players: " + builder.toString());
                } else {
                    player.sendMessage(ChatColor.GRAY + "There are no hidden players");
                }
                return true;
            } else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                return false;
            } else if (player.hasPermission("vanish.vanishother")) {
                Player target = getServer().getPlayer(args[0]);
                if (target == null) target = getServer().getPlayerExact(args[0]);
                if (target == null) { 
                    player.sendMessage(ChatColor.GRAY + "Player doesn't exist or too many results");
                    return true;
                }
                
                if (!isVanished(target)) {
                    vanishPlayer(target);
                    target.sendMessage(ChatColor.GRAY + "You have vanished");
                    player.sendMessage(ChatColor.GRAY + target.getName() + " is now invisible");
                    getServer().broadcastMessage(ChatColor.YELLOW + target.getName() + " left the game.");
                    return true;
                } else {
                    showPlayer(target);
                    target.sendMessage(ChatColor.GRAY + "You are no longer invisible");
                    player.sendMessage(ChatColor.GRAY + target.getName() + " is no longer invisible");
                    getServer().broadcastMessage(ChatColor.YELLOW + target.getName() + " joined the game.");
                    return true;
                }
            }
        } else if (player.hasPermission("vanish.vanish")) {
            if (!isVanished(player)) {
                vanishPlayer(player);
                player.sendMessage(ChatColor.GRAY + "You have vanished");
                getServer().broadcastMessage(ChatColor.YELLOW + player.getName() + " left the game.");
                return true;
            } else {
                showPlayer(player);
                player.sendMessage(ChatColor.GRAY + "You are visible again");
                getServer().broadcastMessage(ChatColor.YELLOW + player.getName() + " joined the game.");
                return true;
            }
        }
        return false;
    }
    
    public boolean isVanished(Player player) {
        return hiddenUsernames.contains(player.getName());
    }

    public void vanishPlayer(Player player) {
        hiddenUsernames.add(player.getName());
        for (Player p1 : getServer().getOnlinePlayers()) {
            if (p1 == player) {
                continue;
            } else if (p1.hasPermission("vanish.seeall")) {
                p1.sendMessage(ChatColor.GRAY + player.getName() + " vanished");
                continue;
            } else if (p1.hasPermission("vanish.list")) {
                p1.hidePlayer(player);
                p1.sendMessage(ChatColor.GRAY + player.getName() + " vanished");
                continue;
            }
            p1.hidePlayer(player);
        }
    }
    
    public void showPlayer(Player player) {
        hiddenUsernames.remove(player.getName());
        for (Player p1 : getServer().getOnlinePlayers()) {
            p1.showPlayer(player);
        }
    }


}
