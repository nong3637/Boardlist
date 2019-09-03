<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>게시글 작성</title>
		<link href="style.css" rel="stylesheet" type="text/css">
	</head>
	<body bgcolor="#FFFFCC">
		<div align="center">
			<br/><br/>
			<table width="80%" cellspacing="0" cellpadding="3">
				<tr>
					<td bgcolor="#84F399" height="25" align="center">글쓰기</td>
				</tr>
			</table>
			<br/>
			<form name="postFrm" method="post" action="boardPost" enctype="multipart/form-data">
				<table width="80%" cellspacing="0" cellpadding="3" align="center">
					<tr>
						<td align="center">
							<table border="0" width="100%" align="center">
								<tr>
									<td width="10%">성  명</td>
									<td width="90%">
										<input name="name" size="10" maxlength="8">
									</td>
								</tr>
								<tr>
									<td width="10%">제  목</td>
									<td width="90%">
										<input name="subject" size="50" maxlength="30">
									</td>
								</tr>
								<tr>
									<td width="10%">내  용</td>
									<td width="90%">
										<textarea rows="10" cols="50" style="resize:none" name="content"></textarea>
									</td>
								</tr>
								<tr>
									<td width="10%">비밀번호</td>
									<td width="90%">
										<input type="password" name="pwd" size="15" maxlength="15">
									</td>
								</tr>
								<tr>
									<td width="10%">파일찾기</td>
									<td width="90%">
										<input type="file" name="filename" size="50" maxlength="30">
									</td>
								</tr>
								<tr>
									<td>내용타입</td>
									<td>HTML<input type="radio" name="contentType" value="HTTP">&nbsp;&nbsp;
									TEXT<input type="radio" name="contentType" value="TEXT" checked>
									</td>
								</tr>
								<tr>
									<td colspan="2"><hr/></td>
								</tr>
								<tr>
									<td colspan="2">
										<input type="submit" value="등록">
										<input type="reset" value="다시쓰기">
										<input type="button" value="리스트" onclick="javascript:location.href='list.jsp'">
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<input type="hidden" name="ip" value="<%=request.getRemoteAddr() %>">
			</form>
		</div>
	</body>
</html>