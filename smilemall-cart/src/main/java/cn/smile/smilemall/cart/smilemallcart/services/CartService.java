package cn.smile.smilemall.cart.smilemallcart.services;

import cn.smile.smilemall.cart.smilemallcart.to.UserInfoTo;
import cn.smile.smilemall.cart.smilemallcart.vo.CartItemVo;
import cn.smile.smilemall.cart.smilemallcart.vo.CartVo;

import java.util.List;

/**
 * <p></p>
 *
 * @author smile
 * @date 2021-02-25
 */
public interface CartService {
	
	/**
	 * <p>添加购物车商品</p>
	 * @author smile
	 * @date 2021/2/26/026
	 * @param skuId 1
	 * @param number 2
	 * @param userInfoTo 3
	 * @return boolean
	 */
	CartItemVo addCart(Long skuId, Integer number, UserInfoTo userInfoTo);
	
	/**
	 * <p>获取购物车的特定skuId的购物项</p>
	 * @author smile
	 * @date 2021/2/27/027
	 * @param skuId 1
	 * @param userInfoTo 2
	 * @return cn.smile.smilemall.cart.smilemallcart.vo.CartItemVo
	 */
	CartItemVo getCartItem(Long skuId, UserInfoTo userInfoTo);
	
	/**
	 * <p>获取购物车中的所有数据</p>
	 * @author smile
	 * @date 2021/2/27/027
	 * @param userInfoTo 1
	 * @return cn.smile.smilemall.cart.smilemallcart.vo.CartVo
	 */
	CartVo getCartInfo(UserInfoTo userInfoTo);
	
	/**
	 * <p>清空购物城</p>
	 * @author smile
	 * @date 2021/2/27/027
	 * @param key 1
	 * @return boolean
	 */
	boolean clearCart(String key);
	
	/**
	 * <p>修改状态</p>
	 * @author smile
	 * @date 2021/2/27/027
	 * @param skuId 1
	 * @param check 2
	 * @param userInfoTo 3
	 * @return void
	 */
	void checkItem(Long skuId, int check, UserInfoTo userInfoTo);
	
	/**
	 * <p>修改购物项数量</p>
	 * @author smile
	 * @date 2021/2/27/027
	 * @param skuId 1
	 * @param num 2
	 * @param userInfoTo 3
	 * @return void
	 */
	void countItem(Long skuId, int num, UserInfoTo userInfoTo);
	
	/**
	 * <p>删除购物项</p>
	 * @author smile
	 * @date 2021/2/27/027
	 * @param skId 1
	 * @param userInfoTo 2
	 * @return void
	 */
	void deleteItem(Long skId, UserInfoTo userInfoTo);
	
	/**
	 * <p>获取所有选中的商品</p>
	 * @author smile
	 * @date 2021/2/28/028
	 * @param userInfoTo
	 * @return cn.smile.smilemall.cart.smilemallcart.vo.CartItemVo
	 */
	List<CartItemVo> getCartHasCheck(UserInfoTo userInfoTo);
}
