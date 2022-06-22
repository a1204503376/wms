<template>
	<view>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u--form labelPosition="left" :model="params">
			<u-form-item label="物品" borderBottom class="textClass" labelWidth="140rpx">
				<u--input v-model="params.skuCode"></u--input>
			</u-form-item>
			<u-form-item label="名称" borderBottom class="textClass" labelWidth="140rpx">
				<u--input v-model="params.skuCode"></u--input>
			</u-form-item>
			<u-form-item label="型号" borderBottom class="textClass" labelWidth="140rpx">
				<!-- 		<view @click="show = true" style="width: 100%;">
				<u--input v-model="params.skuCode" :disabled="true"></u--input>
			    <u-picker v-model="params.skuCode"  :show="show" :columns="columns" keyName="label" @close="close" @cancel="close" @confirm="confirm"></u-picker>
				</view> -->
				<uni-select v-model="params.skuCode"></uni-select>
			</u-form-item>
			<u-form-item label="数量" borderBottom class="textClass" labelWidth="140rpx">
				<u--input v-model="params.skuCode"></u--input>
				<!-- <u-number-box v-model="params.skuCode" @change="valChange"></u-number-box> -->
			</u-form-item>
			<u-form-item label="UOM" borderBottom class="textClass" labelWidth="140rpx">
				<u--input v-model="params.skuCode" :disabled="true"></u--input>
			</u-form-item>
			<u-form-item label="生产批次" borderBottom class="textClass" labelWidth="140rpx">
				<u--input v-model="params.skuCode"></u--input>
			</u-form-item>
			<u-form-item label="LPN" borderBottom class="textClass" labelWidth="140rpx">
				<u--input v-model="params.skuCode"></u--input>
			</u-form-item>
			<u-form-item label="LOC" borderBottom class="textClass" labelWidth="140rpx">
				<u--input v-model="params.skuCode"></u--input>
			</u-form-item>
		</u--form>
			<button @click="aa()"> 1234567</button>
		<keyboard-listener @keydown="emitKeyDown"></keyboard-listener>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
		</view>
	</view>
</template>

<script>
	import receive from '@/api/receive.js'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	import uniSelect from '@/components/uni-select.vue'
	export default {
		components: {
			keyboardListener,
			uniSelect
		},
		data() {
			return {
				params: {
					skuSpec:'CR110',
					receiveId: '',
					skuCode: 'CR110',
				},
				receiveDetailList: [],
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			console.log(parse)
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		methods: {
			aa(){
				console.log(this.params.skuCode)
			},
			confirm(row) {
				console.log(row.value[0].label)
				this.params.skuCode = row.value[0].label;
				this.show = false;
			},
			close() {
				this.show = false;
			},
			esc() {
				this.$u.func.navigateBack();
			},
			scannerCallback(no) {

			},
			emitKeyDown(e) {
				if (e.key == 'Enter') {
					this.getReceiveDetailList();
				}
			}
		}
	}
</script>

<style>
	@import 'receiptHeaderEnquiry.scss';
</style>
