<template>
	<view @keyup.esc="esc">
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="多箱收货" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<!-- 	<v-for="(item,index) in Data"></v-for="(item,index)> -->
		<u--form>
			<u-form-item label="LOC" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="param.locCode" @focus="focus(1)" @blur="blur(1)" @confirm="change"></u--input>
			</u-form-item>
			<u-form-item label="LPN" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="param.lpnCode" @focus="focus(2)" @blur="blur(2)" @confirm="change"
					:focus="this.focusNum == 2"></u--input>
			</u-form-item>
			<u-form-item label="专用客户" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="param.skuLot4" @focus="focus(4)" @confirm="change" @blur="blur(4)"
					:focus="this.focusNum == 4"></u--input>
			</u-form-item>
			<u-form-item label="型号" class="left-text-one-line" labelWidth="100">
				<uni-select v-model="param.skuLot2"></uni-select>
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
	import receive from '@/api/inStock/receiveByMultiBox.js'
	import barCodeService from '@/utils/barcodeFunc.js'
	import uniSelect from '@/components/uni-select.vue'
	import setting from '@/common/setting'
	import tool from '@/utils/tool.js'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	export default {
		components: {
			uniSelect,
			keyboardListener
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				focusNum: 0,
				param: {
					receiveDetailLpnPdaRequestList: [],
					locCode: '',
					lpnCode: '',
					skuLot1: '',
					skuLot2: '',
					skuLot4: '',
				},
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.param.receiveDetailLpnPdaRequestList = parse
			for (let item of this.param.receiveDetailLpnPdaRequestList) {
				if (tool.isNotEmpty(item.lpnCode)) {
					this.param.lpnCode = item.lpnCode
				}
				if (tool.isNotEmpty(item.skuLot2)) {
					this.param.skuLot2 = item.skuLot2
				}
				if (tool.isNotEmpty(item.skuLot4)) {
					this.param.skuLot4 = item.skuLot4
				}
			}
			this.param['locCode'] = 'STAGE'
			this.param['whId'] = uni.getStorageSync('warehouse').whId
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
					_this.param.locCode = _this.$u.func.parseLocCode(_this.param.locCode)
					receive.receiveByMultiBox(_this.param).then(res => {
						_this.$u.func.showToast({
							title: '操作成功'
						})
						uni.navigateBack({
							delta: 2
						});
					})
				}, 1000)
			},
			focus(num) {
				this.focusNum = num
			},
			blur(num) {
				if (num == this.focusNum) {
					this.focusNum = 0;
				}
			},
			change() {
				if (this.focusNum != 4) {
					this.focusNum = this.focusNum + 1;
				} else {
					this.submit()
				}
			},
			esc() {
				uni.navigateBack({
					delta: 1
				});
			},
			scannerCallback(no) {
				let item = barCodeService.parseBarcode(no)
				if (item.type == barCodeService.BarcodeType.Loc) {
					this.param.locCode = item.content;
				} else if (item.type == barCodeService.BarcodeType.Lpn) {
					this.param.lpnCode = item.content;
				} else if (this.focusNum == 2) {
					this.param.skuLot1 = item.content;
				} else {
					this.$u.func.showToast({
						title: '无法识别,不支持的条码类型'
					})
				}

			},

		}
	}
</script>

<style>

</style>
