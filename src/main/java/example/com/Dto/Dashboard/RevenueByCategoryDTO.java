package example.com.Dto.Dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class RevenueByCategoryDTO {

    private String category;

    @JsonProperty("revenue_percent")
    private BigDecimal revenuePercent;

    @JsonProperty("revenue_amount")
    private BigDecimal revenueAmount;
}
