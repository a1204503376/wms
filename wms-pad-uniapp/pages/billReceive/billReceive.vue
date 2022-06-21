<template>
	<view>
		<u-divider text=""></u-divider>
		<template>
			<u-search placeholder="请输入收货单编码/上游编码" v-model="params.no" :show-action="false" @custom="search"
				@search="search">
			</u-search>
		</template>
		<u-list style="height: 990rpx;">
			<u-divider text=""></u-divider>
			<u-list-item v-for="(item, index) in receiveList" :key="index">
				<view @click="clickItem(item)">
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="6">
							<u--text class="demo-layout bg-purple-light" v-text="item.receiveNo"></u--text>
						</u-col>
						<u-col span="6">
							<u--text class="demo-layout bg-purple" v-text="item.billTypeName"></u--text>
						</u-col>
					</u-row>
					<u-row customStyle="margin-bottom: 10px">
						<u-col span="12">
							<u--text class="demo-layout bg-purple" v-text="item.supplierName"></u--text>
						</u-col>
					</u-row>
					<u-divider text=""></u-divider>
				</view>
			</u-list-item>
		</u-list>
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
					no: '',
				},
				receiveList: [],
			}
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
			search() {
				receive.getReceiveList(this.params).then(data => {
					this.receiveList = data.data;
				})
			},
			clickItem(item) {
				uni.$u.func.route('/pages/billReceive/billReceivePageTwo', item);
			},
			scannerCallback(no) {
				this.params.no = no;
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
	.uniList {
		height: 990rpx;
	}
</style>
