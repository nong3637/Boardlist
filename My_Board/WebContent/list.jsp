<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="board.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.Timestamp"%>
<jsp:useBean id="bMgr" class="board.BoardMgr"/>

<%
	request.setCharacterEncoding("UTF-8");
	int totalRecord = 0;
	int numPerPage = 10;
	int pagePerBlock = 15;
	
	int totalPage = 0;
	int totalBlock = 0;
	
	int nowPage = 1;
	int nowBlock = 1;
	
	int start = 0;
	int end = 10;
	
	int listSize = 0;
	String keyWord ="", keyField ="";
	Vector<BoardBean> vlist = null;
	
	if(request.getParameter("keyWord") != null) {
		keyWord = request.getParameter("keyWord");
		keyField = request.getParameter("keyField");
	}
	if(request.getParameter("reload") != null) {
		if(request.getParameter("reload").equals("true")) {
			keyWord = "";
			keyField = "";
		}
	}
	if(request.getParameter("nowPage") != null) {
		nowPage = Integer.parseInt(request.getParameter("nowPage"));
	}
	start = (nowPage * numPerPage) - numPerPage;
	end = start + numPerPage;
	
	totalRecord = bMgr.getTotalCount(keyField, keyWord);
	totalPage = (int)Math.ceil((double)totalRecord / numPerPage);
	nowBlock = (int)Math.ceil((double)nowPage / pagePerBlock);
	totalBlock = (int)Math.ceil((double)totalPage / pagePerBlock);
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>게시판 리스트</title>
		<link href="style.css" rel="stylesheet" type="text/css">
		<script type="text/javascript">
			function list() {
				document.listFrm.action = "list.jsp";
				document.listFrm.submit();
			}
			function pageing(page) {
				document.readFrm.nowPage.value = page;
				document.readFrm.submit();
			}
			function block(value) {
				document.readFrm.nowPage.value = <%=pagePerBlock%>*(value-1)+1;
				document.readFrm.submit();
			}
			function read(num) {
				document.readFrm.num.value = num;
				document.readFrm.action = "read.jsp";
				document.readFrm.submit();
			}
			function check() {
				if(document.searchFrm.keyWord.value == "") {
					alert("검색어를 입력하세요.");
					document.searchFrm.keyWord.focus();
					return;
				}
				document.searchFrm.submit();
			}
		</script>
	</head>  
	<body bgcolor="#FFFFCC">
	<div align="center">
		<h2>Free Board</h2>
		<br/>
		<table align="center" border="0" width="80%">
			<tr>
				<td>Total : <%=totalRecord%>Articles (<font color="red">
				<%=nowPage%>/<%=totalPage%>Pages</font> )</td>
			</tr>
		</table>
		<table align="center" width="80%" border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td align="center" colspan="2">
				<%
				vlist = bMgr.getBoardList(keyField, keyWord, start, end);
				listSize = vlist.size();
				if(vlist.isEmpty()) {
					out.println("등록된 게시물이 없습니다.");
				} else {
				%>
				<table border="0" width="100%" cellpadding="2" cellspacing="0">
					<tr align="center" bgcolor="#D0D0D0" height="120%">
						<td>No.</td>
		  				<td>제   목</td>
						<td>작성자</td>
						<td>작성일</td>
						<td>조 회</td>
					</tr>
					<%
					for(int i=0; i<numPerPage; i++) {
						if(i == listSize) break;
						BoardBean article = vlist.get(i);
						int num = article.getNum();
						String name = article.getName();
						String subject = article.getSubject();
						Timestamp regdate = article.getRegDate();
						int depth = article.getDepth();
						int count = article.getCount();
					%>
					<tr>
						<td align="center"><%=totalRecord-((nowPage-1)*numPerPage)-i %></td>
						<td>
							<%
							if(depth > 0) {
								for(int j=0; j<depth; j++)
									out.println("&nbsp;&nbsp;");
							}
							%>
							<a href="javascript:read('<%=num %>')"><%=subject %></a>
						</td>
						<td align="center"><%=name %></td>
						<td align="center"><%=regdate %></td>
						<td align="center"><%=count %></td> 
					</tr>

					<%
					}//for
					%>
				</table>
				<%
				}//if
				%>
				</td>
			</tr>
			<tr>
				<td colspan="2"><br/><br/></td>
			</tr>
			<tr>
				<td>
				<!-- 페이징 및 블럭 처리 -->
				<%
				int pageStart = (nowBlock - 1) * pagePerBlock + 1;	// 하단 페이지 시작 번호
				int pageEnd = ((pageStart + pagePerBlock) <= totalPage) ? (pageStart + pagePerBlock) : totalPage + 1;
					// 하단 페이지 끝 번호
				if(totalPage !=0){
    			  	if (nowBlock > 1) {%>
    			  		<a href="javascript:block('<%=nowBlock-1%>')">prev...</a><%}%>&nbsp; 
    			  		<%for ( ; pageStart < pageEnd; pageStart++){%>
     			     	<a href="javascript:pageing('<%=pageStart %>')"> 
     					<%if(pageStart==nowPage) {%><font color="blue"> <%}%>
     					[<%=pageStart %>] 
     					<%if(pageStart==nowPage) {%></font> <%}%></a> 
    					<%}//for%>&nbsp; 
    					<%if (totalBlock > nowBlock ) {%>
    					<a href="javascript:block('<%=nowBlock+1%>')">.....next</a>
    				<%}%>&nbsp;  
   				<%}%>
   				<!-- 페이징 및 블럭 처리 end -->
   				</td>
   				<td align="right">
   					<a href="post.jsp">[글쓰기]</a>
   					<a href="javascript:list()">[처음으로]</a>
   				</td>
			</tr>
		</table>
		<hr width="80%"/>
		<form  name="searchFrm"  method="post" action="list.jsp">
			<table border="0" width="527" align=center cellpadding="4" cellspacing="0">
		 		<tr>
		  			<td align="center" valign="bottom">
		   				<select name="keyField" size="1" >
		    				<option value="name"> 이 름</option>
		    				<option value="subject"> 제 목</option>
		    				<option value="content"> 내 용</option>
		   				</select>
		   				<input type="text" size="16" name="keyWord">
		   				<input type="button"  value="찾기" onClick="javascript:check()">
		   				<input type="hidden" name="nowPage" value="1">
		  			</td>
		 		</tr>
			</table>
		</form>
		<form name="listFrm" method="post">
			<input type="hidden" name="reload" value="true"> 
			<input type="hidden" name="nowPage" value="1">
		</form>
		<form name="readFrm" method="get">
			<input type="hidden" name="num"> 
			<input type="hidden" name="nowPage" value="<%=nowPage%>"> 
			<input type="hidden" name="keyField" value="<%=keyField%>"> 
			<input type="hidden" name="keyWord" value="<%=keyWord%>">
		</form>
	</div>
	</body>
</html>