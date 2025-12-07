package example.com.Service.order;

import example.com.Repository.DonHangRepository;
import example.com.Repository.ChiTietDonHangRepository;
import example.com.Repository.SanPhamRepository;
import example.com.Repository.KhachHangRepository;
import example.com.model.DonHang;
import example.com.model.SanPham;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.math.BigDecimal;

@Service
public class ThongKeServiceImpl implements ThongKeService {

    @Autowired
    private DonHangRepository donHangRepo;

    @Autowired
    private ChiTietDonHangRepository ctDonRepo;

    @Autowired
    private SanPhamRepository sanPhamRepo;

    @Autowired
    private KhachHangRepository khRepo;


    // Thống kê theo tháng
 
    @Override
    public Map<String, Object> thongKeTheoThang(int month, int year) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1);

        List<DonHang> donTrongThang = donHangRepo.findByNgayLapBetween(start, end);
        long soDon = donTrongThang.size();
        BigDecimal doanhThu = donHangRepo.doanhThuTheoThang(month);

        Map<String, Object> result = new HashMap<>();
        result.put("soDon", soDon);
        result.put("doanhThu", doanhThu);
        result.put("donHang", donTrongThang);
        return result;
    }

    // Thống kê theo khoảng ngày

    @Override
    public Map<String, Object> thongKeTheoKhoangNgay(LocalDate start, LocalDate end) {
        LocalDateTime s = start.atStartOfDay();
        LocalDateTime e = end.atTime(LocalTime.MAX);

        List<DonHang> dons = donHangRepo.findByNgayLapBetween(s, e);
        double doanhThu = dons.stream()
                .map(d -> d.getTongTien().doubleValue())
                .reduce(0.0, Double::sum);

        Map<String, Object> result = new HashMap<>();
        result.put("tongDon", dons.size());
        result.put("danhSach", dons);
        result.put("doanhThu", doanhThu);
        return result;
    }

    // Thống kê theo nhân viên

    @Override
    public Map<String, Object> thongKeTheoNhanVien(String maNV) {
        List<DonHang> dons = donHangRepo.findByMaNV(maNV);
        double doanhThu = dons.stream()
                .map(d -> d.getTongTien().doubleValue())
                .reduce(0.0, Double::sum);

        Map<String, Object> result = new HashMap<>();
        result.put("soDon", dons.size());
        result.put("doanhThu", doanhThu);
        result.put("donCuaNV", dons);
        return result;
    }

    // Thống kê theo ngày
    
    @Override
    public Map<String, Object> thongKeTheoNgay(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<DonHang> dons = donHangRepo.findByNgayLapBetween(start, end);
        double doanhThu = dons.stream()
                .map(d -> d.getTongTien().doubleValue())
                .reduce(0.0, Double::sum);

        Map<String, Object> result = new HashMap<>();
        result.put("soDon", dons.size());
        result.put("danhSach", dons);
        result.put("doanhThu", doanhThu);
        return result;
    }

    // Thống kê theo khách hàng

    @Override
    public Map<String, Object> thongKeTheoKhachHang(String maKH) {
        Map<String, Object> result = new HashMap<>();
        int tongDon = donHangRepo.soDonHangCuaKhach(maKH);
        BigDecimal tongTien = donHangRepo.tongTienChiCuaKhach(maKH);
        List<String> spDaMua = ctDonRepo.dsSanPhamDaMuaNative(maKH);

        result.put("tongDonHang", tongDon);
        result.put("tongTienDaMua", tongTien);
        result.put("sanPhamDaMua", spDaMua);
        return result;
    }

    // Sản phẩm bán chạy trong tháng
    @Override
    public List<Map<String, Object>> sanPhamBanChayTheoThang(int thang, int nam) {
        List<Object[]> data = ctDonRepo.sanPhamBanChayTheoThang(thang, nam);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Object[] row : data) {
            // row[0] là mã SP (String), row[1] là tổng số lượng bán (Long)
            String maSP = (String) row[0];
            Long tongBan = (Long) row[1];

            SanPham sp = sanPhamRepo.findByMaSP(maSP)
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại: " + maSP));

            Map<String, Object> item = new HashMap<>();
            item.put("maSP", sp.getMaSP());
            item.put("tenSP", sp.getTenSP());
            item.put("soLuongBan", tongBan);
            item.put("donGia", sp.getDonGia());
            item.put("phanLoai", sp.getPhanLoai());
            item.put("url", sp.getUrl());

            result.add(item);
        }

        return result;
    }
}
