<template>
  
	<view>
		<view class="user-box">
			<view class="aa">
				<img src="/static/images/login.png"/>
			</view>
			<view class="user-name"  format="yyyy/MM/dd" style=" font-weight: bolder">用户名：{{ username }} <br/><br/>登录时间:{{loginTime}}</view>
		  
		</view>
		

		<view class="cell" style="margin-left: 10%;margin-right: 10%;">
			
			<div @click="submit">
				<br/><br/>
				<u--form>
					<u-form-item  borderBottom labelWidth="60">
						<u-icon name="edit-pen" size="28"></u-icon>
						<text style="font-size: 100%;">修改密码</text>
					</u-form-item>
				</u--form>
			
				
			</div>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				username: uni.getStorageSync('username'),
				loginTime: uni.getStorageSync('loginTime')
			}
		},
		onLoad() {
			uni.$u.func.registerScanner(this.scannerCallback);
		},
		onUnload() {		
			uni.$u.func.unRegisterScanner();
		},
		methods: {
			scannerCallback(data) {
				this.address=data
			},
			submit() {
			  uni.$u.func.route('/pages/userSetting/pwdSetting');
			}
		}
	}
</script>

<style>
	.aa{
	
				width:80px;
				height:80px;
				margin-left: 15%;
				border-radius:50%;
				overflow:hidden;
			}
			.aa img{
			position: relative ;
	    top: 50%;
	    left: 50%;
	    transform: translate(-50%, -50%);
				margin:0 auto;
				  height:100%;
			}

	.logodiv {
		text-align: center;
		display: block;
		flex-wrap: wrap;
		margin-top: 0.8rem;

		h3 {
			font-size: 20px;
			font-weight: 900;
		}

		h5 {
			font-size: 14px;
		}
	}
	.user-name {
		 font-weight: bolder;
		margin-top: 20rpx;
		margin-right: 50rpx;
		font-size: 36rpx;
		font-family: Source Han Sans CN;
		font-weight: 500;
		
	}
		
	.user-box {
		display: flex;
		justify-content: space-between;
		padding: 20rpx 20rpx 0 64rpx;
		margin-top: 76rpx;
	
		
	
	
	}
</style>
