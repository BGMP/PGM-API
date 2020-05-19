package cl.bgmp.apipgm.commands;

import cl.bgmp.apipgm.PGMAPI;
import cl.bgmp.apipgm.StatsManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

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
    if (args.length == 1) sender.sendMessage(statsManager.buildPrettyStatsMessage(args[0]));
    else sender.sendMessage(statsManager.buildPrettyStatsMessage(sender.getName()));

    return true;
  }
}
