package cn.smile.smilemall.thirdparty.controller;

import cn.smile.common.utils.R;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Smile
 * @Documents
 * @date 2021-01-2021/1/13/013
 */
@RestController
@RequestMapping("/thirdParty")
@Slf4j
@RefreshScope
public class OssController {

	@Autowired(required = false)
	private OSS ossClient;
	
	@Value("${spring.cloud.alicloud.oss.endpoint}")
	private String endpoint;
	@Value("${spring.cloud.alicloud.oss.bucket}")
	private String bucket;
	@Value("${spring.cloud.alicloud.access-key}")
	private String accessKey;
	@GetMapping("/oss/policy")
	public R policy() {
		String host = "https://" + bucket + "." + endpoint ;
		String dir = LocalDate.now().toString();
		try {
			long expireTime = 30;
			long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
			Date expiration = new Date(expireEndTime);
			// PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
			PolicyConditions policyConds = new PolicyConditions();
			policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
			policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
			
			String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
			byte[] binaryData = postPolicy.getBytes("utf-8");
			String encodedPolicy = BinaryUtil.toBase64String(binaryData);
			String postSignature = ossClient.calculatePostSignature(postPolicy);
			
			Map<String, String> respMap = new LinkedHashMap<>();
			respMap.put("policy", encodedPolicy);
			respMap.put("signature", postSignature);
			respMap.put("host", host);
			respMap.put("accessKey", accessKey);
			respMap.put("dir", dir);
			respMap.put("expire", String.valueOf(expireEndTime / 1000));
			return R.ok().put("data", respMap);
		} catch (Exception e) {
		
		} finally {
			ossClient.shutdown();
		}
		return null;
	}
	

}
