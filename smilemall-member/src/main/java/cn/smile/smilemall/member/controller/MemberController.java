package cn.smile.smilemall.member.controller;

import cn.smile.common.exception.BizCodeEnume;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.R;
import cn.smile.common.vo.LoginUserInfo;
import cn.smile.smilemall.member.entity.MemberEntity;
import cn.smile.smilemall.member.exception.PasswordErrorException;
import cn.smile.smilemall.member.exception.PhoneExistException;
import cn.smile.smilemall.member.exception.UserNameExistException;
import cn.smile.smilemall.member.exception.UserNotExistException;
import cn.smile.smilemall.member.service.MemberService;
import cn.smile.smilemall.member.vo.SocialUserVo;
import cn.smile.smilemall.member.vo.UserRegisterVo;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 会员
 *
 * @author smile
 * @email 1367159064@qq.com
 * @date 2021-01-06 23:32:47
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    private final MemberService memberService;
    
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    
    
    @PostMapping("/userLogin")
    public R userLogin(@RequestBody UserRegisterVo userRegisterVo) {
        MemberEntity memberEntity;
        try {
            memberEntity = memberService.userLogin(userRegisterVo);
        } catch ( UserNotExistException e) {
            return R.error(BizCodeEnume.ACCOUNT_ERROR.getCode(), BizCodeEnume.ACCOUNT_ERROR.getMsg());
        } catch (PasswordErrorException e) {
            return R.error(BizCodeEnume.PASSWORD_ERROR.getCode(), BizCodeEnume.PASSWORD_ERROR.getMsg());
        }
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        BeanUtils.copyProperties(memberEntity, loginUserInfo);
        return R.ok().put("userInfo", loginUserInfo);
    }
    
    @PostMapping("/socialLogin")
    public R socialLogin(@RequestBody SocialUserVo socialUserVo) {
        MemberEntity memberEntity = memberService.socialLogin(socialUserVo);
        if(memberEntity == null) {
            return R.error(BizCodeEnume.O_AUTH_FAIL_EXCEPTION.getCode(), BizCodeEnume.O_AUTH_FAIL_EXCEPTION.getMsg());
        }
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        BeanUtils.copyProperties(memberEntity, loginUserInfo);
        return R.ok().put("userInfo", loginUserInfo);
    }
    
    @PostMapping("/register")
    public R register(@RequestBody UserRegisterVo registerVo) {
        try {
            memberService.register(registerVo);
        } catch (Exception e) {
            if(e instanceof PhoneExistException) {
                return R.error(BizCodeEnume.PHONE_EXIT_EXCEPTION.getCode(), BizCodeEnume.PHONE_EXIT_EXCEPTION.getMsg());
            } else if(e instanceof UserNameExistException) {
                return R.error(BizCodeEnume.USER_EXIT_EXCEPTION.getCode(), BizCodeEnume.USER_EXIT_EXCEPTION.getMsg());
            } else {
                return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
            }
        }
        return R.ok();
    }
    
    
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
