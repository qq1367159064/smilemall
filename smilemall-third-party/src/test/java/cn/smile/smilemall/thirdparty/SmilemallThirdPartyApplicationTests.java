package cn.smile.smilemall.thirdparty;

import cn.smile.smilemall.thirdparty.services.AliNoteSendService;
import cn.smile.smilemall.thirdparty.vo.AliNoteVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmilemallThirdPartyApplicationTests {
	
	@Autowired
	private AliNoteSendService aliNoteSendService;
	
	@Test
	void contextLoads() {
		AliNoteVo aliNoteVo = aliNoteSendService.sendNote("13138710292");
		System.out.println(aliNoteVo.toString());
	}
	
}
