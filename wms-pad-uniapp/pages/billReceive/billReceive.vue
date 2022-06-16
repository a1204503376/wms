<template>
	<view>
		<u-divider text=""></u-divider>
		<template>
			<u-search placeholder="请输入收货单编码/上游编码" v-model="params.receiveNo" :show-action="false" @custom="search"
				@search="search">
			</u-search>
		</template>
		<u-list>
			<u-divider text=""></u-divider>
			<u-list-item v-for="(item, index) in receiveList" :key="index">
				<view @click="clickItem(item)">
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="6">
							<view class="demo-layout bg-purple-light">{{item.receiveNo}}</view>
						</u-col>
						<u-col span="6">
							<view class="demo-layout bg-purple">{{item.billTypeName}}</view>
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
				params: {
					receiveNo: '',
				},
				receiveList: [],
			}
		},
		onLoad() {
			this.search();
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		methods: {
			search() {
				receive.getReceiveList(this.params).then(data => {
					this.receiveList = data.data;
					if (data.data.length == 1) {
						uni.$u.func.route('/pages/billReceive/billReceivePageTwo?receive=' + JSON.stringify(data.data[0]));
					}
				})
			},
			clickItem(item) {
				uni.$u.func.route('/pages/billReceive/billReceivePageTwo?receive=' + JSON.stringify(item));
			},
			scannerCallback(data) {
				this.params.receiveNo = data;
				this.search();
			},
			emitKeyDown(e) {
				if (e.key == 'Enter') {
					this.search();
				}
			}
		}
	}
</script>

<style>

</style>
