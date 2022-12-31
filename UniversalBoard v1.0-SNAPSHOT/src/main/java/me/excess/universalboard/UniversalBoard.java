package me.excess.universalboard;

import io.github.thatkawaiisam.assemble.Assemble;
import io.github.thatkawaiisam.assemble.AssembleStyle;
import lombok.Getter;
import me.excess.universalboard.providers.ScoreboardProvider;
import me.excess.universalboard.utils.CC;
import me.excess.universalboard.utils.ConfigCreator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class UniversalBoard extends JavaPlugin {

    @Getter private static UniversalBoard instance;

    public ConfigCreator mainConfig;


    @Override
    public void onEnable() {
        instance = this;

        this.Files();
        this.scoreboard();
        Bukkit.getConsoleSender().sendMessage(CC.translate("&5&lUniversalBoard &2&lEnabled"));
    }

    private void scoreboard() {
        if(getMainConfig().getBoolean("SCOREBOARD.ENABLED")) {
            Assemble assemble = new Assemble(this, new ScoreboardProvider());

            assemble.setTicks(2);

            assemble.setAssembleStyle(AssembleStyle.VIPER);
        }
    }

    private void Files() {
        this.mainConfig = new ConfigCreator(this, "scoreboard");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
