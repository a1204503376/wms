<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="按物品、批次、库位部分解冻" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="物品" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.skuCode"></u--input>
			</u-form-item>
			<u-form-item label="批次" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.lotNumber"></u--input>
			</u-form-item>
			<u-form-item label="LOC" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.locCode"></u--input>
			</u-form-item>
			<u-form-item label="数量" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model="params.qty"></u--input>
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
					locCode: '',
					skuCode: '',
					lotNumber: ''
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
						console.log('库位部分解冻成功')
						return;
					}
					console.log('库位部分解冻失败')
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
