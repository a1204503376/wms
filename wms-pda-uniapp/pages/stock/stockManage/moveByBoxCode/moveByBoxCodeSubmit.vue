<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="按箱移动" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="TOLOC" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.locCode"></u--input>
			</u-form-item>
			<u-form-item label="LPN" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.lpnCode"></u--input>
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
	import stockManage from '@/api/stock/stockManage.js'
	export default {
		components: {
			uniSelect
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					locCode: '',
					lpnCode: '',
					boxCodeList: []
				}
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.params.boxCodeList = parse.boxCodeList;
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
				_this.params.isSn = true;
				uni.$u.throttle(function() {
					if (tool.isNotEmpty(_this.params.locCode)) {
						console.log('按箱移动成功')
						_this.params.whId = uni.getStorageSync('warehouse').whId;
						stockManage.stockMoveByBoxCode(_this.params).then(data => {
							uni.$u.func.showToast({
								title: '移动成功',
							})
							uni.$u.func.routeRedirectTo(
								'/pages/stock/stockManage/moveByBoxCode/moveByBoxCode',
								_this.params);
						})
						return;
					}
					console.log('标准移动失败')
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
