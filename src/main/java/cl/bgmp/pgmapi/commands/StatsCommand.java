package cl.bgmp.pgmapi.commands;

import cl.bgmp.pgmapi.PGMAPI;
import cl.bgmp.pgmapi.StatsManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import tc.oc.pgm.util.component.ComponentUtils;

public class StatsCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof ConsoleCommandSender) {
      sender.sendMessage(ChatColor.RED + "You must be a player to execute this command.");
      return true;
    }

    if (!sender.hasPermission("pgmapi.stats")) {
      sender.sendMessage(ChatColor.RED + "You do not have permission.");
      return true;
    }

    if (args.length > 1) {
      sender.sendMessage(ChatColor.RED + "Too many arguments.");
      return true;
    }

    final StatsManager statsManager = PGMAPI.get().getStatsManager();
    if (args.length == 1) {
      final String targetNick = args[0];

      if (targetNick.toCharArray().length > 16) {
        sender.sendMessage(ChatColor.RED + "Name is too long.");
        return true;
      }

      final Pattern pattern = Pattern.compile("[A-Za-z0-9]+(_[A-Za-z0-9]+)*");
      final Matcher matcher = pattern.matcher(targetNick);
      if (!matcher.matches()) {
        sender.sendMessage(ChatColor.RED + "Invalid player name.");
        return true;
      }

      if (Bukkit.getPlayer(targetNick) == null) {
        sender.sendMessage(
            ComponentUtils.horizontalLineHeading(
                ChatColor.DARK_AQUA + targetNick, ChatColor.RED, ComponentUtils.MAX_CHAT_WIDTH));
      } else {
        sender.sendMessage(
            ComponentUtils.horizontalLineHeading(
                Bukkit.getPlayer(targetNick).getDisplayName(),
                ChatColor.RED,
                ComponentUtils.MAX_CHAT_WIDTH));
      }
      sender.sendMessage(statsManager.buildPrettyStatsMessage(targetNick));
    } else {
      sender.sendMessage(
          ComponentUtils.horizontalLineHeading(
              ((Player) sender).getDisplayName(), ChatColor.RED, ComponentUtils.MAX_CHAT_WIDTH));
      sender.sendMessage(statsManager.buildPrettyStatsMessage(sender.getName()));
    }

    return true;
  }
}
