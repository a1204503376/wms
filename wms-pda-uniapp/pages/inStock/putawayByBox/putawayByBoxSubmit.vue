<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="按件收货" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="箱码" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.boxCode" border="0" disabled></u--input>
			</u-form-item>
			<u-form-item label="LOC" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.locCode"></u--input>
			</u-form-item>
			<u-form-item label="容器编码" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.lpnCode"></u--input>
			</u-form-item>
			<u-form-item label="总数" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.qty" border="0" disabled></u--input>
			</u-form-item>
		</u--form>
		<keyboard-listener @keydown="emitKeyDown"></keyboard-listener>
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
	import putawayByBoxs from '@/api/inStock/putawayByBox.js'
	import barcodeFunc from '@/common/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	export default {
		components: {
			keyboardListener
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {}
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.params = parse;
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		methods: {
			analysisCode(code) {
				var barcode = barcodeFunc.parseBarcode(code);
				var barcodeType = barcodeFunc.BarcodeType;
				switch (barcode.type) {
					case barcodeType.UnKnow:
						this.params.locCode = barcode.content;
						break;
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
			submit() {
				var _this = this;
				uni.$u.throttle(function() {
					console.log(_this.params)
					_this.submitPutawayByBox();
				}, 1000)

			},
			submitPutawayByBox() {
				this.params.qty = 1;
				this.params.whId = uni.getStorageSync('warehouse').whId
				putawayByBoxs.submitPutawayByBox(this.params).then(data => {

				})
			},
			esc() {
				uni.$u.func.navigateBackTo(1);
			},
			scannerCallback(no) {
				this.analysisCode(no);
			},
			emitKeyDown(e) {
				if (e.key == 'Enter') {
					this.analysisCode(this.params.locCode);
					//查询方法
				}
			}
		}
	}
</script>

<style>

</style>
