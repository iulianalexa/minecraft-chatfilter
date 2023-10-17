package com.redacted.chatfilter.event.player;


import java.util.Calendar;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.redacted.chatfilter.Main;



public class ChatFilter implements Listener {
	String[] bannedWords = {"[redacted]"};
	boolean timeFilter = true;
	int timeStart = 21;
	int timeEnd = 7;
	double capsDelRatio = .5;
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String message = event.getMessage().toLowerCase();
		String mCaps = event.getMessage();
		String puid = player.getUniqueId().toString();
		
		for (int i = 0; i < bannedWords.length; i++) {
			if (message.contains(" " + bannedWords[i] + " ") || message.startsWith(bannedWords[i] + " ") || message.endsWith(" " + bannedWords[i]) || message.equals(bannedWords[i])) {
				int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				boolean metd = false;
				if (timeStart < timeEnd) metd = (hour >= timeStart && hour <= timeEnd);
				else if (timeStart > timeEnd) metd = (hour >= timeStart || hour <= timeEnd);
				
				if ((!timeFilter || !metd) && !player.hasPermission("redacted.chatfilter.allowswearing") && !player.isOp()) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "Please do not swear!");
					return;
				}
			}
		}
		
		double capsAmnt = 0;
		double lowerAmnt = 0;
		
		for (int i = 0; i < mCaps.length(); i++) {
			char c = mCaps.charAt(i);
			int asc = (int) c;
			if (asc >= 65 && asc <= 90) capsAmnt++;
			else if (asc >= 97 && asc <= 122) lowerAmnt++;
		}
		
		double ratio = capsAmnt / (lowerAmnt + capsAmnt);
		if (capsAmnt + lowerAmnt > 5 && ratio > capsDelRatio && !player.hasPermission("redacted.chatfilter.allowcaps") && !player.isOp()) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.RED + "Please turn your caps off.");
			return;
			
		}
		
		Map<String, Long> lastMsg = Main.getLastMsg();
		Map<String, String> lastMsgStr = Main.getLastMsgStr();
		
		long timenow = System.currentTimeMillis();
		if (lastMsg.keySet().contains(puid)) {
			long timethen = lastMsg.get(puid);
			if (timethen + 3000 > timenow && !player.hasPermission("redacted.chatfilter.allowspam") && !player.isOp()) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "Please wait for 3 seconds before sending a message.");
				return;
			}
			
		}
		
		if (lastMsgStr.keySet().contains(puid)) {
			String strthen = lastMsgStr.get(puid);
			if (strthen.equals(message) && !player.hasPermission("redacted.chatfilter.allowspam") && !player.isOp()) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "You cannot send the same message as before.");
				return;
			}
		}
		
		lastMsg.put(puid, timenow);
		lastMsgStr.put(puid, message);
		
		Main.setLastMsg(lastMsg);
		Main.setLastMsgStr(lastMsgStr);
	}
}
