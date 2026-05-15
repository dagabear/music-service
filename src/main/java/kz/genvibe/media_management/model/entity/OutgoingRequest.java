package kz.genvibe.media_management.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kz.genvibe.media_management.model.entity.base.CreateEntity;
import lombok.*;

@Entity
@Table(name = "outgoing_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutgoingRequest extends CreateEntity {

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String requestBody;

    @Column(nullable = false)
    private short responseStatusCode;

    @Column(nullable = false)
    private String responseBody;

}
