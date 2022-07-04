<template>
	<view @keyup.esc="esc">
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="呼叫AGV" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u--form labelPosition="left">
			<u-form-item label="箱码" class="left-text-one-line" labelWidth="100">
				<u--input v-model="param.boxCode" @confirm="getReceiveDetailList"></u--input>
			</u-form-item>
		</u--form>
		<view style="margin-top: 35%;margin-left: 10%;margin-right: 10%;">
		<u-row >
			<u-col span="4">
				<view class="demo-layout bg-purple"><u-button text="1" @click="press(1)" style="height: 70px;width: 90%;"></u-button></view>
			</u-col>
			<u-col span="4">
				<view class="demo-layout bg-purple"><u-button text="2"  @click="press(2)" style="height: 70px;width: 90%;"></u-button></view>
			</u-col>
			<u-col span="4">
				<view class="demo-layout bg-purple"><u-button text="3"  @click="press(3)" style="height: 70px;width: 90%;"></u-button></view>
			</u-col>
		</u-row>
		<u-row customStyle="margin-top: 5%">
			<u-col span="4">
				<view class="demo-layout bg-purple"><u-button text="4"  @click="press(4)" style="height: 70px;width: 90%;"></u-button></view>
			</u-col>
			<u-col span="4">
				<view class="demo-layout bg-purple"><u-button text="5"  @click="press(5)"  style="height: 70px;width: 90%;"></u-button></view>
			</u-col>
			<u-col span="4">
				<view class="demo-layout bg-purple"><u-button text="6"  @click="press(6)" style="height: 70px;width: 90%;"></u-button></view>
			</u-col>
		</u-row>
		<u-row customStyle="margin-top: 5%">
			<u-col span="4">
				<view class="demo-layout bg-purple"><u-button text="7"  @click="press(7)" style="height: 70px;width: 90%;"></u-button></view>
			</u-col>
			<u-col span="4">
				<view class="demo-layout bg-purple"><u-button text="8"  @click="press(8)" style="height: 70px;width: 90%;"></u-button></view>
			</u-col>
			<u-col span="4">
				<view class="demo-layout bg-purple"><u-button text="9"  @click="press(9)" style="height: 70px;width: 90%;"></u-button></view>
			</u-col>
		</u-row>
		<u-row customStyle="margin-top: 5%">
			<u-col span="4">
				<view class="demo-layout bg-purple"><u-button text="删除"  @click="press(10)"  style="height: 70px;width: 90%;"></u-button></view>
			</u-col>
			<u-col span="4">
				<view class="demo-layout bg-purple"><u-button text="0"  @click="press(0)" style="height: 70px;width: 90%;"></u-button></view>
			</u-col>
			<u-col span="4">
				<view class="demo-layout bg-purple"><u-button text="提交"  @click="findStockByBoxCode" style="height: 70px;width: 90%;"></u-button></view>
			</u-col>
		</u-row>
		
		</view>
     <view>
     	<u-popup :show="show" mode="center"  customStyle="width=100%">
             <view  v-for="(item, index) in putawayList"  >
             	<u-row >
             		<u-col span="5">
             			<view class="demo-layout bg-purple-light">箱码:{{item.boxCode}}</view>
             		</u-col>
             		<u-col span="4">
             			<view class="demo-layout bg-purple">总数:{{item.qty}}</view>
             		</u-col>
             	</u-row>
         
             </view>
     	</u-popup>
     	
     </view>
	  
	
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
		</view>
	</view>
</template>

<script>
	import receive from '@/api/inStock/callAgv.js'
	import barCodeService from '@/common/barcodeFunc.js'
	import setting from '@/common/setting'
	import tool from '@/utils/tool.js'
	export default {
		components: {

		},
		data() {
			return {
				  show: false,
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				putawayList:[], 
				param: {
					boxCode: '',
                    whId:''
				},

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
				uni.$u.func.route('/pages/home/childrenHome?title=收货');
			},
			press(num){
				if(num==10){
					this.param.boxCode = this.param.boxCode.slice(0,this.param.boxCode.length-1)
					return
				}
			   	this.param.boxCode = this.param.boxCode+num
			},
			findStockByBoxCode() {
				this.param.whId = uni.getStorageSync('warehouse').whId
				receive.findStockByBoxCode(this.param).then(res => {
					this.putawayList = res.data
					if(tool.isNotEmpty(this.putawayList)){
						this.show = true
					}
				})
			},
			
			
			clickItem() {
				uni.$u.func.route('/pages/inStock/receiveByBox/receiveByBox', this.param);
			},
			scannerCallback(no) {
				let item = barCodeService.parseBarcode(no)
				if (item.type == barCodeService.BarcodeType.UnKnow || item.type == barCodeService.BarcodeType.Lpn) {
					this.param.boxCode = item.content;
					this.findStockByBoxCode();
				} else {
					this.$u.func.showToast({
						title: '无法识别,不支持的条码类型'
					})
					return
				}

			},
		}
	}
</script>

<style>

</style>
