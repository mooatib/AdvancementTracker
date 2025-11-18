package com.dib.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ProgressCommand extends BukkitCommand {

    private final Map<String, String> ADVANCEMENT_PATHS = Map.of(
            "adventuring_time", "adventure/adventuring_time",
            "monsters_hunted", "adventure/kill_all_mobs",
            "smithing_with_style", "adventure/trim_with_all_exclusive_armor_patterns",
            "hot_tourist_destinations", "nether/explore_nether",
            "two_by_two", "husbandry/bred_all_animals",
            "balanced_diet", "husbandry/balanced_diet"
    );

    public ProgressCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by players.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /progress <advancement>");
            player.sendMessage(ChatColor.YELLOW + "Available advancements:");
            for (String key : ADVANCEMENT_PATHS.keySet()) {
                player.sendMessage(ChatColor.GRAY + "  - " + key);
            }
            return true;
        }
        String advancementName = args[0].toLowerCase().replace(" ", "_");

        String path = ADVANCEMENT_PATHS.get(advancementName);

        if (path == null) {
            player.sendMessage(ChatColor.RED + "Unknown advancement: " + args[0]);
            player.sendMessage(ChatColor.YELLOW + "Available advancements:");
            for (String key : ADVANCEMENT_PATHS.keySet()) {
                player.sendMessage(ChatColor.GRAY + "  - " + key);
            }
            return true;
        }

        NamespacedKey key = NamespacedKey.minecraft(path);
        Advancement advancement = Bukkit.getAdvancement(key);

        AdvancementProgress advancementProgress = player.getAdvancementProgress(advancement);

        Collection<String> remaining = advancementProgress.getRemainingCriteria();
        Collection<String> awarded = advancementProgress.getAwardedCriteria();

        StringBuilder message = new StringBuilder();
        for (String s : remaining) {
            message.append(ChatColor.GOLD + formatName(s)).append(ChatColor.WHITE + ", ");
        }

        int total = awarded.size() + remaining.size();
        int completed = awarded.size();

        player.sendMessage(ChatColor.GOLD + "\n=== " + formatName(advancementName) + " ===");
        player.sendMessage(ChatColor.GREEN + "Progress: " + completed + "/" + total);

        if (remaining.isEmpty()) {
            player.sendMessage(ChatColor.GREEN + "✓ Advancement completed!");
        } else {
            player.sendMessage(ChatColor.RED + "\nRemaining:");
            player.sendMessage(ChatColor.WHITE + message.toString());
        }

        return true;
    }

    private String formatName(String technicalName) {
        String name = technicalName.replaceFirst("^minecraft:", "");

        name = name.replace("_", " ");

        String[] words = name.split(" ");
        StringBuilder formatted = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                formatted.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    formatted.append(word.substring(1).toLowerCase());
                }
                formatted.append(" ");
            }
        }

        return formatted.toString().trim();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return (args.length == 1 ? StringUtil.copyPartialMatches(args[0], ADVANCEMENT_PATHS.keySet(), new ArrayList<>(ADVANCEMENT_PATHS.size())) : List.of());
    }
}
