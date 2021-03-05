package cn.smile.smilemall.member.service.impl;

import cn.smile.common.utils.PageUtils;
import cn.smile.common.utils.Query;
import cn.smile.smilemall.member.dao.MemberReceiveAddressDao;
import cn.smile.smilemall.member.entity.MemberReceiveAddressEntity;
import cn.smile.smilemall.member.service.MemberReceiveAddressService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("memberReceiveAddressService")
public class MemberReceiveAddressServiceImpl extends ServiceImpl<MemberReceiveAddressDao, MemberReceiveAddressEntity> implements MemberReceiveAddressService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberReceiveAddressEntity> page = this.page(
                new Query<MemberReceiveAddressEntity>().getPage(params),
                new QueryWrapper<MemberReceiveAddressEntity>()
        );

        return new PageUtils(page);
    }
    

    /**
     * <p>获取用户地址</p>
     * @author smile
     * @date 2021/2/28/028
     * @param userId 1
     * @return java.util.List<cn.smile.smilemall.member.entity.MemberReceiveAddressEntity>
     */
    @Override
    public List<MemberReceiveAddressEntity> getAddress(Long userId) {
        return list(new QueryWrapper<MemberReceiveAddressEntity>().eq("member_id", userId));
    }
}