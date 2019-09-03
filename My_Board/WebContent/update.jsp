<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="board.BoardBean" %>
<%
	int num = Integer.parseInt(request.getParameter("num"));
	String nowPage = request.getParameter("nowPage");
	BoardBean article = (BoardBean)session.getAttribute("article");
	String subject = article.getSubject();
	String name = article.getName();
	String content = article.getContent();
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>글 수정</title>
		<link href="style.css" rel="stylesheet" type="text/css">
		<script>
			function check() {
				if(document.updateFrm.pwd.value == "") {
					alert("수정을 위해 패스워드를 입력하세요.");
					document.update.pwd.focus();
					return false;
				}
				document.updateFrm.submit();
			}
		</script>
	</head>
	<body bgcolor="#FFFFCC">
		<div align="center">
			<br/><br/>
			<table width="460" cellspacing="0" cellpadding="3">
				<tr>
					<td bgcolor="FF9018" height="21" align="center">수정하기</td>
				</tr>
			</table>
			<form name="updateFrm" method="post" action="boardUpdate">
				<table width="70%" cellspacing="0" cellpadding="7">
					<tr>
						<td align="center">
							<table>
								<tr>
									<td width="20%">성  명</td>
									<td width="80%">
										<input name="name" value="<%=name %>" size="30" maxlength="20">
									</td>
								</tr>
								<tr>
									<td>제  목</td>
									<td>
										<input name="subject" size="50" value="<%=subject %>" maxlength="50">
									</td>
								</tr>
								<tr>
									<td>내용</td>
									<td>
										<textarea name="content" rows="10" cols="50"><%=content %></textarea> 
									</td>
								</tr>
								<tr>
									<td>비밀번호</td>
									<td>
										<input type="password" name="pwd" size="15" maxlength="15">
										수정 시에는 비밀번호가 필요합니다.
									</td>
								</tr>
								<tr>
									<td colspan="2" height="5"><hr/></td>
								</tr>
								<tr>
									<td colspan="2">
										<input type="button" value="수정" onclick="check()">
										<input type="reset" value="다시수정">
										<input type="button" value="뒤로" onclick="history.go(-1)">
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<input type="hidden" name="nowPage" value="<%=nowPage %>">
				<input type="hidden" name="num" value="<%=num %>">
			</form>
		</div>
	</body>
</html>




