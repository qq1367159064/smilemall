package cn.smile.smilemall.product.entity;

import cn.smile.common.valid.AddGroup;
import cn.smile.common.valid.ListValue;
import cn.smile.common.valid.UpdateGroup;
import cn.smile.common.valid.UpdateStatus;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.apache.ibatis.annotations.Update;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 21:10:08
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@Null(groups = { AddGroup.class }, message = "添加不可有Id")
	@NotNull(message = "修改不能为空", groups = { UpdateGroup.class })
	private Long brandId;
	/**
	 * 品牌名
	 */
	
	@NotBlank(message = "品牌名称不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotEmpty(groups = { AddGroup.class })
	@URL(message = "logo地址不合法", groups = { AddGroup.class, UpdateGroup.class })
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@ListValue(value = { 0, 1}, groups = {UpdateStatus.class, AddGroup.class, UpdateGroup.class })
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotEmpty(message = "不能为空", groups = { AddGroup.class })
	@Pattern(regexp = "^[a-zA-Z]{1}$", message = "不合法检索首字母[范围a-zA-Z,长度只能为1]", groups = { AddGroup.class,
			UpdateGroup.class })
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "不能为空", groups = { AddGroup.class })
	@Digits(integer = 999, fraction = 0, message = "排序参数不合法[必须大于等于0]", groups = { AddGroup.class, UpdateGroup.class })
	private Integer sort;

}
