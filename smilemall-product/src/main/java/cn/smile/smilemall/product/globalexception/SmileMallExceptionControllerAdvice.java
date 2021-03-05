package cn.smile.smilemall.product.globalexception;

import cn.smile.common.exception.BizCodeEnume;
import cn.smile.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Smile
 * @Documents
 * @date 2021-01-2021/1/14/014
 */
@ControllerAdvice(basePackages = {"cn.smile.smilemall.product.controller"})
@ResponseBody
@Slf4j
public class SmileMallExceptionControllerAdvice {
	
	/**
	 * @param e 错误类型 { MethodArgumentNotValidException }
	 * @return cn.smile.common.utils.R
	 * @Description Controller 参数校验异常处理
	 * @author Smile
	 * @date 2021/1/14/014
	 */
	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	public R handleValidatorException(MethodArgumentNotValidException e) {
		log.warn("数据校验错误{}, 异常类型 {}", e.getMessage(), e.getClass());
		BindingResult bindingResult = e.getBindingResult();
		Map<Object, Object> errorMaps = new LinkedHashMap<>();
		bindingResult.getFieldErrors().forEach(item -> {
			errorMaps.put(item.getField(), item.getDefaultMessage());
		});
		return R.error(BizCodeEnume.VALID_EXCEPTION.getCode(), BizCodeEnume.VALID_EXCEPTION.getMsg()).put("errorParameters",
				errorMaps);
	}
	
	/**
	 * @Description Controller全局异常处理类
	 * @author Smile
	 * @date 2021/1/14/014
	 * @param e 异常类型{不确定}
	 * @return cn.smile.common.utils.R
	 */
	@ExceptionHandler(value = { Exception.class })
	public R handleException(Exception e) {
		if(e instanceof HttpMessageNotReadableException) {
			try {
				return R.error(BizCodeEnume.JSONE_PARSE_EXCEPTION.getCode(), BizCodeEnume.JSONE_PARSE_EXCEPTION.getMsg()).put(
						"errorInfo", ((HttpMessageNotReadableException) e).getHttpInputMessage().getBody().toString());
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
 		return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
	}
}
