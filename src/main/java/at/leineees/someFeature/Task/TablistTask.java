package at.leineees.someFeature.Task;

import at.leineees.someFeature.SomeFeatureSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TablistTask implements Runnable {
    private static final TablistTask instance = new TablistTask();
    private final Map<UUID, Integer> headerPosition = new HashMap<>();
    private final Map<UUID, Integer> footerPosition = new HashMap<>();

    private TablistTask() {
    }

    public static TablistTask getInstance() {
        return instance;
    }

    @Override
    public void run() {
        List<String> headerLines = SomeFeatureSettings.getInstance().getTablistHeader();
        List<String> footerLines = SomeFeatureSettings.getInstance().getTablistFooter();

        //MiniMessage miniMessage = MiniMessage.miniMessage();

        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            int headerPos = headerPosition.getOrDefault(uuid, 0) % headerLines.size();
            int footerPos = footerPosition.getOrDefault(uuid, 0) % footerLines.size();

            player.setPlayerListHeaderFooter(
                    "§a" + replacePlaceholders(headerLines.get(headerPos), player),
                    "§c" + replacePlaceholders(footerLines.get(footerPos), player));

            /*player.setPlayerListHeaderFooter(
                    (BaseComponent) miniMessage.deserialize(replacePlaceholders(headerLines.get(headerPos), player)),
                    (BaseComponent) miniMessage.deserialize(replacePlaceholders(footerLines.get(footerPos), player)));*/

            headerPosition.put(uuid, (headerPos + 1) % headerLines.size());
            footerPosition.put(uuid, (footerPos + 1) % footerLines.size());
        }
    }

    private String replacePlaceholders(String line, Player player) {
        return line.replace("%player%", player.getName());
    }
}