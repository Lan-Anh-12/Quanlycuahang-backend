package example.com.Controller;

import example.com.Service.inventory.InventoryService;
import example.com.Dto.sanpham.SanPhamRequest;
import example.com.Dto.sanpham.SanPhamResponse;
import example.com.Dto.khohang.NhapKhoResponse;
import example.com.Dto.khohang.NhapKhoRequest;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("quanly/khohang")

public class QuanLyKhoHangController {
    @Autowired
    private InventoryService inventoryService;

    // tạo sản phẩm mới
    @PostMapping("/taosp")
    public ResponseEntity<SanPhamResponse> taoSanPham(@RequestBody SanPhamRequest sp) {
        return new ResponseEntity<>(inventoryService.taoSanPhamMoi(sp), HttpStatus.CREATED);
    }

    // cập nhật sản phẩm
    @PutMapping("/suasp/{maSP}")
    public ResponseEntity<SanPhamResponse> capNhatSanPham(@PathVariable String maSP,
            @RequestBody SanPhamRequest spReq) {
        SanPhamResponse updatedSp = inventoryService.capNhatSanPham(maSP, spReq);
        return ResponseEntity.ok(updatedSp);
    }

    // lấy tất cả đơn nhập hàng
    @GetMapping("/donnhaphang/tatca")
    public ResponseEntity<List<NhapKhoResponse>> layTatCaDonNhapHang() {
        List<NhapKhoResponse> list = inventoryService.layTatCaPhieuNhap();
        return ResponseEntity.ok(list);
    }

    // Tạo phiếu nhập mới
    @PostMapping("/donnhaphang/tao")
    public ResponseEntity<NhapKhoResponse> taoDonNhap(@RequestBody NhapKhoRequest req) {
        NhapKhoResponse created = inventoryService.nhapKho(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // lấy đơn nhập hàng theo mã nhập hoặc nhà cung cấp
    @GetMapping("/donnhaphang/timkiem")
    public ResponseEntity<List<NhapKhoResponse>> layDonNhapHangTheoMaNKOrNCC(@RequestParam String keyword) {
        List<NhapKhoResponse> list = inventoryService.layTheoMaNKOrNCC(keyword);
        return ResponseEntity.ok(list);
    }

    // // lấy 1 đơn nhập hàng theo mã
    @GetMapping("/donnhaphang/{maNK}")
    public ResponseEntity<NhapKhoResponse> layDonNhapHangTheoMa(@PathVariable String maNK) {
        NhapKhoResponse phieuNhap = inventoryService.layPhieuNhapTheoMa(maNK);
        return ResponseEntity.ok(phieuNhap);
    }

    // Cập nhật phiếu nhập (đơn giản)
    @PutMapping("/donnhaphang/{maNK}")
    public ResponseEntity<NhapKhoResponse> capNhatDonNhap(@PathVariable String maNK, @RequestBody NhapKhoRequest req) {
        // Forward request directly to service; service handles updating quantities in
        // chiTiet if provided
        NhapKhoResponse updated = inventoryService.capNhatPhieuNhap(maNK, req);
        return ResponseEntity.ok(updated);
    }

    // lấy sản phẩm còn bán
    @GetMapping("/sanpham/conban")
    public ResponseEntity<List<SanPhamResponse>> sanPhamConBan() {
        List<SanPhamResponse> list = inventoryService.sanPhamConBan();
        return ResponseEntity.ok(list);
    }


    // Kiểm tra số lượng sản phẩm theo tên
    @GetMapping("/soluong")
    public ResponseEntity<Integer> xemSoLuongSanPham(@RequestParam String keyword) {
        int count = inventoryService.kiemTraSoLuongSpTheoTen(keyword);
        return ResponseEntity.ok(count);
    }

    // Lấy danh sách sản phẩm còn bán
    @GetMapping("/sanpham")
    public ResponseEntity<List<SanPhamResponse>> timSanPhamConBan() {
        List<SanPhamResponse> list = inventoryService.sanPhamConBan();
        if (list.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }

    // lấy sản phẩm theo tên
    @GetMapping("/sanpham/tim")
    public ResponseEntity<List<SanPhamResponse>> timSanPhamTheoTen(@RequestParam String tenSP) {
        List<SanPhamResponse> list = inventoryService.timSanPhamTheoTen(tenSP);
        if (list.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(list);
    }
    // xóa sản phẩm
    @DeleteMapping("/sanpham/xoa/{maSP}")   
    public ResponseEntity<String> xoaSanPham(@PathVariable String maSP) {
        inventoryService.xoaSanPham(maSP);
        return ResponseEntity.ok("Xóa sản phẩm thành công");
    }
}
