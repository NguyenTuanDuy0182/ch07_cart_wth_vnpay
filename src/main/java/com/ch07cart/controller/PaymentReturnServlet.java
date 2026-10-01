package com.ch07cart.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import com.ch07cart.model.*;
import com.ch07cart.util.*;

@WebServlet("/payment-return")
public class PaymentReturnServlet extends HttpServlet {

    // Chống gửi trùng lặp email cho cùng một mã giao dịch (giữ tối đa 2000 giao dịch gần nhất)
    private static final Set<String> processedTransactions = Collections.synchronizedSet(
            Collections.newSetFromMap(new java.util.LinkedHashMap<String, Boolean>(1000, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, Boolean> eldest) {
                    return size() > 2000;
                }
            })
    );

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
                String txnRef = request.getParameter("vnp_TxnRef");

                if (txnRef == null || txnRef.trim().isEmpty()) {
                    response.sendRedirect("payment_failure.jsp?" + request.getQueryString());
                    return;
                }

                String sentKey = "email_sent_" + txnRef;

                // Chỉ gửi email duy nhất 1 lần cho mỗi giao dịch vnp_TxnRef
                // processedTransactions.add(txnRef) trả về false nếu mã giao dịch này đã được ghi nhận trước đó
                boolean isAlreadyProcessed = (session.getAttribute(sentKey) != null)
                        || (!processedTransactions.add(txnRef));

                if (!isAlreadyProcessed) {
                    OrderSnapshot snapshot = (OrderSnapshot) session.getAttribute("orderSnapshot_" + txnRef);
                    User user = (User) session.getAttribute("user");
                    if (user == null && snapshot != null) {
                        user = snapshot.getUser();
                    }

                    if (user != null && user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
                        try {
                            String to = user.getEmail().trim();
                            String subject = "Xác nhận thanh toán thành công - Đơn hàng #" + txnRef;

                            StringBuilder body = new StringBuilder();
                            body.append("Xin chào ").append(user.getUsername() != null ? user.getUsername() : "quý khách").append(",\n\n");
                            body.append("Cảm ơn bạn đã mua hàng. Giao dịch của bạn đã thành công!\n");
                            body.append("Mã giao dịch: ").append(txnRef).append("\n");
                            long amount = 0;
                            try {
                                amount = Long.parseLong(request.getParameter("vnp_Amount")) / 100;
                            } catch (Exception ex) {
                                if (snapshot != null) {
                                    amount = (long) snapshot.getTotal();
                                }
                            }
                            body.append("Số tiền: ").append(String.format("%,d VND", amount)).append("\n\n");

                            body.append("Chi tiết đơn hàng:\n");

                            // Ưu tiên đọc danh sách sản phẩm từ OrderSnapshot để không bao giờ bị rỗng
                            List<LineItem> items = null;
                            if (snapshot != null && snapshot.getItems() != null && !snapshot.getItems().isEmpty()) {
                                items = snapshot.getItems();
                            } else {
                                Cart cart = (Cart) session.getAttribute("cart");
                                if (cart != null && cart.getItems() != null && !cart.getItems().isEmpty()) {
                                    items = cart.getItems();
                                }
                            }

                            if (items != null && !items.isEmpty()) {
                                for (LineItem item : items) {
                                    body.append("- ").append(item.getProduct().getDescription())
                                            .append(" (SL: ").append(item.getQuantity()).append(") - ")
                                            .append(item.getTotalCurrencyFormat()).append("\n");
                                }
                            } else {
                                body.append("- Đơn hàng đã được ghi nhận thành công.\n");
                            }

                            body.append("\nTrân trọng,\nĐội ngũ hỗ trợ.");

                            MailUtilBrevo.sendMail(to, user.getUsername(), subject, body.toString(), false);

                            session.setAttribute(sentKey, Boolean.TRUE);
                            session.removeAttribute("cart");
                        } catch (Exception e) {
                            e.printStackTrace();
                            processedTransactions.remove(txnRef);
                        }
                    }
                }

                String redirectUrl = "payment_success.jsp?" + request.getQueryString();
                if (!redirectUrl.contains("emailSent=")) {
                    redirectUrl += "&emailSent=true";
                }
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect("payment_failure.jsp?" + request.getQueryString());
            }
        } else {
            response.getWriter().println("<html><body><h3>Lỗi: Chữ ký không hợp lệ!</h3></body></html>");
        }
    }
}