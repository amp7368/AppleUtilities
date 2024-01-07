package apple.utilities.database.util.merge;

import apple.utilities.database.util.merge.map.MergeOptionsHandleMap;
import apple.utilities.database.util.merge.map.MergeOptionsHandleMapMergeSameIdEmpty;
import apple.utilities.database.util.merge.map.MergeOptionsHandleMapMergeSameIdImpl;

public class ReflectionsMergeOptions {

    private MergeOptionsHandleMap handleMap;

    public ReflectionsMergeOptions() {
        this.handleMap = new MergeOptionsHandleMapMergeSameIdEmpty();
    }

    public MergeOptionsHandleMap handleMap() {
        return handleMap;
    }

    public MergeOptionsHandleMapMergeSameIdImpl setHandleMapMergeOnSameId() {
        MergeOptionsHandleMapMergeSameIdImpl handleMap = new MergeOptionsHandleMapMergeSameIdImpl();
        this.handleMap = handleMap;
        return handleMap;
    }
}
