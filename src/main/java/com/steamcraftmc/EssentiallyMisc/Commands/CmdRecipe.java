package com.steamcraftmc.EssentiallyMisc.Commands;

import java.util.*;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import com.steamcraftmc.EssentiallyMisc.MainPlugin;
import com.steamcraftmc.EssentiallyMisc.utils.BlockUtil;

import org.bukkit.ChatColor;

public class CmdRecipe extends BaseCommand implements Listener, Runnable, TabCompleter {
	public final TreeMap<String, ItemStack> _itemDB = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	public final TreeMap<String, ItemStack> _rawNames = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	public final Set<UUID> _usingRecipe = new HashSet<UUID>();

	public CmdRecipe(MainPlugin plugin) {
		super(plugin, "recipe", 1, 255);
	}

	public void start() {
		if (_itemDB.size() == 0) {
			plugin.getServer().getScheduler().runTaskLater(plugin, new ItemDbBuilder(plugin, this), 5 * 20);
		}
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void stop() {
		cleanupOpenInventories();
    	HandlerList.unregisterAll(this);
	}

	class ItemDbBuilder implements Runnable {
		MainPlugin plugin;
		CmdRecipe Recipe;

		public ItemDbBuilder(MainPlugin plugin, CmdRecipe cmdRecipe) {
			this.plugin = plugin;
			this.Recipe = cmdRecipe;
		}

		@Override
		public void run() {
			Server svr = this.plugin.getServer();

			for (Material m : Material.values()) {
				for (byte dataId = 0; dataId < 16; dataId++) {
					ItemStack item = new ItemStack(m, 0, (short) 0, dataId);
					List<Recipe> rlist = svr.getRecipesFor(item);
					if (rlist == null) {
						continue;
					}
					for (Recipe recipe : rlist) {
						if (!(recipe instanceof org.bukkit.inventory.ShapedRecipe)
								&& !(recipe instanceof org.bukkit.inventory.ShapelessRecipe))
							continue;

						String rawName = BlockUtil.getRawName(recipe.getResult());
						short data = item.getData() == null ? 0 : item.getData().getData();

						// Manually address a few possible confusions in
						// naming...
						String name = item.getType() == Material.SNOW ? "Snow Layer"
								: item.getType() == Material.WOOD_BUTTON ? "Wood Button"
										: item.getType() == Material.SNOW_BLOCK ? "Snow Block"
												: BlockUtil.itemName(item.getTypeId(), data, true);

						if (this.Recipe._itemDB.containsKey(name)) {
							plugin.log(Level.WARNING, "Duplicate definition for " + name + ": " + rawName);
						} else {
							this.Recipe._itemDB.put(name, item);
						}
						this.Recipe._rawNames.put(rawName, item);
						break;
					}
				}
			}
		}
	}

	@Override
	public void run() {

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> found = new ArrayList<String>();

		String keyStart = String.join(" ", args).trim().toLowerCase();
		if (keyStart.length() <= 1) {
			return found;
		}
		int pruneLength = keyStart.length() - args[args.length - 1].trim().length();

		for (String key : _itemDB.subMap(keyStart, keyStart + Character.MAX_VALUE).keySet()) {
			String add = key;
			if (pruneLength > 0) {
				add = pruneLength <= key.length() ? key.substring(pruneLength).trim() : "";
				if (add.length() == 0) {
					continue;
				}
			}
			found.add(add);
		}

		return found;
	}

	@Override
	protected boolean doPlayerCommand(Player sender, Command cmd, String commandLabel, String[] args) throws Exception {
		if (args.length < 1) {
			return false;
		}
		
		List<String> found = new ArrayList<String>();
		String keyStart = String.join(" ", args).trim().toLowerCase();
		for (String key : _itemDB.subMap(keyStart, keyStart + Character.MAX_VALUE).keySet()) {
			found.add(key);
			if (found.size() > 1) {
				break;
			}
		}
		
		ItemStack item;
		if (found.size() == 0 || null == (item = _itemDB.get(found.get(0)))) {
			sender.sendMessage(plugin.Config.format("message.item-not-found", "&cUnknown item name '{name}'.", "name",
					String.join(" ", args).trim()));
			return true;
		} else if (found.size() > 1) {
			sender.sendMessage(plugin.Config.format("message.multiple-found",
					"&cToo many items matching that name, use <tab> to complete.", "name",
					String.join(" ", args).trim()));
			return true;
		}

		List<Recipe> recipesOfType = plugin.getServer().getRecipesFor(item);
		if (recipesOfType == null || recipesOfType.isEmpty()) {
			sender.sendMessage(plugin.Config.format("message.recipe-not-found", "&cRecipe for '{name}' not found.",
					"name", String.join(" ", args).trim()));
			return true;
		}

		Recipe selectedRecipe = recipesOfType.get(0);

		if (selectedRecipe instanceof FurnaceRecipe) {
			furnaceRecipe(sender, (FurnaceRecipe) selectedRecipe);
		} else if (selectedRecipe instanceof ShapedRecipe) {
			shapedRecipe(sender, (ShapedRecipe) selectedRecipe);
		} else if (selectedRecipe instanceof ShapelessRecipe) {
			if (recipesOfType.size() == 1 && item.getType() == Material.FIREWORK) {
				ShapelessRecipe shapelessRecipe = new ShapelessRecipe(item);
				shapelessRecipe.addIngredient(Material.SULPHUR);
				shapelessRecipe.addIngredient(Material.PAPER);
				shapelessRecipe.addIngredient(Material.FIREWORK_CHARGE);
				shapelessRecipe(sender, shapelessRecipe);
			} else {
				shapelessRecipe(sender, (ShapelessRecipe) selectedRecipe);
			}
		}

		return true;
	}

	public void shapedRecipe(final Player player, final ShapedRecipe recipe) {
		player.closeInventory();
		setRecipeSee(player, true);
		final InventoryView view = player.openWorkbench(null, true);
		final String[] recipeShape = recipe.getShape();
		final Map<Character, ItemStack> ingredientMap = recipe.getIngredientMap();
		for (int j = 0; j < recipeShape.length; j++) {
			for (int k = 0; k < recipeShape[j].length(); k++) {
				final ItemStack item = ingredientMap.get(recipeShape[j].toCharArray()[k]);
				if (item == null) {
					continue;
				}
				if (item.getDurability() == Short.MAX_VALUE) {
					item.setDurability((short) 0);
				}
				view.getTopInventory().setItem(j * 3 + k + 1, item);
			}
		}
	}

	public void shapelessRecipe(final Player player, final ShapelessRecipe recipe) {
		final List<ItemStack> ingredients = recipe.getIngredientList();

		player.closeInventory();
		setRecipeSee(player, true);
		final InventoryView view = player.openWorkbench(null, true);
		for (int i = 0; i < ingredients.size(); i++) {
			final ItemStack item = ingredients.get(i);
			if (item.getDurability() == Short.MAX_VALUE) {
				item.setDurability((short) 0);
			}
			view.setItem(i + 1, item);
		}
	}

	private void furnaceRecipe(Player player, FurnaceRecipe selectedRecipe) {
		player.sendMessage(ChatColor.RED + "Sorry, that isn't implemented yet.");
	}

	private void setRecipeSee(Player player, boolean b) {
		if (b)
			_usingRecipe.add(player.getUniqueId());
		else
			_usingRecipe.remove(player.getUniqueId());
	}

	public void cleanupOpenInventories() {
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (_usingRecipe.contains(player.getUniqueId())) {
				player.getOpenInventory().getTopInventory().clear();
				player.getOpenInventory().close();
				_usingRecipe.remove(player.getUniqueId());
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(final PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (_usingRecipe.contains(player.getUniqueId())) {
			player.getOpenInventory().getTopInventory().clear();
		}
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onInventoryClickEvent(final InventoryClickEvent event) {
		Player refreshPlayer = null;
		final Inventory top = event.getView().getTopInventory();
		final InventoryType type = top.getType();

		if (type == InventoryType.WORKBENCH && event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			if (_usingRecipe.contains(player.getUniqueId())) {
				event.setCancelled(true);
				refreshPlayer = player;
			}
		}

		if (refreshPlayer != null) {
			final Player player = refreshPlayer;
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					player.updateInventory();
				}
			}, 1);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryCloseEvent(final InventoryCloseEvent event) {
		Player refreshPlayer = null;
		final Inventory top = event.getView().getTopInventory();
		final InventoryType type = top.getType();

		if (type == InventoryType.WORKBENCH && event.getPlayer() instanceof Player) {
			final Player player = (Player) event.getPlayer();
			if (_usingRecipe.contains(player.getUniqueId())) {
				event.getView().getTopInventory().clear();
				refreshPlayer = player;
				_usingRecipe.remove(player.getUniqueId());
			}
		}

		if (refreshPlayer != null) {
			final Player player = refreshPlayer;
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					player.updateInventory();
				}
			}, 1);
		}
	}

}
