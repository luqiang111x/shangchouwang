package util;

/**
 *    统一ajax所有请求的返回值类型
 * @author haha
 *
 * @param <T>
 */

public class ResultEntity<T> {

	public static final String SUCCESS ="SUCCESS";
	public static final String FAILED ="FAILED";
	public static final String NO_MESSAGE ="NO_MESSAGE";
	public static final String NO_DATA ="NO_DATA";

	// 操作返回结果 true or false
	private String result;

	// 错误消息
	private String message;

	// 返回给前端的数据
	private T data;



	public ResultEntity() {
		super();
	}



	public ResultEntity(String result, String message, T data) {
		super();
		this.result = result;
		this.message = message;
		this.data = data;
	}



	/**
	 *    返回操作成功，不带数据
	 */
	public static <E> ResultEntity<E> successWithoutData(){
		return new ResultEntity<E>(SUCCESS,NO_MESSAGE,null);
	}

	/**
	 *     返回操作成功，带数据
	 * @return
	 */
	public static <E> ResultEntity<E> successWithData(E data){
		return new ResultEntity<E>(SUCCESS,NO_MESSAGE,data);
	}

	/**
	 *    返回操作失败，不带数据
	 */
	public static <E> ResultEntity<E> failedWithoutData(String message){
		return new ResultEntity<E>(FAILED,message,null);
	}



	@Override
	public String toString() {
		return "ResultEntity [operationResult=" + result + ", operationMessage=" + message
				+ ", queryData=" + data + "]";
	}


	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
