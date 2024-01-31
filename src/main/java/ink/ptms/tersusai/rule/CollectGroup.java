package ink.ptms.tersusai.rule;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.bukkit.Location;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @author sky
 * @since 2019-08-24 8:04
 */
public class CollectGroup {

    private final Set<CollectEntity> entities;
    private final CollectEntity[] maxDistanceCache;
    private final Location center;
    private final double range;

    public CollectGroup(Set<CollectEntity> entities) {
        this.entities = entities;
        this.maxDistanceCache = getMaxDistance();
        this.center = maxDistanceCache[0].getLocation().toVector().midpoint(maxDistanceCache[1].getLocation().toVector()).toLocation(maxDistanceCache[0].getLocation().getWorld());
        this.range = center.distance(maxDistanceCache[0].getLocation());
    }

    public int getDensity() {
        Set<Location> location = Sets.newHashSet();
        return (int) entities.stream().filter(e -> location.add(new Location(e.getLocation().getWorld(), e.getLocation().getBlockX(), e.getLocation().getBlockY(), e.getLocation().getBlockZ()))).count();
    }

    public CollectEntity[] getMaxDistanceCache() {
        return maxDistanceCache;
    }

    public CollectEntity[] getMaxDistance() {
        if (entities.size() < 2) {
            return null;
        }
        CollectEntity[][] array = new CollectEntity[6][2];
        List<CollectEntity> list = Lists.newArrayList(entities);
        list.sort(Comparator.comparingDouble(a -> a.getLocation().getX()));
        array[0][0] = list.get(0);
        list.sort((b, a) -> Double.compare(a.getLocation().getX(), b.getLocation().getX()));
        array[1][0] = list.get(0);
        list.sort(Comparator.comparingDouble(a -> a.getLocation().getY()));
        array[2][0] = list.get(0);
        list.sort((b, a) -> Double.compare(a.getLocation().getY(), b.getLocation().getY()));
        array[3][0] = list.get(0);
        list.sort(Comparator.comparingDouble(a -> a.getLocation().getZ()));
        array[4][0] = list.get(0);
        list.sort((b, a) -> Double.compare(a.getLocation().getZ(), b.getLocation().getZ()));
        array[5][0] = list.get(0);
        Arrays.stream(array).forEach(a -> a[1] = getMaxDistance(a[0], list));
        List<CollectEntity[]> collect = Lists.newArrayList(array);
        collect.sort((b, a) -> Double.compare(a[0].getLocation().distance(a[1].getLocation()), b[0].getLocation().distance(b[1].getLocation())));
        return collect.get(0);
    }

    private CollectEntity getMaxDistance(CollectEntity entity, List<CollectEntity> entities) {
        entities.sort((b, a) -> Double.compare(a.getLocation().distance(entity.getLocation()), b.getLocation().distance(entity.getLocation())));
        return entities.get(0);
    }

    public Set<CollectEntity> getEntities() {
        return entities;
    }

    public Location getCenter() {
        return center;
    }

    public double getRange() {
        return range;
    }
}
