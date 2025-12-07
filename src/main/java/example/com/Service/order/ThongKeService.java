package example.com.Service.order;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ThongKeService {

    Map<String, Object> thongKeTheoThang(int month, int year);

    Map<String, Object> thongKeTheoKhoangNgay(LocalDate start, LocalDate end);

    Map<String, Object> thongKeTheoNhanVien(String maNV);

    Map<String, Object> thongKeTheoNgay(LocalDate date);

    Map<String, Object> thongKeTheoKhachHang(String maKH);

    List<Map<String, Object>> sanPhamBanChayTheoThang(int thang, int nam);
}
