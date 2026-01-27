package ru.skypro.homework.dto.metric;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Бизнес-метрика")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BusinessMetric {

    @Schema(description = "Общее количество записей")
    @Builder.Default
    private Long totalUsers = 0L;

    @Schema(description = "Количество зарегистрированных пользователей")
    @Builder.Default
    private Long activeUsers = 0L;

    @Schema(description = "Количество удаленных пользователей (soft-delete)")
    @Builder.Default
    private Long deletedUsers = 0L;


}
