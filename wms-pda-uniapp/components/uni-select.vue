<template>
	<view style="width: 100%;height: 100%;">
		<view class="uni-list-cell-db" style="width: 100%;height: 100%;">
			<picker style="width: 100%;height: 100%;" v-model="dataSource" v-if="isSkuDropDownBox" :range="columns"
				value="index" @change="bindPickerChange">
				<view class="uni-input-input" style="width: 100%;">
					<u--input style="margin-top: 0rpx; z-index: 99999;" v-model="dataSource"></u--input>
				</view>
			</picker>
			<picker style="width: 100%;height: 100%;" v-model="dataSource" v-if="isSkuByCode" :range="columns"
				value="index" @change="bindPickerChange" :disabled="isSkuByCode">
				<view class="uni-input-input" style="width: 100%;">
					<u--input style="margin-top: 0rpx; z-index: 99999;" v-model="dataSource" :disabled="isSkuByCode">
					</u--input>
				</view>
			</picker>
		</view>
	</view>
</template>

<script>
	import sku from '@/api/sku.js';
	import tool from '@/utils/tool.js'
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
					this.isTwoInto++;
					if (this.isTwoInto<=2) {
						this.dataSource = this.selectVal;
						if (tool.isNotEmpty(this.selectVal)) {
							this.params.no = this.selectVal;
							this.getDataSoueceDefault();
						} else {
							this.getDataSource();
						}
					}

				},
				immediate: true,
				deep: true
			}
		},
		data() {
			return {
				isTwoInto: 0,
				params: {
					no: ''
				},
				dataSource: '',
				show: false,
				columns: [],
				isSkuDropDownBox: false,
				isSkuByCode: false
			};
		},
		methods: {
			getDataSource() {
				sku.getSkuDropDownBox().then(data => {
					this.isSkuDropDownBox = true;
					this.isSkuByCode = false;
					this.columns = data.data;
					this.dataSource = data.data[0];
				})
			},
			getDataSoueceDefault() {
				sku.getSkuByCode(this.params).then(data => {
					this.isSkuDropDownBox = false;
					this.isSkuByCode = true;
					this.columns = data.data;
					this.dataSource = data.data[0]
					if (tool.isEmpty(data.data[0])) {
						this.getDataSource();
					}
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
