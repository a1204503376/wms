<template>
	<view>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u-divider text=""></u-divider>
		<u--form labelPosition="left" :model="params">
			<u-form-item label="物品"  borderBottom >
				<u--input v-model="params.skuCode" border="none"></u--input>
			</u-form-item>
		</u--form>
		<u-divider text="未收货列表"></u-divider>
		<u-divider text=""></u-divider>
		<!-- ${index + 1} -->
		<u-list>
			<u-list-item v-for="(item, index) in receiveDetailList" :key="index">
				<view @click="clickItem(item)">
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="6">
							<view class="demo-layout bg-purple-light">{{index+1}}-{{item.skuCode}}</view>
						</u-col>
						<u-col span="6">
							<view class="demo-layout bg-purple">{{item.planQty}}</view>
						</u-col>
					</u-row>
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="12">
							<view class="demo-layout bg-purple">{{item.supplierName}}</view>
						</u-col>
					</u-row>
					<u-divider text=""></u-divider>
				</view>
			</u-list-item>
		</u-list>
		<keyboard-listener @keydown="emitKeyDown"></keyboard-listener>
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
				params:{
					receiveId:'',
					skuCode:'',
				},
				receiveDetailList:[]
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.params.receiveId=parse.receiveId
			this.getReceiveDetailList();
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		methods: {
            getReceiveDetailList(){
				receive.getReceiveDetailList(this.params).then(data => {
						this.receiveDetailList=data.data;
						console.log(data.data)
				})
			},
			scannerCallback(no) {
				this.params.skuCode = no;
				this.getReceiveDetailList();
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
