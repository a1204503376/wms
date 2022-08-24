<template>
	<view @keyup.esc="esc">
		<u-navbar leftIconColor="#fff" @leftClick="esc()" :fixed="false" :autoBack="false"
			:bgColor="navigationBarBackgroundColor" title="呼叫AGV" titleStyle="color:#ffffff;font-size:21px"
			style="color:#ffffff;font-size:21px">
		</u-navbar>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u--form labelPosition="left">
			<u-form-item label="箱码" class="left-text-one-line" labelWidth="100">
				<u--input v-model.trim="param.boxCode" @confirm="getReceiveDetailList"  readonly></u--input>
			</u-form-item>
		</u--form>
		<view style="margin-top: 30%;margin-left: 2%;margin-right: 2%;">
		<u-row >
			<u-col span="3">
				<view class="demo-layout bg-purple"><u-button  @click="press('A')" style="height: 70px;width: 95%;font-size: 25px;">A</u-button></view>
			</u-col>
			<u-col span="3">
				<view class="demo-layout bg-purple"><u-button @click="press(1)" style="height: 70px;width: 95%;font-size: 25px;">1</u-button></view>
			</u-col>
			<u-col span="3">
				<view class="demo-layout bg-purple"><u-button @click="press(2)" style="height: 70px;width: 95%;font-size: 25px;">2</u-button></view>
			</u-col>
			<u-col span="3">
				<view class="demo-layout bg-purple"><u-button  @click="press(3)" style="height: 70px;width: 95%;font-size: 25px;">3</u-button></view>
			</u-col>
		</u-row>
		<u-row customStyle="margin-top: 5%">
			<u-col span="3">
				<view class="demo-layout bg-purple"><u-button  @click="press('B')" style="height: 70px;width: 95%;font-size: 25px;">B</u-button></view>
			</u-col>
			<u-col span="3">
				<view class="demo-layout bg-purple"><u-button  @click="press(4)" style="height: 70px;width: 95%;font-size: 20px;font-size: 25px;" >4</u-button></view>
			</u-col>
			<u-col span="3">
				<view class="demo-layout bg-purple"><u-button   @click="press(5)" style="height: 70px;width: 95%;font-size: 25px;">5</u-button></view>
			</u-col>
			<u-col span="3">
				<view class="demo-layout bg-purple"><u-button   @click="press(6)" style="height: 70px;width: 95%;font-size: 25px;">6</u-button></view>
			</u-col>
		</u-row>
		<u-row customStyle="margin-top: 5%">
			<u-col span="3">
				<view class="demo-layout bg-purple"><u-button @click="press('C')" style="height: 70px;width: 95%;font-size: 25px;">C</u-button></view>
			</u-col>
			<u-col span="3">
				<view class="demo-layout bg-purple"><u-button  @click="press(7)" style="height: 70px;width: 95%;font-size: 25px;">7</u-button></view>
			</u-col>
			<u-col span="3">
				<view class="demo-layout bg-purple"><u-button   @click="press(8)" style="height: 70px;width: 95%;font-size: 25px;">8</u-button></view>
			</u-col>
			<u-col span="3">
				<view class="demo-layout bg-purple"><u-button   @click="press(9)" style="height: 70px;width: 95%;font-size: 25px;">9</u-button></view>
			</u-col>
		</u-row>
		<u-row customStyle="margin-top: 5%">
		<u-col span="3">
			<view class="demo-layout bg-purple"><u-button  @click="press('D')" style="height: 70px;width: 95%;font-size: 25px;">D</u-button></view>
		</u-col>
		<u-col span="3">
			<view class="demo-layout bg-purple"><u-button  @click="press(10)" style="height: 70px;width: 95%;font-size: 25PX;">删除</u-button></view>
		</u-col>
		<u-col span="3">
			<view class="demo-layout bg-purple"><u-button   @click="press(0)" style="height: 70px;width: 95%;font-size: 25px;">0</u-button></view>
		</u-col>
	
			
		</u-row>
		
		</view>
   
	  
	
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
			<view class="btn-submit" @click="findStockByBoxCode()">
				确定
			</view>
		</view>
	</view>
</template>

<script>
	import receive from '@/api/inStock/callAgv.js'
	import barCodeService from '@/common/barcodeFunc.js'
	import setting from '@/common/setting'
	import tool from '@/utils/tool.js'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	export default {
		components: {
              keyboardListener
		},
		data() {
			return {
				navigationBarBackgroundColor: setting.customNavigationBarBackgroundColor,
				stockList:[], 
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
				uni.navigateBack({
					delta: 1
				});
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
					this.stockList = res.data
				    
					if(tool.isNotEmpty(this.stockList)){
					  if(this.stockList.length==1){
						  if(this.stockList[0].lpnType==='0'){
						  	this.$u.func.showToast({
						  		title: '箱码类型错误，请重新输入'
						  	})
						  	return
						  }
						  uni.$u.func.routeNavigateTo('/pages/inStock/callAgv/callAgv', this.stockList[0]);
						  return
					  }
						this.clickItem()
					}
				})
			},
			
			
			clickItem() {
				uni.$u.func.routeNavigateTo('/pages/inStock/callAgv/callAgvQueryList', this.stockList);
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
