package example.com.Controller;

import example.com.Dto.donhang.DonHangRequest;
import example.com.Dto.donhang.DonHangResponse;
import example.com.Dto.donhang.ChiTietDonHangRequest;
import example.com.Dto.donhang.ChiTietDonHangResponse;
import example.com.Dto.donhang.CapNhatDonHangRequest;
import example.com.Service.order.DonHangService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/quanly/donhang")
public class QuanLyDonHangController {

    @Autowired
    private DonHangService donHangService;

        // Tạo đơn hàng
    @PostMapping("/tao")
    public void taoDonHang(@RequestBody DonHangRequest req) {
        donHangService.TaoDonHang(req);
    }

    // Lấy đơn hàng theo khách hàng
    @GetMapping("/khachhang/{maKH}")
    public ResponseEntity<List<DonHangResponse>> layDonHangTheoKhachHang(@PathVariable String maKH) {
        List<DonHangResponse> donHangs = donHangService.LayDonHangTheoKhachHang(maKH);
        if (donHangs.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(donHangs);
    }

    // Lấy đơn hàng theo nhân viên lập
    @GetMapping("/nhanvien/{maNV}")
    public ResponseEntity<List<DonHangResponse>> layDonHangNhanVien(@PathVariable String maNV) {
        List<DonHangResponse> donHangs = donHangService.XemDonHangNVLap(maNV);
        if (donHangs.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(donHangs);
    }

    // Lấy chi tiết đơn hàng theo mã đơn
    @GetMapping("/chitiet/{maDH}")
    public ResponseEntity<List<ChiTietDonHangResponse>> layChiTietDonHang(@PathVariable String maDH) {
        List<ChiTietDonHangResponse> chiTiets = donHangService.LayChiTietDonHangTheoDonHang(maDH);
        if (chiTiets.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(chiTiets);
    }

    // Lấy đơn hàng theo khoảng ngày
    @GetMapping("/khoangngay")
    public ResponseEntity<List<DonHangResponse>> layDonHangTheoKhoangNgay(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<DonHangResponse> donHangs = donHangService.LayDonHangTheoKhoangNgay(start, end);
        if (donHangs.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(donHangs);
    }

    // Cập nhật đơn hàng
    @PutMapping("/capnhat")
    public ResponseEntity<DonHangResponse> capNhatDonHang(@RequestBody CapNhatDonHangRequest req) {
        DonHangResponse updated = donHangService.CapNhatDonHang(req);
        if (updated == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // lấy tất cả đơn hàng
    @GetMapping("/tatca")
    public ResponseEntity<List<DonHangResponse>> layTatCaDonHang() {
        List<DonHangResponse> list = donHangService.layHetDonHang();
        return ResponseEntity.ok(list);
    }
    

    // Lấy đơn hàng theo khách hàng
    @GetMapping("/khachhang")
    public ResponseEntity<List<DonHangResponse>> layDonHangKhachHang(@RequestParam String maKH) {
        List<DonHangResponse> list = donHangService.LayDonHangTheoKhachHang(maKH);
        if (list.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }
}
