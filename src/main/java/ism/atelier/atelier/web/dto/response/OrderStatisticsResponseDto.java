package ism.atelier.atelier.web.dto.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatisticsResponseDto {
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private List<OrderResponseDto> recentOrders;
    private Map<String, Long> statusCounts;
}