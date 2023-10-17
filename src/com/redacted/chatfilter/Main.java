package com.redacted.chatfilter;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.redacted.chatfilter.event.player.ChatFilter;


public class Main extends JavaPlugin {
	static Map<String, Long> lastMsg = new HashMap<String, Long>();
	static Map<String, String> lastMsgStr = new HashMap<String, String>();
	public void onEnable() {
		
		System.out.println("Starting ChatFilter...");
		registerEvents();
	}
	
	public void onDisable() {
		System.out.println("Stopping ChatFilter...");
	}

	public void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new ChatFilter(), this);
	}
	
	public static Map<String, Long> getLastMsg() {
		return lastMsg;
	}
	
	public static Map<String, String> getLastMsgStr() {
		return lastMsgStr;
		
	}
	
	public static void setLastMsg(Map<String, Long> newLastMsg) {
		lastMsg = newLastMsg;
	}
	
	public static void setLastMsgStr(Map<String, String> newLastMsgStr) {
		lastMsgStr = newLastMsgStr;
	}
	
}

