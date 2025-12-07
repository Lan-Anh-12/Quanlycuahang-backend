package example.com.Service.staff;

import java.util.List;
import example.com.Dto.nhanvien.NhanVienRequest;
import example.com.Dto.nhanvien.NhanVienWithTaiKhoanRequest;
import example.com.Dto.nhanvien.NhanVienResponse;

public interface NhanVienService {
    List<NhanVienResponse> layTatCa();
    NhanVienResponse layTheoMa(String maNV);
    
    // tạo nhân viên kèm tài khoản
    NhanVienResponse taoNhanVien(NhanVienWithTaiKhoanRequest nvRequest);
    
    // cập nhật nhân viên, ko cập nhật tài khoản
    NhanVienResponse capNhatNhanVien(String maNV, NhanVienRequest nvRequest);
    
    // xóa = đổi trạng thái
    void xoaNhanVien(String maNV);
    
    List<NhanVienResponse> timTheoTen(String tenNV);
    
    void ganTaiKhoanChoNhanVien(String maNV, String maTK);
}
