<%@page import="javax.security.auth.Subject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="article" class="board.BoardBean" scope="session"/>
<%
	String nowPage = request.getParameter("nowPage");
	String subject = article.getSubject();
	String content = article.getContent();
%>
<!DOCTYPE html>
<html>
<head>
		<meta charset="UTF-8">
		<title>답글</title>
		<link href="style.css" rel="stylesheet" type="text/css">
	</head>
	<body bgcolor="#FFFFCC">
		<div align="center">
			<br/><br/>
			<table width="460" cellspacing="0" cellpadding="3">
				<tr>
					<td bgcolor="#CCCC00" height="21" align="center">답변하기</td>
				</tr>
			</table>
			<form method="post" action="boardReply">
				<table width="600" cellpadding="7">
					<tr>
						<td align="center">
							<table border="0">
								<tr>
									<td width="20%">성  명</td>
									<td width="80%">
										<input type="text" name="name" size="30" maxlength="20">
									</td>
								</tr>
								<tr>
									<td width="20%">제  목</td>
									<td width="80%">
										<input type="text" name="subject" size="50" maxlength="30" value="RE : <%=subject %>">
									</td>
								</tr>
								<tr>
									<td>내  용</td>
									<td>
										<textarea rows="10" cols="50" style="resize:none" name="content"><%=content %>	// 답변 : </textarea>
									</td>
								</tr>
								<tr>
									<td width="20%">비밀번호</td>
									<td width="80%">
										<input type="password" name="pwd" size="15" maxlength="15">
									</td>
								</tr>
								<tr>
									<td colspan="2"><hr/></td>
								</tr>
								<tr>
									<td colspan="2">
										<input type="submit" value="등록">
										<input type="reset" value="다시쓰기">
										<input type="button" value="뒤로" onclick="history.back()">
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<input type="hidden" name="ip" value="<%=request.getRemoteAddr()%>">
				<input type="hidden" name="nowPage" value="<%=nowPage%>">
				<input type="hidden" name="num" value="<%=article.getNum() %>">
				<input type="hidden" name="ref" value="<%=article.getRef()%>">
				<input type="hidden" name="pos" value="<%=article.getPos()%>">
				<input type="hidden" name="depth" value="<%=article.getDepth()%>">
			</form>
		</div>	
	</body>
</html>




