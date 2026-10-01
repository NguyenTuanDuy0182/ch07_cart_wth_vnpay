<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thanh toán thành công</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="style.css" type="text/css" />
</head>
<body class="payment-page success">
    <div class="container">
        <div class="icon">
            <i class="fas fa-check-circle"></i>
        </div>
        <h2>Thanh toán thành công!</h2>
        <p>Cảm ơn bạn đã hoàn tất thanh toán. Giao dịch của bạn đã được xử lý thành công.</p>
        
        <c:if test="${param.emailSent == 'true'}">
            <p style="color: green; font-weight: bold;">Hóa đơn đã được gửi đến email của bạn.</p>
        </c:if>
        <c:if test="${param.emailSent == 'false'}">
            <p style="color: red;">Không thể gửi email hóa đơn. Vui lòng liên hệ hỗ trợ nếu cần.</p>
        </c:if>

        <div class="info">
            <p><span>Mã giao dịch:</span> <c:out value="${param.vnp_TxnRef}" default="N/A"/></p>
            
            <p><span>Số tiền:</span> 
                <c:catch var="amtErr">
                    <fmt:formatNumber value="${param.vnp_Amount / 100}" pattern="#,###"/> VND
                </c:catch>
                <c:if test="${not empty amtErr}">N/A</c:if>
            </p>
            
            <p><span>Nội dung:</span> <c:out value="${param.vnp_OrderInfo}" default="N/A"/></p>
            <p><span>Mã ngân hàng:</span> <c:out value="${param.vnp_BankCode}" default="N/A"/></p>
            
            <p><span>Thời gian:</span> 
                <c:catch var="dateErr">
                    <fmt:parseDate value="${param.vnp_PayDate}" pattern="yyyyMMddHHmmss" var="parsedDate" />
                    <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm:ss" />
                </c:catch>
                <c:if test="${not empty dateErr}">N/A</c:if>
            </p>
        </div>
        <a href="index.jsp" class="back-button">Quay lại trang chủ</a>
    </div>
</body>
</html>