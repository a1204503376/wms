<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="按箱移动" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="箱码1" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.boxCodeList[0].boxCode"></u--input>
			</u-form-item>
			<u-form-item label="箱码2" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.boxCodeList[1].boxCode"></u--input>
			</u-form-item>
			<u-form-item label="箱码3" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.boxCodeList[2].boxCode"></u--input>
			</u-form-item>
			<u-form-item label="箱码4" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.boxCodeList[3].boxCode"></u--input>
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
					boxCodeList: [{
						boxCode: ''
					}, {
						boxCode: ''
					}, {
						boxCode: ''
					}, {
						boxCode: ''
					}]
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
					if (tool.isNotEmpty(_this.params.boxCodeList[0].boxCode) || tool.isNotEmpty(_this.params
							.boxCodeList[1].boxCode) || tool.isNotEmpty(_this.params.boxCodeList[2].boxCode) ||
						tool.isNotEmpty(_this.params.boxCodeList[3].boxCode)) {
						var boxCode = '';
						var boxCodeIsequal = true;
						for (let i = 0; i < _this.params.boxCodeList.length; i++) {
							if (tool.isEmpty(boxCode) && tool.isNotEmpty(_this.params.boxCodeList[i].boxCode
									.substring(0, 1))) {
								boxCode = _this.params.boxCodeList[i].boxCode.substring(0, 1)
							}
							if (tool.isNotEmpty(boxCode) && tool.isNotEmpty(_this.params.boxCodeList[i].boxCode) &&
								tool.isNotEmpty(_this.params.boxCodeList[i].boxCode
									.substring(0, 1))) {
								if (boxCode == _this.params.boxCodeList[i].boxCode
									.substring(0, 1)) {

								} else {
									uni.$u.func.showToast({
										title: '只能移动同一种箱子',
									})
									return;
								}
							}
						}
						var params = {};
						var boxCodeList = [];
						boxCodeList.push(_this.params.boxCodeList[0].boxCode);
						boxCodeList.push(_this.params.boxCodeList[1].boxCode);
						boxCodeList.push(_this.params.boxCodeList[2].boxCode);
						boxCodeList.push(_this.params.boxCodeList[3].boxCode);
						params.boxCodeList = boxCodeList;
						uni.$u.func.routeNavigateTo(
							'/pages/stock/stockManage/moveByBoxCode/moveByBoxCodeSubmit',
							params);
						return;
					}
					uni.$u.func.showToast({
						title: '至少输入一个箱码',
					})
					return;
				}, 1000)

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
					case barcodeType.UnKnow:
						if (tool.isEmpty(this.params.boxCodeList[0].boxCode)) {
							this.params.boxCodeList[0].boxCode = barcode.content;
						} else if (tool.isEmpty(this.params.boxCodeList[1].boxCode)) {
							this.params.boxCodeList[1].boxCode = barcode.content;
						} else if (tool.isEmpty(this.params.boxCodeList[2].boxCode)) {
							this.params.boxCodeList[2].boxCode = barcode.content;
						} else if (tool.isEmpty(this.params.boxCodeList[3].boxCode)) {
							this.params.boxCodeList[3].boxCode = barcode.content;
						} else {
							this.$u.func.showToast({
								title: '箱码收集完毕，请提交数据'
							});
						}
						break;
					case barcodeType.Lpn:
						if (tool.isEmpty(this.params.boxCodeList[0].boxCode)) {
							this.params.boxCodeList[0].boxCode = barcode.content;
						} else if (tool.isEmpty(this.params.boxCodeList[1].boxCode)) {
							this.params.boxCodeList[1].boxCode = barcode.content;
						} else if (tool.isEmpty(this.params.boxCodeList[2].boxCode)) {
							this.params.boxCodeList[2].boxCode = barcode.content;
						} else if (tool.isEmpty(this.params.boxCodeList[3].boxCode)) {
							this.params.boxCodeList[3].boxCode = barcode.content;
						} else {
							this.$u.func.showToast({
								title: '箱码收集完毕，请提交数据'
							});
						}
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
