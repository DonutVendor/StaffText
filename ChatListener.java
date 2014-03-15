package com.main.stafftext;

import com.main.stafftext.StaffText;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
	
	public static StaffText plugin;
	public ChatListener(StaffText instance){
		plugin = instance;
	}
	
@EventHandler
public void onGlobalChat(AsyncPlayerChatEvent event){
	Player player = event.getPlayer();
	if(plugin.stafftext.contains(player.getName())){
		String msg = event.getMessage();
		for(Player staff : Bukkit.getOnlinePlayers()){
			if(staff.hasPermission("stafftext.chat")) {
				event.setCancelled(true);
				event.getRecipients().clear();
		    	staff.sendMessage(ChatColor.DARK_BLUE + "[" + ChatColor.AQUA + "StaffText" + ChatColor.DARK_BLUE + "] " + ChatColor.RESET + player.getName() + ": " + ChatColor.AQUA + msg);
		    }
		}
	}
}
		
@EventHandler
public void onPartnerChat(AsyncPlayerChatEvent event){
	Player player = event.getPlayer();
	if(plugin.staffpartner.contains(player.getName())){
		String name = plugin.getConfig().getString(".Users." + player.getName() + ".Partner");
		String msgpartner = event.getMessage();
		Player partner = Bukkit.getPlayer(name);
		if(partner.hasPermission("stafftext.chat")) {
			event.setCancelled(true);
			event.getRecipients().clear();
			player.sendMessage(ChatColor.DARK_BLUE + "[" + ChatColor.AQUA + "StaffText" + ChatColor.DARK_BLUE + "] " + "Message sent!");
			partner.sendMessage(ChatColor.DARK_BLUE + "[" + ChatColor.AQUA + "StaffText" + ChatColor.DARK_BLUE + "] " + ChatColor.RESET + player.getName() + ": " + ChatColor.AQUA + msgpartner);
	    }
    }
    }
}
