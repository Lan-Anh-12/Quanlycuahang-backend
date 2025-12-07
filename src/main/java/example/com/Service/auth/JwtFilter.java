package example.com.Service.auth;
// ... (các imports khác)
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // Định nghĩa các URL công khai cần bỏ qua bộ lọc JWT
    private static final List<String> PUBLIC_URLS = Arrays.asList(
            "/api/auth/login",
            "/api/auth/refresh"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String requestUri = request.getRequestURI();

        // 1. Cho OPTIONS qua luôn (preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Bỏ qua hoàn toàn bộ lọc JWT cho các URL công khai
        if (PUBLIC_URLS.contains(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Xử lý JWT Token cho các request còn lại (ĐÃ ĐƯỢC BẢO VỆ)
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (!jwtUtil.validateToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            Claims claims = jwtUtil.extractAllClaims(token);
            String username = claims.get("username", String.class);

            // Set Authentication
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            // Nếu là request yêu cầu xác thực nhưng không có token
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
