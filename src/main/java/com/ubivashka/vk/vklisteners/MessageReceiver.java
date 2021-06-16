package com.ubivashka.vk.vklisteners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import com.ubivashka.vk.VKAPI;
import com.ubivashka.vk.events.VKMessageEvent;
import com.vk.api.sdk.exceptions.ApiException;

import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

public class MessageReceiver {
	private VKAPI plugin;
	public List<Message> messages = null;

	public MessageReceiver(VKAPI plugin) {
		this.plugin = plugin;
		startTask();

	}

	private void startTask() {
		System.out.println(ChatColor.GREEN+"LongPool message listener enabled");
		new BukkitRunnable() {
			@Override
			public void run() {
				MessagesGetLongPollHistoryQuery historyQuery = plugin.vk.messages().getLongPollHistory(plugin.actor)
						.ts(plugin.ts);

				try {
					messages = historyQuery.execute().getMessages().getItems();
				} catch (ApiException | ClientException e) {
					e.printStackTrace();
				}
				if (messages != null)
					if (!messages.isEmpty()) {
						new BukkitRunnable() {
							@Override
							public void run() {
								for (int i = 0; i < messages.size(); i++) {
									callEvent(messages.get(i));
								}
								messages = null;
							}
						}.runTaskLater(plugin, 1);
					}
				plugin.setTS();
			}
		}.runTaskTimerAsynchronously(plugin, 0, plugin.getConfig().getInt("settings.delay"));
	}
	private void callEvent(Message message) {
		if (message.getFromId() == -plugin.actor.getGroupId())
			return;

		if (plugin.getConfig().getBoolean("settings.disableGroupListener"))
			if (message.getFromId() < 0)
				return;
		
		VKMessageEvent messageEvent = new VKMessageEvent(message);
		Bukkit.getPluginManager().callEvent(messageEvent);
		if(messageEvent.isCancelled()) {
			plugin.vkutil.deleteMessage(message);
		}
	}
}
