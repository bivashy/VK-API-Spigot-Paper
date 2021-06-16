package com.ubivashka.vk;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.ubivashka.vk.logfilter.LogFilter;
import com.ubivashka.vk.utils.CallbackAPI;
import com.ubivashka.vk.utils.VKUtil;
import com.ubivashka.vk.vklisteners.LongpollAPI;
import com.ubivashka.vk.vklisteners.MessageReceiver;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiAccessException;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;

public class VKAPI extends JavaPlugin {
	public LogFilter logFilter;
	public VKUtil vkutil;
	public CallbackAPI callbackAPI;
	
	public Random random;
	public Integer ts = null;
	public GroupActor actor;
	public VkApiClient vk;

	private static VKAPI instance;

	public void onEnable() {
		instance = this;
		File config = new File(getDataFolder() + File.separator + "config.yml");
		if (!config.exists()) {
			System.out.println("Creating config.yml");
			getConfig().options().copyDefaults(true);
			saveDefaultConfig();
		}
		logFilter = new LogFilter();
		TransportClient transportClient = HttpTransportClient.getInstance();
		vk = new VkApiClient(transportClient);
		random = new Random();
		actor = new GroupActor(getConfig().getInt("groupInfo.groupID"), getConfig().getString("groupInfo.groupToken"));
		setTS();
		if (ts == null)
			return;
		System.out.println(ChatColor.GREEN + "Group launched!");

		new MessageReceiver(this);
		new LongpollAPI(this);
		vkutil = new VKUtil(this);
		callbackAPI = new CallbackAPI(this);
	}

	public static VKAPI getInstance() {
		return instance;
	}

	public void setTS() {
		int delay = getConfig().getInt("settings.delay");
		if(delay<5 || delay>40) {
			System.out.println(ChatColor.RED+"Ваш delay в конфиг слишком низкий, или слишком высокий, при неправильной работе, попробуйте поставить её на нормальное значение (20)");
		}
		try {
			ts = vk.messages().getLongPollServer(actor).execute().getTs();
		} catch (ApiAccessException e) {
			System.out.println(ChatColor.RED+"Код ошибки: "+e.getCode());
			System.out.println(ChatColor.RED+"В сайте https://vk.com/dev/errors описаны все ошибки связанные с ВК");
			if(e.getCode()==1) {
				System.out.println(ChatColor.RED+"Попробуйте запустить плагин чуть позже");
			}
			if (e.getCode() == 15) {
				System.out.println(ChatColor.RED + "Доступ запрещён.\r\n"
						+ "Убедитесь, что Вы не ошиблись в конфиге");
			}
			if (e.getCode() == 5) {
				System.out.println(ChatColor.RED + "Авторизация не удалась. \r\n"
						+ "Убедитесь, что Вы не ошиблись в конфиге");
			}
			System.out.println("\r\n"+"\r\n"+"\r\n"+"\r\n"+"\r\n");
			e.printStackTrace();
		} catch (ApiException e) {
			System.out.println(ChatColor.RED + "Ошибка:");
			e.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(this);
			;
			return;
		} catch (ClientException e) {
			System.out.println(ChatColor.RED + "Нету доступа к сайту!");
			e.printStackTrace();
		}
	}

}
