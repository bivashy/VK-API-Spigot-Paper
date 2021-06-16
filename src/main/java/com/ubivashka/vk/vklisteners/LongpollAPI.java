package com.ubivashka.vk.vklisteners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonObject;
import com.ubivashka.vk.VKAPI;
import com.ubivashka.vk.events.VKJsonEvent;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.callback.longpoll.responses.GetLongPollEventsResponse;
import com.vk.api.sdk.objects.groups.responses.GetLongPollServerResponse;

public class LongpollAPI {
	private VKAPI plugin;
	private Integer ts;
	private GetLongPollServerResponse longserver;

	public LongpollAPI(VKAPI plugin) {
		this.plugin = plugin;
		try {
			longserver = plugin.vk.groupsLongPoll().getLongPollServer(plugin.actor, plugin.actor.getGroupId())
					.execute();
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
			return;
		}
		ts = Integer.parseInt(longserver.getTs());
		startEventListener();
	}

	private void startEventListener() {
		System.out.println(ChatColor.GREEN + "LongPool event listener enabled");
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					GetLongPollEventsResponse events = plugin.vk.longPoll()
							.getEvents(longserver.getServer(), longserver.getKey(), ts).execute();
					for (JsonObject json : events.getUpdates()) {
						callEvent(json);
					}
					ts=events.getTs();
				} catch (NumberFormatException | ApiException | ClientException e) {
					e.printStackTrace();	
					cancel();
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0, plugin.getConfig().getInt("settings.delay"));
	}
	private void callEvent(JsonObject json) {
		new BukkitRunnable() {
			@Override
			public void run() {
				VKJsonEvent jsonEvent = new VKJsonEvent(json);
				Bukkit.getPluginManager().callEvent(jsonEvent);
				plugin.callbackAPI.parse(json);
			}
		}.runTaskLater(plugin,1);
	}
}
