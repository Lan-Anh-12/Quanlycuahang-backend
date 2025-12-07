package example.com.Controller;

import example.com.Service.staff.NhanVienService;
import example.com.Dto.nhanvien.NhanVienResponse;
import example.com.Dto.nhanvien.NhanVienWithTaiKhoanRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quanly/nhanvien")
public class QuanLyNhanVienController {

    @Autowired
    private NhanVienService nhanVienService;

    // Lấy tất cả nhân viên
    @GetMapping("/tatca")
    public ResponseEntity<List<NhanVienResponse>> layTatCaNhanVien() {
        List<NhanVienResponse> list = nhanVienService.layTatCa();
        return ResponseEntity.ok(list);
    }
    // tìm theo tên nhân viên
    @GetMapping("/timkiem")
    public ResponseEntity<List<NhanVienResponse>> timTheoTen(@RequestParam String tenNV) {
        List<NhanVienResponse> list = nhanVienService.timTheoTen(tenNV);
        return ResponseEntity.ok(list);
    }
    // Thêm nhân viên (kèm tài khoản)
    @PostMapping("/taomoi")
    public ResponseEntity<NhanVienResponse> taoNhanVien(@RequestBody NhanVienWithTaiKhoanRequest nvRequest) {
        NhanVienResponse nvResponse = nhanVienService.taoNhanVien(nvRequest);
        return ResponseEntity.ok(nvResponse);
    }

    // xóa nhân viên (đổi trạng thái)
    @DeleteMapping("/xoa/{maNV}")
    public ResponseEntity<Void> xoaNhanVien(@PathVariable String maNV) {
        nhanVienService.xoaNhanVien(maNV);
        return ResponseEntity.ok().build();
    }

 

}
