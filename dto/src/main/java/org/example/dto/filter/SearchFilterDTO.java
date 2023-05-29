package org.example.dto.filter;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.status.ProjectTeamRole;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class SearchFilterDTO {

    @NonNull
    String projectTitle;
    @Builder.Default
    ProjectTeamRole role = null;
    @Builder.Default
    String emailPrefix = null;
}
