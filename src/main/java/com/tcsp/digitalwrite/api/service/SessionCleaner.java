package com.tcsp.digitalwrite.api.service;

import com.tcsp.digitalwrite.store.repository.SessionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Instant;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
@Transactional
public class SessionCleaner {

    SessionRepository sessionRepository;

    @Scheduled(cron = "0 * * * * *")
    public void clean() {
        sessionRepository.deleteAllByExpiredAtBefore(Instant.now());
    }
}
