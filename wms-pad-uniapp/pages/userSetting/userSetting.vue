<template>
	<view class="u-page">
		<!-- <view class="user-box" style="background-color: aqua;">
		  <view style="margin-left: 10%;background-color: antiquewhite;"  ><u--image shape="circle" width="80px" height="80px" src="/static/images/login.png"></u--image></view>
			
			
			<view class="user-name"  style=" font-weight: bolder;background-color: aquamarine;">{{ username }}</view>
			<view  class="login-time"  style=" font-weight: bolder;background-color: antiquewhite;">登录时间:{{loginTime}}</view>
			
		</view> -->
		 <u-row customStyle="margin-bottom: 10px;margin-top:30px" >
		                    <u-col span="4">
		                        <view style="margin-left: 35%;" class="demo-layout bg-purple-light"><u--image shape="circle" width="80px" height="80px" src="/static/images/login.png"></u--image></view>
		                    </u-col>
		                    <u-col span="4">
		                        <view class="demo-layout bg-purple" style="display: inline-block;margin-top: 20%;margin-left: 16%;">{{ username }}</view>
		                    </u-col>
							<u-col span="2">
							    <view v-if="signStatus" class="demo-layout bg-purple-font" style="display: inline-block;margin-top: 38%;">已签到</view>
								  <view v-if="!signStatus" class="demo-layout bg-purple-font" style="display: inline-block;margin-top: 38%;">未签到</view>
							</u-col>
							
		                </u-row>
		<u-row>
		<u-col span="12">
		    <view class="demo-layout bg-purple" style="margin-left: 38%;">登录时间:{{ loginTime }}</view>
		</u-col>	
			
		</u-row>
		
		<view style="margin-left: 10%;margin-right: 10%;">
			<br /><br />
			<u--form>
				<u-form-item borderBottom labelWidth="60" @click="toPwdSetting">
					<u-icon name="edit-pen" size="28"></u-icon>
					<text style="font-size: 100%;">修改密码</text>
				</u-form-item>
			</u--form>
			<u--form>
				<u-form-item borderBottom labelWidth="60" @click="sign">
					<u-icon name="calendar-fill" size="28"></u-icon>
					<text style="font-size: 100%;">签到</text>
				</u-form-item>
			</u--form>
		</view>
	</view>
</template>
<script>
	export default {
		data() {
			return {
				username: this.$store.state.userName,
				loginTime: this.$store.state.loginTime,
				signStatus:this.$store.state.signStatus
			}
		},
		methods: {
			toPwdSetting() {
				uni.$u.func.route('/pages/userSetting/pwdSetting');
			},
			sign() {
				uni.$u.func.route('/pages/userSetting/signSetting');
			}
		}
	}
</script>

<style lang="scss">
	@import 'userSetting.scss';
</style>
