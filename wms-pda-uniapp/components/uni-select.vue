<template>
	<view style="width: 100%;height: 100%;">
		<view class="uni-list-cell-db" style="width: 100%;height: 100%;">
			<picker style="width: 100%;height: 100%;" v-model="dataSource" :range="columns" value="index"
				@change="bindPickerChange">
				<view class="uni-input-input" style="width: 100%;">
					<u--input style="margin-top: 0rpx; z-index: 99999;" v-model="dataSource"></u--input>
				</view>
			</picker>
		</view>
	</view>
</template>

<script>
	import sku from '@/api/sku.js';
	export default {
		name: "uni-select",
		props: {
			selectVal: [Object, String, Array, Number]
		},
		model: {
			prop: 'selectVal',
			event: 'selectValChange'
		},
		watch: {
			selectVal: {
				handler(selectVal) {
					this.dataSource = this.selectVal;
				},
				immediate: true,
				deep: true
			}
		},
		data() {
			return {
				dataSource: '',
				show: false,
				columns: [],
			};
		},
		created() {
			this.getDataSource();
		},
		methods: {
			getDataSource() {
				sku.getSkuDropDownBox().then(data => {
					this.columns = data.data
				})
			},
			bindPickerChange: function(e) {
				this.index = e.detail.value
				this.dataSource = this.columns[e.detail.value]
				this.$emit('selectValChange', this.columns[e.detail.value]);
			},
		}
	}
</script>

<style>

</style>
