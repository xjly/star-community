<template>
	<view>
		<view class="user-wrap">
			<!-- 此处添加背景图片标签 -->
			<image :src="backgroundImg" class="background-mask"></image>
			<view class="user-container">
				<view class="user-box">
					<u-avatar :src="avatar" show-sex="true" size="large"></u-avatar>
					<view v-if="nickname" class="nickname">
						{{nickname}}
					</view>
					<view v-else class="nickname">星之链用户</view>
					<view class="introduction" @click="goto('../my-info/my-info')">
						点击编辑简介
					</view>
				</view>
			</view>
		</view>
		<view class="bottom-container">
			<view class="list-container">
				<view class="item-box" hover-class="view-hover" :hover-stay-time="50" hover-stop-propagation
					@click="goto(`../homepage/homepage?id=${userId}`)">
					<view class="left-box">
						个人主页
					</view>
					<view class="right-box">
						<image class="icon" src="/static/right.svg"></image>
					</view>
				</view>
				<view class="item-box" hover-class="view-hover" :hover-stay-time="50" hover-stop-propagation
					@click="goto('../my-info/my-info')">
					<view class="left-box">
						个人资料
					</view>
					<view class="right-box">
						<image class="icon" src="/static/right.svg"></image>
					</view>
				</view>
				<view class="item-box" hover-class="view-hover" :hover-stay-time="50" hover-stop-propagation
					@click="goto('../post-check/post-check')">
					<view class="left-box">
						帖子审核
					</view>
					<view class="right-box">
						<view class="text">
							请判断是否为管理员
						</view>
						<image class="icon" src="/static/right.svg"></image>
					</view>
				</view>
				<view class="item-box" hover-class="view-hover" :hover-stay-time="50" hover-stop-propagation
					@click="goto('../comment-check/comment-check')">
					<view class="left-box">
						评论审核
					</view>
					<view class="right-box">
						<view class="text">
							请判断是否为管理员
						</view>
						<image class="icon" src="/static/right.svg"></image>
					</view>
				</view>
				<!-- #ifdef MP-WEIXIN -->
				<button class="contact-btn" hover-class="contact-btn-hover" open-type="contact">
					<view class="item-box" hover-class="view-hover" :hover-stay-time="50" hover-stop-propagation>
						<view class="left-box">
							意见反馈
						</view>
						<view class="right-box">
							<image class="icon" src="/static/right.svg"></image>
						</view>
					</view>
				</button>
				<!-- #endif -->
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				userId: null,
				nickname:'',
				avatar:'',
				backgroundImg:'',
				introduction:''
			};
		},
		onShow() {
			this.nickname = uni.getStorageSync("nickname")
			this.avatar = uni.getStorageSync("avatar")
		},
		created() {
			uni.login({
				provider: "weixin",
				success: (res) => {
				  console.log("微信登录成功，code:", res.code);
				  // 登录成功
				  // 将 code 发送到后
				  this.$request({
					  url:"/user/login",
					  method:'post',
					  data: {
						  code: res.code    //将code发送到后台服务器。
					  }
				  }).then(res => {
					  uni.showToast({
					    title: "登录成功",
					  });
					  uni.setStorageSync("token",res.data.token)
					  uni.setStorageSync("id",res.data.id)
					  this.userId = res.data.id
						// 跳转到首页
						// uni.switchTab({
						//   url: "/pages/discovery/discovery",
						// });
						this.$request({
							url:"/user/info"
						}).then(res => {
							this.nickname = res.data.nickname
							this.avatar = res.data.avatar
							uni.setStorageSync("avatar",res.data.avatar)
							uni.setStorageSync("nickname",res.data.nickname)
						})
					  }
					).catch((error) => {
					  uni.showToast({
						title: "网络错误，请重试",
						icon: "none",
					  });
					});
				},
				fail: (err) => {
				  console.log("微信登录失败", err);
				  uni.showToast({
					title: "微信登录失败",
					icon: "none",
				  });
				},
			  });
		},
		methods: {
			goto(url) {
				uni.navigateTo({
					url
				})
			},
			// 复制文字
			copyText(data, title) {
				uni.showLoading({
					title: ''
				})
				uni.setClipboardData({
					data,
					success: (res) => {
						uni.hideLoading()
						uni.showToast({
							title,
							icon: 'none'
						})
					},
					fail: (err) => {
						uni.hideLoading()
						uni.showToast({
							title: '复制失败',
							icon: 'error'
						})
					}
				})
			}
		}
	}
</script>

<style scoped lang="scss">
	.view-hover {
		background-color: #f0f8ff; /* 更明亮的悬停背景色 */
	}

	.user-wrap {
		position: relative;
		background-color: #55aaff; /* 更鲜明的背景色 */
		border-radius: 0 0 30rpx 30rpx; /* 圆角 */
		overflow: hidden;
		.background-mask {
			z-index: -1;
			position: absolute;
			height: 100%;
			width: 100%;
			background: transparent;
			// background-color: rgba(0, 0, 0, 0.3); /* 更透明的遮罩 */
		}

		.user-container {
			padding: 30rpx 30rpx 90rpx;

			.user-box {
				display: flex;
				flex-direction: column;
				align-items: center;

				.avatar {
					flex-shrink: 0;
					height: 150rpx; /* 增大头像尺寸 */
					width: 150rpx;
					border-radius: 50%;
					border: 4rpx solid #ffffff; /* 添加边框 */
					box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.2); /* 添加阴影 */
				}
				.login-text{
					margin-top: 20rpx;
					font-size: 36rpx; /* 增大字体 */
					font-weight: 700;
					color: #ffffff; /* 白色文字 */
				}
				.nickname {
					margin-top: 20rpx;
					font-size: 36rpx; /* 增大字体 */
					font-weight: 700;
					color: #ffffff; /* 白色文字 */
				}

				.introduction {
					margin-top: 20rpx;
					color: #e0f7fa; /* 更鲜明的简介颜色 */
					font-size: 28rpx; /* 增大字体 */
				}
			}
		}
	}

	.bottom-container {
		margin-top: -50rpx;
		padding-top: 30rpx;
		background-color: #ffffff; /* 白色背景 */
		border-radius: 30rpx 30rpx 0 0;
		overflow: hidden;
		box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.1); /* 添加阴影 */

		.list-container {
			.contact-btn {
				background-color: transparent;
				padding: 0;
			}

			.contact-btn-hover {
				background-color: #f7f8f9;
			}

			.contact-btn::after {
				border: none;
			}

			.item-box {
				box-sizing: border-box;
				padding: 30rpx;
				width: 100%;
				display: inline-flex;
				justify-content: space-between;
				border-bottom: 2rpx #dcdcdc solid;
				font-size: 30rpx;
				line-height: 1;
				background-color: #ffffff; /* 白色背景 */
				transition: background-color 0.3s ease; /* 添加过渡效果 */

				&:hover {
					background-color: #f0f8ff; /* 悬停背景色 */
				}

				.left-box {
					display: flex;
					flex-direction: column;
					justify-content: center;
					align-items: flex-start;
					color: #333333; /* 更深的文字颜色 */
					font-weight: 600; /* 加粗 */
				}

				.right-box {
					display: inline-flex;
					align-items: center;

					.text {
						color: #888888; /* 更浅的文字颜色 */
						font-size: 26rpx; /* 减小字体 */
					}

					.icon {
						margin-left: 20rpx;
						height: 30rpx;
						width: 30rpx;
					}
				}
			}
		}

		.logo-box {
			margin: 100rpx 0 30rpx;
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;

			.logo {
				width: 25%;
			}

			.text {
				margin-top: 30rpx;
				color: #9c9c9c;
			}
		}
	}
</style>