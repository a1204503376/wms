<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="出库复核" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="箱码" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.boxCode"></u--input>
			</u-form-item>
		</u--form>
		<h4 align="center" style='background-color:#33cbcc;height: 70rpx;' class="font-in-page">
			已核列表({{boxs}}/{{boxCodeList.length}}箱)</h4>
		<!-- ${index + 1} -->
		<u-list style="height: 650rpx;">
			<u-list-item v-for="(item, index) in boxCodeList" :key="index" :style="item.backgroundColor">
				<u-swipe-action>
					<u-swipe-action-item>
						<view class="swipe-action u-border-top u-border-bottom" :style="item.backgroundColor">
							<view class="swipe-action__content">
								<text class="swipe-action__content__text font-in-page">{{item.boxCode}}</text>
							</view>
						</view>
					</u-swipe-action-item>
				</u-swipe-action>
			</u-list-item>
		</u-list>
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
	import barcodeFunc from '@/utils/barcodeFunc.js'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	import pick from '@/api/picking/picking.js'
	import tool from '@/utils/tool.js'
	export default {
		components: {
			keyboardListener
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
					boxCode: '',
					soBillId: undefined
				},
				boxCodeList: [],
				boxs: 0
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.params.soBillId = parse.soBillId;
			pick.findBoxCountBySoHeaderId(this.params).then(res => {
				this.boxs = res.data
			})
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
				}
			};
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
			remove(index) {
				this.boxCodeList.splice(index, 1)
			},
			esc() {
				this.clearEmitKeyDown();
				uni.navigateBack({
					delta: 1
				});
			},
			submit() {
				this.esc();
			},
			analysisCode(code) {
				var barcode = barcodeFunc.parseBarcode(code);
				var barcodeType = barcodeFunc.BarcodeType;
				// switch (barcode.type) {
				// 	case barcodeType.UnKnow:
				// 		this.listRepeated(barcode);
				// 		break;
				// 	default:
				// 		this.$u.func.showToast({
				// 			title: '条码识别失败,不支持的条码类型'
				// 		});
				// 		break;
				// }
				let isExist = this.boxCodeList.findIndex(item => item.boxCode === barcode.content);
				if (isExist >= 0) {
					this.$u.func.showToast({
						title: '箱码已复核'
					});
					return;
				}
				this.params.boxCode = barcode.content;
				pick.outStockCheckout(this.params).then(res => {
					this.$u.func.showToast({
						title: '复核完成'
					});
					let boxCode = res.data
					this.boxCodeList.push({
						boxCode: boxCode,
						backgroundColor: "background-color: #fff;"
					});
					if (this.boxCodeList.length == this.boxs) {
                      pick.closes(this.params).then(res => {
						  console.log(res)
					  })
					}
					console.log(this.boxCodeList)
				})
			},
			clearEmitKeyDown() {
				this.emitKeyDown = function() {};
			},
			emitKeyDown(e) {
				if (e.key == 'Enter') {
					this.analysisCode(this.params.boxCode);
				}
			},
			scannerCallback(no) {
				this.analysisCode(no);
			}
		}
	}
</script>

<style lang="scss">
</style>
