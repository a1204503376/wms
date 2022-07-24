<template>
	<view>
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="解冻" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<view>
			<u-cell-group>
				<u-cell title="1.按库位解冻" isLink url="/pages/stock/freezeOrUnFreeze/unFreeze/unFreezeByLocCode"></u-cell>
				<u-cell title="2.按批次号解冻" isLink url="/pages/stock/freezeOrUnFreeze/unFreeze/unFreezeByLotNumber">
				</u-cell>
				<u-cell title="3.按序列号解冻" isLink url="/pages/stock/freezeOrUnFreeze/unFreeze/unFreezeBySerialNumber">
				</u-cell>
				<u-cell title="4.按物品、批次、库位部分解冻" isLink url="/pages/stock/freezeOrUnFreeze/unFreeze/portionUnFreeze">
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
