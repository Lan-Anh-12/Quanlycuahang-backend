package example.com.Service.inventory;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import example.com.Repository.ChiTietNhapKhoRepository;
import example.com.Repository.NhapKhoRepository;
import example.com.Repository.SanPhamRepository;

import example.com.model.CT_NhapKho;
import example.com.model.NhapKho;
import example.com.model.SanPham;

import example.com.Dto.khohang.NhapKhoRequest;
import example.com.Dto.khohang.CT_NhapKhoRequest;
import example.com.Dto.khohang.NhapKhoResponse;
import example.com.Dto.khohang.CT_NhapKhoResponse;
import example.com.Dto.sanpham.SanPhamRequest;
import example.com.Dto.sanpham.SanPhamResponse;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private ChiTietNhapKhoRepository chiTietNhapKhoRepo;

    @Autowired
    private NhapKhoRepository nhapKhoRepo;

    @Autowired
    private SanPhamRepository sanPhamRepo;

    // ===================== MAPPING =====================
    private CT_NhapKhoResponse mapCTEntityToResp(CT_NhapKho ct) {
        CT_NhapKhoResponse r = new CT_NhapKhoResponse();
        r.setMaCTNK(ct.getMaCTNK());
        r.setMaSP(ct.getMaSP());
        r.setTenSP(sanPhamRepo.findTenSPByMaSP(ct.getMaSP()));
        r.setSoLuong(ct.getSoLuong());
        r.setDonGia(ct.getDonGia());
        r.setThanhTien(ct.getDonGia().multiply(BigDecimal.valueOf(ct.getSoLuong())));
        return r;
    }

    private NhapKhoResponse mapPhieuEntityToResp(NhapKho ph) {
        NhapKhoResponse r = new NhapKhoResponse();
        r.setMaNK(ph.getMaNK());
        r.setMaNV(ph.getMaNV());
        r.setNhaCungCap(ph.getNhaCungCap());
        r.setNgayNhap(ph.getNgayNhap());

        List<CT_NhapKho> cts = chiTietNhapKhoRepo.findByMaNK(ph.getMaNK());
        List<CT_NhapKhoResponse> ctResp = cts.stream()
                .map(this::mapCTEntityToResp)
                .collect(Collectors.toList());
        r.setChiTiet(ctResp);

        BigDecimal tong = ctResp.stream()
                .map(CT_NhapKhoResponse::getThanhTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        r.setTongTien(tong);
        return r;
    }

    private SanPhamResponse mapSanPhamToResp(SanPham sp) {
        SanPhamResponse r = new SanPhamResponse();
        r.setMaSP(sp.getMaSP());
        r.setTenSP(sp.getTenSP());
        r.setPhanLoai(sp.getPhanLoai());
        r.setGiaBan(sp.getDonGia());
        r.setSoLuongTon(sp.getSoLuongTon());
        r.setUrl(sp.getUrl());
        r.setMoTa(sp.getMoTa());
        return r;
    }

    // ===================== SAN PHAM =====================
    private String generateNextMaSP() {
        String maxMaSP = sanPhamRepo.findMaxMaSP(); // query trả về SP lớn nhất
        if (maxMaSP == null)
            return "SP000001";
        int next = Integer.parseInt(maxMaSP.substring(2)) + 1;
        return String.format("SP%06d", next);
    }

    // Generate next MaNK for NhapKho (format NK######)
    private String generateNextMaNK() {
        // find the maximum existing MaNK by lexicographical order and increment numeric
        // suffix
        List<NhapKho> all = nhapKhoRepo.findAll();
        String max = null;
        for (NhapKho n : all) {
            if (n.getMaNK() == null)
                continue;
            if (max == null || n.getMaNK().compareTo(max) > 0)
                max = n.getMaNK();
        }
        if (max == null)
            return "NK000001";
        try {
            String digits = max.replaceAll("[^0-9]", "");
            int next = Integer.parseInt(digits) + 1;
            return String.format("NK%06d", next);
        } catch (Exception e) {
            // fallback
            return max + "-1";
        }
    }
    private String generateNextMaCTNK() {
        // find the maximum existing MaNK by lexicographical order and increment numeric
        // suffix
        List<CT_NhapKho> all = chiTietNhapKhoRepo.findAll();
        String max = null;
        for (CT_NhapKho n : all) {
            if (n.getMaCTNK() == null)
                continue;
            if (max == null || n.getMaCTNK().compareTo(max) > 0)
                max = n.getMaCTNK();
        }
        if (max == null)
            return "CTNK000001";
        try {
            String digits = max.replaceAll("[^0-9]", "");
            int next = Integer.parseInt(digits) + 1;
            return String.format("CTNK%06d", next);
        } catch (Exception e) {
            // fallback
            return max + "-1";
        }
    }

    @Override
    @Transactional
    public SanPhamResponse taoSanPhamMoi(SanPhamRequest spReq) {
        SanPham sp = new SanPham();
        sp.setMaSP(generateNextMaSP());
        sp.setTenSP(spReq.getTenSP());
        sp.setPhanLoai(spReq.getPhanLoai());
        sp.setDonGia(spReq.getGiaBan());
        sp.setSoLuongTon(spReq.getSoLuong() == null ? 0 : spReq.getSoLuong());
        sp.setUrl(spReq.getUrl());
        sp.setMoTa(spReq.getMoTa());
        sp.setTrangThai("Hien");
        SanPham saved = sanPhamRepo.save(sp);
        return mapSanPhamToResp(saved);
    }

    @Override
    @Transactional
    public SanPhamResponse capNhatSanPham(String maSP, SanPhamRequest spReq) {
        SanPham sp = sanPhamRepo.findByMaSP(maSP)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        sp.setTenSP(spReq.getTenSP());
        sp.setDonGia(spReq.getGiaBan());
        sp.setMoTa(spReq.getMoTa());
        sp.setSoLuongTon(spReq.getSoLuong());
        SanPham saved = sanPhamRepo.save(sp);
        return mapSanPhamToResp(saved);
    }

    @Override
    @Transactional
    public void xoaSanPham(String maSP) {
        sanPhamRepo.softDelete(maSP);
    }

    @Override
    public List<SanPhamResponse> sanPhamConBan() {
        return sanPhamRepo.findAllNonDeleted().stream()
                .map(this::mapSanPhamToResp)
                .collect(Collectors.toList());
    }

    @Override
    public int xemTonKho(String maSP) {
        return sanPhamRepo.getSoLuongByMaSP(maSP);
    }

    @Override
    public int kiemTraSoLuongSpTheoTen(String keyword) {
        return sanPhamRepo.getSoLuongByTenSP(keyword);
    }

    @Override
    public String layUrlTheoMa(String maSp) {
        return sanPhamRepo.findUrlByMaSP(maSp);
    }

    // ===================== NHAP KHO =====================
    @Override
    @Transactional
    public NhapKhoResponse nhapKho(NhapKhoRequest request) {
        NhapKho phieu = new NhapKho();
        // generate MaNK since it's a String primary key (not auto-generated)
        phieu.setMaNK(generateNextMaNK());
        phieu.setMaNV(request.getMaNV());
        phieu.setNhaCungCap(request.getNhaCungCap());
        phieu.setNgayNhap(request.getNgayNhap() == null ? LocalDateTime.now() : request.getNgayNhap());

        NhapKho savedPhieu = nhapKhoRepo.save(phieu);

        BigDecimal tongTien = BigDecimal.ZERO;

        for (CT_NhapKhoRequest ctReq : request.getChiTiet()) {
            SanPham sp = sanPhamRepo.findById(ctReq.getMaSP())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại: " + ctReq.getMaSP()));

            CT_NhapKho ct = new CT_NhapKho();
            // generate MaCTNK based on phieu id and index to ensure uniqueness
            ct.setMaCTNK(generateNextMaCTNK());
            ct.setMaNK(savedPhieu.getMaNK());
            ct.setMaSP(ctReq.getMaSP());
            ct.setSoLuong(ctReq.getSoLuong());
            ct.setDonGia(ctReq.getDonGia() == null ? sp.getDonGia() : ctReq.getDonGia());
            ct.setThanhTien(ct.getDonGia().multiply(BigDecimal.valueOf(ct.getSoLuong())));

            chiTietNhapKhoRepo.save(ct);

            // cập nhật tồn kho
            int updated = sanPhamRepo.tangSoLuong(ct.getMaSP(), ct.getSoLuong());
            if (updated == 0)
                throw new RuntimeException("Không tìm thấy sản phẩm với mã: " + ct.getMaSP());

            tongTien = tongTien.add(ct.getThanhTien());
        }

        savedPhieu.setTongTien(tongTien);
        nhapKhoRepo.save(savedPhieu);

        return mapPhieuEntityToResp(savedPhieu);
    }

    @Override
    public List<NhapKhoResponse> layTatCaPhieuNhap() {
        return nhapKhoRepo.findAll().stream()
                .map(this::mapPhieuEntityToResp)
                .collect(Collectors.toList());
    }

    // @Override
    @Override
    public NhapKhoResponse layPhieuNhapTheoMa(String maNK) {
        NhapKho ph = nhapKhoRepo.findById(maNK)
                .orElseThrow(() -> new RuntimeException("Phiếu nhập không tồn tại: " + maNK));
        return mapPhieuEntityToResp(ph);
    }

    @Override
    @Transactional
    public NhapKhoResponse capNhatPhieuNhap(String maNK, NhapKhoRequest request) {
        NhapKho ph = nhapKhoRepo.findById(maNK)
                .orElseThrow(() -> new RuntimeException("Phiếu nhập không tồn tại: " + maNK));

        // Cập nhật những trường cơ bản
        if (request.getMaNV() != null)
            ph.setMaNV(request.getMaNV());
        if (request.getNhaCungCap() != null)
            ph.setNhaCungCap(request.getNhaCungCap());
        if (request.getNgayNhap() != null)
            ph.setNgayNhap(request.getNgayNhap());

        // Nếu frontend gửi chi tiết (chỉ cho phép chỉnh số lượng), cập nhật từng dòng
        // và điều chỉnh tồn kho
        if (request.getChiTiet() != null && !request.getChiTiet().isEmpty()) {
            List<CT_NhapKho> existing = chiTietNhapKhoRepo.findByMaNK(maNK);

            java.math.BigDecimal tong = java.math.BigDecimal.ZERO;

            for (CT_NhapKhoRequest ctReq : request.getChiTiet()) {
                // match existing by maSP
                CT_NhapKho found = null;
                for (CT_NhapKho e : existing) {
                    if (e.getMaSP().equals(ctReq.getMaSP())) {
                        found = e;
                        break;
                    }
                }

                if (found != null) {
                    int oldQty = found.getSoLuong();
                    int newQty = ctReq.getSoLuong();
                    int delta = newQty - oldQty;

                    // update product stock by delta (can be negative)
                    int updated = sanPhamRepo.tangSoLuong(found.getMaSP(), delta);
                    if (updated == 0)
                        throw new RuntimeException("Không tìm thấy sản phẩm với mã: " + found.getMaSP());

                    // update detail
                    found.setSoLuong(newQty);
                    if (ctReq.getDonGia() != null)
                        found.setDonGia(ctReq.getDonGia());
                    java.math.BigDecimal dg = found.getDonGia() == null ? java.math.BigDecimal.ZERO : found.getDonGia();
                    found.setThanhTien(dg.multiply(java.math.BigDecimal.valueOf(found.getSoLuong())));
                    chiTietNhapKhoRepo.save(found);

                    tong = tong.add(found.getThanhTien());
                } else {
                    // ignore new lines: editing only quantity of existing items is supported
                    continue;
                }
            }

            ph.setTongTien(tong);
        }

        NhapKho saved = nhapKhoRepo.save(ph);
        return mapPhieuEntityToResp(saved);
    }

    @Override
    public List<NhapKhoResponse> layTheoMaNKOrNCC(String keyword) {
        return nhapKhoRepo.findByMaNKOrNhaCungCap(keyword).stream()
                .map(this::mapPhieuEntityToResp)
                .collect(Collectors.toList());
    }



    @Override
    public List<SanPhamResponse> timSanPhamTheoTen(String tenSP) {
        if (tenSP == null || tenSP.trim().isEmpty()) {
            return List.of();
        }

        // gọi repo tìm các sản phẩm có tên chứa tenSP
        List<SanPham> spList = sanPhamRepo.findByTenSPContainingIgnoreCase(tenSP.trim());

        // map sang DTO
        return spList.stream()
                .map(this::mapSanPhamToResp)
                .collect(Collectors.toList());
    }

}
