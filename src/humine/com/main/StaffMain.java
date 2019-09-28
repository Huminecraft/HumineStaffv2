package humine.com.main;

import humine.com.commands.*;
import humine.com.commands.permissions.PermissionCommand;
import humine.com.commands.voteban.HideVoteBoardCommand;
import humine.com.commands.voteban.OpenVoteBanCommand;
import humine.com.commands.voteban.VoteBanCommand;
import humine.com.events.*;
import humine.com.events.permissions.PermissionJoinEvent;
import humine.com.permissions.PermissionGroup;
import humine.com.permissions.PermissionGroupManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StaffMain extends JavaPlugin {

	private static StaffMain instance;
	
	private ArrayList<Player> vanished;
	private AutoMessage autoMessage;
	private VoteBan voteBan;
	private PermissionGroupManager permissionGroupManager;
	private List<String> PlayerInBed;

	@Override
	public void onEnable() {
		instance = this;
		this.vanished = new ArrayList<Player>();
		this.autoMessage = new AutoMessage();
		this.voteBan = new VoteBan();
		this.permissionGroupManager = new PermissionGroupManager();
		this.PlayerInBed = new ArrayList<String>();
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(player.isSleeping())
				this.PlayerInBed.add(player.getName());
		}
		this.saveDefaultConfig();
		FileManager.makeDeFaultConfiguration(this.getDataFolder());
		try {
			Message.initiliaze(this.getDataFolder());
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.autoMessage.getOnFile(this.getDataFolder());
		
		initializePermission();
		initiliazeEvents();
		initializeCommands();
		TPSCommand.startTPSManager();
	}
	
	@Override
	public void onDisable() {
		try {
			this.autoMessage.save(this.getDataFolder());
			FileManager.saveTPSConfig(TPSCommand.isEnabled());
			FileManager.saveUpvoteConfig(Check.getDelay(), Check.getReward());
			for(PermissionGroup group : this.permissionGroupManager.getPermissionGroups()) {
				File file = new File(this.getDataFolder(), "Group/"+group.getName()+".yml");
				group.save(file);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initializePermission()
	{
		File folder = new File(this.getDataFolder(), "Group");
		File prefixFile = new File(this.getDataFolder(), "prefix.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(prefixFile);

		String name;
		for(File file : Objects.requireNonNull(folder.listFiles())) {
			name = file.getName();
			name = name.substring(0, name.length() - 4);
			PermissionGroup group = new PermissionGroup(this, name);
			group.getSave(file);
			
			if(config.contains(group.getName()))
				group.setPrefix(config.getString(group.getName()));
			
			this.permissionGroupManager.addPermissionGroup(group);
		}
		
		if(!this.permissionGroupManager.containsDefaultPermissionGroup()) {
			PermissionGroup group = new PermissionGroup(this, "user");
			group.setDefault(true);
			for(Player player : Bukkit.getOnlinePlayers())
				group.addPlayer(player);
			
			this.permissionGroupManager.addPermissionGroup(group);
			
		}
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(StaffMain.getInstance().getPermissionGroupManager().containsPlayer(player)) {
				StaffMain.getInstance().getPermissionGroupManager().calculatePermission(player);
			}
			else if(StaffMain.getInstance().getPermissionGroupManager().containsDefaultPermissionGroup() && !StaffMain.getInstance().getPermissionGroupManager().getDefaultPermissionGroup().containsPlayer(player))
				StaffMain.getInstance().getPermissionGroupManager().addPlayerToDefault(player);
		}
	}
	
	private void initiliazeEvents() {
		this.getServer().getPluginManager().registerEvents(new SaveEnderChestEvent(), this);
		this.getServer().getPluginManager().registerEvents(new SavePlayerInventoryEvent(), this);
		this.getServer().getPluginManager().registerEvents(new MessageJoinEvent(), this);
		this.getServer().getPluginManager().registerEvents(new MessageQuitEvent(), this);
		this.getServer().getPluginManager().registerEvents(new BreakDiamondBlockEvent(), this);
		this.getServer().getPluginManager().registerEvents(new FilterBlockInEnderChestEvent(), this);
		this.getServer().getPluginManager().registerEvents(new PermissionJoinEvent(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerSleepEvent(), this);
	}
	
	private void initializeCommands() {
		Objects.requireNonNull(this.getCommand("invsee")).setExecutor(new OpenInventoryCommand());
		Objects.requireNonNull(this.getCommand("endsee")).setExecutor(new OpenEnderChestCommand());
		Objects.requireNonNull(this.getCommand("annonce")).setExecutor(new AnnonceCommand());
		Objects.requireNonNull(this.getCommand("vanish")).setExecutor(new VanishCommand());
		Objects.requireNonNull(this.getCommand("voteban")).setExecutor(new OpenVoteBanCommand());
		Objects.requireNonNull(this.getCommand("voteban")).setExecutor(new VoteBanCommand());
		Objects.requireNonNull(this.getCommand("voteban")).setExecutor(new HideVoteBoardCommand());
		Objects.requireNonNull(this.getCommand("automessage")).setExecutor(new AutoMessageCommand());
		Objects.requireNonNull(this.getCommand("lien")).setExecutor(new OpenLienCommand());
		Objects.requireNonNull(this.getCommand("permission")).setExecutor(new PermissionCommand());
		Objects.requireNonNull(this.getCommand("tpsmonitor")).setExecutor(new TPSCommand());
		Objects.requireNonNull(this.getCommand("upvote")).setExecutor(new UpvoteCommand());
	}
	
	
	public static StaffMain getInstance() {
		return instance;
	}
	
	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.DARK_AQUA + "[HumineStaff] " + ChatColor.RESET + message);
	}
	
	public static void sendBroadCastMessage(String message) {
		Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "[HumineStaff] " + ChatColor.RESET + message);
	}

	public ArrayList<Player> getVanished() {
		return vanished;
	}

	public AutoMessage getAutoMessage() {
		return autoMessage;
	}

	public VoteBan getVoteBan() {
		return voteBan;
	}

	public void setVoteBan(VoteBan voteBan) {
		this.voteBan = voteBan;
	}

	public PermissionGroupManager getPermissionGroupManager()
	{
		return permissionGroupManager;
	}

	public void setPermissionGroupManager(PermissionGroupManager permissionGroupManager)
	{
		this.permissionGroupManager = permissionGroupManager;
	}

	public List<String> getPlayerInBed() {
		return PlayerInBed;
	}

	public void setPlayerInBed(List<String> playerInBed) {
		PlayerInBed = playerInBed;
	}

}
