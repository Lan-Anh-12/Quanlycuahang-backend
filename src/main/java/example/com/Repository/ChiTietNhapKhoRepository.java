package example.com.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import example.com.model.CT_NhapKho;

import java.util.List;

public interface ChiTietNhapKhoRepository extends JpaRepository<CT_NhapKho,String > {
    // Danh sách chi tiết nhập kho theo mã nhập kho
    List<CT_NhapKho> findByMaNK(String maNK);


}
