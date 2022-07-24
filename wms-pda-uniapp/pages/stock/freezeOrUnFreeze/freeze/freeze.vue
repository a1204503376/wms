<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="冻结" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<view>
			<u-cell-group>
				<u-cell title="1.按库位冻结" isLink url="/pages/stock/freezeOrUnFreeze/freeze/freezeByLocCode"></u-cell>
				<u-cell title="2.按批次号冻结" isLink url="/pages/stock/freezeOrUnFreeze/freeze/freezeByLotNumber"></u-cell>
				<u-cell title="3.按序列号冻结" isLink url="/pages/stock/freezeOrUnFreeze/freeze/freezeBySerialNumber">
				</u-cell>
				<u-cell title="4.按物品、批次、库位部分冻结" isLink url="/pages/stock/freezeOrUnFreeze/freeze/portionFreeze">
				</u-cell>
			</u-cell-group>
		</view>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
		</view>
	</view>
</template>

<script>
	import setting from '@/common/setting'
	export default {
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor
			}
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
				uni.$u.func.navigateBackTo(1);
			}
		}
	}
</script>

<style>

</style>
