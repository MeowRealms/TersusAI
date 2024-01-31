package ink.ptms.tersusai.rule;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import taboolib.common.util.RandomKt;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author sky
 * @since 2019-08-24 8:29
 */
public class CollectTask {

    private final List<CollectGroup> groups = Lists.newCopyOnWriteArrayList();
    private final List<CollectEntity> entities;

    public CollectTask(List<CollectEntity> entities) {
        this.entities = Lists.newArrayList(entities);
    }

    public CollectTask collect() {
        groups.clear();
        CollectEntity random;
        while ((random = getRandom()) != null) {
            Set<CollectEntity> entities = getNearlyUnlimited(random, Sets.newHashSet());
            if (entities.size() > Collector.getCollectSize()) {
                groups.add(new CollectGroup(entities));
            }
            entities.forEach(CollectEntity::check);
        }
        return this;
    }

    public CollectEntity getRandom() {
        List<CollectEntity> randomList = entities.stream().filter(e -> !e.isChecked()).collect(Collectors.toList());
        return randomList.isEmpty() ? null : randomList.get(RandomKt.random(randomList.size()));
    }

    public Set<CollectEntity> getSingle() {
        Set<CollectEntity> entities = Sets.newHashSet(this.entities);
        groups.stream().flatMap(collectGroup -> collectGroup.getEntities().stream()).forEach(entities::remove);
        return entities;
    }

    public Set<CollectEntity> getNearly(CollectEntity center) {
        return entities.stream().filter(e -> center.getLocation().distance(e.getLocation()) < Collector.getCollectRange()).collect(Collectors.toSet());
    }

    public Set<CollectEntity> getNearlyUnlimited(CollectEntity center, Set<CollectEntity> total) {
        Set<CollectEntity> collect = Sets.newHashSet();
        Set<CollectEntity> nearly = getNearly(center);
        if (nearly.size() >= Collector.getCollectSize()) {
            Set<CollectEntity> recheck = nearly.stream().filter(e -> !total.contains(e)).collect(Collectors.toSet());
            collect.addAll(nearly);
            total.addAll(nearly);
            recheck.forEach(e -> collect.addAll(getNearlyUnlimited(e, total)));
        } else {
            collect.addAll(nearly);
        }
        return collect;
    }

    public List<CollectGroup> getGroups() {
        return groups;
    }

    public List<CollectEntity> getEntities() {
        return entities;
    }
}
