package org.nodes.wms.dao.tianyi.skubox;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.tianyi.skubox.entities.SkuBox;
import org.nodes.wms.dao.tianyi.skubox.mapper.SkuBoxMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author caiyun
 */
@Repository
@RequiredArgsConstructor
public class SkuBoxDao extends BaseServiceImpl<SkuBoxMapper, SkuBox> {

	private final NumberFormat numberFormat = NumberFormat.getNumberInstance();
	Map<String, SkuBox> skuBoxMap;

	/**
	 * 获取物品所属箱的标识
	 *
	 * @param skuName 物品名称
	 * @param spec 型号
	 * @return 两位的数字
	 */
	public String getBoxId(String skuName, String spec){
		AssertUtil.notEmpty(skuName, "生成箱码失败,物品名称参数为空");
		AssertUtil.notEmpty(spec, "生成箱码失败,物品型号参数为空");

		SkuBox skuBox = getFromLocal(skuName, spec);
		if (Func.notNull(skuBox)){
			return createBoxId(skuBox.getId());
		}

		skuBox = getFromDb(skuName, spec);
		if (Func.notNull(skuBox)){
			skuBoxMap.put(skuBox.getSkuName() + skuBox.getSkuSpec(), skuBox);
			return createBoxId(skuBox.getId());
		}

		skuBox = newAndSaveSkuBox(skuName, spec);
		skuBoxMap.put(skuBox.getSkuName() + skuBox.getSkuSpec(), skuBox);
		return createBoxId(skuBox.getId());
	}

	private SkuBox newAndSaveSkuBox(String skuName, String spec) {
		SkuBox skuBox = new SkuBox();
		skuBox.setSkuName(skuName);
		skuBox.setSkuSpec(spec);
		super.save(skuBox);

		return skuBox;
	}

	private SkuBox getFromDb(String skuName, String spec) {
		LambdaQueryWrapper<SkuBox> skuBoxLambdaQueryWrapper = Wrappers.lambdaQuery(SkuBox.class);
		skuBoxLambdaQueryWrapper.eq(SkuBox::getSkuName, skuName)
			.eq(SkuBox::getSkuSpec, spec);
		return super.getOne(skuBoxLambdaQueryWrapper);
	}

	private SkuBox getFromLocal(String skuName, String spec) {
		if (Func.isNull(skuBoxMap)){
			skuBoxMap = new HashMap<>();
			return null;
		}

		return skuBoxMap.get(skuName + spec);
	}

	private String createBoxId(Integer boxId){
		numberFormat.setMinimumIntegerDigits(2);
		numberFormat.setGroupingUsed(false);
		return numberFormat.format(boxId);
	}
}
