package example.com.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import example.com.Service.customer.KhachHangService;
import example.com.Service.inventory.InventoryService;
import example.com.Service.order.DonHangService;
import example.com.Service.order.ThongKeService;
import example.com.Dto.Dashboard.*;
import example.com.Dto.donhang.ChiTietDonHangResponse;
import example.com.Dto.donhang.DonHangResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private ThongKeService thongKeService;

    // ========================
    // 1. KPIs: tổng khách hàng
    // ========================
    @GetMapping("/kpis")
    public long getDashboardKPI() {
        long totalCustomers = khachHangService.demTongKhachHang();  
        return totalCustomers;
    }

    // ========================
    // 2. Top 5 khách hàng theo tổng chi tiêu
    // ========================
    @GetMapping("/top-customers")
    public List<CustomerRankingDTO> getTopCustomers() {
        List<CustomerRankingDTO> result = khachHangService.layTatCaKhachHang().stream()
            .map(c -> {
                BigDecimal totalSpent = donHangService.LayDonHangTheoKhachHang(c.getMaKH()).stream()
                        .map(o -> o.getTongTien())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                CustomerRankingDTO dto = new CustomerRankingDTO();
                dto.setName(c.getTenKH());
                dto.setTotalSpent(totalSpent);
                return dto;
            })
            .sorted((a, b) -> b.getTotalSpent().compareTo(a.getTotalSpent()))
            .limit(5)
            .collect(Collectors.toList());

        for (int i = 0; i < result.size(); i++) {
            result.get(i).setRank(i + 1);
        }

        return result;
    }

    // ========================
    // 3. Doanh thu tuần hiện tại (Thứ 2 -> Chủ nhật)
    // ========================
    @GetMapping("/weekly-revenue")
    public List<WeeklyRevenueDTO> getWeeklyRevenue() {
        LocalDate now = LocalDate.now();
        LocalDate monday = now.with(DayOfWeek.MONDAY);

        List<WeeklyRevenueDTO> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate day = monday.plusDays(i);
            BigDecimal revenue = donHangService.LayDonHangTheoKhoangNgay(
                    day.atStartOfDay(),
                    day.plusDays(1).atStartOfDay()
            ).stream()
             .map(o -> o.getTongTien())
             .reduce(BigDecimal.ZERO, BigDecimal::add);

            String dayName = switch (day.getDayOfWeek()) {
                case MONDAY -> "Thứ Hai";
                case TUESDAY -> "Thứ Ba";
                case WEDNESDAY -> "Thứ Tư";
                case THURSDAY -> "Thứ Năm";
                case FRIDAY -> "Thứ Sáu";
                case SATURDAY -> "Thứ Bảy";
                case SUNDAY -> "Chủ Nhật";
            };

            WeeklyRevenueDTO dto = new WeeklyRevenueDTO();
            dto.setDay(dayName);
            dto.setRevenue(revenue);
            result.add(dto);
        }
        return result;
    }

    // ========================
    // 4. Sản phẩm sắp hết hàng (tồn kho <= 20)
    // ========================
    @GetMapping("/low-stock")
    public List<LowStockProductDTO> getLowStockProducts() {
        return inventoryService.sanPhamConBan().stream()
                .map(p -> {
                    int stock = ((Number) inventoryService.xemTonKho(p.getMaSP())).intValue();
                    LowStockProductDTO dto = new LowStockProductDTO();
                    dto.setName(p.getTenSP());
                    dto.setStock(stock);
                    return dto;
                })
                .filter(p -> p.getStock() <= 20)
                .sorted(Comparator.comparingInt(LowStockProductDTO::getStock)) // sắp xếp tăng dần
                .limit(5) // lấy tối đa 10 sản phẩm
                .collect(Collectors.toList());
    }

    // ========================
    // 5. Doanh thu theo category (tháng hiện tại)
    // ========================
    @GetMapping("/revenue-by-category")
public List<RevenueByCategoryDTO> getRevenueByCategory() {
    // Lấy tất cả đơn hàng
    List<DonHangResponse> allOrders = donHangService.layHetDonHang();

    // Map từ maSP -> category
    Map<String, String> productCategoryMap = inventoryService.sanPhamConBan().stream()
            .collect(Collectors.toMap(p -> p.getMaSP(), p -> p.getPhanLoai()));

    Map<String, BigDecimal> categoryRevenue = new HashMap<>();

    for (DonHangResponse order : allOrders) {
        for (ChiTietDonHangResponse ct : order.getChiTiet()) {
            String category = productCategoryMap.getOrDefault(ct.getMaSP(), "Khác");
            BigDecimal amount = ct.getThanhTien();
            categoryRevenue.put(category, categoryRevenue.getOrDefault(category, BigDecimal.ZERO).add(amount));
        }
    }

    BigDecimal totalRevenue = categoryRevenue.values().stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    List<RevenueByCategoryDTO> result = categoryRevenue.entrySet().stream()
            .map(entry -> {
                BigDecimal percent = totalRevenue.compareTo(BigDecimal.ZERO) > 0
                        ? entry.getValue().multiply(BigDecimal.valueOf(100))
                          .divide(totalRevenue, 0, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;

                RevenueByCategoryDTO dto = new RevenueByCategoryDTO();
                dto.setCategory(entry.getKey());
                dto.setRevenueAmount(entry.getValue());
                dto.setRevenuePercent(percent);
                return dto;
            })
            .collect(Collectors.toList());

    return result;
}
    


}
