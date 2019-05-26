package net.jan_br.autoconfig;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;

public interface ConfigurationPath<T> {

    ConfigurationPath getPath(String path);

    Object get(String key);

    ConfigurationPath set(String key, Object value);

    Collection<String> getKeys();

    T unsafe();

    default Object[] toObjectArray(Object primitive) {
        if (primitive instanceof int[]) {
        } else if (primitive instanceof float[]) {
            primitive = ArrayUtils.toObject((float[]) primitive);
        } else if (primitive instanceof double[]) {
            primitive = ArrayUtils.toObject((double[]) primitive);
        } else if (primitive instanceof long[]) {
            primitive = ArrayUtils.toObject((long[]) primitive);
        } else if (primitive instanceof char[]) {
            primitive = ArrayUtils.toObject((char[]) primitive);
        } else if (primitive instanceof boolean[]) {
            primitive = ArrayUtils.toObject((boolean[]) primitive);
        } else if (primitive instanceof byte[]) {
            primitive = ArrayUtils.toObject((byte[]) primitive);
        } else if (primitive instanceof short[]) {
            primitive = ArrayUtils.toObject((short[]) primitive);
        }
        return (Object[]) primitive;
    }

}
