package example.com.Service.order;

import java.time.LocalDateTime;
import java.util.List;

import example.com.Dto.donhang.ChiTietDonHangResponse;
import example.com.Dto.donhang.DonHangRequest;
import example.com.Dto.donhang.DonHangResponse;
import example.com.Dto.donhang.CapNhatDonHangRequest;


public interface DonHangService {
    //CRUD 
    void TaoDonHang(DonHangRequest donHangRequest);
    DonHangResponse CapNhatDonHang(CapNhatDonHangRequest request);

    // lấy đơn hàng, ct đơn hàng
    List<DonHangResponse> layHetDonHang(); 
    List<DonHangResponse> LayDonHangTheoKhachHang(String maKH);
    List<ChiTietDonHangResponse> LayChiTietDonHangTheoDonHang(String maDH);
    List<DonHangResponse> XemDonHangNVLap(String maNV);
    List<DonHangResponse> LayDonHangTheoKhoangNgay(LocalDateTime start, LocalDateTime end);
    

    
}
