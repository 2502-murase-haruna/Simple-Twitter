/**
 *
 */
package chapter6.service;

/**
 * @author trainee1207
 *
 */
import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.User;
import chapter6.dao.UserDao;
import chapter6.logging.InitApplication;
import chapter6.utils.CipherUtil;

public class UserService {
    /**
    * ロガーインスタンスの生成
    */
	Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public UserService() {
    	InitApplication application = InitApplication.getInstance();
    	application.init();
    }

    public void insert(User user) {
    	log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
		    // パスワード暗号化
			String encPassword = CipherUtil.encrypt(user.getPassword());
			user.setPassword(encPassword);

			connection = getConnection();
			new UserDao().insert(connection, user);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
		    throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
		    throw e;
		} finally {
		    close(connection);
		}
    }

    public User select(String accountOrEmail, String password) {

    	log.info(new Object(){}.getClass().getEnclosingClass().getName() +
		    " : " + new Object(){}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
		    // パスワード暗号化
			String encPassword = CipherUtil.encrypt(password);

			connection = getConnection();
			User user = new UserDao().select(connection, accountOrEmail, encPassword);
			commit(connection);

		    return user;
		} catch (RuntimeException e) {
			rollback(connection);
		    log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
		    throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
		    throw e;
		} finally {
		    close(connection);
		}
    }

    public User select(int userId) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
		" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			connection = getConnection();
			User user = new UserDao().select(connection, userId);
			commit(connection);

			return user;
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
		    throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
		    throw e;
		} finally {
		    close(connection);
		}
    }

    public void update(User user) {

		log.info(new Object(){}.getClass().getEnclosingClass().getName() +
		" : " + new Object(){}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			if(!StringUtils.isBlank(user.getPassword())) {
				/*serviceで行っている処理のうち、分岐させなければいけないものをここに入れる*/

				 //①パスワード暗号化
				String encPassword = CipherUtil.encrypt(user.getPassword());
				user.setPassword(encPassword);
			}
			/*入っていても入っていなくてもする処理はこの下に記載*/

			//②DB接続
			connection = getConnection();

			/*③Dao呼び出し*/
			new UserDao().update(connection, user);

			/*④コミット*/
			commit(connection);

		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
		    throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
		    throw e;
		} finally {
		    close(connection);
		}
    }

    /*setting、signUpサーブレット-バリデーション(重複チェック用)select */
    public User select(String account) {

    	Connection connection = null;
    	try {
    		connection = getConnection();

            /*dao(selectを呼び出し、検索結果(return)を変数userに格納*/
    		User user = new UserDao().select(connection, account);

    		commit(connection);

            /*servletに検索結果をreturn*/
    		return user;

        } catch (RuntimeException e) {
        	rollback(connection);
        	throw e;
        } catch (Error e) {
        	rollback(connection);
        	throw e;
        } finally {
            close(connection);
        }
    }

}
