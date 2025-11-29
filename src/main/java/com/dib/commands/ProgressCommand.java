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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ProgressCommand extends BukkitCommand {

    // Using an Enum to structure the advancement data
    private enum TrackedAdvancement {
        ADVENTURING_TIME("adventure/adventuring_time"),
        MONSTERS_HUNTED("adventure/kill_all_mobs"),
        SMITHING_WITH_STYLE("adventure/trim_with_all_exclusive_armor_patterns"),
        HOT_TOURIST_DESTINATIONS("nether/explore_nether"),
        TWO_BY_TWO("husbandry/bred_all_animals"),
        BALANCED_DIET("husbandry/balanced_diet"),
        COMPLETE_CATALOGUE("husbandry/complete_catalogue");

        private final String key;

        TrackedAdvancement(String key) {
            this.key = key;
        }

        public NamespacedKey getNamespacedKey() {
            return NamespacedKey.minecraft(this.key);
        }

        public static TrackedAdvancement fromString(String name) {
            try {
                // Convert to uppercase for enum matching
                return valueOf(name.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    public ProgressCommand(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return true;
        }

        // Display help if no arguments are provided
        if (args.length == 0) {
            sendHelp(player, ChatColor.RED + "Usage: /progress <advancement> [done]");
            return true;
        }

        // Retrieve advancement via the Enum
        TrackedAdvancement target = TrackedAdvancement.fromString(args[0]);
        if (target == null) {
            sendHelp(player, ChatColor.RED + "Unknown advancement: " + args[0]);
            return true;
        }

        Advancement advancement = Bukkit.getAdvancement(target.getNamespacedKey());
        AdvancementProgress progress = player.getAdvancementProgress(advancement);

        // Display the results
        sendProgressMessage(player, args[0], progress, args.length > 1 && args[1].equalsIgnoreCase("showCompleted"));

        return true;
    }

    /**
     * Sends the formatted progress message to the player.
     * * @param player The player to send the message to.
     * @param advName The command input name of the advancement.
     * @param progress The AdvancementProgress object.
     * @param showCompleted Whether to show the list of completed criteria.
     */
    private void sendProgressMessage(Player player, String advName, AdvancementProgress progress, boolean showCompleted) {
        Collection<String> remaining = progress.getRemainingCriteria();
        Collection<String> awarded = progress.getAwardedCriteria();

        int total = awarded.size() + remaining.size();

        player.sendMessage(ChatColor.GOLD + "\n=== " + prettifyName(advName) + " ===");
        player.sendMessage(ChatColor.GREEN + "Progress: " + awarded.size() + "/" + total);

        if (progress.isDone()) {
            player.sendMessage(ChatColor.GREEN + "✓ Advancement completed!");
            return;
        }

        // Display remaining criteria
        player.sendMessage(ChatColor.RED + "\nRemaining :");
        player.sendMessage(ChatColor.WHITE + formatCriteriaList(remaining));

        // Optional display of awarded criteria
        if (showCompleted && !awarded.isEmpty()) {
            player.sendMessage(ChatColor.GREEN + "\nCompleted :");
            player.sendMessage(ChatColor.WHITE + formatCriteriaList(awarded));
        }
    }

    /**
     * Sends the usage and available advancements list to the player (DRY principle).
     * * @param player The player to send the help message to.
     * @param errorHeader The usage or error message to display first.
     */
    private void sendHelp(Player player, String errorHeader) {
        player.sendMessage(errorHeader);
        player.sendMessage(ChatColor.YELLOW + "Available advancements:");
        for (TrackedAdvancement adv : TrackedAdvancement.values()) {
            player.sendMessage(ChatColor.GRAY + "  - " + adv.name().toLowerCase());
        }
    }

    /**
     * Uses Streams to format the criteria list neatly (no trailing comma).
     * * @param criteria The collection of technical criteria names.
     * @return A comma-separated, colored list of user-friendly names.
     */
    private String formatCriteriaList(Collection<String> criteria) {
        return criteria.stream()
                .map(this::prettifyName)
                .map(s -> ChatColor.GOLD + s + ChatColor.WHITE)
                .collect(Collectors.joining(", "));
    }

    /**
     * Converts the technical criteria name (e.g., 'minecraft:oak_log') into a
     * user-friendly, capitalized name (e.g., 'Oak Log').
     * * @param raw The technical name.
     * @return The pretty name.
     */
    private String prettifyName(String raw) {
        // Standard cleanup (removing minecraft: namespace, replacing underscores)
        String clean = raw.replace("minecraft:", "").replace("_", " ");

        // Efficient capitalization logic
        char[] chars = clean.toCharArray();
        boolean capitalizeNext = true;
        for (int i = 0; i < chars.length; i++) {
            if (Character.isWhitespace(chars[i])) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                chars[i] = Character.toTitleCase(chars[i]);
                capitalizeNext = false;
            } else {
                chars[i] = Character.toLowerCase(chars[i]);
            }
        }
        return new String(chars);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        // Tab completion for advancement names
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0],
                    Arrays.stream(TrackedAdvancement.values()).map(e -> e.name().toLowerCase()).toList(),
                    new java.util.ArrayList<>());
        }
        // Tab completion for the optional "done" flag
        if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], List.of("showcompleted"), new java.util.ArrayList<>());
        }
        return List.of();
    }
}