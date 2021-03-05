package cn.smile.smilemall.product.vo;

import cn.smile.smilemall.product.entity.AttrEntity;
import cn.smile.smilemall.product.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Smile
 * @Documents
 * @date 2021-01-2021/1/24/024
 */
@Data
public class AttrGroupWithAttrsVo extends AttrGroupEntity {
	
	private List<AttrEntity> attrs;
}
