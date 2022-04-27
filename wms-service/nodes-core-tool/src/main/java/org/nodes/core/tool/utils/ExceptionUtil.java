package org.nodes.core.tool.utils;

import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.api.IResultCode;
import org.springblade.core.tool.utils.Func;


/**
 * 异常辅助工具类
 *
 * @author HCL
 * @since 2018-07-24
 */
public final class ExceptionUtil {

	private ExceptionUtil() {
	}

	/**
	 * 返回一个新的异常，统一构建，方便统一处理
	 *
	 * @param resultCode 消息枚举
	 * @return 返回异常
	 */
	public static ServiceException mpe(IResultCode resultCode) {
		return new ServiceException(resultCode);
	}

	/**
	 * 返回一个新的异常，统一构建，方便统一处理
	 *
	 * @param resultCode 消息枚举
	 * @param t          异常信息
	 * @return 返回异常
	 */
	public static ServiceException mpe(IResultCode resultCode, Throwable t) {
		return new ServiceException(resultCode, t);
	}

	/**
	 * 重载的方法
	 *
	 * @param msg 消息
	 * @return 返回异常
	 */
	public static ServiceException mpe(String msg, Object... params) {
		return new ServiceException(Func.format(msg, params));
	}

}
