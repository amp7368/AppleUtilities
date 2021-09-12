package apple.utilities.request;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface AppleRequest<T> {
    T get() throws AppleRequestException;

    default AppleRequest<T> andThen(AppleRequest<T> n) {
        return new AppleRequestAndThen<>(this, n);
    }

    class AppleRequestAndThen<T> implements AppleRequest<T> {
        private final List<AppleRequest<T>> requests = new ArrayList<>();
        private AppleRequest<T> last;

        public AppleRequestAndThen(AppleRequest<T> oldRequest, AppleRequest<T> newRequest) {
            last = newRequest;
            requests.add(oldRequest);
        }

        @Override
        public AppleRequest<T> andThen(AppleRequest<T> n) {
            requests.add(last);
            last = n;
            return this;
        }

        @Override
        public T get() throws AppleRequestException {
            for (AppleRequest<T> request : requests) {
                request.get();
            }
            return last.get();
        }
    }

    class AppleRequestException extends Exception {
        public AppleRequestException() {
        }

        public AppleRequestException(String msg) {
            super(msg);
        }

        public AppleRequestException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    class AppleRuntimeRequestException extends RuntimeException {
        public AppleRuntimeRequestException() {
        }

        public AppleRuntimeRequestException(String msg) {
            super(msg);
        }

        public AppleRuntimeRequestException(Exception e) {
            super(e.getMessage(), e);
        }

        public AppleRuntimeRequestException(String msg, Throwable cause) {
            super(msg, cause);
        }


    }
}
