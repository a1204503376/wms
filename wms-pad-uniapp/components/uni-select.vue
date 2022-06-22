<template>
	<view style="width: 100%;height: 100%;">
		<view @click="show = true" style="width: 100%;height: 100rpx;">
			<u--input v-model="dataSource" :disabled="true" placeholder="请选择规格型号"></u--input>
			<u-picker v-model="dataSource" :show="show" :columns="columns" keyName="label" 
				@cancel="close" @confirm="confirm" :closeOnClickOverlay="true" @close="close"></u-picker>
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
					this.getDataSource();
				},
				immediate: true,
				deep: true
			}
		},
		data() {
			return {
				dataSource: '',
				show: false,
				columns: [
					[]
				],
			};
		},
		methods: {
			getDataSource() {
				sku.getSkuDropDownBox().then(data => {
					data.data.forEach((item, index) => {
						if (this.columns[0].length != data.data.length) {
							this.columns[0].push(item)
						}
					});
				})
			},
			confirm(row) {
				this.dataSource = row.value[0].label;
				this.show = false;
				this.$emit('selectValChange', row.value[0].label);
			},
			close() {
				this.show = false;
			},
		}
	}
</script>

<style>

</style>
