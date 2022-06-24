<template>
	<view>
		<!-- 注意，如果需要兼容微信小程序，最好通过setRules方法设置rules规则 -->
		<u--form labelPosition="left" >
			<u-form-item label="箱码"  borderBottom >
				<u--input v-model="param.boxCode"></u--input>
			</u-form-item>
		</u--form>
		
			  <view style="margin-top: 5%;" v-if="this.param.num !=0" @click="clickItem">
			                 <u-row customStyle="margin-bottom: 10px">
			                     <u-col span="6">
			                         <view class="demo-layout bg-purple-light">箱码:{{param.boxCode}}</view>
			                     </u-col>
			                     <u-col span="6">
			                         <view class="demo-layout bg-purple">总数:{{param.num}}</view>
			                     </u-col>
			                 </u-row>
			  </view>	
			

			 
		<keyboard-listener @keydown="emitKeyDown"></keyboard-listener>
		<view class="footer">
			<view class="btn-cancle" @click="esc()">
				返回
			</view>
		</view>
	</view>
</template>

<script>
	import receive from '@/api/receiveByBox.js'
	import barCodeService from '@/common/barcodeFunc.js'
	import keyboardListener from '@/components/keyboard-listener/keyboard-listener'
	export default {
		components: {
			keyboardListener
		},
		data() { 
			return {
			   param:{
				boxCode:'',
				num:0,
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
				this.$u.func.navigateBack();
			},
            getReceiveDetailList(){
				receive.getReceiveDetailLpn(this.param.boxCode).then(res => {
						this.param = res.data
						if(this.param.num!=0){
							this.clickItem()
						}
				})
			},
			clickItem(){
				 uni.$u.func.route('/pages/inStock/receiveByBox/receiveByBox',this.param);
			},
	      scannerCallback(no) {
			  let item = barCodeService.parseBarcode(no)
			  if(item.type != barCodeService.BarcodeType.UnKnow || item.type != barCodeService.BarcodeType.Lpn){
				  this.$u.func.showToast({title: '条码识别失败,不支持的条码类型'})
				  return
			  }
	      	this.param.boxCode = item.content;
	      	this.getReceiveDetailList();
	      },
			emitKeyDown(e) {
				if (e.key == 'Enter') {
					this.getReceiveDetailList();
				}
			}
		}
	}
</script>

<style>

</style>
