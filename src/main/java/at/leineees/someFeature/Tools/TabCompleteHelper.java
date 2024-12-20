package at.leineees.someFeature.Tools;

import at.leineees.someFeature.CustomItems.CustomItems;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TabCompleteHelper {

    public static List<String> filterSuggestions(String input, List<String> suggestions) {
        List<String> filtered = new ArrayList<>();
        for (String suggestion : suggestions) {
            if (suggestion.toLowerCase().contains(input.toLowerCase())) {
                filtered.add(suggestion);
            }
        }
        return filtered;
    }
    
    /*public static List<String> filterSuggestions(String input, Set<String> suggestions) {
        List<String> filtered = new ArrayList<>();
        for (String suggestion : suggestions) {
            if (suggestion.toLowerCase().contains(input.toLowerCase())) {
                filtered.add(suggestion);
            }
        }
        return filtered;
    }*/
    public static List<String> filterSuggestions(String input, Set<NamespacedKey> suggestions) {
        List<String> filtered = new ArrayList<>();
        for (NamespacedKey suggestion : suggestions) {
            if (suggestion.asString().toLowerCase().contains(input.toLowerCase())) {
                filtered.add(suggestion.toString());
            }
        }
        return filtered;
    }
    
    public static List<String> getAllItems() {
        List<String> items = new ArrayList<>();
        for (NamespacedKey customItem : CustomItems.getAllCustomItems().keySet()) {
            items.add(customItem.toString());
        }
        for (Material material : Material.values()) {
            items.add("minecraft:" +  material.name().toLowerCase());
        }
        return items;
    }
    
}
