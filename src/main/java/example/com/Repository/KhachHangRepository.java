package example.com.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import example.com.model.KhachHang;

public interface KhachHangRepository extends JpaRepository<KhachHang, String> {

    // tìm tên theo keyword
    List<KhachHang> findByTenKHContainingIgnoreCase(String keyword);

    // tìm theo số điện thoại
    List<KhachHang> findBySdtContainingIgnoreCase(String sdt);

    // tìm kiếm theo từ khóa (tên hoặc sđt)
    @Query("SELECT k FROM KhachHang k " +
           "WHERE LOWER(k.tenKH) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "   OR LOWER(k.sdt) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<KhachHang> searchByKeyword(@Param("keyword") String keyword);

    // Tìm theo địa chỉ
    List<KhachHang> findByDiaChiContainingIgnoreCase(String diaChi);
    
    // Tìm mã khách hàng lớn nhất
    @Query("SELECT MAX(k.maKH) FROM KhachHang k")
    String findLastMaKH();
}
