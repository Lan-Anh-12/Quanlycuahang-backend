package example.com.Repository;

import java.util.List;
import java.math.BigDecimal;
import example.com.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SanPhamRepository extends JpaRepository<SanPham, String> {

    // lấy theo tên 
    List<SanPham> findByTenSPContainingIgnoreCase(String keyword);
    Optional<SanPham> findByTenSP(String tenSP);

    // lấy sản phẩm theo loại
    List<SanPham> findByPhanLoaiContainingIgnoreCase(String loai);
    
    // lấy theo MoTa
    List<SanPham> findByMoTaContaining(String keyword);

    // lấy ra số lượng theo mã sp
    @Query("SELECT s.soLuong FROM SanPham s WHERE s.maSP = :maSP")
    Integer getSoLuongByMaSP(@Param("maSP") String maSP);

    // lấy ra số lượng theo tên
    @Query("SELECT s.soLuong FROM SanPham s WHERE LOWER(s.tenSP) LIKE LOWER(CONCAT('%', :tenSP, '%'))")
    Integer getSoLuongByTenSP(String tenSP);

    // lấy url theo mã
    @Query("SELECT s.url FROM SanPham s WHERE s.maSP = :maSP")
    String findUrlByMaSP(@Param("maSP") String maSP);
    
    // xem còn hàng không 
    List<SanPham> findByTrangThai(String trangThai);

    // Lấy thông tin sản phẩm theo mã
    Optional<SanPham> findByMaSP(String maSP);

    // lấy sản phẩm còn bán 
    @Query("SELECT sp FROM SanPham sp WHERE sp.trangThai <> 'An'")
    List<SanPham> findAllNonDeleted();

    // lấy tên sp theo mã sp
    @Query("SELECT s.tenSP FROM SanPham s WHERE s.maSP = :maSP")
    String findTenSPByMaSP(@Param("maSP") String maSP);

    // tìm kiếm sp theo từ khóa
    @Query("SELECT s FROM SanPham s " +
           "WHERE LOWER(s.tenSP) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "   OR LOWER(s.phanLoai) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<SanPham> searchByKeyword(@Param("keyword") String keyword);

    //CRUD sản phẩm

    // xóa sp (soft delete)
    @Modifying
    @Transactional
    @Query("UPDATE SanPham s SET s.trangThai = 'An' WHERE s.maSP = :maSP")
    void softDelete(@Param("maSP") String maSP);

    // tăng số lượng
    @Modifying
    @Transactional
    @Query("UPDATE SanPham s SET s.soLuong = s.soLuong + :amount WHERE s.maSP = :maSP")
    int tangSoLuong(@Param("maSP") String maSP, @Param("amount") int amount);

    // cập nhật trạng thái
    @Transactional
    @Modifying
    @Query("UPDATE SanPham sp SET sp.trangThai = :trangThai WHERE sp.maSP = :maSP")
    void updateTrangThai(@Param("maSP") String maSP,
                         @Param("trangThai") String trangThai);

       // lấy mã sp lớn nhất
   @Query(value = "SELECT MaSP FROM sanpham ORDER BY MaSP DESC LIMIT 1", nativeQuery = true)
   String findMaxMaSP();
   
       // lấy đơn giá theo mã sp
   @Query("SELECT s.donGia FROM SanPham s WHERE s.maSP = :maSP")
    BigDecimal findDonGiaByMaSP(String maSP);

}
