import postapp.OracleAccess;
import postapp.exception.EmptyInputtedException;
import postapp.parameter.Reply;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommentDeleteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        String pw = request.getParameter("pw");
        String replyNumber = request.getParameter("replynumber");

        // パスワードが空白の場合例外発生
        if(pw.equals("")) {
            throw new EmptyInputtedException();
        }

        boolean isPwCorrect = false;

        try {
            OracleAccess access = new OracleAccess();

            String passWord = access.commentPwSelect(replyNumber);

            if(pw.equals(passWord)){    // パスワードが同じ場所
                isPwCorrect = true;

                access.commentDelete(replyNumber);  // replyNumberが同じコメントを削除
            }

            access.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("postnumber", request.getParameter("postnumber"));
        RequestDispatcher dispatcher;

        if(isPwCorrect) {
            dispatcher = request.getRequestDispatcher("commentdeleted");
        } else {
            dispatcher = request.getRequestDispatcher("pwincorrect");
        }

        dispatcher.forward(request, response);
    }
}
