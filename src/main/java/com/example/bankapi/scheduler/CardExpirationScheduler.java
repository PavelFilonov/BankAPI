package com.example.bankapi.scheduler;

import com.example.bankapi.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import static com.example.bankapi.enums.CardStatus.EXPIRED;
import static com.example.bankapi.repository.CardRepository.deactivationDateEarlierThanSpec;
import static java.time.LocalDate.now;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardExpirationScheduler {

    private final CardRepository cardRepository;

    @Scheduled(
            cron = "${application.scheduler.card-expiration.cron}",
            zone = "${application.scheduler.timezone}"
    )
    @SchedulerLock(name = "expireCards")
    public void expireCards() {
        log.info("Начало планировщика expireCards");
        try {
            var cardsToExpire = cardRepository.findAll(deactivationDateEarlierThanSpec(now()));
            log.info("Найдено {} истёкших карт", cardsToExpire.size());
            if (!CollectionUtils.isEmpty(cardsToExpire)) {
                cardsToExpire.forEach(card -> card.setStatus(EXPIRED));
                cardRepository.saveAllAndFlush(cardsToExpire);
            }
        } catch (Exception e) {
            log.error("Ошибка в планировшике expireCards: {}", e.getMessage(), e);
        }
        log.info("Окончание планировщика expireCards");
    }

}
