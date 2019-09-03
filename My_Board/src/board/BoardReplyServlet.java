package board;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BoardReplyServlet
 */
@WebServlet("/boardReply")
public class BoardReplyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BoardReplyServlet() {
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
		
		BoardMgr bMgr = new BoardMgr();
		BoardBean reArticle = new BoardBean();
		reArticle.setNum(Integer.parseInt(request.getParameter("num")));
		reArticle.setName(request.getParameter("name"));
		reArticle.setSubject(request.getParameter("subject"));
		reArticle.setContent(request.getParameter("content"));
		reArticle.setRef(Integer.parseInt(request.getParameter("ref")));
		reArticle.setPos(Integer.parseInt(request.getParameter("pos")));
		reArticle.setDepth(Integer.parseInt(request.getParameter("depth")));
		reArticle.setPwd(request.getParameter("pwd"));
		reArticle.setIp(request.getParameter("ip"));
		
		bMgr.replyUpBoard(reArticle.getRef(), reArticle.getPos());
		bMgr.replyBoard(reArticle);
		
		String nowPage = request.getParameter("nowPage");
		response.sendRedirect("list.jsp?nowPage="+nowPage);
	}

}
