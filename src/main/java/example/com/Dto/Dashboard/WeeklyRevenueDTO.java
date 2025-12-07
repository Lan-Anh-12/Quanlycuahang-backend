package example.com.Dto.Dashboard;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class WeeklyRevenueDTO {

    private String day;

    private BigDecimal revenue;
}
