<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="LPN移动" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="LPN" :required="true" class="left-text-one-line" labelWidth="100">
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
					lpnCode: ''
				}
			}
		},
		onLoad() {},
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
					if (tool.isNotEmpty(_this.params.lpnCode)) {
						_this.params.whId = uni.getStorageSync('warehouse').whId;
						stockManage.estimateStockMoveByLpn(_this.params).then(data => {
							uni.$u.func.routeNavigateTo('/pages/stock/stockManage/moveByLpnCode/moveByLpnCodeSbumit', _this.params);
						})
						return;
					}
					_this.$u.func.showToast({
						title: '请输入LPN'
					});
				}, 1000)

			},
			esc() {
				uni.navigateBack({
					delta: 1
				});
			}
		}
	}
</script>

<style>

</style>
