package example.com.Repository;

import example.com.model.NhapKho;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NhapKhoRepository extends JpaRepository<NhapKho, String> {
    
    // danh sách nhập kho theo mã nhân viên
    List<NhapKho> findByMaNV(String maNV);

    // danh sách theo nhà cung cấp
    List<NhapKho> findByNhaCungCap(String nhaCungCap);

    // đơn nhập kho theo ngày nhập hàng
    List<NhapKho> findByNgayNhap(LocalDateTime ngayNhap);

    // tìm theo mã phiếu nhập
    NhapKho findByMaNK(String maPhieuNhap);

    //tìm theo mã nhập or tên nhà cung cấp
    @Query("SELECT nk FROM NhapKho nk WHERE nk.maNK = :keyword OR LOWER(nk.nhaCungCap) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<NhapKho> findByMaNKOrNhaCungCap(@Param("keyword") String keyword);
}
