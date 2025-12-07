package example.com.Repository;

import java.util.List;
import example.com.model.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, String> {
    // lấy tài khoản của nhân viên, quản lý
    List<TaiKhoan> findByRole(String role);

    // lấy tên tk
    Optional<TaiKhoan> findByUsername(String username);

    // tìm tài khoản theo mã tk
    Optional<TaiKhoan> findByMaTK(String maTK);


    

}
