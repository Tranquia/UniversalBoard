package me.excess.universalboard.providers;

import io.github.thatkawaiisam.assemble.AssembleAdapter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.excess.universalboard.UniversalBoard;
import me.excess.universalboard.utils.CC;
import me.excess.universalboard.utils.ConfigCreator;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ScoreboardProvider implements AssembleAdapter {

    private final ConfigCreator config = UniversalBoard.getInstance().getMainConfig();

    private long lastMillisFooter = System.currentTimeMillis();
    private long lastMillisTitle = System.currentTimeMillis();
    private int iFooter = 0;
    private int iTitle = 0;

    @Override
    public String getTitle(Player player) {
        if (config.getBoolean("SCOREBOARD.TITLE.ANIMATION.ENABLED")){
            return titles();
        } else {
            return CC.translate(config.getString("SCOREBOARD.TITLE.NO-ANIMATION.TITLE"));
        }
    }

    @Override
    public List<String> getLines(Player player) {
        List<String> toReturn = new ArrayList<>();

        config.getStringList("SCOREBOARD.LINES").stream()
                .map(CC::translate)
                .map(line -> PlaceholderAPI.setPlaceholders(player, line))
                .map(line -> line.replace("%player%", player.getName())) // Replace %player% to player names
                .forEach(toReturn::add);

        if(config.getBoolean("SCOREBOARD.FOOTER.ANIMATION.ENABLED")){
            String footer = footer();
            toReturn = toReturn.stream().map(s -> s.replace("%FOOTER%", footer)).collect(Collectors.toList());
        }

        if(config.getBoolean("SCOREBOARD.TITLE.ANIMATION.ENABLED")) {
            String title = titles();
            toReturn = toReturn.stream().map(s -> s.replace("%TITLE%", title)).collect(Collectors.toList());
        }

        return toReturn;
    }

    private String footer() {
        List<String> footers = CC.translate(config.getStringList("SCOREBOARD.FOOTER.ANIMATION.FOOTER"));
        long time = System.currentTimeMillis();
        long interval = TimeUnit.SECONDS.toMillis(config.getInt("SCOREBOARD.FOOTER.ANIMATION.INTERVAL"));

        if(lastMillisFooter + interval <= time) {
            if(iFooter != footers.size() - 1) {
                iFooter++;
            } else {
                iFooter = 0;
            }
            lastMillisFooter = time;
        }
        return footers.get(iFooter);
    }

    private String titles() {
        List<String> titles = CC.translate(config.getStringList("SCOREBOARD.TITLE.ANIMATION.TITLE"));
        long time = System.currentTimeMillis();
        long interval = TimeUnit.SECONDS.toMillis(config.getInt("SCOREBOARD.TITLE.ANIMATION.INTERVAL"));

        if(lastMillisTitle+ interval <= time) {
            if(iTitle != titles.size() - 1) {
                iTitle++;
            } else {
                iTitle = 0;
            }
            lastMillisTitle = time;
        }
        return titles.get(iTitle);
    }
}
