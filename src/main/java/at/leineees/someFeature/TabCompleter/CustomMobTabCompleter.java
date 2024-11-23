package at.leineees.someFeature.TabCompleter;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomMobTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("spawncustommob")) {
            if (args.length == 1) {
                return Arrays.stream(EntityType.values())
                        .filter(EntityType::isAlive)
                        .map(EntityType::name)
                        .collect(Collectors.toList());
            } else if (args.length == 2) {
                return Arrays.asList("<name_tag>");
            } else if (args.length == 3 || args.length == 4 || args.length == 5) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    switch (args.length) {
                        case 3:
                            return Arrays.asList(String.valueOf((int) player.getLocation().getX()));
                        case 4:
                            return Arrays.asList(String.valueOf((int) player.getLocation().getY()));
                        case 5:
                            return Arrays.asList(String.valueOf((int) player.getLocation().getZ()));
                    }
                }
            } else if (args.length == 6) {
                return Arrays.asList("true", "false", "<ai>");
            } else if (args.length == 7) {
                return Arrays.asList("true", "false", "<silent>");
            } else if (args.length == 8) {
                return Arrays.asList("true", "false", "<invulnerable>");
            } else if (args.length >= 9) {
                try {
                    Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                    commandMapField.setAccessible(true);
                    SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(Bukkit.getServer());
                    return commandMap.getCommands().stream()
                            .map(Command::getName)
                            .filter(cmd -> cmd.startsWith(args[args.length - 1]))
                            .collect(Collectors.toList());
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ArrayList<>();
    }
}