package org.remind.melembraai.domain.user.dto;

import lombok.Data;
import org.remind.melembraai.domain.user.model.User;
import org.remind.melembraai.utils.BRDatesUtils;

import java.util.UUID;

@Data
public class ResponseUserDTO {
    private UUID id;
    private String name;
    private String email;
    private Boolean isActive;
    private Boolean isVerified;
    private Boolean firstLogin;
    private String role;
    private String createdAt;
    private String updateAt;
    private String lastActivityDate;

    public ResponseUserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.isActive = user.getIsActive();
        this.isVerified = user.getIsVerified();
        this.firstLogin = user.getFirstLogin();
        this.role = user.getRole();
        this.createdAt = BRDatesUtils.transformLocalDateTimeToBrazilDateTimeFormatString(user.getCreatedAt());
        this.updateAt = BRDatesUtils.transformLocalDateTimeToBrazilDateTimeFormatString(user.getUpdateAt());
        this.lastActivityDate = BRDatesUtils.transformLocalDateTimeToBrazilDateTimeFormatString(user.getLastActivityDate());
    }
}
