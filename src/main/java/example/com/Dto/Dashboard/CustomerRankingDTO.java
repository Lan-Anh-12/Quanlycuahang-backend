package example.com.Dto.Dashboard;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CustomerRankingDTO {

    private int rank;

    private String name;

    @JsonProperty("total_spent")
    private BigDecimal totalSpent;
}
