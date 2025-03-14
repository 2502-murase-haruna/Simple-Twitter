package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {

	Logger log = Logger.getLogger("twitter");

	public EditServlet() {
        InitApplication application = InitApplication.getInstance();
        application.init();
    }

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        HttpSession session = request.getSession();
        boolean messages = false;

        String messageId = request.getParameter("messageId");
        int messageIdInt = Integer.parseInt(messageId);

        Message messages = new MessageService().edit(messageIdInt);
        request.setAttribute("messages", messages);
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

		/*sessionを取得　＝　sessionを使えるようにする*/
		HttpSession session = request.getSession();
		/*エラーメッセージ格納リストを作成*/
		List<String> errorMessages = new ArrayList<String>();

		/*値を取得：message(投稿内容、テキスト)をリクエストからgetしている*/
        String text = request.getParameter("text");

        /*エラーチェックを行う*/
		if (isValid(text, errorMessages)) {
			/*エラーメッセージをセット*/
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
        }

		/*チェックを抜けて　変数messageを作成（インスタンス生成）*/
        Message message = new Message();
        /*テキストをセット*/
        message.setText(text);

        /*user型で変数を作成sessionからログインユーザーを取得*/
        User user = (User) session.getAttribute("loginUser");

        /*ユーザid(session取得したログインユーザ情報から持ってきたもの)をセット*/
        message.setUserId(user.getId());

        /*serviceを呼出し updateメソッド(引数：message)*/
        new MessageService().update(messageId);
        /*リダイレクト*/
        response.sendRedirect("./");

    }

	private boolean isValid(String text, List<String> errorMessages) {

		  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	        if (StringUtils.isBlank(text)) {
	            errorMessages.add("メッセージを入力してください");
	        } else if (140 < text.length()) {
	            errorMessages.add("140文字以下で入力してください");
	        }

	        if (errorMessages.size() != 0) {
	            return false;
	        }
	        return true;
	    }
}
