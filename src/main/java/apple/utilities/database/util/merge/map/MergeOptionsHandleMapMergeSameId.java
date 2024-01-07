package apple.utilities.database.util.merge.map;

import apple.utilities.database.util.merge.ReflectionsMergeFn;
import apple.utilities.database.util.merge.ReflectionsMergeOptions;
import java.rmi.AccessException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class MergeOptionsHandleMapMergeSameId implements MergeOptionsHandleMap {

    @Override
    public Map<?, ?> handleMap(Map<?, ?> mergeOnto, Map<?, ?> mergeFrom, int depth, ReflectionsMergeOptions options)
        throws AccessException {
        @SuppressWarnings("rawtypes")
        ReflectionsMergeFn mergeFn = findMergeFn(mergeOnto, mergeFrom);

        Map<Object, Object> handledMap = new HashMap<>(mergeOnto);
        for (Entry<?, ?> entry : mergeFrom.entrySet()) {
            Object mergeOntoValue = handledMap.get(entry.getKey());
            if (mergeOntoValue == null) {
                handledMap.put(entry.getKey(), entry.getValue());
                continue;
            }
            Object mergeFromValue = entry.getValue();

            @SuppressWarnings("unchecked")
            Object mergedValue = mergeFn.merge(mergeOntoValue, mergeFromValue, depth + 1, options);
            handledMap.put(entry.getKey(), mergedValue);
        }
        return handledMap;
    }

    protected abstract ReflectionsMergeFn<?> findMergeFn(Map<?, ?> mergeOnto, Map<?, ?> mergeFrom);
}
