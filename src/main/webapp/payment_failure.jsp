<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thanh toán thất bại</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="style.css" type="text/css" />
</head>
<body class="payment-page failure">
    <div class="container">
        <div class="icon">
            <i class="fas fa-times-circle"></i>
        </div>
        <h2>Thanh toán thất bại!</h2>
        <p>Đã có lỗi xảy ra trong quá trình thanh toán. Vui lòng thử lại hoặc liên hệ với chúng tôi để được hỗ trợ.</p>
        
        <c:set var="errorCode" value="${param.vnp_TransactionStatus}" />
        <c:set var="message" value="Lý do không xác định (Hoặc do sai chữ ký, số tiền không khớp)." />
        
        <c:choose>
            <c:when test="${errorCode == '02'}">
                <c:set var="message" value="Giao dịch không thành công do thông tin thẻ/tài khoản không hợp lệ." />
            </c:when>
            <c:when test="${errorCode == '07'}">
                <c:set var="message" value="Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới fraud)." />
            </c:when>
            <c:when test="${errorCode == '09'}">
                <c:set var="message" value="Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng." />
            </c:when>
            <c:when test="${errorCode == '10'}">
                <c:set var="message" value="Thẻ/Tài khoản của khách hàng đã bị khóa." />
            </c:when>
            <c:when test="${errorCode == '11'}">
                <c:set var="message" value="Khách hàng nhập sai mật khẩu xác thực giao dịch (OTP)." />
            </c:when>
            <c:when test="${errorCode == '24'}">
                <c:set var="message" value="Khách hàng hủy giao dịch." />
            </c:when>
        </c:choose>

        <div class="reason">
            <p><strong>Lý do:</strong>
                <c:out value="${message}" />
            </p>
        </div>
        <div class="actions">
            <a href="index.jsp" class="action-button">Thử lại</a>
            <a href="#" class="action-button contact-support">Liên hệ hỗ trợ</a>
        </div>
    </div>
</body>
</html>