package example.com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import example.com.model.DonHang;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DonHangRepository extends JpaRepository<DonHang, String> {

    // Tìm đơn theo mã KH
    List<DonHang> findByMaKH(String maKH);

    // Tìm đơn theo tháng
    @Query("SELECT d FROM DonHang d WHERE MONTH(d.ngayLap) = :month")
    List<DonHang> findByMonth(@Param("month") int month);

    // Tính tổng doanh thu theo tháng
    @Query("SELECT SUM(d.tongTien) FROM DonHang d WHERE MONTH(d.ngayLap) = :month")
    BigDecimal doanhThuTheoThang(@Param("month") int month);

    // Tìm đơn theo mã nhân viên
    List<DonHang> findByMaNV(String maNV);

    // Tìm đơn theo mã đơn hàng
    Optional<DonHang> findByMaDH(String maDH);

    // Tìm đơn trong khoảng thời gian
    List<DonHang> findByNgayLapBetween(LocalDateTime start, LocalDateTime end);

    // Tổng tiền khách đã chi
    @Query("SELECT SUM(d.tongTien) FROM DonHang d WHERE d.maKH = :maKH")
    BigDecimal tongTienChiCuaKhach(@Param("maKH") String maKH);

    // Số đơn khách đã mua
    @Query("SELECT COUNT(d) FROM DonHang d WHERE d.maKH = :maKH")
    int soDonHangCuaKhach(@Param("maKH") String maKH);

    // Tìm đơn hàng theo từ khóa (mã đơn hàng, mã khách hàng, mã nhân viên)
    @Query("SELECT d FROM DonHang d WHERE d.maDH LIKE %:keyword% OR d.maKH LIKE %:keyword% OR d.maNV LIKE %:keyword%")
    List<DonHang> searchByKeyword(@Param("keyword") String keyword);

    // lấy mã dh lớn nhất
    @Query(value = "SELECT MaDH FROM donhang ORDER BY MaDH DESC LIMIT 1", nativeQuery = true)
    String findLastMaDH();

    // Tổng số khách hàng đã mua đơn (distinct maKH)
    @Query("SELECT COUNT(DISTINCT d.maKH) FROM DonHang d")
    long totalCustomers();
}
