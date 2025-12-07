package example.com.Controller;

import example.com.model.KhachHang;
import example.com.Dto.khachhang.KhachHangRequest;
import example.com.Dto.khachhang.KhachHangResponse;
import example.com.Service.customer.KhachHangService;

import org.springframework.web.bind.annotation.*;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.time.LocalDateTime;


@RestController
@RequestMapping("quanly/khachhang")
public class QuanLyKhachHangController {
    @Autowired
    private KhachHangService khachHangService;


    // cập nhật khách hàng
    @PutMapping("/capnhat/{maKH}")
    public ResponseEntity<KhachHangResponse> capNhatKhach(
            @PathVariable String maKH,
            @RequestBody KhachHangRequest request) {

        KhachHangResponse updated = khachHangService.capNhatKhachHang(maKH, request);
        return ResponseEntity.ok(updated);
    }

    // lấy tất cả khách hàng
    @GetMapping("/tatca")
    public ResponseEntity<List<KhachHangResponse>> layTatCaKhach() {
        List<KhachHangResponse> list = khachHangService.layTatCaKhachHang();
        return ResponseEntity.ok(list);
    }

    //tìm theo tên hoặc sđt
    @GetMapping("/tim")
    public ResponseEntity<List<KhachHangResponse>> timTheoTenHoacSDT(
            @RequestParam String ten) {
        List<KhachHangResponse> list = khachHangService.searchKhachHang(ten);
        return ResponseEntity.ok(list);
    }
     // Lấy khách hàng theo mã
     @GetMapping("/{maKH}")
    public ResponseEntity<KhachHangResponse> layTheoMa(@PathVariable String maKH) {
        return ResponseEntity.ok(khachHangService.layKhachHangTheoMa(maKH));
    }

    // tìm khách theo tên
    @GetMapping("/timkiem")
    public ResponseEntity<List<KhachHangResponse>> timKhachTheoTen(
            @RequestParam String tenKH) {
        List<KhachHangResponse> list = khachHangService.timKhachHangTheoTen(tenKH);
        return ResponseEntity.ok(list);
    }


   
}
