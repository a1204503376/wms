<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="标准移动" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="物品" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.skuCode"></u--input>
			</u-form-item>
			<u-form-item label="批次" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.lotNumber"></u--input>
			</u-form-item>
			<u-form-item label="LOC" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.locCode"></u--input>
			</u-form-item>
			<u-form-item label="数量" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.qty"></u--input>
			</u-form-item>
		</u--form>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
			<view class="btn-submit" @click="submit()">
				确定
			</view>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	import receive from '@/api/inStock/receiveByPcs.js'
	import uniSelect from '@/components/uni-select.vue'
	import barcodeFunc from '@/utils/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	export default {
		components: {
			uniSelect
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					locCode: '',
					skuCode: '',
					lotNumber: '',
					qty: undefined
				}
			}
		},
		onLoad() {

		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		onBackPress(event) {
			// #ifdef APP-PLUS
			if (event.from === 'backbutton') {
				this.esc();
				return true;
			}
			// #endif
		},
		methods: {
			submit() {
				var _this = this;
				uni.$u.throttle(function() {
					if (_this.params.qty > 0 && tool.isNotEmpty(_this.params.locCode) && tool.isNotEmpty(_this
							.params.lotNumber) && tool.isNotEmpty(_this.params.skuCode)) {
						uni.$u.func.routeNavigateTo('/pages/stock/stockManage/standardMove/standardMoveSubmit',
							_this.params);
						return;
					}
					_this.$u.func.showToast({
						title: '标准移动失败，请输入必填字段'
					});
					return;
				}, 10000)
			},
			esc() {
				uni.navigateBack({
					delta: 1
				});
			},
			scannerCallback(no) {
				this.analysisCode(no);
			},
			analysisCode(code) {
				var barcode = barcodeFunc.parseBarcode(code);
				var barcodeType = barcodeFunc.BarcodeType;
				switch (barcode.type) {
					case barcodeType.Loc:
						this.params.locCode = barcode.content;
						break;
					case barcodeType.Sku:
						this.params.skuCode = barcode.content;
						break;
					case barcodeType.LotNumber:
						this.params.lotNumber = barcode.content;
						break;
					default:
						this.$u.func.showToast({
							title: '条码识别失败,不支持的条码类型'
						});
						break;
				}
			},
		}
	}
</script>

<style>

</style>
