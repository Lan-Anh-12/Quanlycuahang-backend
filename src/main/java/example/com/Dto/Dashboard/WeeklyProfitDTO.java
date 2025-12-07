package example.com.Dto.Dashboard;

import java.math.BigDecimal;

public class WeeklyProfitDTO {
    private BigDecimal profit;

    public WeeklyProfitDTO(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
}
