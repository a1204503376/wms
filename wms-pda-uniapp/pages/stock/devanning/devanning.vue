<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="拆箱" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u--form labelPosition="left" :model="params">
			<u-form-item label="箱码" borderBottom class="left-text-one-line font-in-page" labelWidth="100">
				<u--input v-model.trim="params.boxCode"></u--input>
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
	import barcodeFunc from '@/utils/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	import devanning from '@/api/stock/devanning.js'
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
			this.params.boxCode = undefined;
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
				if (tool.isEmpty(this.params.boxCode)) {
					uni.$u.func.showToast({
						title: '查询失败，请输入箱码后重试'
					});
				}

				this.clearEmitKeyDown();
				devanning.getAllSerialNumberManage(this.params).then(data => {
					var params = {};
					params = data.data;
					params.boxCode = this.params.boxCode;
					params.whId = this.params.whId;
					if (data.data.isSn) {
						uni.$u.func.routeNavigateTo('/pages/stock/devanning/devanningSerialNumber', params);
					} else {
						uni.$u.func.routeNavigateTo('/pages/stock/devanning/goodsDevanning', params);
					}
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
				uni.navigateBack({
					delta: 1
				});
			},
			scannerCallback(no) {
				this.analysisCode(no);
				//查询方法
				this.getPutawayData();
			},
			clearEmitKeyDown() {
				this.emitKeyDown = function() {};
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
