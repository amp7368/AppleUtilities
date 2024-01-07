package apple.utilities.database.util.merge.map;

import apple.utilities.database.util.ReflectionsUtil;
import apple.utilities.database.util.merge.ReflectionsMergeFn;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MergeOptionsHandleMapMergeSameIdImpl extends MergeOptionsHandleMapMergeSameId {

    private final Map<Class<?>, ReflectionsMergeFn<?>> mergeFn = new HashMap<>();

    public <T> MergeOptionsHandleMapMergeSameIdImpl addMergeFn(Class<T> type, ReflectionsMergeFn<T> mergeFn) {
        this.mergeFn.put(type, mergeFn);
        return this;
    }

    @Override
    protected ReflectionsMergeFn<?> findMergeFn(Map<?, ?> mergeOnto, Map<?, ?> mergeFrom) {
        List<Object> keys = Stream.concat(
                mergeOnto.keySet().stream(),
                mergeFrom.keySet().stream())
            .toList();
        return findMergeFn(keys);
    }

    private ReflectionsMergeFn<?> findMergeFn(List<Object> keys) {
        for (Class<?> typeI : mergeFn.keySet()) {
            for (Object key : keys) {
                Class<?> keyType = key.getClass();
                if (typeI.isAssignableFrom(keyType)) return mergeFn.get(typeI);
            }
        }
        return ReflectionsUtil::merge;
    }
}
