package apple.utilities.database.util.merge;

import java.rmi.AccessException;

@FunctionalInterface
public interface ReflectionsMergeFn<T> {

    T merge(T mergeOnto, T mergeFrom, int depth, ReflectionsMergeOptions options) throws AccessException;
}
