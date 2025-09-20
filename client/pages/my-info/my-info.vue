<template>
	<view>
		<view class="top-box">
			<view class="avatar-box" @click="chooseAvatar">
				<image class="avatar" :src="avatar"></image>
				<image class="select-icon" src="/static/selectAvatar.svg"></image>
			</view>
		</view>
		<view class="list-container">
			<view class="item-box" hover-class="view-hover" :hover-stay-time="50" hover-stop-propagation
				@click="goto('./nickname/nickname')">
				<view class="left-box">
					昵称
				</view>
				<view class="right-box">
					<view class="text">
						{{ nickname }}
					</view>
					<image class="icon" src="/static/right.svg"></image>
				</view>
			</view>
			<view class="item-box" hover-class="view-hover" :hover-stay-time="50" hover-stop-propagation
				@click="goto('./introduction/introduction')">
				<view class="left-box">
					简介
				</view>
				<view class="right-box">
					<view class="text">
						{{ introduction }}
					</view>
					<image class="icon" src="/static/right.svg"></image>
				</view>
			</view>
			<view class="item-box" hover-class="view-hover" :hover-stay-time="50" hover-stop-propagation
				@click="goto('./gender/gender')">
				<view class="left-box">
					性别
				</view>
				<view class="right-box">
					<view class="text">
						{{ genderText }}
					</view>
					<image class="icon" src="/static/right.svg"></image>
				</view>
			</view>
			<view class="item-box" hover-class="view-hover" :hover-stay-time="50" hover-stop-propagation
				@click="chooseBackgroundImg">
				<view class="left-box">
					背景图
				</view>
				<view class="right-box">
					<image class="img" :src="backgroundImg" mode="aspectFill"></image>
					<image class="icon" src="/static/right.svg"></image>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				avatar: 'http://dummyimage.com/50x50/f2b879&text=avatar', // 头像
				nickname: '', // 昵称
				introduction: '', // 简介
				gender: 0, // 性别
				backgroundImg: 'http://dummyimage.com/120x90', //背景图
			};
		},
		computed: {
			// 中文性别
			genderText() {
				let gender = this.gender
				return gender === 0 ? '女' : gender === 1 ? '男' : '未知'
			}
		},
		onShow() {
			// 刷新用户信息
			this.nickname = uni.getStorageSync("nickname")
			this.introduction = uni.getStorageSync("introduction")
			this.gender = uni.getStorageSync("gender")
			this.avatar = uni.getStorageSync("avatar")
			this.backgroundImg = uni.getStorageSync("backgroundImg")
		},
		methods: {
			// 选择头像
			async chooseAvatar() {
				let img = await this.chooseImg(1)
				console.log(img);
				if (img) {
					this.avatar = img
				}
				uni.setStorageSync("avatar",this.avatar)
				this.$request({
					url:"/user/avatar",
					data:{
						avatar:this.avatar
					}
				})
			},
			// 选择背景图
			async chooseBackgroundImg() {
				let img = await this.chooseImg(1)
				console.log(img)
				if (img) {
					this.backgroundImg = img
				}
				uni.setStorageSync("backgroundImg",this.backgroundImg)
				this.$request({
					url:"/user/background",
					data:{
						background:this.backgroundImg
					}
				})
			},
			// 选择头像，推荐使用图片裁剪能力
			chooseImg(count, maxSize = 5) {
			    return new Promise((resolve, reject) => {
			        // #ifndef MP-WEIXIN
			        // 非微信小程序的代码保持不变...
			        // #endif
			        
			        // #ifdef MP-WEIXIN
			        uni.chooseMedia({
			            count,
			            mediaType: ['image'],
			            sizeType: ['compressed'],
			            success: async (res) => {
			                const tempFiles = res.tempFiles
			                const maxSizeBytes = maxSize * 1024 * 1024
			                const filePath = tempFiles[0].tempFilePath
			                const fileSize = tempFiles[0].size
			
			                if (fileSize > maxSizeBytes) {
			                    uni.showToast({
			                        title: '图片大小不能超过5MB',
			                        duration: 2000
			                    });
			                    return resolve(null)
			                } else {
			                    try {
			                        const uploadRes = await new Promise((uploadResolve, uploadReject) => {
			                            uni.uploadFile({
			                                url: this.$base_url + '/user/upload',
			                                filePath: filePath,
			                                name: 'file',
			                                header: {
			                                    'User-Auth': uni.getStorageSync('token') || ''
			                                },
			                                success: (res) => {
			                                    try {
			                                        // 尝试解析返回的JSON数据
			                                        const data = JSON.parse(res.data)
			                                        uploadResolve(data)
			                                    } catch (e) {
			                                        // 如果解析失败，直接返回原始数据
			                                        uploadResolve(res.data)
			                                    }
			                                },
			                                fail: (err) => {
			                                    uploadReject(err)
			                                }
			                            });
			                        });
			                        
			                        // 返回上传接口的响应数据
			                        return resolve(uploadRes)
			                    } catch (err) {
			                        console.error('上传失败', err);
			                        return reject(err)
			                    }
			                }
			            },
			            fail: (err) => {
			                return reject(err)
			            }
			        });
			        // #endif
			    })
			},
			goto(url) {
				uni.navigateTo({
					url
				})
			}
		}
	}
</script>

<style scoped lang="scss">
	.top-box {
		display: flex;
		justify-content: center;
		padding: 50rpx 0;

		.avatar-box {
			position: relative;

			.avatar {
				height: 180rpx;
				width: 180rpx;
				border-radius: 50%;
			}

			.select-icon {
				position: absolute;
				right: 0;
				bottom: 0;
				height: 60rpx;
				width: 60rpx;
			}
		}
	}

	.list-container {

		.item-box {
			box-sizing: border-box;
			padding: 30rpx;
			width: 100%;
			display: inline-flex;
			justify-content: space-between;
			border-bottom: 2rpx #dcdcdc solid;
			font-size: 30rpx;
			line-height: 1;

			.left-box {
				width: 160rpx;
				flex-shrink: 0;
				display: flex;
				align-items: center;
			}

			.right-box {
				display: inline-flex;
				align-items: center;

				.img {
					flex-shrink: 0;
					height: 100rpx;
					width: 100rpx;
				}

				.text {}

				.icon {
					flex-shrink: 0;
					margin-left: 20rpx;
					height: 30rpx;
					width: 30rpx;
				}
			}
		}
	}
</style>