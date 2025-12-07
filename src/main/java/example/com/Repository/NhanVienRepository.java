package example.com.Repository;

import example.com.model.NhanVien;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NhanVienRepository extends JpaRepository<NhanVien, String> {

    // Tìm nhân viên theo tên (keyword)
    List<NhanVien> findByTenNVContainingIgnoreCase(String keyword);

    // Tìm theo email (duy nhất)
    Optional<NhanVien> findByEmailIgnoreCase(String email);

    // Tìm theo khoảng thời gian vào làm
    List<NhanVien> findByNgayVaoLamBetween(LocalDate start, LocalDate end);

    // Tìm theo ngày vào làm đúng
    List<NhanVien> findByNgayVaoLam(LocalDate date);

    // tìm theo mã tài khoản
    Optional<NhanVien> findByMaTK(String maTK);
}
