<template>
	<view class="container">
		<view class="introduction-box">
			<textarea v-model="introduction" placeholder="请输入简介" maxlength="100" />
		</view>
		<button class="save-btn" type="primary" @click="saveUpdate">保存</button>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				introduction: ''
			};
		},
		onLoad() {
			this.introduction = uni.getStorageSync("introduction")
		},
		methods: {
			// 保存更改
			saveUpdate() {
				uni.setStorageSync("introduction",this.introduction)
				this.$request({
					url:"/user/introduction",
					data:{
						introduction:this.introduction
					}
				})
				uni.navigateBack({
					delta:1
				})
			}
		}
	}
</script>

<style scoped lang="scss">
	.container {
		height: 100vh;
		padding: 20rpx;
		background-color: #eee;
	}

	.introduction-box {
		padding: 20rpx 30rpx;
		background-color: #fff;
		border-radius: 20rpx;
	}

	.save-btn {
		margin-top: 50rpx;
	}
</style>