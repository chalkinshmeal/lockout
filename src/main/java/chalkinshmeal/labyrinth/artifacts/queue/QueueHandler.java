package chalkinshmeal.labyrinth.artifacts.queue;

import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.getRandNum;

public class QueueHandler {
    private final JavaPlugin plugin;
    private final ConfigHandler config;
    private final List<Queue> queues;

    public QueueHandler(JavaPlugin plugin, ConfigHandler config, String labyrinthName) {
        this.plugin = plugin;
        this.config = config;
        this.queues = getQueuesFromConfig(this.plugin, this.config, "labyrinths." + labyrinthName + ".queues");
    }

    public void addQueue(Location loc) { this.queues.add(new Queue(plugin, config, loc)); }
    public void clearQueue() { this.queues.clear(); }
    public Queue getRandQueue() { return this.queues.get(getRandNum(0, this.getQueueCount()-1)); }
    public List<Queue> getQueues() { return this.queues; }
    public int getQueueCount() { return this.queues.size(); }

    // Config
    public void setQueuesToConfig(String pathToQueues) {
        for (Queue queue : this.getQueues()) {
            int index = this.getQueues().indexOf(queue);
            queue.setQueueToConfig(pathToQueues + "." + index);
        }
    }
    public static List<Queue> getQueuesFromConfig(JavaPlugin plugin, ConfigHandler config, String pathToQueues) {
        if (!config.doesKeyExist(pathToQueues)) return new ArrayList<>();

        List<Queue> queues = new ArrayList<>();
        for (String labyrinthName : config.getKeyListFromKey(pathToQueues)) {
            int index = config.getKeyListFromKey(pathToQueues).indexOf(labyrinthName);
            queues.add(Queue.getQueueFromConfig(plugin, config,pathToQueues + "." + index));
        }
        return queues;
    }
}
