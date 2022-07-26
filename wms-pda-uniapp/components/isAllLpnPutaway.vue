<template>
	<view style="width: 100%;height: 100%;">
		<view class="uni-list-cell-db" style="width: 100%;height: 100%;">
			<picker style="width: 100%;height: 100%;" v-model="dataSource" :range="isAllLpnPutawayList" range-key="name"
				value="index" @change="bindPickerChange">
				<view class="uni-input-input" style="width: 100%;">
					<u--input style="margin-top: 0rpx; z-index: 99999;" v-model="dataSource"></u--input>
				</view>
			</picker>
		</view>
	</view>
</template>

<script>
	export default {
		name: "isAllLpnPutaway",
		props: {
			selectVal: [Object, String, Array, Number, Boolean]
		},
		model: {
			prop: 'selectVal',
			event: 'selectValChange'
		},
		watch: {
			selectVal: {
				handler(selectVal) {},
				immediate: true,
				deep: true
			}
		},
		data() {
			return {
				dataSource: "否",
				isAllLpnPutawayList: [{
						id: 1,
						name: "否",
						isAllLpnPutaway: false
					},
					{
						id: 2,
						name: "是",
						isAllLpnPutaway: true
					},
				]
			};
		},
		methods: {
			bindPickerChange: function(e) {
				this.index = e.detail.value
				this.dataSource = this.isAllLpnPutawayList[e.detail.value].name;
				this.$emit('selectValChange', this.isAllLpnPutawayList[e.detail.value].isAllLpnPutaway);
			},
		},
		created() {
			this.dataSource = this.isAllLpnPutawayList[0].name;
			this.$emit('selectValChange', this.isAllLpnPutawayList[0].isAllLpnPutaway);
		}
	}
</script>

<style>

</style>
