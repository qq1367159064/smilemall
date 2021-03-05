package cn.smile.smilemall.member.service;

import cn.smile.smilemall.member.exception.PasswordErrorException;
import cn.smile.smilemall.member.exception.PhoneExistException;
import cn.smile.smilemall.member.exception.UserNameExistException;
import cn.smile.smilemall.member.exception.UserNotExistException;
import cn.smile.smilemall.member.vo.SocialUserVo;
import cn.smile.smilemall.member.vo.UserRegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.smile.common.utils.PageUtils;
import cn.smile.smilemall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:32:47
 */
public interface MemberService extends IService<MemberEntity> {

	/**
	 * <p>通用分页查询</p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @param params 1
	 * @return cn.smile.common.utils.PageUtils
	 */
    PageUtils queryPage(Map<String, Object> params);
	
    /**
     * <p>用户注册</p>
     * @author smile
     * @date 2021/2/23/023
     * @param registerVo 1
	 * @throws Exception
     * @return boolean
     */
	boolean register(UserRegisterVo registerVo) throws Exception;
	
	/**
	 * <p>检查邮箱是否唯一</p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @param email 1
	 * @return boolean
	 */
	boolean checkEmailUnique(String email);
	/**
	 * <p>检查用户名是否唯一</p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @param username 1
	 * @throws UserNameExistException 用户名已存在异常
	 * @return boolean
	 */
	boolean checkUserNameUnique(String username) throws UserNameExistException;
	/**
	 * <p>检查手机号是否唯一</p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @throws  PhoneExistException 如果手机号已经存在抛出手机号已经存在异常
	 * @param phone 1
	 * @return boolean
	 */
	boolean checkPhoneUnique(String phone) throws PhoneExistException;
	
	/**
	 * <p>用户登录</p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @param registerVo 1
	 * @throws PasswordErrorException
	 * @throws UserNotExistException
	 * @return cn.smile.smilemall.member.entity.MemberEntity
	 */
	MemberEntity userLogin(UserRegisterVo registerVo) throws PasswordErrorException, UserNotExistException;
	
	/**
	 * <p>社交登录</p>
	 * @author smile
	 * @date 2021/2/23/023
	 * @param socialUserVo 1
	 * @return MemberEntity
	 */
	MemberEntity socialLogin(SocialUserVo socialUserVo);
}

