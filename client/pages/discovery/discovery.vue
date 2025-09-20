<template>
  <view>
    <!-- 新增顶部话题分类导航 -->
    <u-tabs 
      :list="tabList"  
      :current="currentTab" 
      @change="tabChange"
      active-color="#55aaff"
      inactive-color="#666"
      bar-width="60"
      bar-height="4"
    ></u-tabs>
	
    <mescroll-body ref="mescrollRef" :down="downOption" :up="upOption" @init="mescrollInit" @down="downCallback"
      @up="upCallback">
      <StarPostList :list="filteredPostList" :option="postOption" :my_user="vuex_user" @selectPost="selectPost"
        @longpressPost="longpressPost" @clickUserAvatar="clickPostUserAvatar"
        @clickUserNickname="clickPostUserNickname" @clickFocusUser="clickPostFocusUser"
        @clickChatUser="clickPostChatUser" @clickMore="clickMore" @clickSharePost="clickSharePost"
        @clickCommentPost="clickCommentPost" @toggleLike="togglePostLike">
      </StarPostList>
    </mescroll-body>
    
    <view class="release-fixed" @click.stop="clickReleasePost">
      +
    </view>
  </view>
</template>

<script>
	import MescrollMixin from '@/uni_modules/mescroll-uni/components/mescroll-uni/mescroll-mixins.js'
	import StarPostList from '@/uni_modules/star-community/components/star-post-list/star-post-list.vue'

	export default {
		mixins: [MescrollMixin], // 使用mixin
		components: {
			StarPostList,
		},
		   data() {
		      return {
		        currentTab: 0, // 当前选中的tab索引
		        tabList: [
		          { name: '全部', value: 0 },
		          { name: '二手闲置', value: 1 },
		          { name: '打听求助', value: 2 },
		          { name: '失物招领', value: 3 },
		          { name: '恋爱交友', value: 4 },
		          { name: '校园趣事', value: 5 }
		        ],
		        allPostList: [], // 存储所有帖子数据
		        postOption: {
		          lineShow: 6,
		        },
		        downOption: {
		          use: false,
		        },
		        upOption: {
		          isBounce: false,
		          noMoreSize: 4,
		          toTop: {
		            right: '20rpx',
		            bottom: '140rpx',
		            safearea: true,
		            width: '80rpx',
		            radius: '50%',
		          }
		        }
		      };
		    },
		    computed: {
		      // 根据当前选中的tab筛选帖子
		      filteredPostList() {
		        if (this.currentTab === 0) return this.allPostList; // 全部
		        const currentTopicValue = this.tabList[this.currentTab].value;
		        return this.allPostList.filter(post => post.topic === currentTopicValue);
		      }
		    },
		    onPullDownRefresh() {
		      this.mescroll.resetUpScroll()
		      uni.stopPullDownRefresh()
		    },
		    methods: {
		      // tab切换事件
		      tabChange(index) {
		        this.currentTab = index;
		        // 不需要重新请求数据，直接使用计算属性过滤
		        this.mescroll.scrollTo(0, 0); // 滚动到顶部
		      },
		      
		      async upCallback(page) {
		        try {
		          this.$request({
		            url: '/user/post/list'
		          }).then((res) => {
		            // 首次加载时清空数据，追加加载时合并数据
		            this.allPostList = res.data;
		          })
		        } catch (err) {
		          console.log(err);
		          this.mescroll.endErr();
		        }
				this.mescroll.endSuccess(this.allPostList.length)
		      },
			// 选中贴子
			selectPost(post) {
				console.log('selectPost');
				this.incr(post)
				this.goto(`../post-detail/post-detail?id=${post.id}`)
			},
			// 长按帖子
			longpressPost(post) {
				console.log('longpressPost');
				this.showPostMoreActionSheet(post)
			},
			// 点击帖子中的关注用户
			clickPostFocusUser(post) {
				console.log('clickPostFocusUser');
				uni.showToast({
					title: '关注成功'
				})
			},
			// 点击帖子中的私聊用户
			clickPostChatUser(post) {
				console.log('clickPostChatUser')
				this.goto(`../chat/chat?userId=${post.userId}`)
			},
			// 点击帖子更多
			clickMore(post) {
				console.log('clickMore');
				this.showPostMoreActionSheet(post)
			},
			// 点击分享帖子
			clickSharePost(post) {
				console.log('clickSharePost');
			},
			// 点击评论帖子
			clickCommentPost(post) {
				// console.log(post)
				console.log('clickCommentPost');
				this.incr(post)
				// 建议跳转后自动下滑到评论区
				this.goto(`../post-detail/post-detail?id=${post.id}&comment=yes`)
			},
			// 点击帖子用户头像
			clickPostUserAvatar(post) {
				console.log('clickPostUserAvatar');
				this.goto(`../homepage/homepage?id=${post.userId}`)
			},
			// 点击贴子用户昵称
			clickPostUserNickname(post) {
				console.log('clickPostUserNickname');
				this.goto(`../homepage/homepage?id=${post.userId}`)
			},
			// 点赞帖子
			togglePostLike(post, isLiked) {
				console.log('togglePostLike', isLiked);
				this.$request({
				  url: '/user/post/like',
				  data: {
				    id :post.id
				  }
				}).then(res =>{
					post.isLiked = res.data
				})
			},
			// 点击发布帖子按钮
			clickReleasePost() {
				this.goto('../post-release/post-release')
			},
			// 显示帖子更多操作菜单选项
			showPostMoreActionSheet(post) {
				uni.showActionSheet({
					itemList: ['举报'],
					success: (res) => {
						console.log('选中了第' + (res.tapIndex + 1) + '个按钮');
						uni.showToast({
							title: '举报成功'
						})
					},
					fail: (res) => {
						console.log(res.errMsg);
					}
				});
			},
			goto(url) {
				uni.navigateTo({
					url
				})
			},
			incr(post){
				this.$request({
				  url: '/user/post/view',
				  data: {
				    id :post.id
				  }
				})
			}
		}
	}
</script>

<style scoped lang="scss">
	/* 调整tabs样式 */
	  .u-tabs {
	    position: sticky;
	    top: 0;
	    z-index: 999;
	    background-color: #fff;
	    box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	  }
	  
	  /* 调整mescroll-body位置 */
	  mescroll-body {
	    margin-top: 80rpx;
	  }
	  
	  .release-fixed {
	    position: fixed;
	    /* #ifdef MP */
	    bottom: 50rpx;
	    /* #endif */
	    /* #ifndef MP */
	    bottom: calc(130rpx + constant(safe-area-inset-bottom));
	    bottom: calc(130rpx + env(safe-area-inset-bottom));
	    /* #endif */
	    right: 20rpx;
	    background-color: #55aaff;
	    color: #fff;
	    height: 80rpx;
	    width: 80rpx;
	    display: flex;
	    align-items: center;
	    justify-content: center;
	    border-radius: 50%;
	    z-index: 998;
	  }
</style>