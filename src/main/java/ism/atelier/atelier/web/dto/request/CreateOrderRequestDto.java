package ism.atelier.atelier.web.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequestDto {
    private String shippingAddress;
    private String phoneNumber;
    private String customerName;
    private String notes;
    private List<OrderItemRequestDto> items;
}