package at.leineees.someFeature;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class SomeFeatureSettings {
    private final static SomeFeatureSettings instance = new SomeFeatureSettings();
    private File file;
    private YamlConfiguration settings;
    
    private SomeFeatureSettings() {}
    public static SomeFeatureSettings getInstance() {
        return instance;
    }
    
    public void load() {
        file = new File(SomeFeature.getInstance().getDataFolder(), "settings.yml");
        if(!file.exists()) {
            SomeFeature.getInstance().saveResource("settings.yml", false);
        }
        settings = new YamlConfiguration();
        settings.options().parseComments(true);
        try{
            settings.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void set(String path, Object value) {
        settings.set(path, value);
        
        save();
    }
    
    public void save() {
        try {
            settings.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getTablistHeader() {
        List<String> header = settings.getStringList("Tablist.Header");
        return header;
    }

    public List<String> getTablistFooter() {
        List<String> footer = settings.getStringList("Tablist.Footer");
        return footer;
    }
    
    public SomeFeatureSettings getSettings() {
        return instance;
    }
}