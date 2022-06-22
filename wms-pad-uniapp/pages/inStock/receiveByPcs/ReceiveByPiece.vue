<template>
	<view>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u--form labelPosition="left" :model="params">
			<u-form-item label="物品" borderBottom class="textAlignClass" labelWidth="140rpx">
				<u--input v-model="params.skuCode"></u--input>
			</u-form-item>
			<u-form-item label="名称" borderBottom class="textAlignClass" labelWidth="140rpx">
				<u--input v-model="params.skuName"></u--input>
			</u-form-item>
			<u-form-item label="型号" borderBottom class="textAlignClass" labelWidth="140rpx">
				<uni-select v-model="params.skuLot2"></uni-select>
			</u-form-item>
			<u-form-item label="数量" borderBottom class="textAlignClass" labelWidth="140rpx">
				<u--input v-model="params.surplusQty"></u--input>
				<!-- <u-number-box v-model="params.skuCode" @change="valChange"></u-number-box> -->
			</u-form-item>
			<u-form-item label="UOM" borderBottom class="textAlignClass" labelWidth="140rpx">
				<u--input v-model="params.umName" :disabled="true"></u--input>
			</u-form-item>
			<u-form-item label="生产批次" borderBottom class="textAlignClass" labelWidth="140rpx">
				<u--input v-model="params.skuLot1"></u--input>
			</u-form-item>
			<u-form-item label="箱码" borderBottom class="textAlignClass" labelWidth="140rpx">
				<u--input v-model="params.boxCode"></u--input>
			</u-form-item>
			<u-form-item label="LOC" borderBottom class="textAlignClass" labelWidth="140rpx">
				<u--input v-model="params.locCode"></u--input>
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
					skuCode:undefined,
					skuName:undefined,
					skuLot2:undefined,
					surplusQty:undefined,
					umName:undefined,
					skuLot1:undefined,
					boxCode:undefined,
					locCode:undefined
				},
				receiveDetailId: '',
				receiveDetailList: [],
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.receiveDetailId = parse.receiveDetailId;
			this.getDetailByDetailId();
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		methods: {
			submit() {
				if(this.params.isSn==1){
					uni.$u.func.route('/pages/inStock/receiveByPcs/receiptDetailEnquiry', this.params);
				    return;
				}
				//提交表单数据 收货
			},
			getDetailByDetailId() {
				let params = {
					receiveDetailId: this.receiveDetailId
				};
				receive.getDetailByDetailId(params).then(data => {
					this.params = data.data;
					console.log(this.params)
				})
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
	
</style>
