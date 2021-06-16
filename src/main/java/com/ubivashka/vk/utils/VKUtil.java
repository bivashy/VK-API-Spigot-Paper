package com.ubivashka.vk.utils;

import java.util.Arrays;
import java.util.List;

import com.ubivashka.vk.VKAPI;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import com.vk.api.sdk.objects.users.UserFull;
import com.vk.api.sdk.queries.messages.MessagesSendQuery;

public class VKUtil {
	private VKAPI plugin;

	public VKUtil(VKAPI plugin) {
		this.plugin = plugin;
	}

	public void sendAnswerMSG(Message message, String text, boolean replyTo) {
		MessagesSendQuery query = plugin.vk.messages().send(plugin.actor);
		query = query.randomId(plugin.random.nextInt(10000));
		query = query.message(text);
		query = query.peerId(message.getPeerId());
		try {
			query.execute();
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
		}
	}

	public void sendMSGtoUser(Integer id,String text) {
		MessagesSendQuery query = plugin.vk.messages().send(plugin.actor);
		query = query.randomId(plugin.random.nextInt(10000));
		query = query.message(text);
		query = query.userId(id);
		try {
			query.execute();
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMSGtoPeer(Integer peerID, String text) {
		MessagesSendQuery query = plugin.vk.messages().send(plugin.actor);
		query = query.randomId(plugin.random.nextInt(10000));
		query = query.message(text);
		query = query.peerId(peerID);
		try {
			query.execute();
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
		}
	}

	public List<UserFull> getMembers(Message message) {
		try {
			return plugin.vk.messages().getConversationMembers(plugin.actor, message.getPeerId()).execute()
					.getProfiles();
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
		}
		return null;

	}

	public void deleteMessage(Message message) {
		try {
			plugin.vk.messages().delete(plugin.actor).messageIds(Arrays.asList(message.getId())).deleteForAll(true)
					.execute();
		} catch (ApiException | ClientException e) {
			e.printStackTrace();
		}
	}

}
