package com.main.stafftext;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.main.stafftext.StaffText;
import com.main.stafftext.ChatListener;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffText extends JavaPlugin{
	public final Logger logger = Logger.getLogger("Minecraft");
	public static StaffText plugin;
	public static Economy econ = null;
    public static Permission perms = null;
    public static Chat chat = null;
    
    public final ChatListener chatl = new ChatListener(this);
    
    List<String> stafftext = new ArrayList<String>();
	List<String> staffpartner = new ArrayList<String>();
	
	public void onReload() {
		saveDefaultConfig();
		saveConfig();
	}
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " StaffText has been disabled");
		this.saveDefaultConfig();
        saveConfig();
	}

	@Override
	public void onEnable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " Version " + pdffile.getVersion() + " StaffText has been enabled");
        getConfig().options().copyDefaults(true);
		saveConfig();
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(chatl, this);
        setupPermissions();
        if (!setupEconomy() ) {
            logger.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(commandLabel.equalsIgnoreCase("stafftext")){
		    	if(args.length==0){
		    		if(player.hasPermission("stafftext.chat")){
		    			if(stafftext.contains(player.getName())){
		    				stafftext.remove(player.getName());
		    				player.sendMessage(ChatColor.GREEN + "StaffText Chat Disabled");
		    			}else if(!(stafftext.contains(player.getName()))){
		    				stafftext.add(player.getName());
		    				player.sendMessage(ChatColor.GREEN + "StaffText Chat Enabled");
		    			}
		    		}else if(!(player.hasPermission("stafftext.chat"))){
		    			player.sendMessage(ChatColor.DARK_RED + "You do not have permission to use that!");
		    		}
		    	}else if(args.length==1){
		    		getConfig().set(".Users." + player.getName() + ".Partner", args[0]);
		    		saveDefaultConfig();
		    		saveConfig();
		    		if(player.hasPermission("stafftext.chat")){
		    			if(staffpartner.contains(player.getName())){
		    				staffpartner.remove(player.getName());
		    				player.sendMessage(ChatColor.GREEN + "Private StaffText Chat Disabled");
		    			}else if(!(staffpartner.contains(player.getName()))){
		    				staffpartner.add(player.getName());
		    				player.sendMessage(ChatColor.GREEN + " Private StaffText Chat Enabled");
		    			}
		    		}else if(!(player.hasPermission("stafftext.chat"))){
		    			player.sendMessage(ChatColor.DARK_RED + "You do not have permission to use that!");
		    		}	
		    	}
		}
	}

		return false;

	}
}
