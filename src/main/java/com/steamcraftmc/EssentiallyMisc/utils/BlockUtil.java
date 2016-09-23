package com.steamcraftmc.EssentiallyMisc.utils;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;

import net.minecraft.server.v1_10_R1.Item;
import net.minecraft.server.v1_10_R1.ItemStack;

public class BlockUtil {

	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //

	public static Set<Material> getTransparentBlocks() {
		final HashSet<Material> _transparent = new HashSet<Material>();
		for (Material m : Material.values()) {
			if (m.isTransparent()) {
				_transparent.add(m);
			}
		}
		return _transparent;
	}
	
	public static String getRawName(org.bukkit.inventory.ItemStack item) {
		if (item.getData() != null)
			return String.valueOf(item.getType()) + ":" + item.getData().getData();
		return String.valueOf(item.getType() + ":0");
	}

	public static String properCase(String text) {
		char[] chars = text.toCharArray();
		boolean makeUpper = true;
		for (int ix = 0; ix < chars.length; ix++) {
			char ch = makeUpper ? Character.toUpperCase(chars[ix]) : Character.toLowerCase(chars[ix]);
			makeUpper = !Character.isLetter(ch);
			chars[ix] = makeUpper ? ' ' : ch;
		}

		return new String(chars).replaceAll("\\s\\s+", " ").trim();
	}
	
	public static String itemName(int blockId, int blockData, boolean hasData) {
		String tmp = null;
		try {
			ItemStack stck = new ItemStack(Item.getById(blockId), 0, blockData);
			tmp = stck == null ? null : stck.getName();
		}
		catch (Exception e) {}
		if (tmp == null || tmp.length() == 0) {
			tmp = Material.getMaterial(blockId).name();
		}
		tmp = properCase(tmp);
		if (!hasData) {
			tmp = tmp.replaceAll("\\s(On|Off)$", "");
			if (tmp.startsWith("White ")) {
				tmp = tmp.substring(6);
			}
		}
		return tmp;
	}

	public static String itemName(int mvcftId) {
		int blockData = 0, blockId = mvcftId;
		boolean hasData = false;
		if (blockId > 10000) {
			blockId -= 10000;
			blockData = blockId & 0x0F;
			blockId = blockId >> 4;
			hasData = true;
		}
		return itemName(blockId, blockData, hasData);
	}

	public static void itemNames(int blk, Set<String> blockList) {
		if (blk < 10000) {
			// Wool, Carpet, Stained Glass, Glass Pane, Clay
			if (blk == 35 || blk == 95 || blk == 159 || blk == 160 || blk == 171) {
				blockList.add(itemName(blk));
				return;
			}
			for (int ix = 0; ix < 16; ix++) {
				int shiftedID = (blk<<4) + ix + 10000;
				blockList.add(itemName(shiftedID));
			}
		}
		else {
			blockList.add(itemName(blk));
		}
	}

}
