<template>
	<view>
		<view v-for="item of numberOfOpen" v-if="onLineSkuLot">
			<u-form-item :label="skuParams['skuLot'+item]" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params['skuLot'+item]" disabled></u--input>
			</u-form-item>
		</view>
		<view v-for="item of numberOfOpen" v-if="defaultSkuLot">
			<u-form-item :label="'批属性'+item" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="params['skuLot'+item]" disabled></u--input>
			</u-form-item>
		</view>

	</view>
</template>

<script>
	import skuLot from '@/api/skuLot.js';
	import tool from '@/utils/tool.js'
	export default {
		name: "skuLotView",
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
					this.params = this.selectVal;
					this.getDataSource(this.selectVal)
				},
				immediate: true,
				deep: true
			}
		},
		data() {
			return {
				params: {},
				defaultSkuLot: false,
				onLineSkuLot: false,
				skuParams: {},
				numberOfOpen: 0,
			};
		},
		created() {},
		methods: {
			getDataSource(soure) {
				let params = {
					woId: soure.woId
				}
				skuLot.findAllSkuLotByWoId(params).then(data => {
					if (tool.isEmpty(data.data)) {
						this.onLineSkuLot = false;
						this.defaultSkuLot = true;
					} else {
						this.skuParams = data.data;
						this.defaultSkuLot = false;
						this.onLineSkuLot = true;
					}
					this.numberOfOpen = data.data.numberOfOpen;
				})
			}
		}
	}
</script>

<style>

</style>
