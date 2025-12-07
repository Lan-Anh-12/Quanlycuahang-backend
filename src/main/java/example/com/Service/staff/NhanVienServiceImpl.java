package example.com.Service.staff;

import example.com.Repository.NhanVienRepository;
import example.com.Repository.TaiKhoanRepository;
import example.com.model.NhanVien;
import example.com.model.TaiKhoan;
import example.com.Dto.nhanvien.TaiKhoanRequest;
import example.com.Dto.nhanvien.NhanVienRequest;
import example.com.Dto.nhanvien.NhanVienWithTaiKhoanRequest;
import example.com.Dto.nhanvien.NhanVienResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NhanVienServiceImpl implements NhanVienService {

    @Autowired
    private NhanVienRepository nhanVienRepo;

    @Autowired
    private TaiKhoanRepository taiKhoanRepo;

    // ===== Helper =====
    private NhanVienResponse mapToResponse(NhanVien nv) {
        NhanVienResponse res = new NhanVienResponse();
        res.setMaNV(nv.getMaNV());
        res.setTenNV(nv.getTenNV());
        res.setSDT(nv.getSDT());
        res.setEmail(nv.getEmail());
        res.setNgayVaoLam(nv.getNgayVaoLam());
        res.setMaTK(nv.getMaTK());
        return res;
    }

    private NhanVien mapToEntity(NhanVienRequest req) {
        NhanVien nv = new NhanVien();
        nv.setTenNV(req.getTenNV());
        nv.setSDT(req.getSDT());
        nv.setEmail(req.getEmail());
        nv.setNgayVaoLam(req.getNgayVaoLam());
        nv.setTrangThai("LamViec");
        return nv;
    }

    private NhanVien mapToEntity(NhanVienWithTaiKhoanRequest req) {
        NhanVien nv = new NhanVien();
        nv.setTenNV(req.getTenNV());
        nv.setSDT(req.getSDT());
        nv.setEmail(req.getEmail());
        nv.setNgayVaoLam(req.getNgayVaoLam());
        nv.setTrangThai("LamViec");
        return nv;
    }

    // ===== CRUD =====

    @Override
    public List<NhanVienResponse> layTatCa() {
        return nhanVienRepo.findAll()
                .stream()
                .filter(nv -> "LamViec".equals(nv.getTrangThai()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public NhanVienResponse layTheoMa(String maNV) {
        NhanVien nv = nhanVienRepo.findById(maNV)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên!"));
        return mapToResponse(nv);
    }

    @Override
    @Transactional
    public NhanVienResponse taoNhanVien(NhanVienWithTaiKhoanRequest nvRequest) {
        // Tạo entity nhân viên
        NhanVien nv = mapToEntity(nvRequest);

        // Tạo tài khoản nếu có
        if (nvRequest.getTaiKhoan() != null) {
            TaiKhoanRequest tkReq = nvRequest.getTaiKhoan();
            TaiKhoan tk = new TaiKhoan();
            tk.setUsername(tkReq.getUsername());
            tk.setMatKhau(tkReq.getMatKhau());
            tk.setRole(tkReq.getRole());
            tk = taiKhoanRepo.save(tk);  // lưu tài khoản vào DB
            nv.setTaiKhoan(tk);          // gán vào nhân viên
            nv.setMaTK(tk.getMaTK());    // gán FK vào NhanVien
        }

        // Lưu nhân viên
        NhanVien saved = nhanVienRepo.save(nv);

        return mapToResponse(saved);
    }

    @Override
    public NhanVienResponse capNhatNhanVien(String maNV, NhanVienRequest nvRequest) {
        NhanVien nv = nhanVienRepo.findById(maNV)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên!"));

        nv.setTenNV(nvRequest.getTenNV());
        nv.setSDT(nvRequest.getSDT());
        nv.setEmail(nvRequest.getEmail());
        nv.setNgayVaoLam(nvRequest.getNgayVaoLam());

        NhanVien updated = nhanVienRepo.save(nv);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void xoaNhanVien(String maNV) {
        NhanVien nv = nhanVienRepo.findById(maNV)
            .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại: " + maNV));
        // chỉ cập nhật trạng thái
        nv.setTrangThai("NghiViec"); 
        nhanVienRepo.save(nv);
    }

    @Override
    public List<NhanVienResponse> timTheoTen(String tenNV) {
        return nhanVienRepo.findByTenNVContainingIgnoreCase(tenNV)
                .stream()
                .filter(nv -> "LamViec".equals(nv.getTrangThai()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void ganTaiKhoanChoNhanVien(String maNV, String maTK) {
        NhanVien nv = nhanVienRepo.findById(maNV)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại: " + maNV));

        TaiKhoan tk = taiKhoanRepo.findById(maTK)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại: " + maTK));

        nv.setTaiKhoan(tk);
        nv.setMaTK(tk.getMaTK());
        nhanVienRepo.save(nv);
    }
}
