package com.ch07cart;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/payment-return")
public class PaymentReturnServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        Map<String, String> fields = new HashMap<>();
        for (String key : request.getParameterMap().keySet()) {
            String value = request.getParameter(key);
            if (value != null && !value.isEmpty()) {
                fields.put(key, value);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        String signValue = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, VNPayConfig.hashAllFields(fields));

        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                HttpSession session = request.getSession();
                User user = (User) session.getAttribute("user");
                if (user != null) {
                    try {
                        String to = user.getEmail();
                        String subject = "Xác nhận thanh toán thành công - Đơn hàng của bạn";

                        StringBuilder body = new StringBuilder();
                        body.append("Xin chào ").append(user.getUsername()).append(",\n\n");
                        body.append("Cảm ơn bạn đã mua hàng. Giao dịch của bạn đã thành công!\n");
                        body.append("Mã giao dịch: ").append(request.getParameter("vnp_TxnRef")).append("\n");
                        long amount = Long.parseLong(request.getParameter("vnp_Amount")) / 100;
                        body.append("Số tiền: ").append(String.format("%,d VND", amount)).append("\n\n");

                        body.append("Chi tiết đơn hàng:\n");
                        Cart cart = (Cart) session.getAttribute("cart");
                        if (cart != null && cart.getItems() != null) {
                            for (LineItem item : cart.getItems()) {
                                body.append("- ").append(item.getProduct().getDescription())
                                        .append(" (SL: ").append(item.getQuantity()).append(") - ")
                                        .append(item.getTotalCurrencyFormat()).append("\n");
                            }
                            // Clear cart
                            session.removeAttribute("cart");
                        }

                        body.append("\nTrân trọng,\nĐội ngũ hỗ trợ.");

                        MailUtilGmail.sendMail(to, null, subject, body.toString(), false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                response.sendRedirect("payment_success.jsp?" + request.getQueryString());
            } else {
                response.sendRedirect("payment_failure.jsp?" + request.getQueryString());
            }
        } else {
            response.getWriter().println("<html><body><h3>Lỗi: Chữ ký không hợp lệ!</h3></body></html>");
        }
    }
}