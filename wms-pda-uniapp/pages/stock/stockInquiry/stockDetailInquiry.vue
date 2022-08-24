<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="库存详情" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<u--form>
			<u-form-item label="物品" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.skuCode"  disabled></u--input>
			</u-form-item>
			<u-form-item label="可用" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.stockEnable" disabled></u--input>
				<u--text slot="right" class="font-in-page" v-text="params.wsuCode"></u--text>
			</u-form-item>
			<u-form-item label="余额" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.stockBalance" disabled></u--input>
				<u--text slot="right" class="font-in-page" v-text="params.wsuCode"></u--text>
			</u-form-item>
			<u-form-item label="LOC" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.locCode" disabled></u--input>
			</u-form-item>
			<u-form-item label="箱码" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.boxCode" disabled></u--input>
			</u-form-item>
			<u-form-item label="LPN" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.lpnCode" disabled></u--input>
			</u-form-item>
			<u-form-item label="状态" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.stockStatus" disabled></u--input>
			</u-form-item>
			<u-form-item label="货主" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params.ownerName"  disabled></u--input>
			</u-form-item>
			<sku-lot-view v-model="params"></sku-lot-view>
			<u-form-item class="left-text-one-line" labelWidth="100">
				<u--input  disabled></u--input>
			</u-form-item>
		</u--form>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	import uniSelect from '@/components/uni-select.vue'
	import barcodeFunc from '@/common/barcodeFunc.js'
	import tool from '@/utils/tool.js'
	import skuLotView from '@/components/skuLotView.vue'
	export default {
		components: {
			uniSelect,
			skuLotView
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				params: {
				},
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.params= parse;
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
			esc() {
				uni.navigateBack({
					delta: 1
				});
			},
			scannerCallback(no) {
				this.analysisCode(no);
			}
		}
	}
</script>

<style>

</style>
