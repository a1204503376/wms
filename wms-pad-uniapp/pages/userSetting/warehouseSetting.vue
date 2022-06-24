<template>
	<view>
		<view class="uni-list">
			<radio-group @change="radioChange" :model="whId" style="transform: scale(0.9);">
				<label class="uni-list-cell uni-list-cell-pd" v-for="(item, index) in warehouseList" :key="item.whId">
					<view class="font-in-page">{{item.whName}}</view>
					<view>
						<radio :value="item" :checked="warehouse.whId==item.whId" />
					</view>
				</label>
			</radio-group>
		</view>
		<view class="footer">
			<u-button class="btn-cancle" @click="esc()">
				返回
			</u-button>
			<u-button class="btn-submit" @click="submit()" :throttleTime="1000">
				确定
			</u-button>
		</view>
	</view>
</template>
<script>
	import api from '@/api/user.js'
	import md5 from '@/utils/md5.js'
	import warehouse from '@/api/warehouse.js'
	export default {
		data() {
			return {
				warehouseList: [],
				warehouse: uni.getStorageSync('warehouse'),
				whName: uni.getStorageSync('warehouse').whName,
				whId: uni.getStorageSync('warehouse').whId
			};
		},
		onLoad() {
			warehouse.getWarehouseList().then(data => {
				this.warehouseList = data.data;
			})
		},
		methods: {
			esc() {
				this.$u.func.navigateBack();
			},
			submit() {
				uni.setStorageSync('warehouse', this.warehouse);
				uni.redirectTo({
					url: '/pages/home/home'
				})
			},
			radioChange(row) {
				this.warehouse = row.detail.value;
			}
		},
	};
</script>
<style lang="scss">
	@import 'warehouseSetting.scss';
</style>
