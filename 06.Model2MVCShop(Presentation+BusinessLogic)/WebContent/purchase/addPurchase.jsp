<%@ page contentType="text/html; charset=euc-kr" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
<title>addPurchase.jsp</title>
</head>

<body>

<form name="updatePurchase" action="/updatePurchaseView.do?tranNo=${ purchase.tranNo }" method="post">

�� ������ ���� ���Ű� �Ǿ����ϴ�.

<table border=1>
	<tr>
		<td>��ǰ��ȣ</td>
		<td>${ purchase.purchaseProd.prodNo }</td>
		<td></td>
	</tr>
	<tr>
		<td>�����ھ��̵�</td>
		<td>${ purchase.buyer.userId}</td>
		<td></td>
	</tr>
	<tr>
		<td>���Ź��</td>
		<td>
			${ purchase.paymentOption == '1  ' ? "���ݱ���" : "�ſ뱸��" }
		</td>
		<td></td>
	</tr>
	<tr>
		<td>�������̸�</td>
		<td>${ purchase.receiverName }</td>
		<td></td>
	</tr>
	<tr>
		<td>�����ڿ���ó</td>
		<td>${ purchase.receiverPhone }</td>
		<td></td>
	</tr>
	<tr>
		<td>�������ּ�</td>
		<td>${ purchase.divyAddr }</td>
		<td></td>
	</tr>
		<tr>
		<td>���ſ�û����</td>
		<td>${ purchase.divyRequest }</td>
		<td></td>
	</tr>
	<tr>
		<td>����������</td>
		<td>${ purchase.divyDate }</td>
		<td></td>
	</tr>
</table>
</form>

</body>
</html>