package apple.utilities.database.util.merge.map;

import apple.utilities.database.util.ReflectionsUtil;
import apple.utilities.database.util.merge.ReflectionsMergeFn;
import java.util.Map;

public class MergeOptionsHandleMapMergeSameIdEmpty extends MergeOptionsHandleMapMergeSameId {

    @Override
    protected ReflectionsMergeFn<?> findMergeFn(Map<?, ?> mergeOnto, Map<?, ?> mergeFrom) {
        return ReflectionsUtil::merge;
    }
}
