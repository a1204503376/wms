<template>
	<view>
	<!-- 	<v-for="(item,index) in Data"></v-for="(item,index)> -->
		<u--form>
			<u-form-item label="箱码" class="left-text-one-line" labelWidth="100">
				<u--input v-model="param.boxCode" border="0" disabled></u--input>
			</u-form-item>
			<u-form-item label="LOC" :required="true"  class="left-text-one-line" labelWidth="100">
				<u--input v-model="param.locCode"></u--input>
			</u-form-item>
				<template v-for="(item, index) in param.receiveDetailLpnItemDtoList">
				<u-form-item label="物品" class="left-text-one-line" labelWidth="100">
					<u--input v-model="item.skuCode" border="0" disabled></u--input>
				</u-form-item>
				<u-form-item label="数量" class="left-text-one-line" labelWidth="100">
					<u--input v-model="item.planQty" border="0" disabled></u--input>
				</u-form-item>	
				</template>
				
			<u-form-item label="生产批次" :required="true"  class="left-text-one-line" labelWidth="100">
				<u--input v-model="param.skuLot1"></u--input>
			</u-form-item>
			<u-form-item label="LPN" :required="true"  class="left-text-one-line" labelWidth="100">
				<u--input v-model="param.lpnCode"></u--input>
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
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	export default {
		components: {
			keyboardListener,
		
		},
		data() {
			return {
				param:{
					id:'',
					receiveDetailId:'',
					boxCode:'',
					lpnCode:'',
					skuLot1:'',
					num:'',
					receiveDetailLpnItemDtoList:[],
				},
				receiveDetailId: '',
				receiveDetailList: [],
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.param = parse
			this.param['locCode'] = 'STAGE'
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		
		methods: {
			submit() {
				this.params.locCode = uni.getStorageSync('warehouse').whCode + this.params.locCode;
				// if (this.params.isSn == 1) {
				// 	uni.$u.func.route('/pages/inStock/receiveByPcs/receiptDetailEnquiry', this.params);
				// 	return;
				// }
				console.log(this.params)
				//提交表单数据 收货
			},
		
			esc() {
				this.$u.func.navigateBack();
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
