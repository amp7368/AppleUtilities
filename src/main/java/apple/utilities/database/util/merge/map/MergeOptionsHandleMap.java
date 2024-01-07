package apple.utilities.database.util.merge.map;

import apple.utilities.database.util.merge.ReflectionsMergeOptions;
import java.rmi.AccessException;
import java.util.Map;

public interface MergeOptionsHandleMap {

    Map<?, ?> handleMap(Map<?, ?> initialFieldVal, Map<?, ?> loadedFieldVal, int depth, ReflectionsMergeOptions options)
        throws AccessException;
}
