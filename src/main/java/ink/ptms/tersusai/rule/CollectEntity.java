package ink.ptms.tersusai.rule;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.Objects;
import java.util.UUID;

/**
 * @author sky
 * @since 2019-08-24 7:55
 */
public class CollectEntity {

    private final UUID uniqueId;
    private Location location;
    private EntityType entityType;
    private LivingEntity livingEntity;
    private boolean checked;

    CollectEntity(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public CollectEntity(LivingEntity entity) {
        this.uniqueId = entity.getUniqueId();
        this.location = entity.getLocation();
        this.entityType = entity.getType();
        this.livingEntity = entity;
    }

    public CollectEntity check() {
        this.checked = true;
        return this;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public Location getLocation() {
        return location;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public LivingEntity getLivingEntity() {
        return livingEntity;
    }

    public boolean isChecked() {
        return checked;
    }

    public static CollectEntity of(UUID uuid) {
        return new CollectEntity(uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollectEntity)) {
            return false;
        }
        CollectEntity that = (CollectEntity) o;
        return Objects.equals(getUniqueId(), that.getUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId());
    }
}
