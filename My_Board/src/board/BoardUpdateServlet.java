package board;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class BoardUpdateServlet
 */
@WebServlet("/boardUpdate")
public class BoardUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html); charset=UTF-8");
		
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		
		BoardMgr bMgr = new BoardMgr();
		BoardBean article = (BoardBean) session.getAttribute("article");
		String nowPage = request.getParameter("nowPage");
		
		BoardBean upArticle = new BoardBean();
		upArticle.setNum(Integer.parseInt(request.getParameter("num")));
		upArticle.setName(request.getParameter("name"));
		upArticle.setSubject(request.getParameter("subject"));
		upArticle.setContent(request.getParameter("content"));
		upArticle.setPwd(request.getParameter("pwd"));
		upArticle.setIp(request.getParameter("ip"));
		
		String upPwd = upArticle.getPwd();
		String inPwd = article.getPwd();
		
		if(upPwd.equals(inPwd)) {
			bMgr.updateBoard(upArticle);
			String url = "read.jsp?nowPage=" + nowPage + "&num=" + upArticle.getNum();
			response.sendRedirect(url);
		} else {
			out.println("<script>");
			out.println("alert('입력하신 비밀번호가 아닙니다');");
			out.println("history.back();");
			out.println("</script>");
		}
	}

}






