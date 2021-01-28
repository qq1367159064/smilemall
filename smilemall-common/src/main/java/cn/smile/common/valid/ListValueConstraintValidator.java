package cn.smile.common.valid;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Smile
 * @Documents
 * @creationTime 2021-01-2021/1/15/015
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {
	
	private Set<Integer> listValueSet = new LinkedHashSet<>();
	
	/**
	 * @param value   1
	 * @param context 2
	 * @return boolean
	 * @Description 判断是否校验成功
	 * @author Smile
	 * @date 2021/1/15/015
	 */
	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		// value 用户传过来的值
		return listValueSet.contains(value);
	}
	
	/**
	 * @param constraintAnnotation 1
	 * @return void
	 * @Description 初始化化方法
	 * @author Smile
	 * @date 2021/1/15/015
	 */
	@Override
	public void initialize(ListValue constraintAnnotation) {
		// 获取指定的值
		int[] value = constraintAnnotation.value();
		Arrays.stream(value).forEach(item -> {
			if (!StringUtils.isEmpty(item)) {
				listValueSet.add(item);
			}
		});
	}
}
