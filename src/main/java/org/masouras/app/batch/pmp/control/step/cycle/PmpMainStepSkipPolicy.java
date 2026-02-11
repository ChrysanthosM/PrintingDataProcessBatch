package org.masouras.app.batch.pmp.control.step.cycle;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.infrastructure.item.validator.ValidationException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Component
public class PmpMainStepSkipPolicy implements SkipPolicy {

    @Override
    public boolean shouldSkip(@NonNull Throwable throwable, long skipCount) throws SkipLimitExceededException {
        Throwable cause = Stream.iterate(throwable, Objects::nonNull, Throwable::getCause)
                .reduce((_, second) -> second)
                .orElse(null);

        if (cause instanceof ValidationException) {
            if (log.isWarnEnabled()) log.warn("Skipping ValidationException: {}", cause.getMessage());
            return true;
        }
        return false;
    }
}
