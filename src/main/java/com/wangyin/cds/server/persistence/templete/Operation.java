package com.wangyin.cds.server.persistence.templete;

import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.wangyin.cds.server.persistence.PersistenceManager;


/**   
 * @author wy   
 */
public class Operation {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
			.getLogger(Operation.class);
	public static ResultInfo callBack(IAction action) {
		ResultInfo resultInfo = new ResultInfo();
		TransactionFactory transactionFactory = new JdbcTransactionFactory();  
		SqlSession session = PersistenceManager.getSession().openSession();
		Transaction newTransaction = transactionFactory.newTransaction(session.getConnection());  
		try{
			resultInfo.setResult(action.doAction(session));
			resultInfo.setSuccess(true);
			newTransaction.commit();
		}catch(Exception e){
			resultInfo.setSuccess(false);
			resultInfo.setMessage(e.toString());
			try {
				newTransaction.rollback();
			} catch (SQLException e1) {
				logger.error(e1.toString());
			}
		}finally{
			try {
				newTransaction.close();
			} catch (SQLException e) {
				resultInfo.setSuccess(false);
				resultInfo.setMessage(e.toString());
				logger.error(e.toString());
			}
		}
		return resultInfo;
	}

}