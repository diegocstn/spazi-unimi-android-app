package it.unimi.unimiplaces.core.model;

/**
 * Available service item
 */
public class AvailableService extends BaseEntity {
    public String key;
    public String label;

    public AvailableService(String key, String label){
        this.key    = key;
        this.label  = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvailableService)) return false;

        AvailableService service = (AvailableService) o;

        if (key != null ? !key.equals(service.key) : service.key != null) return false;
        return !(label != null ? !label.equals(service.label) : service.label != null);

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }
}
