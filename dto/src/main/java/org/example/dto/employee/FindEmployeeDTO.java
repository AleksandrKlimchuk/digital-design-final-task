package org.example.dto.employee;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class FindEmployeeDTO {

    Long id;
    UUID account;

    public static FindEmployeeDTO ofId(@NonNull Long id) {
        return new FindEmployeeDTO(id, null);
    }

    public static FindEmployeeDTO ofAccount(@NonNull UUID account) {
        return new FindEmployeeDTO(null, account);
    }
}
