<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="按箱上架" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u--form labelPosition="left" :model="params">
			<u-form-item label="箱码" borderBottom class="left-text-one-line font-in-page" labelWidth="100">
				<u--input v-model="params.boxCode"></u--input>
			</u-form-item>
		</u--form>
		<keyboard-listener @keydown="emitKeyDown"></keyboard-listener>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	import barcodeFunc from '@/common/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	import putawayByBox from '@/api/inStock/putawayByBox.js'
	export default {
		components: {
			keyboardListener
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					boxCode: '',
					whId: uni.getStorageSync('warehouse').whId
				},
				putawayData: {}
			}
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
			var that = this;
			that.emitKeyDown = function(e) {
				if (e.key == 'Enter') {
					that.analysisCode(that.params.boxCode);
					//查询方法
					that.getPutawayData();
				}
			};
		},
		methods: {
			getPutawayData() {
				putawayByBox.getPutawayData(this.params).then(data => {
					if (tool.isEmpty(data.data)) {
						uni.$u.func.showToast({
							title: '查找不到该箱码的相关信息,请换个箱码后重试'
						});
					}
					this.putawayData = data.data;
					this.clearEmitKeyDown();
					uni.$u.func.routeNavigateTo('/pages/inStock/putawayByBox/putawayByBoxSubmit', this
						.putawayData);
				})
			},
			analysisCode(code) {
				var barcode = barcodeFunc.parseBarcode(code);
				var barcodeType = barcodeFunc.BarcodeType;
				switch (barcode.type) {
					case barcodeType.UnKnow:
						this.params.boxCode = barcode.content;
						break;
					case barcodeType.Lpn:
						this.params.boxCode = barcode.content;
						break;
					default:
						this.$u.func.showToast({
							title: '条码识别失败,不支持的条码类型'
						});
						break;
				}
			},
			esc() {
				this.clearEmitKeyDown();
				uni.$u.func.navigateBackTo(1);
			},
			scannerCallback(no) {
				this.analysisCode(no);
				//查询方法
				this.getPutawayData();
			},
			clearEmitKeyDown() {
				this.emitKeyDown = null;
			},
			emitKeyDown(e) {
				if (e.key == 'Enter') {
					this.analysisCode(this.params.boxCode);
					//查询方法
					this.getPutawayData();
				}
			}
		}
	}
</script>

<style>

</style>
