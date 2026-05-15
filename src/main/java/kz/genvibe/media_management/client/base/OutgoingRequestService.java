package kz.genvibe.media_management.client.base;

import kz.genvibe.media_management.repository.OutgoingRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutgoingRequestService {

    private final OutgoingRequestRepository outgoingRequestRepository;

    public void saveOutgoingRequest() {}

}
