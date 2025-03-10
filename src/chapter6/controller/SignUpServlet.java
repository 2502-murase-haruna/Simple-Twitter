/**
 *
 */
package chapter6.controller;

/**
 * @author trainee1207
 *
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.UserService;

@WebServlet(urlPatterns = { "/signup" })
public class SignUpServlet extends HttpServlet {
	/**
	 * ロガーインスタンスの生成
	 */
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public SignUpServlet() {
	    InitApplication application = InitApplication.getInstance();
	    application.init();

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	    " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	    request.getRequestDispatcher("signup.jsp").forward(request, response);
	}

	/*ユーザ登録　の　登録(insert)*/
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws IOException, ServletException {


	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	    " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	    List<String> errorMessages = new ArrayList<String>();

	    /*リクエストから入力値(user)をget*/
	    User user = getUser(request);

	    /*user(入力値)：バリデーションチェック falseの時、
	     * エラーメッセージをセット、signup.jspにforward*/
	    if (!isValid(user, errorMessages)) {
	        request.setAttribute("errorMessages", errorMessages);
	        request.getRequestDispatcher("signup.jsp").forward(request, response);
	        return;
	    }

	    /*エラーが無い場合、serviceを呼び出し(insert)*/
	    new UserService().insert(user);
	    response.sendRedirect("./");
	}

	private User getUser(HttpServletRequest request) throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	    " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	    User user = new User();
	    user.setName(request.getParameter("name"));
	    user.setAccount(request.getParameter("account"));
	    user.setPassword(request.getParameter("password"));
	    user.setEmail(request.getParameter("email"));
	    user.setDescription(request.getParameter("description"));
	    return user;
	}

	/*バリデーション…入力値のチェック
	 * true:エラーが　0件
	 * false:エラーが　1件以上*/
	private boolean isValid(User user, List<String> errorMessages) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	    " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	    /*リクエストからgetした入力値(＝引数のuser)各入力値をget*/
	    String name = user.getName();
	    String account = user.getAccount();
	    String password = user.getPassword();
	    String email = user.getEmail();

	    /*バリデーションチェックを行うためのserviceを呼び出し(select),
	     * confirmAccount … セレクト結果*/
	    User confirmAccount = new UserService().select(account);

	    if (!StringUtils.isEmpty(name) && (20 < name.length())) {
	        errorMessages.add("名前は20文字以下で入力してください");
	    }

	    /*文字数*/
	    if (StringUtils.isEmpty(account)) {
	        errorMessages.add("アカウント名を入力してください");
	    } else if(20 < account.length()) {
	        errorMessages.add("アカウント名は20文字以下で入力してください");
	    }

	    /*重複*/
	    if (confirmAccount != null) {
	    	errorMessages.add("ユーザーが重複しています");
	    }

	    if (StringUtils.isEmpty(password)) {
	        errorMessages.add("パスワードを入力してください");
	    }

	    if (!StringUtils.isEmpty(email) && (50 < email.length())) {
	        errorMessages.add("メールアドレスは50文字以下で入力してください");
	    }

	    if (errorMessages.size() != 0) {
	        return false;
	    }
	    return true;
	}
}
