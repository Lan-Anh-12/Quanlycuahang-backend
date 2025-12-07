package example.com.Service.order;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import example.com.Dto.donhang.*;
import example.com.Repository.ChiTietDonHangRepository;
import example.com.Repository.DonHangRepository;
import example.com.Repository.KhachHangRepository;
import example.com.Repository.SanPhamRepository;
import example.com.Service.auth.AuthService;
import example.com.model.CT_DonHang;
import example.com.model.DonHang;
import example.com.model.KhachHang;
import example.com.model.SanPham;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DonHangServiceImpl implements DonHangService {

    @Autowired
    private DonHangRepository donHangRepo;

    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepo;

    @Autowired
    private SanPhamRepository sanPhamRepo;

    @Autowired
    private KhachHangRepository khachHangRepo;

    @Autowired
    private AuthService authService; 
    
            
    // ============================
    // Helper: map entity -> DTO
    // ============================
    private DonHangResponse mapToResponse(DonHang dh) {
        DonHangResponse res = new DonHangResponse();
        res.setMaDH(dh.getMaDH());
        res.setMaKH(dh.getMaKH());
        res.setMaNV(dh.getMaNV());
        res.setNgayLap(dh.getNgayLap());
        res.setTongTien(dh.getTongTien());

        List<CT_DonHang> chiTietList = chiTietDonHangRepo.findByMaDH(dh.getMaDH());
        List<ChiTietDonHangResponse> chiTietResp = chiTietList.stream().map(ct -> {
            ChiTietDonHangResponse ctRes = new ChiTietDonHangResponse();
            ctRes.setMaCTDH(ct.getMaCTDH());
            ctRes.setMaSP(ct.getMaSP());
            ctRes.setSoLuong(ct.getSoLuong());
            ctRes.setDonGia(ct.getDonGia());
            ctRes.setThanhTien(ct.getThanhTien());
            return ctRes;
        }).collect(Collectors.toList());

        res.setChiTiet(chiTietResp);
        return res;
    }

    // ============================
    // Helper: sinh mã tự động
    // ============================
    private String generateMaDH() {
    // Lấy tất cả đơn hàng và tìm mã lớn nhất theo thứ tự từ điển
    List<DonHang> all = donHangRepo.findAll();
    String max = null;
    for (DonHang d : all) {
        if (d.getMaDH() == null) continue;
        if (max == null || d.getMaDH().compareTo(max) > 0)
            max = d.getMaDH();
    }
    if (max == null) return "DH000001";
    try {
        // Lấy phần số của mã
        String digits = max.replaceAll("[^0-9]", "");
        int next = Integer.parseInt(digits) + 1;
        // Format với 6 chữ số
        return String.format("DH%06d", next);
    } catch (Exception e) {
        // Nếu lỗi parse, fallback bằng cách thêm "-1"
        return max + "-1";
    }
}


    private String generateMaCTDH() {
    List<CT_DonHang> all = chiTietDonHangRepo.findAll();
    String max = null;
    for (CT_DonHang c : all) {
        if (c.getMaCTDH() == null) continue;
        if (max == null || c.getMaCTDH().compareTo(max) > 0)
            max = c.getMaCTDH();
    }
    if (max == null) return "CTDH000001";
    try {
        String digits = max.replaceAll("[^0-9]", "");
        int next = Integer.parseInt(digits) + 1;
        return String.format("CTDH%06d", next);
    } catch (Exception e) {
        return max + "-1";
    }
}

private String generateMaKH() {
    List<KhachHang> all = khachHangRepo.findAll();
    String max = null;
    for (KhachHang k : all) {
        if (k.getMaKH() == null) continue;
        if (max == null || k.getMaKH().compareTo(max) > 0)
            max = k.getMaKH();
    }
    if (max == null) return "KH000001";
    try {
        String digits = max.replaceAll("[^0-9]", "");
        int next = Integer.parseInt(digits) + 1;
        return String.format("KH%06d", next);
    } catch (Exception e) {
        return max + "-1";
    }
}

    // ============================
    // Tạo đơn hàng
    // ============================
   @Override
@Transactional
public void TaoDonHang(DonHangRequest request) {

    // Lấy maNV từ username trong token
    
    String maNV = request.getMaNV();

    // Xử lý khách hàng
    String maKH;
    if (request.getMaKH() == null || request.getMaKH().isEmpty()) {
        maKH = generateMaKH(); // sinh mã khách hàng mới
        KhachHang kh = new KhachHang();
        kh.setMaKH(maKH);
        kh.setTenKH(request.getTenKH());
        kh.setNamSinh(request.getNamSinh());
        kh.setDiaChi(request.getDiaChi());
        kh.setsdt(request.getSdt());
        khachHangRepo.save(kh);
    } else {
        maKH = request.getMaKH();
        khachHangRepo.findById(maKH)
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));
    }

    // Sinh mã đơn hàng & chuẩn bị chi tiết
    String maDH = generateMaDH();
    BigDecimal tongTien = BigDecimal.ZERO;
    List<CT_DonHang> chiTietEntities = new ArrayList<>();

    // Lấy tất cả sản phẩm 1 lần để tránh N+1 query
    Map<String, SanPham> sanPhamMap = sanPhamRepo.findAllById(
            request.getChiTietDonHangs().stream().map(ChiTietDonHangRequest::getMaSP).toList()
    ).stream().collect(Collectors.toMap(SanPham::getMaSP, sp -> sp));

    for (ChiTietDonHangRequest ctReq : request.getChiTietDonHangs()) {
        SanPham sp = sanPhamMap.get(ctReq.getMaSP());
        if (sp == null) throw new RuntimeException("Không tìm thấy sản phẩm " + ctReq.getMaSP());
        if (sp.getSoLuongTon() < ctReq.getSoLuong()) {
            throw new RuntimeException("Sản phẩm " + sp.getTenSP() + " chỉ còn " + sp.getSoLuongTon() + " trong kho, không đủ để tạo đơn");
        }
        BigDecimal thanhTien = sp.getDonGia().multiply(BigDecimal.valueOf(ctReq.getSoLuong()));
        tongTien = tongTien.add(thanhTien);

        CT_DonHang ct = new CT_DonHang();
        ct.setMaCTDH(generateMaCTDH());
        ct.setMaDH(maDH);
        ct.setMaSP(ctReq.getMaSP());
        ct.setSoLuong(ctReq.getSoLuong());
        ct.setDonGia(sp.getDonGia());
        ct.setThanhTien(thanhTien);
        // Trừ kho
        sp.setSoLuongTon(sp.getSoLuongTon() - ctReq.getSoLuong());
        sanPhamRepo.save(sp);

        chiTietEntities.add(ct);
    }

    // Lưu đơn hàng
    DonHang dh = new DonHang();
    dh.setMaDH(maDH);
    dh.setMaKH(maKH);
    dh.setMaNV(maNV);
    dh.setNgayLap(LocalDateTime.now());
    dh.setTongTien(tongTien);
    donHangRepo.save(dh);

    // Lưu chi tiết đơn hàng
    chiTietDonHangRepo.saveAll(chiTietEntities);
}



    // ============================
    // Các method còn lại
    // ============================
    @Override
    public List<DonHangResponse> layHetDonHang() {
        return donHangRepo.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DonHangResponse> LayDonHangTheoKhachHang(String maKH) {
        return donHangRepo.findByMaKH(maKH).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChiTietDonHangResponse> LayChiTietDonHangTheoDonHang(String maDH) {
        return chiTietDonHangRepo.findByMaDH(maDH).stream()
                .map(ct -> {
                    ChiTietDonHangResponse ctRes = new ChiTietDonHangResponse();
                    ctRes.setMaCTDH(ct.getMaCTDH());
                    ctRes.setMaSP(ct.getMaSP());
                    ctRes.setSoLuong(ct.getSoLuong());
                    ctRes.setDonGia(ct.getDonGia());
                    ctRes.setThanhTien(ct.getThanhTien());
                    return ctRes;
                }).collect(Collectors.toList());
    }

    @Override
    public List<DonHangResponse> XemDonHangNVLap(String maNV) {
        return donHangRepo.findByMaNV(maNV).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DonHangResponse> LayDonHangTheoKhoangNgay(LocalDateTime start, LocalDateTime end) {
        return donHangRepo.findByNgayLapBetween(start, end).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DonHangResponse CapNhatDonHang(CapNhatDonHangRequest request) {
        DonHang dh = donHangRepo.findById(request.getMaDH())
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        BigDecimal tongTien = BigDecimal.ZERO;
        List<CT_DonHang> chiTietCu = chiTietDonHangRepo.findByMaDH(dh.getMaDH());

        for (ChiTietDonHangRequest ctReq : request.getChiTiet()) {
            CT_DonHang ctCu = chiTietCu.stream()
                    .filter(ct -> ct.getMaSP().equals(ctReq.getMaSP()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Chi tiết sản phẩm không tồn tại"));

            int delta = ctCu.getSoLuong() - ctReq.getSoLuong();
            sanPhamRepo.tangSoLuong(ctCu.getMaSP(), delta);

            ctCu.setSoLuong(ctReq.getSoLuong());
            ctCu.setThanhTien(ctCu.getDonGia().multiply(BigDecimal.valueOf(ctReq.getSoLuong())));
            chiTietDonHangRepo.save(ctCu);

            tongTien = tongTien.add(ctCu.getThanhTien());
        }

        dh.setTongTien(tongTien);
        donHangRepo.save(dh);

        return mapToResponse(dh);
    }

    
}
