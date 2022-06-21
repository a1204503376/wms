<template>
	<view>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u--form labelPosition="left" :model="params">
			<u-form-item label="物品" borderBottom>
				<u--input v-model="params.skuCode"></u--input>
			</u-form-item>
			<u-form-item label="名称" borderBottom>
				<u--input v-model="params.skuCode"></u--input>
			</u-form-item>
			<u-form-item label="型号" borderBottom>
				<u-picker v-model="params.skuCode" :show="show" :columns="columns" keyName="label"></u-picker>
			</u-form-item>
			<u-form-item label="数量" borderBottom>
				<u--input v-model="params.skuCode"></u--input>
				<!-- <u-number-box v-model="params.skuCode" @change="valChange"></u-number-box> -->
			</u-form-item>
			<u-form-item label="UOM" borderBottom>
				<u--input v-model="params.skuCode" :disabled="true"></u--input>
			</u-form-item>
			<u-form-item label="生产批次" borderBottom>
				<u--input v-model="params.skuCode"></u--input>
			</u-form-item>
			<u-form-item label="LPN" borderBottom>
				<u--input v-model="params.skuCode"></u--input>
			</u-form-item>
			<u-form-item label="LOC" borderBottom>
				<u--input v-model="params.skuCode"></u--input>
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
	import receive from '@/api/receive.js'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	export default {
		components: {
			keyboardListener
		},
		data() {
			return {
				params: {
					receiveId: '',
					skuCode: '',
				},
				receiveDetailList: [],
				show: false,
				columns: [
					[{
						label: '雪月夜',
						// 其他属性值
						id: 2021
						// ...
					}, {
						label: '冷夜雨',
						id: 804
					}]
				],
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

</style>
