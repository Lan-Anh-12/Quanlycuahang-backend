package example.com.Service.customer;

import example.com.Dto.khachhang.KhachHangRequest;
import example.com.Dto.khachhang.KhachHangResponse;

import java.util.List;

public interface KhachHangService {

    // CRUD khách hàng
    // KhachHangResponse taoKhachHang(KhachHangRequest kh);

    KhachHangResponse capNhatKhachHang(String maKH, KhachHangRequest kh); // cập nhật thông tin khách hàng

    KhachHangResponse layKhachHangTheoMa(String maKH);

    List<KhachHangResponse> layTatCaKhachHang();

    // Tìm kiếm
    List<KhachHangResponse> searchKhachHang(String ten); // tìm kiếm theo tên hoặc sđt

    List<KhachHangResponse> timKhachHangTheoTen(String tenKH); // tìm kiếm theo tên

    long demTongKhachHang(); // đếm tổng số khách hàng
}
