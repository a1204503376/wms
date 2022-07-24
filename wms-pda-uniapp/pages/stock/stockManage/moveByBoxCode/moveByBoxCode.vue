<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="按箱移动" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="箱码1" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.boxCodeList[0].boxCode"></u--input>
			</u-form-item>
			<u-form-item label="箱码2" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.boxCodeList[1].boxCode"></u--input>
			</u-form-item>
			<u-form-item label="箱码3" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.boxCodeList[2].boxCode"></u--input>
			</u-form-item>
			<u-form-item label="箱码4" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.boxCodeList[3].boxCode"></u--input>
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
	import barcodeFunc from '@/common/barcodeFunc.js'
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
						console.log('按箱移动')
						uni.$u.func.routeNavigateTo('/pages/stock/stockManage/moveByBoxCode/moveByBoxCodeSubmit',
							_this.params);
						return;
					}
					console.log('至少输入一个箱码')
					return;
				}, 1000)

			},
			esc() {
				uni.$u.func.navigateBackTo(1);
			}
		}
	}
</script>

<style>

</style>
