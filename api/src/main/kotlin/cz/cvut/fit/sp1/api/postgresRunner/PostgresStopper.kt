package cz.cvut.fit.sp1.api.postgresRunner

import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PostgresStopper(
    private val postgresRunner: PostgresRunner?
) {
    @EventListener(ContextClosedEvent::class)
    fun onAppStop() {
        postgresRunner?.stop()
    }

}