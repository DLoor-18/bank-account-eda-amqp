package ec.com.sofka;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.sofka.gateway.busMessage.ErrorBusMessage;
import ec.com.sofka.model.ErrorMessage;
import ec.com.sofka.queries.usecases.ErrorLogSavedViewUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class LogErrorBusListener implements ErrorBusMessage {
    private final ErrorLogSavedViewUseCase errorLogSavedViewUseCase;

    public LogErrorBusListener(ErrorLogSavedViewUseCase errorLogSavedViewUseCase) {
        this.errorLogSavedViewUseCase = errorLogSavedViewUseCase;
    }

    @Override
    @RabbitListener(queues = "${log.queue}")
    public void sendMsg(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ErrorMessage errorMessage = objectMapper.readValue(message, ErrorMessage.class);
            errorLogSavedViewUseCase.accept(errorMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
