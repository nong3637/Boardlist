package board;

import java.sql.*;
import java.util.*;
import java.io.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class BoardMgr {
	
	private DBConnectionMgr pool;
	private static final String SAVEFOLDER = "C:\\z_kgp\\java\\My_Board\\WebContent\\fileupload";
	private static final String ENCTYPE = "UTF-8";
	private static int MAXSIZE = 5*1024*1024;
	
	public BoardMgr() {
		try {
			pool = DBConnectionMgr.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Vector<BoardBean> getBoardList(String keyField, String keyWord, int start, int end) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		Vector<BoardBean> vlist = new Vector<BoardBean>();
		
		try {
			conn = pool.getConnection();
			if(keyWord.equals("null") || keyWord.equals("")) {
				sql = "select * from"
						+ " (select ROWNUM rnum, num, name, subject, content, pos, ref, depth, regdate, pwd, ip, count, filename, filesize"
						+ " from (select * from m_board order by ref desc, depth asc) m_board)"
						+ " where rnum>=? and rnum<=?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, start+1);
				pstmt.setInt(2, end);
			} else {
				sql = "select * from"
						+ " (select ROWNUM rnum, num, name, subject, content, pos, ref, depth, regdate, pwd, ip, count, filename, filesize"
						+ " from (select * from m_board order by ref desc, depth asc) m_board)"
						+ " where rnum>=? and rnum<=? and "
						+ keyField
						+ " like ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, start+1);
				pstmt.setInt(2, end);
				pstmt.setString(3, "%" + keyWord + "%");
			}
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardBean article = new BoardBean();
				article.setNum(rs.getInt("num"));
				article.setName(rs.getString("name"));
				article.setSubject(rs.getString("subject"));
				article.setPos(rs.getInt("pos"));
				article.setRef(rs.getInt("ref"));
				article.setDepth(rs.getInt("depth"));
				article.setRegDate(rs.getTimestamp("regdate"));
				article.setCount(rs.getInt("count"));
				vlist.add(article);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(conn, pstmt, rs);
		}
		
		return vlist;
	}
	public int getTotalCount(String keyField, String keyWord) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int totalCount = 0;
		
		try {
			conn = pool.getConnection();
			if(keyWord.equals("null") || keyWord.equals("")) {
				sql = "select count(num) from m_board";
				pstmt = conn.prepareStatement(sql);
			} else {
				sql = "select count(num) from m_board where "
						+ keyField
						+ " like ? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "%" + keyWord + "%");
			}
			rs = pstmt.executeQuery();
			if(rs.next()) {
				totalCount = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(conn, pstmt, rs);
		}
		
		return totalCount;
	}
	

	public void insertBoard(HttpServletRequest req) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		MultipartRequest multi = null;
		int filesize = 0;
		String filename = null;
		try {
			conn = pool.getConnection();
			sql = "select max(num) from m_board";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int ref = 1;
			if(rs.next())
				ref = rs.getInt(1) + 1;
			File file = new File(SAVEFOLDER);
			if(!file.exists())
				file.mkdir();
			multi = new MultipartRequest(req, SAVEFOLDER, MAXSIZE, ENCTYPE, new DefaultFileRenamePolicy());
			
			if(multi.getFilesystemName("filename") != null) {
				filename = multi.getFilesystemName("filename");
				filesize = (int) multi.getFile("filename").length();
			}
			String content = multi.getParameter("content");
			if(multi.getParameter("contentType").equalsIgnoreCase("TEXT")) {
				content = UtilMgr.replace(content, "<", "&lt;");
			}
			sql = "insert into m_board"
					+ " (name, subject, content, ref, pos, depth, regdate, pwd, count, ip, filename, filesize)"
					+ " values(?, ?, ?, ?, 0, 0, CURRENT_TIMESTAMP, ?, 0, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, multi.getParameter("name"));
			pstmt.setString(2, multi.getParameter("subject"));
			pstmt.setString(3, content);
			pstmt.setInt(4, ref);
			pstmt.setString(5, multi.getParameter("pwd"));
			pstmt.setString(6, multi.getParameter("ip"));
			pstmt.setString(7, filename);
			pstmt.setInt(8, filesize);
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(conn, pstmt, rs);
		}
	}
	
	public BoardBean getBoard(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		BoardBean article = new BoardBean();
		
		try {
			conn = pool.getConnection();
			sql = "select * from m_board where num = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				article.setNum(rs.getInt("num"));
				article.setName(rs.getString("name"));
				article.setSubject(rs.getString("subject"));
				article.setContent(rs.getString("content"));
				article.setPos(rs.getInt("pos"));
				article.setRef(rs.getInt("ref"));
				article.setDepth(rs.getInt("depth"));
				article.setRegDate(rs.getTimestamp("regdate"));
				article.setPwd(rs.getString("pwd"));
				article.setCount(rs.getInt("count"));
				article.setFileName(rs.getString("filename"));
				article.setFileSize(rs.getInt("filesize"));
				article.setIp(rs.getString("ip"));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(conn, pstmt, rs);
		}
		
		return article;
	}
	
	public void upCount(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = pool.getConnection();
			sql = "update m_board set count=count+1 where num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(conn, pstmt);
		}
	}
	
	public void deleteBoard(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			conn = pool.getConnection();
			sql = "select filename from m_board where num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if(rs.next() && rs.getString(1) != null) {
				if(!rs.getString(1).equals("")) {
					File file = new File(SAVEFOLDER + "/" + rs.getString(1));
					if(file.exists())
						UtilMgr.delete(SAVEFOLDER + "/" + rs.getString(1));
				}
			}
			sql = "delete from m_board where num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(conn, pstmt, rs);
		}
	}
	
	public void updateBoard(BoardBean article) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		try {
			conn = pool.getConnection();
			sql = "update m_board set name=?, subject=?, content=? where num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, article.getName());
			pstmt.setString(2, article.getSubject());
			pstmt.setString(3, article.getContent());
			pstmt.setInt(4, article.getNum());
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(conn, pstmt);
		}
	}
	
	public void replyBoard(BoardBean article) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		try {
			conn = pool.getConnection();
			sql = "insert into m_board (name, content, subject, ref, pos, depth, regdate, pwd, count, ip) "
					+ " values(?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, 0, ?)";
			int depth = article.getDepth() + 1;
			int pos = article.getPos() + 1;
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, article.getName());
			pstmt.setString(2, article.getContent());
			pstmt.setString(3, article.getSubject());
			pstmt.setInt(4, article.getRef());
			pstmt.setInt(5, pos);
			pstmt.setInt(6, depth);
			pstmt.setString(7, article.getPwd());
			pstmt.setString(8, article.getIp());
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(conn, pstmt, rs);
		}
	}
	
	public void replyUpBoard(int ref, int pos) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		
		try {
			conn = pool.getConnection();
			sql = "update m_board set pos=pos+1 where ref=? and pos>?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, ref);
			pstmt.setInt(2, pos);
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(conn, pstmt);
		}
	}
	
	public void download(HttpServletRequest req, HttpServletResponse res, JspWriter out, PageContext pageContext) {
		try {
			String filename = req.getParameter("filename");
			File file = new File(UtilMgr.con(SAVEFOLDER + File.separator + filename));
			byte b[] = new byte[(int) file.length()];
			res.setHeader("Accept-Ranges", "bytes");
			String strClient = req.getHeader("User-Agent");
			
			if (strClient.indexOf("MSIE6.0") != -1) {
				res.setContentType("application/smnet;charset=UTF-8");
				res.setHeader("Content-Disposition", "filename=" + filename + ";");
			} else {
				res.setContentType("application/smnet;charset=UTF-8");
				res.setHeader("Content-Disposition", "attachment;filename="+ filename + ";");
			}
			out.clear();
			out=pageContext.pushBody();
			if (file.isFile()) {
				BufferedInputStream fin = new BufferedInputStream(
						new FileInputStream(file));
				BufferedOutputStream outs = new BufferedOutputStream(
						res.getOutputStream());
				int read = 0;
				while ((read = fin.read(b)) != -1) {
					outs.write(b, 0, read);
				}
				outs.close();
				fin.close();
			}
			
		} catch(Exception e) {
			
		}
	}
}









