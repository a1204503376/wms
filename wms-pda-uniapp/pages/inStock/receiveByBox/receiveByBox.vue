<template>
	<view @keyup.esc="esc">
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="按箱收货" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<!-- 	<v-for="(item,index) in Data"></v-for="(item,index)> -->
		<u--form>
			<u-form-item label="箱码" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="param.boxCode" border="0" disabled></u--input>
			</u-form-item>
			<u-form-item label="LOC" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="param.locCode" @focus="focus(1)" @blur="blur(1)" @confirm="change"></u--input>
			</u-form-item>
			<u-form-item label="生产批次" :required="true" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="param.skuLot1" @focus="focus(2)" @confirm="change" @blur="blur(2)"
					:focus="this.focusNum == 2"></u--input>
			</u-form-item>
			<u-form-item label="专用客户" :required="false" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="param.skuLot4" @focus="focus(3)" @blur="blur(3)" @confirm="change"
					:focus="this.focusNum == 3"></u--input>
			</u-form-item>
			
			<u-form-item label="LPN" :required="false" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="param.lpnCode" @focus="focus(4)" @blur="blur(4)" @confirm="change"
					:focus="this.focusNum == 4"></u--input>
			</u-form-item>

			<u-form-item label="型号" class="left-text-one-line" labelWidth="100">
				<uni-select v-model="param.skuLot2"></uni-select>
			</u-form-item>
			<h4 align="center" style='background-color:#33cbcc;height: 70rpx;' class="font-in-page">物品列表</h4>
			<u-list>
				<u-list-item v-for="(item, index) in skuList" :key="index">
					<u-form-item label="物品" class="left-text-one-line" labelWidth="100">
						<u--input v-model.trim="item.skuCode" border="0" disabled></u--input>
					</u-form-item>
					<u-form-item label="数量" class="left-text-one-line" labelWidth="100">
						<u--input v-model.trim="item.qty" border="0" disabled></u--input>
					</u-form-item>
				</u-list-item>
			</u-list>
		</u--form>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
			<view class="btn-submit" @click="submit()">
				确定
			</view>
		</view>
	</view>
</template>

<script>
	import receive from '@/api/inStock/receiveByBox.js'
	import barCodeService from '@/common/barcodeFunc.js'
	import uniSelect from '@/components/uni-select.vue'
	import setting from '@/common/setting'
	export default {
		components: {
			uniSelect,
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				focusNum: 0,
				skuList: [],
				param: {
					id: '',
					receiveDetailId: '',
					boxCode: '',
					locCode: '',
					lpnCode: '',
					skuLot1: '',
					skuLot2: '',
					skuLot4:'',
					num: '',
					receiveDetailLpnItemDtoList: [],
				},
			}
		},
		onLoad: function(option) {
			var parse = JSON.parse(option.param)
			this.param = parse
			this.param['locCode'] = 'STAGE'
			this.param['whId'] = uni.getStorageSync('warehouse').whId
			this.skuList = this.param.receiveDetailLpnItemDtoList.reduce((total, cur, index) => {
				let hasValue = total.findIndex(current => {
					return current.skuCode === cur.skuCode;
				});
				cur.qty=cur.planQty;
				hasValue === -1 && total.push(cur);
				hasValue !== -1 && (total[hasValue].qty = total[hasValue].qty + cur.planQty);
				return total;
			}, []);
		},
		onUnload() {
			uni.$u.func.unRegisterScanner();
		},
		onShow() {
			uni.$u.func.registerScanner(this.scannerCallback);
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
			submit() {
				this.param.locCode = this.$u.func.parseLocCode(this.param.locCode)
				receive.receiveByCode(this.param).then(res => {
					this.$u.func.showToast({
						title: this.param.boxCode+'收货成功'
					})
					this.esc()
				})
			},
			focus(num) {
				this.focusNum = num
			},
			blur(num) {
				if (num == this.focusNum) {
					this.focusNum = 0;
				}
			},
			change() {
				if (this.focusNum != 4) {
					this.focusNum = this.focusNum + 1;
				} else {
					this.submit()
				}
			},
			esc() {
				uni.navigateBack({
					delta: 1
				});
			},
			scannerCallback(no) {
				let item = barCodeService.parseBarcode(no)
				if (item.type == barCodeService.BarcodeType.Loc) {
					this.param.locCode = item.content;
				} else if (item.type == barCodeService.BarcodeType.Lpn) {
					this.param.lpnCode = item.content;
				} else if (this.focusNum == 2) {
					this.param.skuLot1 = item.content;
				} else {
					this.$u.func.showToast({
						title: '无法识别,不支持的条码类型'
					})
				}

			},

		}
	}
</script>

<style>

</style>
