package cn.smile.smilemall.member.service.impl;

import cn.hutool.core.convert.ConvertException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.smile.common.smilemall.utils.HttpUtils;
import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;
import cn.smile.smilemall.member.dao.MemberDao;
import cn.smile.smilemall.member.entity.MemberEntity;
import cn.smile.smilemall.member.entity.MemberLevelEntity;
import cn.smile.smilemall.member.exception.PasswordErrorException;
import cn.smile.smilemall.member.exception.PhoneExistException;
import cn.smile.smilemall.member.exception.UserNameExistException;
import cn.smile.smilemall.member.exception.UserNotExistException;
import cn.smile.smilemall.member.service.MemberLevelService;
import cn.smile.smilemall.member.service.MemberService;
import cn.smile.smilemall.member.vo.SocialUserVo;
import cn.smile.smilemall.member.vo.UserRegisterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author smile
 */
@Service("memberService")
@Slf4j
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {
    
    private final MemberLevelService memberLevelService;
    
    public MemberServiceImpl(MemberLevelService memberLevelService) {
        this.memberLevelService = memberLevelService;
    }
    
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }
    
    @Override
    public boolean register(UserRegisterVo registerVo) throws Exception {
        MemberEntity memberEntity = new MemberEntity();
    
        List<MemberLevelEntity> default_status = memberLevelService.list(new QueryWrapper<MemberLevelEntity>().eq("default_status", 1));
        if(default_status != null && default_status.size() !=0) {
            memberEntity.setLevelId(default_status.get(0).getId());
        } else {
            memberEntity.setLevelId(0L);
        }
        
        checkPhoneUnique(registerVo.getPhone());
        checkUserNameUnique(registerVo.getUsername());
        memberEntity.setMobile(registerVo.getPhone());
        memberEntity.setUsername(registerVo.getUsername());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(registerVo.getPassword());
        memberEntity.setPassword(encode);
        memberEntity.setCreateTime(new Date());
        int insert = baseMapper.insert(memberEntity);
        return insert == 1;
    }
    
    @Override
    public boolean checkEmailUnique(String email) {
        return false;
    }
    
    @Override
    public boolean checkUserNameUnique(String username) {
        Integer count = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if(count > 0) {
            throw new UserNameExistException();
        }
        return count == 0;
    }
    
    @Override
    public boolean checkPhoneUnique(String phone) throws PhoneExistException {
        Integer count = baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if(count > 0) {
            throw new PhoneExistException();
        }
        return count == 0;
    }
    
    /**
     * <p>站点默认登录</p>
     * @author smile
     * @date 2021/2/23/023
     * @param registerVo 1
     * @return cn.smile.smilemall.member.entity.MemberEntity
     */
    @Override
    public MemberEntity userLogin(UserRegisterVo registerVo) throws PasswordErrorException, UserNotExistException {
        MemberEntity memberEntity = baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("username", registerVo.getUsername()).or().eq("mobile",
                registerVo.getUsername()));
        if (memberEntity == null) {
            throw new UserNotExistException();
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(!bCryptPasswordEncoder.matches(registerVo.getPassword(), memberEntity.getPassword())) {
            throw new PasswordErrorException();
        }
        return memberEntity;
    }
    
    /**
     * <p>社交登录</p>
     * @author smile
     * @date 2021/2/23/023
     * @param socialUserVo 1
     * @return cn.smile.smilemall.member.entity.MemberEntity
     */
    @Override
    public MemberEntity socialLogin(SocialUserVo socialUserVo) {
        
        MemberEntity exitMember = baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("uid", socialUserVo.getUid()));
        if(exitMember != null) {
            MemberEntity updMember = new MemberEntity();
            updMember.setAccessToken(socialUserVo.getAccess_token());
            updMember.setExpiresIn(socialUserVo.getExpires_in());
            updMember.setId(exitMember.getId());
            baseMapper.updateById(updMember);
            return exitMember;
        } else {
            MemberEntity insertMember = new MemberEntity();
            // 查询当前社交用户的社交信息
            Map<String, String> getParameter = new HashMap<>(10);
            getParameter.put("access_token", socialUserVo.getAccess_token());
            getParameter.put("uid", socialUserVo.getUid());
            try {
                HttpResponse response = HttpUtils.doGet("https://api.weibo.com", "/2/users/show.json", "GET",
                        new HashMap<>(0), getParameter);
                String userInfo = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = JSONUtil.toBean(userInfo, JSONObject.class);
                String nickName = jsonObject.get("name", String.class);
                insertMember.setNickname(nickName);
            } catch (ConvertException e) {
                log.error("社交用户信息获取失败{}", e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            insertMember.setUid(socialUserVo.getUid());
            insertMember.setAccessToken(socialUserVo.getAccess_token());
            insertMember.setExpiresIn(socialUserVo.getExpires_in());
            insertMember.setLevelId(1L);
            insertMember.setCreateTime(new Date());
            baseMapper.insert(insertMember);
            return insertMember;
        }
    }
}